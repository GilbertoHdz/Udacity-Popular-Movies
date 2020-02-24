package mx.com.ghg.movies.api.utilities;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mx.com.ghg.movies.api.models.Movie;

public final class MovieJsonUtils {

    private static final String RESULTS = "results";
    private static final String ID = "id";
    private static final String POPULARITY = "popularity";
    private static final String VOTE_COUNT = "vote_count";
    private static final String VIDEO = "video";
    private static final String POSTER_PATH = "poster_path";
    private static final String ADULT = "adult";
    private static final String BACKDROP_PATH = "backdrop_path";
    private static final String ORIGINAL_LANGUAGE = "original_language";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String GENRE_IDS = "genre_ids";
    private static final String TITLE = "title";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String OVERVIEW = "overview";
    private static final String RELEASE_DATE = "release_date";

    private static final String STATUS_CODE = "status_code";
    private static final String STATUS_MESSAGE = "status_message";

    public static ArrayList<Movie> getMoviesValuesFromJson(
            String movieJsonStr
    ) throws JSONException {

        JSONObject moviesJson = new JSONObject(movieJsonStr);

        if (moviesJson.has(STATUS_CODE)) {
            return null;
        }

        JSONArray jsonResults = moviesJson.getJSONArray(RESULTS);
        ArrayList movies = new ArrayList(jsonResults.length());

        for (int i = 0; i < jsonResults.length(); i++) {

            Integer _id;
            Double _popularity;
            Double _voteCount;
            Boolean _video;
            String _posterPath;
            Boolean _adult;
            String _backdropPath;
            String _originalTitle;
            String _originalLanguage;
            String _title;
            Double _voteAverage;
            String _overview;
            String _releaseDate;

            JSONObject movieJson = jsonResults.getJSONObject(i);

            _id = movieJson.getInt(ID);
            _popularity = movieJson.getDouble(POPULARITY);
            _voteCount = movieJson.getDouble(VOTE_COUNT);
            _video = movieJson.getBoolean(VIDEO);
            _posterPath = movieJson.getString(POSTER_PATH);

            _adult = movieJson.getBoolean(ADULT);
            _backdropPath = movieJson.getString(BACKDROP_PATH);
            _originalTitle = movieJson.getString(ORIGINAL_TITLE);
            _originalLanguage = movieJson.getString(ORIGINAL_LANGUAGE);
            _title = movieJson.getString(TITLE);
            _voteAverage = movieJson.getDouble(VOTE_AVERAGE);
            _overview = movieJson.getString(OVERVIEW);
            _releaseDate = movieJson.getString(RELEASE_DATE);

            JSONArray genreIdsJson= movieJson.getJSONArray(GENRE_IDS);
            ArrayList _genreIds = new ArrayList(genreIdsJson.length());
            for (int j = 0; j < genreIdsJson.length(); j++) {
                _genreIds.add(genreIdsJson.get(j));
            }

            Movie _movie = new Movie(
                     _id,
                     _title,
                     _voteAverage,
                     _popularity,
                     _voteCount,
                     _video,
                     _posterPath,
                     _adult,
                     _backdropPath,
                     _originalLanguage,
                     _originalTitle,
                     _overview,
                     _releaseDate,
                    _genreIds
            );

            movies.add(_movie);
        }

        return movies;
    }
}
