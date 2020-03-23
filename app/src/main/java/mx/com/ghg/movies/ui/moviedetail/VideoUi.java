package mx.com.ghg.movies.ui.moviedetail;

import java.io.Serializable;

public class VideoUi extends MovieDetailUi implements Serializable {

    private String name;
    private String key;

    public VideoUi(String id, String name, String key) {
        super(id);
        this.name = name;
        this.key = key;
    }

    public String getId() {
        return super.getId();
    }

    public void setId(String id) {
        super.setId(id);
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

