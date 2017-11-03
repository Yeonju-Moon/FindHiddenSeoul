package com.seoulapp.findhiddenseoul.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.seoulapp.findhiddenseoul.ApplicationController;
import com.seoulapp.findhiddenseoul.Model.UserInfo;
import com.seoulapp.findhiddenseoul.NetworkService;
import com.seoulapp.findhiddenseoul.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by BANGYURA on 2017-06-01.
 */

public class JoinActivity extends AppCompatActivity {
    EditText joinId, joinNn, joinPw, joinPw2, joinPh;

    TextView join_tv_signup, pwText;

    Button joinNowBtn;

    String pw, pw2;

    ApplicationController applicationController;
    private NetworkService networkService;

    UserInfo userInfo = new UserInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_join);

        // 액션바 뒤로가기
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        applicationController = new ApplicationController();
        applicationController.onCreate();

        applicationController = ApplicationController.getInstance();
        applicationController.buildNetworkService();
        networkService = ApplicationController.getInstance().getNetworkService();

        join_tv_signup = (TextView) findViewById(R.id.join_tv_signup);
        join_tv_signup.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/NotoSansKR-Bold.mp3"));
        join_tv_signup.setIncludeFontPadding(false);
        join_tv_signup.setTextSize(30);

        joinId = (EditText) findViewById(R.id.joinId);
        joinNn = (EditText) findViewById(R.id.joinNickname);
        joinPw = (EditText) findViewById(R.id.joinPw);
        joinPw2 = (EditText) findViewById(R.id.joinPw2);
        joinPh = (EditText) findViewById(R.id.joinPh);

        pwText = (TextView) findViewById(R.id.pwText);

        joinNowBtn = (Button) findViewById(R.id.joinNowBtn);

        joinId.setFocusable(false);
        joinId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinId.setBackgroundResource(R.drawable.text);

                final EditText edittext = new EditText(JoinActivity.this);
                edittext.setBackgroundResource(R.drawable.text);
                edittext.setGravity(Gravity.CENTER);

                AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                builder.setTitle("ID 중복확인");
                //builder.setMessage("AlertDialog Content");
                builder.setView(edittext);
                builder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {

                        userInfo.setUserId(edittext.getText().toString());

                        Call<UserInfo> thumbnailCall = networkService.post_idCheck(userInfo);
                        thumbnailCall.enqueue(new Callback<UserInfo>() {
                            @Override
                            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                                if(response.isSuccessful()){
                                    UserInfo userInfo_temp = response.body();
                                    if(userInfo_temp.getUserId().equals("true")){
                                        dialog.cancel();
                                        joinId.setBackgroundResource(R.drawable.text);
                                        joinId.setText(edittext.getText().toString());
                                    } else if (userInfo_temp.getUserId().equals("false")){
                                        //다이얼로그
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(JoinActivity.this);
                                        // 제목셋팅
                                        alertDialogBuilder.setTitle("알림");
                                        // AlertDialog 셋팅
                                        alertDialogBuilder
                                                .setMessage("이미 사용중인 아이디입니다.")
                                                .setCancelable(false)
                                                .setNegativeButton("취소",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(
                                                                    DialogInterface dialog, int id) {
                                                                // 다이얼로그를 취소한다
                                                                dialog.cancel();
                                                            }
                                                        });

                                        // 다이얼로그 생성
                                        AlertDialog alertDialog = alertDialogBuilder.create();

                                        // 다이얼로그 보여주기
                                        alertDialog.show();
                                    }

                                }else{
                                    int statusCode = response.code();
                                    Log.i("mytag", "응답코드 : " + statusCode);
                                }
                            }

                            @Override
                            public void onFailure(Call<UserInfo> call, Throwable t) {
                                Log.i("MyTag", "서버 onFailure 에러내용 : " + t.getMessage());
                            }
                        });
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 다이얼로그를 취소한다
                        dialog.cancel();

                    }
                });
                builder.show();
            }
        });

        joinNn.setFocusable(false);
        joinNn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinNn.setBackgroundResource(R.drawable.text);

                final EditText edittext = new EditText(JoinActivity.this);
                edittext.setBackgroundResource(R.drawable.text);
                edittext.setGravity(Gravity.CENTER);

                AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                builder.setTitle("닉네임 중복확인");
                //builder.setMessage("AlertDialog Content");
                builder.setView(edittext);
                builder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {

                        userInfo.setNickName(edittext.getText().toString());

                        Call<UserInfo> thumbnailCall = networkService.post_nickCheck(userInfo);
                        thumbnailCall.enqueue(new Callback<UserInfo>() {
                            @Override
                            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                                if(response.isSuccessful()){
                                    //UserInfo userInfo_temp = response.body();
                                    UserInfo userInfo_temp = response.body();
                                    if(userInfo_temp.getNickName().equals("true")){
                                        dialog.cancel();
                                        joinNn.setBackgroundResource(R.drawable.text);
                                        joinNn.setText(edittext.getText().toString());
                                    } else if (userInfo_temp.getNickName().equals("false")){
                                        //다이얼로그
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(JoinActivity.this);
                                        // 제목셋팅
                                        alertDialogBuilder.setTitle("알림");
                                        // AlertDialog 셋팅
                                        alertDialogBuilder
                                                .setMessage("이미 사용중인 닉네임입니다.")
                                                .setCancelable(false)
                                                .setNegativeButton("취소",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(
                                                                    DialogInterface dialog, int id) {
                                                                // 다이얼로그를 취소한다
                                                                dialog.cancel();
                                                            }
                                                        });

                                        // 다이얼로그 생성
                                        AlertDialog alertDialog = alertDialogBuilder.create();

                                        // 다이얼로그 보여주기
                                        alertDialog.show();
                                    }

                                }else{
                                    int statusCode = response.code();
                                    Log.i("mytag", "응답코드 : " + statusCode);
                                }
                            }

                            @Override
                            public void onFailure(Call<UserInfo> call, Throwable t) {
                                Log.i("MyTag", "서버 onFailure 에러내용 : " + t.getMessage());
                            }
                        });
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 다이얼로그를 취소한다
                        dialog.cancel();

                    }
                });
                builder.show();
            }
        });

        joinPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                joinPw.setBackgroundResource(R.drawable.text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        joinPw2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //EditText joinPw2 값이 변하는 중
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                joinPw2.setBackgroundResource(R.drawable.text);

                pw = joinPw.getText().toString();
                pw2 = joinPw2.getText().toString();

                // 패스워드끼리 비교
                if(joinPw.getText().toString() != joinPw2.getText().toString()){
                    pwText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/NotoSansKR-Regular.mp3"));
                    pwText.setIncludeFontPadding(false);
                    pwText.setTextSize(10);
                    pwText.setText("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
                }
            }

            //EditText joinPw2의 값이 다 변화한 후
            @Override
            public void afterTextChanged(Editable s) {
                if (joinPw.getText().toString().equals(joinPw2.getText().toString())) {
                    pwText.setText("");
                }
            }
        });

        joinPh.setFocusable(false);
        joinPh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinPh.setBackgroundResource(R.drawable.text);

                final EditText edittext = new EditText(JoinActivity.this);
                edittext.setBackgroundResource(R.drawable.text);
                edittext.setGravity(Gravity.CENTER);


                AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                builder.setTitle("휴대폰번호 중복확인");
                //builder.setMessage("AlertDialog Content");
                builder.setView(edittext);
                builder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {

                        userInfo.setPhone(edittext.getText().toString());

                        Call<UserInfo> thumbnailCall = networkService.post_phoneCheck(userInfo);
                        thumbnailCall.enqueue(new Callback<UserInfo>() {
                            @Override
                            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                                if(response.isSuccessful()){
                                    //UserInfo userInfo_temp = response.body();
                                    UserInfo userInfo_temp = response.body();
                                    if(userInfo_temp.getPhone().equals("true")){
                                        dialog.cancel();
                                        joinPh.setBackgroundResource(R.drawable.text);
                                        joinPh.setText(edittext.getText().toString());
                                    } else if (userInfo_temp.getPhone().equals("false")){
                                        //다이얼로그
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(JoinActivity.this);
                                        // 제목셋팅
                                        alertDialogBuilder.setTitle("알림");
                                        // AlertDialog 셋팅
                                        alertDialogBuilder
                                                .setMessage("이미 사용중인 번호입니다.")
                                                .setCancelable(false)
                                                .setNegativeButton("취소",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(
                                                                    DialogInterface dialog, int id) {
                                                                // 다이얼로그를 취소한다
                                                                dialog.cancel();
                                                            }
                                                        });

                                        // 다이얼로그 생성
                                        AlertDialog alertDialog = alertDialogBuilder.create();

                                        // 다이얼로그 보여주기
                                        alertDialog.show();
                                    }

                                }else{
                                    int statusCode = response.code();
                                    Log.i("mytag", "응답코드 : " + statusCode);
                                }
                            }

                            @Override
                            public void onFailure(Call<UserInfo> call, Throwable t) {
                                Log.i("MyTag", "서버 onFailure 에러내용 : " + t.getMessage());
                            }
                        });
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 다이얼로그를 취소한다
                        dialog.cancel();

                    }
                });
                builder.show();
            }
        });
    }

    public void joinClicked(View view){
        final String str_id = joinId.getText().toString();
        final String str_nn = joinNn.getText().toString();
        final String str_pw = joinPw.getText().toString();
        final String str_pw2 = joinPw2.getText().toString();
        final String str_ph = joinPh.getText().toString();

        if(str_id.length() == 0 || str_nn.length() == 0 || str_pw.length() == 0 || str_pw2.length() == 0 || str_ph.length() == 0){
            //다이얼로그
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(JoinActivity.this);

            // 제목셋팅
            alertDialogBuilder.setTitle("알림");

            // AlertDialog 셋팅
            alertDialogBuilder
                    .setMessage("내용을 모두 입력해 주세요")
                    .setCancelable(false)
                    .setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    // 다이얼로그를 취소한다
                                    dialog.cancel();
                                }
                            });


            // 다이얼로그 생성
            AlertDialog alertDialog = alertDialogBuilder.create();

            // 다이얼로그 보여주기
            alertDialog.show();

            joinId.setText(str_id);
            joinNn.setText(str_nn);
            joinPw.setText(str_pw);
            joinPw2.setText(str_pw2);
            joinPh.setText(str_ph);
        }
        else{
            if(str_pw.equals(str_pw2)){
                userInfo.setUserId(str_id);
                userInfo.setNickName(str_nn);
                userInfo.setPassword(str_pw);
                userInfo.setPhone(str_ph);

                Call<UserInfo> thumbnailCall = networkService.post_signup(userInfo);
                thumbnailCall.enqueue(new Callback<UserInfo>() {
                    @Override
                    public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                        if(response.isSuccessful()){
                            //UserInfo userInfo_temp = response.body();

                        }else{
                            int statusCode = response.code();
                            Log.i("mytag", "응답코드 : " + statusCode);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserInfo> call, Throwable t) {
                        Log.i("MyTag", "서버 onFailure 에러내용 : " + t.getMessage());
                    }
                });

                finish();
                Toast.makeText(JoinActivity.this, "회원가입 완료", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(JoinActivity.this, MainActivity.class);
                startActivity(intent);
            }else{
                //다이얼로그
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(JoinActivity.this);

                // 제목셋팅
                alertDialogBuilder.setTitle("알림");

                // AlertDialog 셋팅
                alertDialogBuilder
                        .setMessage("비밀번호가 일치하지 않습니다.")
                        .setCancelable(false)
                        .setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        // 다이얼로그를 취소한다
                                        dialog.cancel();
                                    }
                                });


                // 다이얼로그 생성
                AlertDialog alertDialog = alertDialogBuilder.create();

                // 다이얼로그 보여주기
                alertDialog.show();

                joinId.setText(str_id);
                joinNn.setText(str_nn);
                joinPw.setText("");
                joinPw2.setText("");
                joinPh.setText(str_ph);
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}