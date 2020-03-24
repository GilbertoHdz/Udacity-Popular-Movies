package mx.com.ghg.movies.ui.moviedetail;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import com.bumptech.glide.Glide;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

import mx.com.ghg.movies.R;
import mx.com.ghg.movies.api.models.Review;
import mx.com.ghg.movies.api.models.Video;
import mx.com.ghg.movies.api.network.AppExecutors;
import mx.com.ghg.movies.api.network.InternetCheck;
import mx.com.ghg.movies.api.utilities.NetworkUtils;
import mx.com.ghg.movies.api.utilities.ReviewJsonUtils;
import mx.com.ghg.movies.api.utilities.VideoJsonUtils;
import mx.com.ghg.movies.db.AppDatabase;
import mx.com.ghg.movies.db.entities.ReviewEntity;
import mx.com.ghg.movies.db.entities.VideoEntity;
import mx.com.ghg.movies.ui.movies.MainActivity;
import mx.com.ghg.movies.ui.movies.MovieUi;

public class MovieDetailActivity
        extends AppCompatActivity
        implements MovieDetailAdapter.VideoItemClickListener {

    public static String ARG_MOVIE_UI = "arg.movie.ui.detail.activity";

    private ImageView _image;
    private TextView _title;
    private TextView _average;
    private TextView _release;
    private TextView _synopsis;
    private RecyclerView _videosRecycler;
    private ProgressBar _loader;
    private TextView _error_message;
    private Button _mark_as_favorite_btn;

    private MovieUi movieUi;
    private MovieDetailAdapter _videoAdapter;

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intentDetail = getIntent();
        if (intentDetail != null) {
            if (intentDetail.hasExtra(ARG_MOVIE_UI)) {
                movieUi = (MovieUi) intentDetail.getSerializableExtra(ARG_MOVIE_UI);
            } else {
                throw new IllegalArgumentException("the movie data should'n be null");
            }
        }

        _image = (ImageView) findViewById(R.id.movie_detail_image);
        _title = (TextView) findViewById(R.id.movie_detail_title);
        _average = (TextView) findViewById(R.id.movie_detail_average_text);
        _release = (TextView) findViewById(R.id.movie_detail_date_text);
        _synopsis = (TextView) findViewById(R.id.movie_detail_overview_text);
        _videosRecycler = (RecyclerView) findViewById(R.id.movie_detail_videos_recycler);
        _loader = (ProgressBar) findViewById(R.id.movie_detail_loader_pb);
        _error_message = (TextView) findViewById(R.id.movie_detail_error_message_text);
        _mark_as_favorite_btn = (Button) findViewById(R.id.movie_detail_mark_favorite_action);

        // Set up recycler
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                false
        );

        _videosRecycler.setLayoutManager(linearLayoutManager);
        _videosRecycler.setHasFixedSize(true);
        _videoAdapter = new MovieDetailAdapter(this);
        _videosRecycler.setAdapter(_videoAdapter);

        updateUiState();

        mDb = AppDatabase.getInstance(getApplicationContext());
        checkNetworkConnection();
    }

    @Override
    public void onVideoItemClick(MovieDetailUi movieUI) {
        if (movieUI instanceof VideoUi) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(((VideoUi) movieUI).getYoutubeUrl()));
            startActivity(intent);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void updateUiState() {
        Glide
                .with(this)
                .load(movieUi.getImage())
                .centerCrop()
                .into(_image);

        _title.setText(movieUi.getTitle());
        _average.setText(movieUi.getRating().toString());
        _release.setText(movieUi.getReleaseDate());
        _synopsis.setText(movieUi.getSynopsis());
    }

    private void checkNetworkConnection() {
        _loader.setVisibility(View.VISIBLE);
        new InternetCheck(new InternetCheck.Consumer() {
            @Override
            public void accept(Boolean internet) {
                if (internet) {
                    new VideosQueryTask().execute(movieUi.getId().toString());
                } else {
                    showErrorMessage(R.string.movies_error_network_message);
                }
            }
        });
    }

    private void showVideos() {
        _videosRecycler.setVisibility(View.VISIBLE);
        _loader.setVisibility(View.GONE);
        _error_message.setVisibility(View.GONE);

        // continue with review
        new ReviewQueryTask().execute(movieUi.getId().toString());
    }

    private void showErrorMessage(int resMsgId) {
        String message = getApplicationContext().getString(resMsgId);
        _error_message.setText(message);
        _error_message.setVisibility(View.VISIBLE);
        _loader.setVisibility(View.GONE);
        _videosRecycler.setVisibility(View.INVISIBLE);
    }

    private void showReviewErrorMessage(int resMsgId) {
        String message = getApplicationContext().getString(resMsgId);
        _error_message.setText(message);
        _error_message.setVisibility(View.VISIBLE);
    }

    /**
     * Async request for Videos by Id
     */
    public class VideosQueryTask extends AsyncTask<String, Void, ArrayList<Video>> {
        @Override
        protected ArrayList<Video> doInBackground(String... url) {
            try {
                URL requestUrl = NetworkUtils.buildUrl(url[0], "videos");
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(requestUrl);

                return VideoJsonUtils.getVideosFromJson(jsonResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Video> videos) {
            super.onPostExecute(videos);
            if (null == videos || videos.size() == 0) {
                showErrorMessage(R.string.videos_error_empty_message);
            } else {

                ArrayList videosUi = new ArrayList(videos.size());
                final ArrayList localEntity = new ArrayList(videos.size());

                for (int i = 0; i < videos.size(); i++) {
                    Video video = videos.get(i);

                    VideoUi videoUi = new VideoUi(
                            video.getId(),
                            video.getName(),
                            video.getKey()
                    );

                    videosUi.add(videoUi);

                    VideoEntity entity = new VideoEntity(
                            videoUi.getId(),
                            videoUi.getName(),
                            videoUi.getKey()
                    );

                    localEntity.add(entity);
                }

                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.movieDao().insertVideos(localEntity);
                    }
                });

                _videoAdapter.setVideoItems(videosUi);
                showVideos();
            }
        }
    }

    /**
     * Async request for Reviews by Id
     */
    public class ReviewQueryTask extends AsyncTask<String, Void, ArrayList<Review>> {
        @Override
        protected ArrayList<Review> doInBackground(String... url) {
            try {
                URL requestUrl = NetworkUtils.buildUrl(url[0], "reviews");
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(requestUrl);

                return ReviewJsonUtils.getReviewsFromJson(jsonResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Review> reviews) {
            super.onPostExecute(reviews);
            if (null == reviews ||reviews.size() == 0) {
                showReviewErrorMessage(R.string.review_error_empty_message);
            } else {
                ArrayList reviewsUi = new ArrayList(reviews.size());
                final ArrayList localEntity = new ArrayList(reviews.size());

                for (int i = 0; i < reviews.size(); i++) {
                    Review review = reviews.get(i);

                    ReviewUi videoUi = new ReviewUi(
                            review.getId(),
                            review.getAuthor(),
                            review.getContent(),
                            review.getUrl()
                    );

                    reviewsUi.add(videoUi);

                    ReviewEntity entity = new ReviewEntity(
                            videoUi.getId(),
                            movieUi.getId(),
                            videoUi.getAuthor(),
                            videoUi.getContent(),
                            videoUi.getUrl()
                    );
                }

                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.movieDao().insertVideos(localEntity);
                    }
                });

                _videoAdapter.addReviewItems(reviewsUi);
            }
        }
    }
}
