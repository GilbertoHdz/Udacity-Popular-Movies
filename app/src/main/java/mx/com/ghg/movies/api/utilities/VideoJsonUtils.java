package mx.com.ghg.movies.api.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mx.com.ghg.movies.api.models.Video;

public final class VideoJsonUtils {

    private static final String RESULTS = "results";
    private static final String ID = "id";
    private static final String ISO_639 = "iso_639_1";
    private static final String ISO_3166 = "iso_3166_1";
    private static final String KEY = "key";
    private static final String NAME = "name";
    private static final String SITE = "site";
    private static final String SIZE = "size";
    private static final String TYPE = "type";

    public static ArrayList<Video> getVideosFromJson(
            String strVideoJson
    ) throws JSONException {
        JSONObject videoJson = new JSONObject(strVideoJson);

        JSONArray jsonResults = videoJson.getJSONArray(RESULTS);
        ArrayList videos = new  ArrayList(jsonResults.length());

        for (int i = 0; i < jsonResults.length(); i++) {

            String _id;
            String _iso639;
            String _iso3166;
            String _key;
            String _name;
            String _site;
            Integer _size;
            String _type;

            JSONObject _videoJson = jsonResults.getJSONObject(i);

            _id = _videoJson.getString(ID);
            _iso639 = _videoJson.getString(ISO_639);
            _iso3166 = _videoJson.getString(ISO_3166);
            _key = _videoJson.getString(KEY);
            _name = _videoJson.getString(NAME);
            _site = _videoJson.getString(SITE);
            _size = _videoJson.getInt(SIZE);
            _type = _videoJson.getString(TYPE);

            Video video = new Video(
                    _id,
                    _iso639,
                    _iso3166,
                    _key,
                    _name,
                    _site,
                    _size,
                    _type
            );

            videos.add(video);
        }

        return videos;
    }
}
