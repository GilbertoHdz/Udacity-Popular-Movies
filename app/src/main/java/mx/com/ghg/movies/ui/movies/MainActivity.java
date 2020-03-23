package mx.com.ghg.movies.ui.movies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

import mx.com.ghg.movies.R;
import mx.com.ghg.movies.api.models.Movie;
import mx.com.ghg.movies.api.network.InternetCheck;
import mx.com.ghg.movies.api.utilities.MovieJsonUtils;
import mx.com.ghg.movies.api.utilities.NetworkUtils;
import mx.com.ghg.movies.ui.moviedetail.MovieDetailActivity;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieItemClickListener {

    private static final String KEY_QUERY_MOVIE = "MainActivity.movie.query.task";
    private int _menu_id_selected = -1;

    RecyclerView _moviesRecycler;
    ProgressBar _moviesLoader;
    TextView _error_message_text;
    MovieAdapter _moviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ui Declarations
        _moviesRecycler = (RecyclerView) findViewById(R.id.movies_recycler);
        _moviesLoader = (ProgressBar) findViewById(R.id.movies_progress);
        _error_message_text = (TextView) findViewById(R.id.error_message_display_text);

        // RecyclerView Configurations
        int GRID_SPAN = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                getApplicationContext(),
                GRID_SPAN,
                RecyclerView.VERTICAL,
                false
        );
        _moviesRecycler.setLayoutManager(gridLayoutManager);
        _moviesRecycler.setHasFixedSize(true);
        _moviesAdapter = new MovieAdapter(this);
        _moviesRecycler.setAdapter(_moviesAdapter);

        if (null != savedInstanceState
                && savedInstanceState.containsKey(KEY_QUERY_MOVIE)
                && savedInstanceState.getInt(KEY_QUERY_MOVIE) != -1
        ) {
            checkNetworkConnection(savedInstanceState.getInt(KEY_QUERY_MOVIE));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_most_popular:
                checkNetworkConnection(R.id.menu_most_popular);
                return true;
            case R.id.menu_top_rated:
                checkNetworkConnection(R.id.menu_top_rated);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_QUERY_MOVIE, _menu_id_selected);
    }

    private void checkNetworkConnection(final int menuOptionSelected) {
        _menu_id_selected = menuOptionSelected;
        _moviesLoader.setVisibility(View.VISIBLE);
        new InternetCheck(new InternetCheck.Consumer() {
            @Override
            public void accept(Boolean internet) {
                if (internet) {
                    switch (menuOptionSelected) {
                        case R.id.menu_most_popular:
                            new MovieQueryTask().execute("top_rated");
                            break;
                        case R.id.menu_top_rated:
                            new MovieQueryTask().execute("popular");
                            break;
                    }
                } else {
                    showErrorMessage(R.string.movies_error_network_message);
                }
            }
        });
    }

    private void showDataView() {
        _moviesRecycler.setVisibility(View.VISIBLE);
        _error_message_text.setVisibility(View.INVISIBLE);
        _moviesLoader.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage(int resMsgId) {
        String message = getApplicationContext().getString(resMsgId);
        _error_message_text.setText(message);
        _error_message_text.setVisibility(View.VISIBLE);
        _moviesRecycler.setVisibility(View.INVISIBLE);
        _moviesLoader.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onMovieItemClick(MovieUi movieUi) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.ARG_MOVIE_UI, movieUi);
        startActivity(intent);
    }

    /**
     * Network Task
     */
    public class MovieQueryTask extends AsyncTask<String, Void, ArrayList<Movie>> {
        @Override
        protected ArrayList<Movie> doInBackground(String... url) {
            try {
                URL weatherRequestUrl = NetworkUtils.buildUrl(url[0]);
                String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);

                return MovieJsonUtils.getMoviesValuesFromJson(jsonMoviesResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            if (null == movies || movies.size() == 0) {
                showErrorMessage(R.string.movies_error_empty_message);
            } else {
                ArrayList moviesUi = new ArrayList(movies.size());
                for (int i = 0; i < movies.size(); i++) {
                    Movie movie = movies.get(i);
                    MovieUi movieUi = new MovieUi(
                            movie.getId(),
                            movie.getTitle(),
                            movie.getPosterPath(),
                            movie.getOverview(),
                            movie.getVoteAverage(),
                            movie.getReleaseDate()
                    );
                    moviesUi.add(movieUi);
                }

                _moviesAdapter.setMovieItems(moviesUi);
                showDataView();
            }
        }
    }
}
