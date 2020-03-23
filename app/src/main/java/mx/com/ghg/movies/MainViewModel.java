package mx.com.ghg.movies;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import mx.com.ghg.movies.ui.movies.MovieUi;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<MovieUi>> movies;

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.movies = movies;
    }

    public LiveData<List<MovieUi>> getMovies() {
        return movies;
    }
}
