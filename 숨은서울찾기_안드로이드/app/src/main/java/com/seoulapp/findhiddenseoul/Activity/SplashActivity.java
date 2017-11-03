package com.seoulapp.findhiddenseoul.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.seoulapp.findhiddenseoul.ApplicationController;
import com.seoulapp.findhiddenseoul.NetworkService;
import com.seoulapp.findhiddenseoul.R;
import com.seoulapp.findhiddenseoul.Model.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HOME on 2017-10-03.
 */
public class SplashActivity extends Activity {

    // 서버 연동 변수
    private NetworkService networkService;

    // 메인화면으로 넘겨줄 값들
    String nick;
    int level;
    int rank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // sharedPreference
        SharedPreferences setting = getSharedPreferences("setting", 0);
        final SharedPreferences.Editor editor = setting.edit();

        /****************************************************************************************************************
         * 서버 연동
         ****************************************************************************************************************/

        // ip, port 연결
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        networkService = ApplicationController.getInstance().getNetworkService();

        // 쉐어드에서 토큰 값 가져옴
        final String token = setting.getString("token", "");
        Log.e("쉐어드", "@@@@@쉐어드 토큰 : " + token);

        // 통신할 객체에 토큰 저장
        Token token_info = new Token();
        token_info.setToken(token);

        Call<Token> thumbnailCall = networkService.post_info(token_info);
        thumbnailCall.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                Log.e("토큰 값", "@@@@토큰 값 확인 : " + response.body().getNick_name());
                if (response.body().getNick_name() != null) { // 서버에 토큰값 저장되어 있으면 유저정보 가져옴
                    editor.putString("nickname", response.body().getNick_name());
                    editor.putInt("level", response.body().getLevel());
                    editor.putInt("rank", response.body().getRank());
                    editor.putString("charURL", response.body().getCharURL());
                    editor.commit();
                } else { // 토큰값 저장되어 있지 않으면
                    editor.putString("nickname", "익명");
                    editor.putInt("level", 0);
                    editor.putInt("rank", 0);
                    editor.putString("charURL", " ");
                    editor.commit();
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {

            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 메인 액티비티 시작
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);

    }
}
