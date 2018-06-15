package com.gomar.parcial2_00011616;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.gomar.parcial2_00011616.Entity.CategoryEntity;
import com.gomar.parcial2_00011616.Entity.FavEntity;
import com.gomar.parcial2_00011616.Entity.NewsEntity;
import com.gomar.parcial2_00011616.Entity.PlayersEntity;
import com.gomar.parcial2_00011616.Entity.UserEntity;

import java.util.List;


public class ViewModel {

    private Repository gameNewsRepository;
    private LiveData<List<NewsEntity>> newList;
    private LiveData<List<PlayersEntity>> playerList;
    private LiveData<UserEntity> currentUser;

    public ViewModel(@NonNull Application application) {
        super(application);
        gameNewsRepository = new Repository(application);
        newList = Repository.getAllNews();
        playerList = Repository.getAllPlayer();
    }

    public void refreshNews(){
        gameNewsRepository.refreshNews();
    }

    public LiveData<List<NewsEntity>> getFavoriteObjectNews(){
        return gameNewsRepository.getFavoritesObjectNews();
    }
    public void updateNewFaState(String value,String idNew){
        gameNewsRepository.updateFavNewState(value,idNew);
    }


    public void addToFavList(FavEntity favorite){
        gameNewsRepository.exectInserFavorite(favorite);
    }

    public LiveData<List<FavEntity>> getFavorieList(){
        return gameNewsRepository.getAllFavorites();
    }
    public LiveData<UserEntity> getCurrentUser(){
        currentUser = gameNewsRepository.getCurrentUser();
        return currentUser;
    }

    public LiveData<List<NewsEntity>> getAllNews() {
        return newList;
    }

    public LiveData<List<NewsEntity>> getNewsByGame(String game){
        newList = gameNewsRepository.getNewsByGame(game);
        return newList;
    }
    public LiveData<NewsEntity> getNew(String id){
        return gameNewsRepository.getNew(id);
    }

    public LiveData<List<PlayersEntity>> getPlayersByGame(String game){
        playerList = gameNewsRepository.getPlayersByGame(game);
        return playerList;
    }
    public LiveData<List<PlayersEntity>> getAllPlayers(){
        playerList = gameNewsRepository.getAllPlayer();
        return playerList;
    }

    public LiveData<List<CategoryEntity>> getGameList() {
        LiveData<List<CategoryEntity>> gameList = gameNewsRepository.getAllGames();
        return gameList;
    }
}
