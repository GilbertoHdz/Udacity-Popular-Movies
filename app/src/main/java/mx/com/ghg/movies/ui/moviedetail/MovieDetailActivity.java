package mx.com.ghg.movies.ui.moviedetail;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import com.bumptech.glide.Glide;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

import mx.com.ghg.movies.R;
import mx.com.ghg.movies.api.models.Video;
import mx.com.ghg.movies.api.network.InternetCheck;
import mx.com.ghg.movies.api.utilities.NetworkUtils;
import mx.com.ghg.movies.api.utilities.VideoJsonUtils;
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

    private MovieUi movieUi;
    private MovieDetailAdapter _videoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        checkNetworkConnection();
    }

    @Override
    public void onVideoItemClick(MovieDetailUi movieUI) {

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
    }

    private void showErrorMessage(int resMsgId) {
        String message = getApplicationContext().getString(resMsgId);
        _error_message.setText(message);
        _error_message.setVisibility(View.VISIBLE);
        _loader.setVisibility(View.GONE);
        _videosRecycler.setVisibility(View.GONE);
    }

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
            int size = videos.size();
            if (size == 0) {
                showErrorMessage(R.string.videos_error_empty_message);
            } else {
                ArrayList videosUi = new ArrayList(size);
                for (int i = 0; i < size; i++) {
                    Video video = videos.get(i);

                    MovieDetailUi videoUi = new MovieDetailUi(
                            video.getId(),
                            video.getName(),
                            video.getKey()
                    );

                    videosUi.add(videoUi);
                }

                _videoAdapter.setVideoItems(videosUi);
                showVideos();
            }
        }
    }
}
