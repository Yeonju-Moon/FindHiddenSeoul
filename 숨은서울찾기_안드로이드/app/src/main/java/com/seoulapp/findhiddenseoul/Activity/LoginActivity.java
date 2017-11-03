package com.seoulapp.findhiddenseoul.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;
import com.seoulapp.findhiddenseoul.ApplicationController;
import com.seoulapp.findhiddenseoul.Model.UserInfo;
import com.seoulapp.findhiddenseoul.NetworkService;
import com.seoulapp.findhiddenseoul.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.seoulapp.findhiddenseoul.R.id.Login_et_id;
import static com.seoulapp.findhiddenseoul.R.id.Login_et_password;

/**
 * Created by HOME on 2017-10-03.
 */
public class LoginActivity extends Activity {

    // sharedPreference
    SharedPreferences setting;
    SharedPreferences.Editor editor;

    // login, signout
    private String login;
    private EditText login_id, login_pw;
    private TextView signIn, signUp, find_id, find_pw;
    private Button login_button;

    // 서버 연동 변수
    private NetworkService networkService;
    private String token;
    private int level;
    private int rank;
    private String url;


    //네이버 로그인 API에 필요한 값
    private static String OAUTH_CLIENT_ID = "70zmvP0lCdJZ_4wMWEsp";
    private static String OAUTH_CLIENT_SECRET = "jztzDxsfmy";
    private static String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인";

    // 네이버 로그인을 위한 변수
    private OAuthLoginButton btOAuthLoginButton;
    static OAuthLogin mOAuthLoginModule;
    Context mContext = LoginActivity.this;
    static String accessToken;
    JSONObject jsonObject1;
    JSONObject jsonObject2;
    String id, nickName;

 /*   public void onPause() {
        super.onPause();

        // Remove the activity when its off the screen
        finish();
    }*/

    /****************************************************************************************************************
     * 네이버 로그인 연동
     ****************************************************************************************************************/

    //네이버 로그인을 위한 핸들러(네이버 로그인 버튼의 동작을 정의한다.)
    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                accessToken = mOAuthLoginModule.getAccessToken(mContext);
                new NaverProfileGet().execute();
            } else {
                String errorCode = mOAuthLoginModule.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(mContext);
               /* Toast.makeText(LoginActivity.this, "errorcode: " + errorCode + ", errordecs:"
                        + errorDesc, Toast.LENGTH_SHORT).show();*/
            }
        }
    };


    //네이버 프로필 조회 API
    public class NaverProfileGet extends AsyncTask<String, Void, String> {
        //네이버 프로필 조회 API에 보낼 헤더
        String header = "Bearer " + accessToken;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            //네이버 프로필 조회 API에서 프로필을 jSON 형식으로 받아오는 부분
            StringBuffer response = new StringBuffer();
            try {
                String apiURL = "https://openapi.naver.com/v1/nid/me";
                URL url = new URL(apiURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", header);
                int responseCode = conn.getResponseCode();
                BufferedReader br;

                if (responseCode == 200) {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return response.toString();
        }

        //네이버 로그인 - 프로필 조회 API에서 받은 jSON에서 원하는 데이터를 뽑아내는 부분
        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
            try {
                final Intent intent = new Intent();
                jsonObject1 = new JSONObject(result);
                jsonObject2 = (JSONObject) jsonObject1.get("response");
                id = jsonObject2.getString("id");
                nickName = jsonObject2.getString("name");

                /****************************************************************************************************************
                 * 네이버 서버 연동 데이터 전달
                 ****************************************************************************************************************/

                UserInfo userInfo = new UserInfo();
                userInfo.setUserId(id);
                userInfo.setNickName(nickName);

                Call<UserInfo> thumbnailCall = networkService.post_user(userInfo);
                thumbnailCall.enqueue(new Callback<UserInfo>() {
                    @Override

                    public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                        if (response.isSuccessful()) {
                            token = response.body().getToken(); // 서버에서 보내 준 토큰
                            Log.e("LoginActivity 서버 연동", "@@@@@@서버에서 토큰 값 받아왔따 : " + token);

                            // 쉐어드 불러와서 토큰값 저장
                            setting = getSharedPreferences("setting", 0); // setting.xml 파일 생성 0 은 읽기, 쓰기 모드
                            editor = setting.edit();
                            editor.putString("token", token);
                            editor.commit();

                            if (setting.getString("mypage", "").equals("true")) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Log.e("Login 쉐어드에 토큰 저장", "@@@@@@쉐어드에 저장된 토큰 값 : " + setting.getString("token", ""));
                                setResult(100, intent);
                                finish();
                            }
                            editor.putString("mypage", "false");
                            editor.commit();
                        } else {
                            int statusCode = response.code();
                            Log.i("MyTag", "응답코드 : " + statusCode);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserInfo> call, Throwable t) {
                        Log.i("MyTag", "서버 onFailure 에러내용 : " + t.getMessage());
                    }
                });

            } catch (Exception e) {
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }


    /****************************************************************************************************************
     * LoginActivity onCreate
     ****************************************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /****************************************************************************************************************
         * onCreate 서버 연동
         ****************************************************************************************************************/

        // ip, port 연결
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        networkService = ApplicationController.getInstance().getNetworkService();

        // login, join us
        login_id = (EditText) findViewById(Login_et_id);
        login_pw = (EditText) findViewById(Login_et_password);
        login_button = (Button) findViewById(R.id.Login_btn_login);
        find_id = (TextView) findViewById(R.id.Login_tv_findid);
        find_pw = (TextView) findViewById(R.id.Login_tv_findpw);
        signIn = (TextView) findViewById(R.id.Login_tv_signin);
        signUp = (TextView) findViewById(R.id.Login_tv_signup);

        signIn.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/NotoSansKR-Bold.mp3"));
        signUp.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/NotoSansKR-Bold.mp3"));

        login_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                login_id.setBackgroundResource(R.drawable.text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        login_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                login_pw.setBackgroundResource(R.drawable.text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // login button
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUserId(login_id.getText().toString());
                userInfo.setPassword(login_pw.getText().toString());

                Call<UserInfo> call = networkService.post_login(userInfo);
                call.enqueue(new Callback<UserInfo>() {
                    @Override
                    public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                        if (response.isSuccessful()) {
                            UserInfo userInfo_temp = response.body();
                            login = userInfo_temp.getLogin();
                            if (login.equals("wrong PW")) {
                                Toast.makeText(LoginActivity.this, "비밀번호가 올바르지 않습니다.", Toast.LENGTH_LONG).show();
                            } else if (login.equals("wrong ID")) {
                                Toast.makeText(LoginActivity.this, "아이디가 올바르지 않습니다.", Toast.LENGTH_LONG).show();
                            } else {
                                token = userInfo_temp.getLogin(); // 서버에서 보내 준 토큰
                                Log.e("LoginActivity 서버 연동", "@@@@@@서버에서 토큰 값 받아왔따 : " + token);

                                Intent intent = new Intent();

                                // 쉐어드 불러와서 토큰값 저장
                                setting = getSharedPreferences("setting", 0); // setting.xml 파일 생성 0 은 읽기, 쓰기 모드
                                editor = setting.edit();
                                editor.putString("token", token);
                                editor.commit();

                                setResult(100, intent);
                                finish();
                            }
                        } else {
                            Log.v("code", response.code() + "");
                        }
                    }

                    @Override
                    public void onFailure(Call<UserInfo> call, Throwable t) {
                        Log.v("login error", t.getMessage());
                    }
                });
            }
        });

        // signUp textView
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // find ID
        find_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FindIdActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // find PW
        find_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FindPwActivity.class);
                startActivity(intent);
                finish();
            }
        });

        /****************************************************************************************************************
         * onCreate 네이버 로그인 연동
         ****************************************************************************************************************/

        //네이버 로그인 인스턴스 초기화
        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(
                LoginActivity.this,
                OAUTH_CLIENT_ID,
                OAUTH_CLIENT_SECRET,
                OAUTH_CLIENT_NAME
        );

        //네이버 로그인 버튼 구현
        mOAuthLoginModule.logout(mContext);
        btOAuthLoginButton = (OAuthLoginButton) findViewById(R.id.Login_btn_naver);
        btOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
    }

}
