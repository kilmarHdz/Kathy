package com.gomar.parcial2_00011616.Activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.gomar.parcial2_00011616.Entity.NewsEntity;
import com.gomar.parcial2_00011616.Model.ViewModel;
import com.gomar.parcial2_00011616.R;
import com.squareup.picasso.Picasso;

public class OneNew extends AppCompatActivity {

    private String idNew;
    private ViewModel model;
    private NewsEntity notice;
    private ImageView imageView;
    private TextView title,game,body,date;
    private FloatingActionButton favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_new);

        Intent responseIntent = getIntent();
        if(responseIntent!=null){
            idNew = responseIntent.getStringExtra("ID_NEW");
            Log.d("ID_NEW",idNew);
        }
        imageView = findViewById(R.id.imageview_new_single);
        title = findViewById(R.id.text_titulo_single);
        game = findViewById(R.id.text_category_single);
        date = findViewById(R.id.text_date_single);
        body = findViewById(R.id.text_body_new_single);

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        model = ViewModelProviders.of(this).get(ViewModel.class);
        model.getNew(idNew).observe(this, new Observer<NewsEntity>() {
            @Override
            public void onChanged(@Nullable NewsEntity aNew) {
                notice = aNew;
                if(notice!=null){
                    Picasso.get().load(notice.getCoverImage()).error(R.drawable.ic_videogame_asset_black_24dp).into(imageView);
                    title.setText(notice.getTitle());
                    game.setText(notice.getGame());
                    date.setText(notice.getCreated_date());
                    body.setText(notice.getBody());
                }
            }
        });

    }
}
