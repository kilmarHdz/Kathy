package com.gomar.parcial2_00011616.Entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
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

    public NewsEntity(@NonNull String id, String title, String body, String game, String description, String coverImage, String created_date) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.game = game;
        this.description = description;
        this.coverImage = coverImage;
        created_Date = created_date;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getCreated_Date() {
        return created_Date;
    }

    public void setCreated_Date(String created_Date) {
        this.created_Date = created_Date;
    }
}
