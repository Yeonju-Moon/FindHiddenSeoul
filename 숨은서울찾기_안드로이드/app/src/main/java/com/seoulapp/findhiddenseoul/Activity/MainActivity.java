package com.seoulapp.findhiddenseoul.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.seoulapp.findhiddenseoul.ApplicationController;
import com.seoulapp.findhiddenseoul.Model.Token;
import com.seoulapp.findhiddenseoul.NetworkService;
import com.seoulapp.findhiddenseoul.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // 로그인 화면에서 데이터 받아오려고 설정한 변수
    private final int REQUEST_CODE_LOGIN = 100;
    private String nick = "";
    private int level, rank;

    // sharedPreference
    //SharedPreferences setting = getSharedPreferences("setting", 0);
    //SharedPreferences.Editor editor = setting.edit();

    String shared_token;
    //Context mContext = MainActivity.this;
    private long pressedTime;
    // 서버 연동
    private NetworkService networkService;


    /****************************************************************************************************************
     * 액션바 - 마이페이지 버튼
     ****************************************************************************************************************/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_mypage) {
            if (shared_token.equals("")) {
                //Toast.makeText(this, "로그인이 나와야 해요", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent, REQUEST_CODE_LOGIN);
            } else {
                Intent intent = new Intent(MainActivity.this, MyPageActivity.class);
                //Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 기기 취소 버튼 눌렸을 때
    @Override
    public void onBackPressed() {
        if (pressedTime == 0) {
            Toast.makeText(MainActivity.this, " 한 번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show();
            pressedTime = System.currentTimeMillis();
        } else {
            int seconds = (int) (System.currentTimeMillis() - pressedTime);

            if (seconds > 2000) {
                Toast.makeText(MainActivity.this, " 한 번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show();
                pressedTime = 0;
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mypage, menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    /****************************************************************************************************************
     * 로그인 화면에서 데이터 받아오기
     ****************************************************************************************************************/

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == REQUEST_CODE_LOGIN) {
            Log.v("result", 100+"");
            /****************************************************************************************************************
             * onActivityResult 서버 연동 (로그인에서 메인으로)
             ****************************************************************************************************************/

            // Intent intent = getIntent();

            final ImageButton Main_profile_login = (ImageButton) findViewById(R.id.Main_profile_login);
            final RelativeLayout relative = (RelativeLayout) findViewById(R.id.info);

            final TextView Main_profile_nick = (TextView) findViewById(R.id.Main_profile_nick);
            final TextView Main_profile_level = (TextView) findViewById(R.id.Main_profile_level);
            final TextView Main_profile_rank = (TextView) findViewById(R.id.Main_profile_rank);
            final ImageView Main_profile_img = (ImageView) findViewById(R.id.Main_profile_img);

            // sharedPreference
            final SharedPreferences setting = getSharedPreferences("setting", 0);
            final SharedPreferences.Editor editor = setting.edit();

            shared_token = setting.getString("token", "");

            // ip, port 연결
            ApplicationController application = ApplicationController.getInstance();
            application.buildNetworkService();
            networkService = ApplicationController.getInstance().getNetworkService();

            // 통신할 객체에 토큰 저장
            Token token_info = new Token();
            token_info.setToken(shared_token);

            Call<Token> thumbnailCall = networkService.post_info(token_info);
            thumbnailCall.enqueue(new Callback<Token>() {
                @Override
                public void onResponse(Call<Token> call, Response<Token> response) {
                    Log.e("두번째 토큰 값", "@@@@토큰 값 확인 : " + response.body().getToken());
                    editor.putString("nickname", response.body().getNick_name());
                    editor.putInt("level", response.body().getLevel());
                    editor.putInt("rank", response.body().getRank());
                    editor.putString("charURL", response.body().getCharURL());
                    editor.commit();

                    /****************************************************************************************************************
                     * 프로필
                     ****************************************************************************************************************/

                    Log.e("두번째 토큰 값2", "@@@@토큰 값 확인 : " + setting.getString("nickname", ""));

                    Main_profile_login.setVisibility(View.INVISIBLE);

                    Main_profile_nick.setText(setting.getString("nickname", ""));
                    Main_profile_level.setText(setting.getInt("level", 0) + "");
                    Main_profile_rank.setText(setting.getInt("rank", 0) + "");
                    Glide.with(MainActivity.this).load(setting.getString("charURL", "")).into(Main_profile_img);

                    relative.setVisibility(View.VISIBLE);

                }

                @Override
                public void onFailure(Call<Token> call, Throwable t) {
                    Log.e("로그인에서 메인으로 왔을 때 오류", "@@@@@@@@@@@서버 연동 오류" + t);

                }
            });

            //Log.e("MainActivity 쉐어드에 들어갔니", "@@@@@@@@@쉐어드 닉네임 제발 : " + setting.getString("nickname", ""));
            //profile_nick.setText("test" + setting.getString("nickname", ""));
            //profile_level.setText(setting.getInt("level", 0) + "");
            //profile_rank.setText(rank);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Spinner
        final Spinner guSpinner = (Spinner) findViewById(R.id.Main_spinner_gu);
        final ArrayAdapter guAdapter = ArrayAdapter.createFromResource(this, R.array.gu, R.layout.spinner_item);
        guSpinner.setAdapter(guAdapter);

        /****************************************************************************************************************
         * onCreate 구 검색 spinner 값 전달
         ****************************************************************************************************************/
        // str_gu = guSpinner.getSelectedItem().toString();
        ImageButton Main_btn_guSearch = (ImageButton) findViewById(R.id.Main_btn_guSearch);
        Main_btn_guSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, H_ListActivity.class);
                intent.putExtra("guText", guSpinner.getSelectedItem().toString());
                startActivity(intent);
            }
        });

        // sharedPreference
        final SharedPreferences setting = getSharedPreferences("setting", 0);
        final SharedPreferences.Editor editor = setting.edit();

        shared_token = setting.getString("token", "");
        Log.e("Main onCreate token", "token 확인 " + shared_token);

        /****************************************************************************************************************
         * onCreate 서버 연동
         ****************************************************************************************************************/

        // ip, port 연결
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        networkService = ApplicationController.getInstance().getNetworkService();

        /****************************************************************************************************************
         * onCreate 프로필
         ****************************************************************************************************************/

        // Intent intent = getIntent();
        ImageButton Main_profile_login = (ImageButton) findViewById(R.id.Main_profile_login);
        RelativeLayout relative = (RelativeLayout) findViewById(R.id.info);

        TextView Main_profile_nick = (TextView) findViewById(R.id.Main_profile_nick);
        TextView Main_profile_level = (TextView) findViewById(R.id.Main_profile_level);
        TextView Main_profile_rank = (TextView) findViewById(R.id.Main_profile_rank);
        ImageView Main_profile_img = (ImageView) findViewById(R.id.Main_profile_img);

        TextView Main_profile_level_text = (TextView) findViewById(R.id.Main_profile_level_text);
        TextView Main_profile_rank_text = (TextView) findViewById(R.id.Main_profile_rank_text);

        Log.e("onCreate Main 프로필 ", "@@@@@@@@@@ 프로필 닉네임 : " + setting.getString("nickname", ""));

        if (setting.getString("nickname", "").equals("")) {

            Log.e("null인가요", "null이길");
            Main_profile_login.setVisibility(View.VISIBLE);
            relative.setVisibility(View.INVISIBLE);
        } else {

            Log.e("null인가요", "null 아니야");

            Main_profile_login.setVisibility(View.INVISIBLE);

            Main_profile_nick.setText(setting.getString("nickname", ""));
            Main_profile_level.setText(setting.getInt("level", 0) + "");
            Main_profile_rank.setText(setting.getInt("rank", 0) + "");
            Glide.with(MainActivity.this).load(setting.getString("charURL", "")).into(Main_profile_img);

            relative.setVisibility(View.VISIBLE);
        }

        Main_profile_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shared_token.equals("")) {
                    //Toast.makeText(this, "로그인이 나와야 해요", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_LOGIN);
                    //finish();
                }
            }
        });
    }

}