package com.gomar.parcial2_00011616.Room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.gomar.parcial2_00011616.Entity.FavEntity;

import java.util.List;

public interface FavDao {

        @Query("SELECT * FROM table_fav")
        LiveData<List<FavEntity>> getAllFavorite();

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertFavorite(FavEntity favorite);

        @Query("DELETE FROM table_fav")
        void deleteAll();

}
