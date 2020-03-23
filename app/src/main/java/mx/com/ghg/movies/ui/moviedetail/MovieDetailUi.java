package mx.com.ghg.movies.ui.moviedetail;

import java.io.Serializable;

public class MovieDetailUi implements Serializable {

    private String id;
    private String name;
    private String key;

    public MovieDetailUi(String id, String name, String key) {
        this.id = id;
        this.name = name;
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYoutubeUrl() {
        return "https://www.youtube.com/watch?v=" + this.key;
    }
}
