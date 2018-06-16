package com.gomar.parcial2_00011616;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.gomar.parcial2_00011616.API.GamesNewsApi;
import com.gomar.parcial2_00011616.Activity.MainActivity;
import com.gomar.parcial2_00011616.Entity.CategoryEntity;
import com.gomar.parcial2_00011616.API.Deserializer.FavDeserializer;
import com.gomar.parcial2_00011616.API.Deserializer.NewsDeserializer;
import com.gomar.parcial2_00011616.API.Deserializer.PlayerDeserializer;
import com.gomar.parcial2_00011616.API.Deserializer.UserDeserializer;
import com.gomar.parcial2_00011616.Entity.FavEntity;
import com.gomar.parcial2_00011616.Entity.NewsEntity;
import com.gomar.parcial2_00011616.Entity.PlayersEntity;
import com.gomar.parcial2_00011616.Entity.UserEntity;
import com.gomar.parcial2_00011616.API.Response.FavoriteResponse;
import com.gomar.parcial2_00011616.API.Response.NewsResponse;
import com.gomar.parcial2_00011616.API.Response.PlayersResponse;
import com.gomar.parcial2_00011616.API.Response.UserResponse;
import com.gomar.parcial2_00011616.Room.CategoryDao;
import com.gomar.parcial2_00011616.Room.FavDao;
import com.gomar.parcial2_00011616.Room.GameRoomDatabase;
import com.gomar.parcial2_00011616.Room.NewsDao;
import com.gomar.parcial2_00011616.Room.PlayerDao;
import com.gomar.parcial2_00011616.Room.UserDao;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {
    private GamesNewsApi api;
    private CompositeDisposable disposable = new CompositeDisposable();
    private UserDao userDao;
    private NewsDao newDao;
    private PlayerDao playerDao;
    private CategoryDao gameDao;
    private FavDao favoriteDAO;

    private LiveData<List<UserEntity>> userList;
    private LiveData<List<NewsEntity>> newList;
    private LiveData<List<PlayersEntity>> playerList;
    private LiveData<List<CategoryEntity>> gameList;
    private LiveData<UserEntity> currentUser;
    private LiveData<List<FavEntity>> favoriteList;

    public Repository(Application application){
        GameRoomDatabase db = GameRoomDatabase.getDatabase(application);
        userDao = db.userDao();
        newDao = db.newDao();
        playerDao = db.playerDao();
        gameDao = db.categoryGameDao();
        favoriteDAO = db.favoriteDAO();

        userList = userDao.getAllUsers();
        newList = newDao.getAllNews();
        playerList = playerDao.getAllPlayer();
        gameList = gameDao.getAllCategories();
        currentUser = userDao.getCurrentUser();
        favoriteList = favoriteDAO.getAllFavorite();
        createAPI();
    }

    /**
     *GETTERS
     */

    public LiveData<List<FavEntity>> getAllFavorites(){
        return favoriteList;
    }
    public LiveData<UserEntity> getCurrentUser(){
        return currentUser;
    }
    public LiveData<List<CategoryEntity>> getAllGames(){
        api = getGamesFromAPI();
        disposable.add(api.getGameList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getGameList()));
        return gameList;
    }
    public LiveData<NewsEntity> getNew(String id){ return newDao.getNew(id); }
    public LiveData<List<NewsEntity>> getNewsByGame(String game){
        newList = newDao.getNewsByCategory(game);
        return newList;
    }
    public LiveData<List<PlayersEntity>> getPlayersByGame(String game){
        playerList = playerDao.getAllPlayerByGame(game);
        return playerList;
    }
    public LiveData<List<PlayersEntity>> getAllPlayer(){
        return playerList;
    }
    public LiveData<List<UserEntity>> getAllUsers(){
        return userList;
    }
    public LiveData<List<NewsEntity>> getAllNews() {
        return newList;
    }
    public LiveData<List<NewsEntity>> getFavoritesObjectNews(){ return newDao.getFavoritesNews(); }

    /**
     * SETTERS
     */

    public void addFavoriteNew(String idUser,String idNew){
        api = createAddFavRequest();
        disposable.add(api.addFavorite(idUser,idNew)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(addFavObserver()));
    }
    public void removeFavoriteNew(String User,String idNew){
        api = createAddFavRequest();
        disposable.add(api.removeFavorite(User,idNew)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(removeFavObserver()));
    }
    public void exectInserFavorite(FavEntity fab){
        insertFavorite(fab);
    }
    /**
     * Metodos para obtener informacion de la API
     */
    public void refreshCurrentUser(){
        api = getCurrentUserByRepo();
        disposable.add(api.getCurrentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getUserLogged()));
    }
    public void refreshNews(){
        disposable.add(api.getNewsByRepo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getNewsRepoObserver()));
    }
    public void refreshFavoritesListID(){
        api = getFavoritesNoticesByRepo();
        disposable.add(api.getFavoritesListUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getFavoritesObserver()));
    }
    public void refreshTopPlayers(){
        api = getPlayersFromAPI();
        disposable.add(api.getAllPlayers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getPlayerRepoResponse()));
    }


    /**
     *METODOS QUE EJECTUTAN LOS THREADS
     */
    public void insertGame(CategoryEntity game){
        new categoryInsertAsyncTask(gameDao).execute(game);
    }

    public void insertUser(UserEntity user){
        new userInsertAsyncTask(userDao).execute(user);
    }

    public void insertNews(NewsEntity news){
        new newsInsertAsyncTask(newDao).execute(news);
    }

    public void insertPlayer(PlayersEntity player){
        new playerInsertAsyncTask(playerDao).execute(player);
    }

    private void insertFavorite(FavEntity fab) {
        new favoritesInsertAsyncTask(favoriteDAO).execute(fab);
    }

    public void updateFavNewState(String value, String idNew){
        new favoritesUpdateAsyncTask(newDao).execute(value,idNew);
    }
    public void deleteAllFavotitesID(){
        new deleteAllFavotitesIDAsyncTask(favoriteDAO).execute();
    }

    public void deleteAllUsers(){
        new deleteAllUsersAsyncTask(userDao).execute();
    }





    /**
     * CREACION DE THREADS ENCARGADOS DE LA INSERCION DE DATOS EN LA BASE DE DATOS
     */
    private static class categoryInsertAsyncTask extends AsyncTask<CategoryEntity,Void,Void>{
        private CategoryDao gameDao;

        public categoryInsertAsyncTask(CategoryDao gameDao){
            this.gameDao = gameDao;
        }
        @Override
        protected Void doInBackground(CategoryEntity... CategoryEntity) {
            gameDao.insertCategory(CategoryEntity[0]);
            return null;
        }
    }
    private static class playerInsertAsyncTask extends AsyncTask<PlayersEntity,Void,Void>{
        private PlayerDao playerDao;

        public playerInsertAsyncTask(PlayerDao playerDao){
            this.playerDao = playerDao;
        }
        @Override
        protected Void doInBackground(PlayersEntity... players) {
            playerDao.insertPayer(players[0]);
            return null;
        }
    }
    private static class userInsertAsyncTask extends AsyncTask<UserEntity,Void,Void>{
        private UserDao userDao;

        public userInsertAsyncTask(UserDao userDao){
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(final UserEntity... users) {
            userDao.insertUser(users[0]);
            return null;
        }
    }
    private  static class newsInsertAsyncTask extends AsyncTask<NewsEntity,Void,Void>{
        private NewsDao newDao;

        public newsInsertAsyncTask(NewsDao newDao){
            this.newDao = newDao;
        }

        @Override
        protected Void doInBackground(NewsEntity... news) {
            newDao.insertNew(news[0]);
            return null;
        }
    }
    private static class favoritesInsertAsyncTask extends AsyncTask<FavEntity,Void,Void>{

        private FavDao favoriteDAO;

        public favoritesInsertAsyncTask(FavDao favoriteDAO){
            this.favoriteDAO = favoriteDAO;
        }

        @Override
        protected Void doInBackground(FavEntity... favorites) {
            favoriteDAO.insertFavorite(favorites[0]);
            return null;
        }
    }
    private static class favoritesUpdateAsyncTask extends AsyncTask<String,Void,Void>{
        private NewsDao dao;

        public favoritesUpdateAsyncTask(NewsDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(String... values) {
            Log.d("UPDATE_VALUES",values[0] +" "+ values[1]);
            dao.updateFavoriteState(Integer.parseInt(values[0]),values[1]);
            return null;
        }
    }

    private static class deleteAllFavotitesIDAsyncTask extends AsyncTask<Void,Void,Void>{
        private FavDao dao;
        public deleteAllFavotitesIDAsyncTask(FavDao dao){
            this.dao =dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            return null;
        }
    }
    private static class deleteAllUsersAsyncTask extends AsyncTask<Void,Void,Void>{
        private UserDao userDao;
        public deleteAllUsersAsyncTask(UserDao userDao){
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userDao.deleteAllUser();
            return null;
        }
    }



    private void createAPI(){
        Gson gson = new GsonBuilder()
                .setDateFormat("dd/MM/yyyy")
                .registerTypeAdapter(NewsResponse.class,new NewsDeserializer())
                .create();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Authorization","Bearer "+ MainActivity.securityToken.getTokenSecurity())
                                .build();
                        return chain.proceed(newRequest);
                    }

                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GamesNewsApi.ENDPOINT)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        api = retrofit.create(GamesNewsApi.class);

    }
    private DisposableSingleObserver<List<NewsResponse>> getNewsRepoObserver(){
        return new DisposableSingleObserver<List<NewsResponse>>() {
            @Override
            public void onSuccess(List<NewsResponse> news) {
                if(!news.isEmpty()){
                    for(NewsResponse notice:news){
                        NewsEntity newNotice = new NewsEntity();
                        newNotice.set_id(notice.get_id());
                        newNotice.setTitle(notice.getTitle());
                        newNotice.setDescription(notice.getDescription());
                        newNotice.setCoverImage(notice.getCoverImage());
                        newNotice.setBody(notice.getBody());
                        newNotice.setGame(notice.getGame());
                        insertNews(newNotice);
                    }
                    refreshFavoritesListID();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d("ERROR_REPO_USER: ",e.getMessage());
            }
        };
    }

    private GamesNewsApi getPlayersFromAPI(){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(PlayersResponse.class,new PlayerDeserializer())
                .create();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Authorization","Bearer "+ MainActivity.securityToken.getTokenSecurity())
                                .build();
                        return chain.proceed(newRequest);
                    }

                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GamesNewsApi.ENDPOINT)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        api = retrofit.create(GamesNewsApi.class);
        return api;
    }
    private DisposableSingleObserver<List<PlayersResponse>> getPlayerRepoResponse(){
        return new DisposableSingleObserver<List<PlayersResponse>>() {
            @Override
            public void onSuccess(List<PlayersResponse> players) {
                if(!players.isEmpty()){
                    for(PlayersResponse player:players){
                        PlayersEntity p = new PlayersEntity();
                        p.set_id(player.get_id());
                        p.setAvatar(player.getAvatar());
                        p.setBiografia(player.getBiografia());
                        p.setGame(player.getGame());
                        p.setName(player.getName());
                        insertPlayer(p);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d("ERROR_REPO",e.getMessage());
            }
        };
    }

    private GamesNewsApi getGamesFromAPI(){
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("Authorization","Bearer "+ MainActivity.securityToken.getTokenSecurity())
                                .build();
                        return chain.proceed(request);
                    }
                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GamesNewsApi.ENDPOINT)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(GamesNewsApi.class);
    }
    private DisposableSingleObserver<List<String>> getGameList(){
        return new DisposableSingleObserver<List<String>>() {
            @Override
            public void onSuccess(List<String> games) {
                if(!games.isEmpty()){
                    for(String game:games){
                        insertGame(new CategoryEntity(game));
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d("ERROR_GAME_LIST",e.getMessage());
            }
        };
    }

    private GamesNewsApi getCurrentUserByRepo(){
        Gson gson = new GsonBuilder()
                .setDateFormat("dd/MM/yyyy")
                .registerTypeAdapter(UserResponse.class,new UserDeserializer())
                .create();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Authorization","Bearer "+ MainActivity.securityToken.getTokenSecurity())
                                .build();
                        return chain.proceed(newRequest);
                    }

                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GamesNewsApi.ENDPOINT)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        api = retrofit.create(GamesNewsApi.class);
        return api;
    }
    private DisposableSingleObserver<UserResponse> getUserLogged(){
        return new DisposableSingleObserver<UserResponse>() {
            @Override
            public void onSuccess(UserResponse value) {
                UserEntity user = new UserEntity();
                user.set_id(value.get_id());
                user.setUsername(value.getUsername());
                user.setAvatar(value.getAvatar());
                user.setPassword(value.getPassword());
                user.setCreateDate(value.getCreateDate());
                insertUser(user);
            }

            @Override
            public void onError(Throwable e) {
                //Log.d("ERROR_GET_USER",e.getMessage());
            }
        };
    }

    private GamesNewsApi getFavoritesNoticesByRepo(){
        Gson gson = new GsonBuilder()
                .setDateFormat("dd/MM/yyyy")
                .registerTypeAdapter(FavoriteResponse.class,new FavDeserializer())
                .registerTypeAdapter(NewsResponse.class,new NewsDeserializer())
                .create();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Authorization","Bearer "+ MainActivity.securityToken.getTokenSecurity())
                                .build();
                        return chain.proceed(newRequest);
                    }

                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GamesNewsApi.ENDPOINT)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        api = retrofit.create(GamesNewsApi.class);
        return api;
    }
    private DisposableSingleObserver<FavoriteResponse> getFavoritesObserver(){
        return new DisposableSingleObserver<FavoriteResponse>() {
            @Override
            public void onSuccess(FavoriteResponse values) {
                deleteAllFavotitesID();
                for(String value:values.get_id()){
                    insertFavorite(new FavEntity(value));
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d("FAVORITESIDREPO",e.getMessage());
            }
        };
    }

    private GamesNewsApi createAddFavRequest(){
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("Authorization","Bearer "+ MainActivity.securityToken.getTokenSecurity())
                                .build();
                        return chain.proceed(request);
                    }
                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GamesNewsApi.ENDPOINT)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(GamesNewsApi.class);
    }
    private DisposableSingleObserver<Void> addFavObserver(){
        return new DisposableSingleObserver<Void>() {
            @Override
            public void onSuccess(Void value) {

            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }
    private DisposableSingleObserver<Void> removeFavObserver(){
        return new DisposableSingleObserver<Void>() {
            @Override
            public void onSuccess(Void value) {

            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }

}