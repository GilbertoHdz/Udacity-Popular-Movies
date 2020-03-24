package mx.com.ghg.movies.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

import mx.com.ghg.movies.db.entities.PopularEntity;
import mx.com.ghg.movies.db.entities.ReviewEntity;
import mx.com.ghg.movies.db.entities.TopRatedEntity;
import mx.com.ghg.movies.db.entities.VideoEntity;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPopularMovies(ArrayList<PopularEntity> movies);

    @Query("SELECT * FROM popular_movie")
    LiveData<List<PopularEntity>> loadPopularMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTopRatedMovies(ArrayList<TopRatedEntity> movies);

    @Query("SELECT * FROM top_rated_movie")
    LiveData<List<TopRatedEntity>> loadTopRatedMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReviews(ArrayList<ReviewEntity> reviews);

    @Query("SELECT * FROM review")
    LiveData<List<ReviewEntity>> loadMovieReviews();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVideos(ArrayList<VideoEntity> videos);

    @Query("SELECT * FROM video")
    LiveData<List<VideoEntity>> loadMovieVideos();
}
