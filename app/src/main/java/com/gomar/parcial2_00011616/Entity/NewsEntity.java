package com.gomar.parcial2_00011616.Entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "table_news")

public class NewsEntity {

    @PrimaryKey
    @NonNull

    @ColumnInfo(name="id")
    private String id;
    @ColumnInfo(name="title")
    private String title;
    @ColumnInfo(name="body")
    private String body;
    @ColumnInfo(name="game")
    private String game;
    @ColumnInfo(name="description")
    private String description;
    @ColumnInfo(name="coverImage")
    private String coverImage;
    @ColumnInfo(name="created_Date")
    private String created_Date;
    @ColumnInfo(name = "favorite")
    private int favorite=0;

    @Ignore
    public NewsEntity(){}

    public NewsEntity(@NonNull String _id, String title, @NonNull String coverImage, String created_date, String description, String body, @NonNull String game) {
        this.id = _id;
        this.title = title;
        this.coverImage = coverImage;
        this.created_Date = created_date;
        this.description = description;
        this.body = body;
        this.game = game;
    }

    public void setId(@NonNull String _id) {
        this.id = _id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCoverImage(@NonNull String coverImage) {
        this.coverImage = coverImage;
    }

    public void setCreated_Date(String created_date) {
        this.created_Date = created_date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setGame(@NonNull String game) {
        this.game = game;
    }

    public String get_id() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public String getCreated_Date() {
        return created_Date;
    }

    public String getDescription() {
        return description;
    }

    public String getBody() {
        return body;
    }

    public String getGame() {
        return game;
    }

    @NonNull
    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(@NonNull int favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return "New{" +
                "_id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", coverImage='" + coverImage + '\'' +
                ", created_date='" + created_Date + '\'' +
                ", description='" + description + '\'' +
                ", body='" + body + '\'' +
                ", game='" + game + '\'' +
                '}';
    }

}
