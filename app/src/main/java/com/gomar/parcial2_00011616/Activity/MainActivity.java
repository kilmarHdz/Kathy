package com.gomar.parcial2_00011616.Activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.gomar.parcial2_00011616.Adapter.News_Adapter;
import com.gomar.parcial2_00011616.Entity.CategoryEntity;
import com.gomar.parcial2_00011616.Entity.FavEntity;
import com.gomar.parcial2_00011616.Entity.NewsEntity;
import com.gomar.parcial2_00011616.Entity.SecurityToken;
import com.gomar.parcial2_00011616.Entity.UserEntity;
import com.gomar.parcial2_00011616.Fragments.Conteiner_tab;
import com.gomar.parcial2_00011616.Fragments.Favorito;
import com.gomar.parcial2_00011616.Fragments.MainNews;
import com.gomar.parcial2_00011616.Fragments.TopPlayer;
import com.gomar.parcial2_00011616.Model.ViewModel;
import com.gomar.parcial2_00011616.R;
import com.gomar.parcial2_00011616.Room.FavTools;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MainNews.MainSetters ,
        Favorito.FavoriteNewsTools,
        TopPlayer.TopPlayersTools,
        FavTools{


    public static SecurityToken securityToken;
    private final static int ID_INFLATED_MENU = 101010101;
    private News_Adapter newsAdapter;
    private ViewModel viewModel;
    private ImageView avatar;
    private TextView username,created_date;
    private List<CategoryEntity> gameList;
    private ActionBar actionBar;
    public static String TOKEN_SECURITY = "SECURITY_PREFERENCE_TOKEN";
    private UserEntity currentUser;
    private List<FavEntity> idNewList;
    private List<NewsEntity> favoritesNewList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String value = getApplicationContext().getSharedPreferences("Token",MODE_PRIVATE).getString(TOKEN_SECURITY,"");
        securityToken = new SecurityToken(value);
        Log.d("TOKEN",securityToken.getTokenSecurity());
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        executeLists();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.screen_fragment, new MainNews());
        ft.commit();

    }

    private void executeLists() {
        newsAdapter = new News_Adapter(this);
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);

        viewModel.getCurrentUser();
        viewModel.refreshNews();
        viewModel.refreshNewsListID();
        viewModel.refreshTopPlayers();

        viewModel.getCurrentUser().observe(this, new Observer<UserEntity>() {
            @Override
            public void onChanged(@Nullable UserEntity user) {
                if (user != null) {
                    currentUser = user;
                    if(currentUser!=null) {
                        initControls(currentUser);
                    }
                }
            }
        });

        viewModel.getGameList().observe(this, new Observer<List<CategoryEntity>>() {
            @Override
            public void onChanged(@Nullable List<CategoryEntity> categoryGames) {
                if(gameList!=null){
                    gameList.clear();
                }
                gameList = categoryGames;
                addMenuItemInNavMenuDrawer();
            }
        });
        viewModel.getFavorieList().observe(this, new Observer<List<FavEntity>>() {
            @Override
            public void onChanged(@Nullable List<FavEntity> favorites) {
                if(idNewList!=null) {
                    idNewList.clear();
                }
                idNewList = favorites;
                if (favorites != null) {
                    for(FavEntity value:favorites){
                        viewModel.updateNewFaState("1",value.get_id());
                        Log.d("ID_FAVS",value.get_id());
                    }
                }
            }
        });
        viewModel.getAllNews().observe(this, new Observer<List<NewsEntity>>() {
            @Override
            public void onChanged(@Nullable List<NewsEntity> newList) {
                if(newList!=null) {
                    newsAdapter.fillNews(newList);
                }
            }
        });

        viewModel.getFavoriteObjectNews().observe(this, new Observer<List<NewsEntity>>() {
            @Override
            public void onChanged(@Nullable List<NewsEntity> newList) {
                if(newList!=null) {
                    if(favoritesNewList!=null) {
                        favoritesNewList.clear();
                    }
                    favoritesNewList = newList;
                }
            }
        });




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        if(id==R.id.News_menu){
            actionBar.setTitle(R.string.app_name);
            fragment = new MainNews();

        }
        if(id == R.id.favorites){
            fragment = Favorito.newInstance(favoritesNewList);
        }
        if(id == R.id.logout){
            viewModel.deleteAllUsers();
            loggOut();
        }

        int i = 0;
        if(gameList!=null) {
            for (i = 0; i < gameList.size(); i++) {
                if (id == ID_INFLATED_MENU + i) {
                    actionBar.setElevation(0);
                    actionBar.setTitle(gameList.get(i).getCategoryName());
                    fragment = Conteiner_tab.newInstance(gameList.get(i).getCategoryName());
                    break;
                }
            }
        }

        if(fragment!=null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.screen_fragment, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void initControls(UserEntity user){
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        username = headerView.findViewById(R.id.username_bar);
        avatar = headerView.findViewById(R.id.avatar_user_bar);

        username.setText(user.getUsername());
        created_date.setText(user.getCreateDate());
        Picasso.get().load(user.getAvatar()).into(avatar);

    }

    private void addMenuItemInNavMenuDrawer(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem menuGames = menu.findItem(R.id.menu_games);
        menuGames.setTitle("Games");
        SubMenu subMenuGames = menuGames.getSubMenu();
        //AÑADIENDO LISTA DE JUEGOS
        subMenuGames.clear();
        for(int i=0;i<gameList.size();i++){
            subMenuGames.add(R.id.grup_games,ID_INFLATED_MENU+i,i,gameList.get(i).getCategoryName()).setCheckable(true);
        }
        navigationView.invalidate();
    }


    @Override
    public void addFavorites(String idNew) {
        viewModel.updateNewFaState("1",idNew);
        viewModel.addFavoriteNew(currentUser.get_id(),idNew);
        viewModel.refreshNews();
    }

    @Override
    public void removeFavorites(String idNew) {
        viewModel.updateNewFaState("0",idNew);
        viewModel.removeFavoriteNew(currentUser.get_id(),idNew);
        viewModel.refreshNews();
    }
    @Override
    public void setAdapters(RecyclerView rv) {
        rv.setAdapter(newsAdapter);
    }

    @Override
    public void refreshNews() {
        viewModel.refreshNews();
        //viewModel.refreshNewsListID();
    }

    @Override
    public void refreshFavorites() {
        viewModel.refreshNewsListID();
    }

    @Override
    public void refreshTopPlayers() {
        viewModel.refreshTopPlayers();
    }

    public void loggOut(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
        startActivity(new Intent(MainActivity.this,Login.class));
    }

}
