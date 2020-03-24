package mx.com.ghg.movies.db.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "popular_movie")
public class PopularEntity {

    @PrimaryKey private int id;
    private String title;
    private String image;
    private String synopsis; // overview
    private Double rating; // vote_average
    private String releaseDate;

    public PopularEntity(
            int id,
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
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
