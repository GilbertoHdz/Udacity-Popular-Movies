package mx.com.ghg.movies.api.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mx.com.ghg.movies.api.models.Review;

public final class ReviewJsonUtils {

    private static final String ID = "id";
    private static final String AUTHOR = "author";
    private static final String CONTENT = "content";
    private static final String URL = "url";
    private static final String RESULTS = "results";

    public static ArrayList<Review> getMovieReviewFromJson(
            String strJsonReview
    ) throws JSONException {
        JSONObject reviewsJson = new JSONObject(strJsonReview);

        JSONArray jsonResults = reviewsJson.getJSONArray(RESULTS);
        ArrayList reviews = new ArrayList(jsonResults.length());

        for (int i = 0; i < jsonResults.length(); i++) {
            String _id;
            String _author;
            String _content;
            String _url;

            JSONObject reviewJson = jsonResults.getJSONObject(i);

            _id = reviewJson.getString(ID);
            _author = reviewJson.getString(AUTHOR);
            _content = reviewJson.getString(CONTENT);
            _url = reviewJson.getString(URL);

            Review review = new Review(_id, _author, _content, _url);

            reviews.add(review);
        }

        return reviews;
    }
}
