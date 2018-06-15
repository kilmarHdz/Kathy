package com.gomar.parcial2_00011616;

import com.gomar.parcial2_00011616.Response.NewsResponse;
import com.gomar.parcial2_00011616.Response.PlayersResponse;
import com.gomar.parcial2_00011616.Response.UserResponse;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface GamesNewsApi {

    String  LINKEND = "http://gamenewsuca.herokuapp.com";

    @POST("/login")
    @FormUrlEncoded
    Single<Token> getSecurityToken(@Field("user")String username,@Field("password")String password);


    @GET("/users/detail")
    Single<UserResponse> getCurrentUser();


    /**ENDPOINTS ABOUT NEWS**/
    @GET("/news")
    Single<List<NewsResponse>> getNewsbyRepository();

    @GET("/news/type/list")
    Single<List<String>> getGameList();

    /**ENDS POINTS ABOUT PLAYERS**/
    @GET("/players")
    Single<List<PlayersResponse>> getAllPlayers();
}
