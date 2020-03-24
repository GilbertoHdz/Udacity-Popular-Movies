package mx.com.ghg.movies.db.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "video")
public class VideoEntity {

    @PrimaryKey @NonNull
    private String id;
    private String name;
    private String key;

    public VideoEntity(String id, String name, String key) {
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
