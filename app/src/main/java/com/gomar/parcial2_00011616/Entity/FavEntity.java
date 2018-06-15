package com.gomar.parcial2_00011616.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "table_fav")
public class FavEntity {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "_id")
    private String _id;

    public FavEntity(@NonNull String _id) {
        this._id = _id;
    }

    @NonNull
    public String get_id() {
        return _id;
    }

    @Override
    public String toString() {
        return "Favorite{" +
                "_id='" + _id + '\'' +
                '}';
    }
}