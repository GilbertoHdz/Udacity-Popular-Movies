package mx.com.ghg.movies.ui;

import java.io.Serializable;

public class MovieUi implements Serializable {

    Integer id;
    String title;
    String image;
    String synopsis; // overview
    Double rating; // vote_average
    String releaseDate;

    public MovieUi(
            Integer id,
            String title,
            String image,
            String synopsis,
            Double rating,
            String releaseDate
    ) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.synopsis = synopsis;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return "http://image.tmdb.org/t/p/w185" + image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
