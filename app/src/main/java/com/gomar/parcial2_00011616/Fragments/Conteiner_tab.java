package com.gomar.parcial2_00011616.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.gomar.parcial2_00011616.Adapter.News_Adapter;
import com.gomar.parcial2_00011616.Adapter.Players_Adapter;
import com.gomar.parcial2_00011616.Adapter.ViewPagerAdapter;
import com.gomar.parcial2_00011616.Entity.NewsEntity;
import com.gomar.parcial2_00011616.Entity.PlayersEntity;
import com.gomar.parcial2_00011616.Model.ViewModel;
import com.gomar.parcial2_00011616.R;

import java.util.List;

public class Conteiner_tab extends Fragment {

    private String game;
    private ViewModel model;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private News_Adapter newsAdapter;
    private Players_Adapter playersAdapter;


    public Conteiner_tab() {
    }

    public static Conteiner_tab newInstance(String game){
        Conteiner_tab fragment = new Conteiner_tab();
        fragment.setGame(game);
        return fragment;
    }

    public void setGame(String game){
        this.game = game;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        model = ViewModelProviders.of(this).get(ViewModel.class);
        model.getNewsByGame(game).observe(this, new Observer<List<NewsEntity>>() {
            @Override
            public void onChanged(@Nullable List<NewsEntity> newList) {
                newsAdapter.fillNews(newList);

            }
        });
        model.getPlayersByGame(game).observe(this, new Observer<List<PlayersEntity>>() {
            @Override
            public void onChanged(@Nullable List<PlayersEntity> playerList) {
                playersAdapter.fillPlayers(playerList);
            }
        });


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.conteiner_tab,container,false);
        tabLayout = view.findViewById(R.id.tablayout_fragment_container);
        viewPager = view.findViewById(R.id.viewpager_fragment_container);

        newsAdapter = new News_Adapter(view.getContext());
        playersAdapter = new Players_Adapter(view.getContext());

        viewPager.setAdapter(viewPagerAdapter);

        viewPagerAdapter.addFragment(NewsGame.newInstance(newsAdapter),"News");
        viewPagerAdapter.addFragment(TopPlayer.newInstance(playersAdapter),"TOP PLAYERS");

        viewPagerAdapter.notifyDataSetChanged();

        tabLayout.setupWithViewPager(viewPager);
        //viewPager.setCurrentItem(0);
        return view;
    }
}