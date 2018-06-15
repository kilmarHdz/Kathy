package com.gomar.parcial2_00011616.Room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.gomar.parcial2_00011616.Entity.CategoryEntity;
import com.gomar.parcial2_00011616.Entity.FavEntity;
import com.gomar.parcial2_00011616.Entity.NewsEntity;
import com.gomar.parcial2_00011616.Entity.PlayersEntity;
import com.gomar.parcial2_00011616.Entity.UserEntity;



@Database(entities = {NewsEntity.class, UserEntity.class, PlayersEntity.class, CategoryEntity.class, FavEntity.class},version = 1)
public abstract class GameRoomDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract NewsDao newDao();
    public abstract PlayerDao playerDao();
    public abstract CategoryDao categoryGameDao();
    public abstract FavDao favoriteDAO();


    private static GameRoomDatabase INSTANCE;

    public static GameRoomDatabase getDatabase(final Context context){
        if(INSTANCE==null){
            synchronized (GameRoomDatabase.class){
                if(INSTANCE==null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),GameRoomDatabase.class,"game_news_db")
                            .addCallback(roomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            new CleanUserCache(INSTANCE).execute();
        }
    };

    private static class CleanUserCache extends AsyncTask<UserEntity,Void,Void>{

        private UserDao userDao;
        public CleanUserCache(GameRoomDatabase db){
            userDao = db.userDao();
        }

        @Override
        protected Void doInBackground(UserEntity... users) {
            userDao.deleteAllUser();
            return null;
        }
    }
}
