package mx.com.ghg.movies.ui.moviedetail;

import java.io.Serializable;

public class ReviewUi extends MovieDetailUi implements Serializable {

    private String author;
    private String content;
    private String url;

    public ReviewUi(String id, String author, String content, String url) {
        super(id);
        this.author = author;
        this.content = content;
        this.url = url;
    }

    public String getId() {
        return super.getId();
    }

    public void setId(String id) {
        super.setId(id);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

