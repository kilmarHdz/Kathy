package com.gomar.parcial2_00011616.Room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.gomar.parcial2_00011616.Entity.UserEntity;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM table_user")
    LiveData<UserEntity> getCurrentUser();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserEntity user);
    @Update
    void modifyUser(UserEntity... users);
    @Query("SELECT * FROM table_user WHERE username = :username")
    UserEntity getUser(String username);
    @Query("SELECT * FROM table_user")
    LiveData<List<UserEntity>> getAllUsers();
    @Query("DELETE FROM table_user")
    void deleteAllUser();
}