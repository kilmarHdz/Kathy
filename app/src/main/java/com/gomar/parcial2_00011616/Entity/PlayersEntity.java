package com.gomar.parcial2_00011616.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "table_players")

public class PlayersEntity {

        @PrimaryKey
        @NonNull
        @ColumnInfo(name = "id")
        private String _id;
        @NonNull
        @ColumnInfo(name = "name")
        private String name;
        @NonNull
        @ColumnInfo(name = "game")
        private String game;


        @NonNull
        public String get_id() {
            return _id;
        }

        public void set_id(@NonNull String _id) {
            this._id = _id;
        }

        @NonNull
        public String getName() {
            return name;
        }

        public void setName(@NonNull String name) {
            this.name = name;
        }


        @NonNull
        public String getGame() {
            return game;
        }

        public void setGame(@NonNull String game) {
            this.game = game;
        }

        @Override
        public String toString() {
            return "PlayersEntity{" +
                    "id='" + _id + '\'' +
                    ", name='" + name + '\'' +
                    ", game='" + game + '\'' +
                    '}';
        }
    }
