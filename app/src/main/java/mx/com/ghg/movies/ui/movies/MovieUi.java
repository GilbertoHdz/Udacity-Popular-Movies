package mx.com.ghg.movies.ui.movies;

import java.io.Serializable;

import mx.com.ghg.movies.api.models.Movie;
import mx.com.ghg.movies.db.entities.PopularEntity;
import mx.com.ghg.movies.db.entities.TopRatedEntity;

public class MovieUi implements Serializable {

    Integer id;
    String title;
    String image;
    String synopsis; // overview
    Double rating; // vote_average
    String releaseDate;

    public MovieUi(Movie movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.image = movie.getPosterPath();
        this.synopsis = movie.getOverview();
        this.rating = movie.getVoteAverage();
        this.releaseDate = movie.getReleaseDate();
    }

    public MovieUi(TopRatedEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.image = entity.getImage();
        this.synopsis = entity.getSynopsis();
        this.rating = entity.getRating();
        this.releaseDate = entity.getReleaseDate();
    }

    public MovieUi(PopularEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.image = entity.getImage();
        this.synopsis = entity.getSynopsis();
        this.rating = entity.getRating();
        this.releaseDate = entity.getReleaseDate();
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
