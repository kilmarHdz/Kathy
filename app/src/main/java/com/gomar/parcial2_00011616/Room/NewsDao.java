package com.gomar.parcial2_00011616.Room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.gomar.parcial2_00011616.Entity.NewsEntity;

import java.util.List;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM table_news ORDER BY created_Date DESC")
    LiveData<List<NewsEntity>> getAllNews();

    @Query("SELECT * FROM table_news WHERE game = :game ORDER BY created_Date DESC")
    LiveData<List<NewsEntity>> getNewsByCategory(String game);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNew(NewsEntity news);
    @Query("SELECT * FROM table_news WHERE id = :id")
    LiveData<NewsEntity> getNewInfo(String id);

    @Query("DELETE FROM table_news")
    void deleteAll();
}


