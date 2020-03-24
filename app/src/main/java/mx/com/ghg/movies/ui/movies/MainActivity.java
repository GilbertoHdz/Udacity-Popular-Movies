package mx.com.ghg.movies.ui.movies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
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
import java.util.List;

import mx.com.ghg.movies.MainViewModel;
import mx.com.ghg.movies.R;
import mx.com.ghg.movies.api.models.Movie;
import mx.com.ghg.movies.api.network.AppExecutors;
import mx.com.ghg.movies.api.network.InternetCheck;
import mx.com.ghg.movies.api.utilities.MovieJsonUtils;
import mx.com.ghg.movies.api.utilities.NetworkUtils;
import mx.com.ghg.movies.db.AppDatabase;
import mx.com.ghg.movies.db.entities.PopularEntity;
import mx.com.ghg.movies.db.entities.TopRatedEntity;
import mx.com.ghg.movies.ui.moviedetail.MovieDetailActivity;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieItemClickListener {

    private static final String KEY_QUERY_MOVIE = "MainActivity.movie.query.task";
    private MainViewModel viewModel;
    private int _menu_id_selected = -1;

    private RecyclerView _moviesRecycler;
    private ProgressBar _moviesLoader;
    private TextView _error_message_text;
    private MovieAdapter _moviesAdapter;

    private AppDatabase mDb;

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

        mDb = AppDatabase.getInstance(getApplicationContext());
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        _menu_id_selected = item.getItemId();

        switch (_menu_id_selected) {
            case R.id.menu_most_popular:
                getPopularityMovies();
                return true;
            case R.id.menu_top_rated:
                getTopRatedMovies();
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

    private void getTopRatedMovies() {
        viewModel.getTopRated().observe(this, new Observer<List<TopRatedEntity>>() {
            @Override
            public void onChanged(List<TopRatedEntity> topRatedEntities) {
                if (null == topRatedEntities || topRatedEntities.size() == 0) {
                    checkNetworkConnection(R.id.menu_top_rated);
                    return;
                }

                ArrayList moviesUi = new ArrayList(topRatedEntities.size());
                for (int i = 0; i < topRatedEntities.size(); i++) {
                    MovieUi movieUi = new MovieUi(topRatedEntities.get(i));
                    moviesUi.add(movieUi);
                }

                _moviesAdapter.setMovieItems(moviesUi);
            }
        });
    }

    private void getPopularityMovies() {
        viewModel.getPopularity().observe(this, new Observer<List<PopularEntity>>() {
            @Override
            public void onChanged(List<PopularEntity> entities) {
                if (null == entities || entities.size() == 0) {
                    checkNetworkConnection(R.id.menu_most_popular);
                    return;
                }

                ArrayList moviesUi = new ArrayList(entities.size());
                for (int i = 0; i < entities.size(); i++) {
                    MovieUi movieUi = new MovieUi(entities.get(i));
                    moviesUi.add(movieUi);
                }

                _moviesAdapter.setMovieItems(moviesUi);
            }
        });
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
                final ArrayList localEntity = new ArrayList(movies.size());
                for (int i = 0; i < movies.size(); i++) {
                    Movie movie = movies.get(i);
                    MovieUi movieUi = new MovieUi(movie);
                    moviesUi.add(movieUi);

                    if (_menu_id_selected == R.id.menu_most_popular) {
                        PopularEntity entity = new PopularEntity(
                                movieUi.id,
                                movieUi.title,
                                movieUi.image,
                                movieUi.synopsis,
                                movieUi.rating,
                                movieUi.releaseDate
                        );
                        localEntity.add(entity);
                    } else {
                        TopRatedEntity entity = new TopRatedEntity(
                                movieUi.id,
                                movieUi.title,
                                movieUi.image,
                                movieUi.synopsis,
                                movieUi.rating,
                                movieUi.releaseDate
                        );
                        localEntity.add(entity);
                    }
                }

                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (_menu_id_selected == R.id.menu_most_popular) {
                            mDb.movieDao().insertPopularMovies(localEntity);
                        } else {
                            mDb.movieDao().insertTopRatedMovies(localEntity);
                        }
                    }
                });

                _moviesAdapter.setMovieItems(moviesUi);
                showDataView();
            }
        }
    }
}
