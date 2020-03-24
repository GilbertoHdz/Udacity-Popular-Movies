package mx.com.ghg.movies.db;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import mx.com.ghg.movies.db.dao.MovieDao;
import mx.com.ghg.movies.db.entities.PopularEntity;
import mx.com.ghg.movies.db.entities.ReviewEntity;
import mx.com.ghg.movies.db.entities.TopRatedEntity;
import mx.com.ghg.movies.db.entities.VideoEntity;

@Database(entities = {
        TopRatedEntity.class,
        PopularEntity.class,
        VideoEntity.class,
        ReviewEntity.class
    }, version = 1, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getCanonicalName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "gilinho.movie.db";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {

        if (null == sInstance) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        AppDatabase.DATABASE_NAME
                ).build();
            }
        }

        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract MovieDao movieDao();
}
