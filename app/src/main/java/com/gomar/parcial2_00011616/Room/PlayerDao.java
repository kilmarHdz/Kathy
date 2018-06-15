package com.gomar.parcial2_00011616.Room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.gomar.parcial2_00011616.Entity.PlayersEntity;

import java.util.List;

    @Dao
    public interface PlayerDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertPayer(PlayersEntity player);

        @Query("SELECT * FROM table_players")
        LiveData<List<PlayersEntity>> getAllPlayer();

        @Query("SELECT * FROM table_players WHERE game = :game")
        LiveData<List<PlayersEntity>> getAllPlayerByGame(String game);

    }

