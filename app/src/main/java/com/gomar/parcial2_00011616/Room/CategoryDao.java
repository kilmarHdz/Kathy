package com.gomar.parcial2_00011616.Room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM table_category")
    LiveData<List<CategoryDao>> getAllCategories();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategory(CategoryDao categoryGame);
}
