package com.gomar.parcial2_00011616;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {

    private EditText text_username, text_password;
    private Button btn_iniciarSesion;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private GamesNewsApi api;
    private Token securityToken;
    public static String TOKEN_SECURITY = "SECURITY_PREFERENCE_TOKEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String savedToken = getPreferences(Context.MODE_PRIVATE).getString(TOKEN_SECURITY, "");
        if (!savedToken.equals("")) {
            securityToken = new Token(savedToken);
            Intent i = new Intent(Login.this, MainActivity.class);
            i.putExtra("SECURITY_TOKEN", securityToken);
            startActivity(i);
        }
        setContentView(R.layout.activity_login);
        text_username = findViewById(R.id.username);
        text_password = findViewById(R.id.password);
        btn_iniciarSesion = findViewById(R.id.btn_login);
        api = createAPI();
        btn_iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = text_username.getText().toString();
                String password = text_password.getText().toString();
                compositeDisposable.add(api.getSecurityToken(username, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(getTokenSecurity()));

            }
        });

    }

    private GamesNewsApi createAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GamesNewsApi.LINKEND)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(GamesNewsApi.class);
    }

    private DisposableSingleObserver<Token> getTokenSecurity() {
        return new DisposableSingleObserver<Token>() {
            @Override
            public void onSuccess(Token value) {
                securityToken = value;
                SharedPreferences shared = Login.this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString(TOKEN_SECURITY, securityToken.getTokenSecurity());
                editor.commit();
                Intent i = new Intent(Login.this, MainActivity.class);
                i.putExtra("SECURITY_TOKEN", securityToken);
                startActivity(i);
            }

            @Override
            public void onError(Throwable e) {

            }

        };
    }
}
