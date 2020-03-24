package mx.com.ghg.movies;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import mx.com.ghg.movies.db.AppDatabase;
import mx.com.ghg.movies.db.entities.PopularEntity;
import mx.com.ghg.movies.db.entities.TopRatedEntity;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<TopRatedEntity>> getTopRated() {
        AppDatabase db = AppDatabase.getInstance(this.getApplication());
        return db.movieDao().loadTopRatedMovies();
    }

    public LiveData<List<PopularEntity>> getPopularity() {
        AppDatabase db = AppDatabase.getInstance(this.getApplication());
        return db.movieDao().loadPopularMovies();
    }
}
