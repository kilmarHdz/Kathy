package com.gomar.parcial2_00011616.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gomar.parcial2_00011616.Adapter.News_Adapter;
import com.gomar.parcial2_00011616.Entity.NewsEntity;
import com.gomar.parcial2_00011616.Model.ViewModel;
import com.gomar.parcial2_00011616.R;

import java.util.List;

public class Favorito extends Fragment {

    private FavoriteNewsTools tools;
    private List<NewsEntity> favoritesNews;
    private ViewModel model;
    private RecyclerView recyclerView;
    private News_Adapter adapter;
    private SwipeRefreshLayout refreshContent;


    public Favorito() {
    }

    public static Favorito newInstance(List<NewsEntity> favoritesNews){
        Favorito fragment = new Favorito();
        fragment.setFavoritesNews(favoritesNews);
        return fragment;
    }

    public void setFavoritesNews(List<NewsEntity> favoritesNews){
        this.favoritesNews = favoritesNews;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_game,container,false);
        recyclerView = view.findViewById(R.id.recyclerview_news_by_games);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshContent = view.findViewById(R.id.refreshContent);
        refreshContent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tools.refreshFavorites();
                refreshContent.setRefreshing(false);
            }
        });
        adapter = new News_Adapter(getActivity());
        adapter.fillNews(favoritesNews);
        recyclerView.setAdapter(adapter);
        return view;
    }

    public interface FavoriteNewsTools{
        void refreshFavorites();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof FavoriteNewsTools){
            tools = (FavoriteNewsTools) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        tools=null;
    }
}