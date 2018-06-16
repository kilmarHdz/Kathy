package com.gomar.parcial2_00011616.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gomar.parcial2_00011616.Adapter.News_Adapter;
import com.gomar.parcial2_00011616.R;


public class NewsGame extends Fragment {

    View view;
    private News_Adapter adapter;
    private RecyclerView rv;
    public NewsGame() {
        // Required empty public constructor
    }

    public static NewsGame newInstance(News_Adapter adapter) {
        NewsGame fragment = new NewsGame();
        fragment.setAdapter(adapter);
        return fragment;
    }
    private void setAdapter(News_Adapter adapter){
        this.adapter = adapter;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news_game, container, false);
        rv = view.findViewById(R.id.recyclerview_news_by_games);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);
        return view;
    }

}