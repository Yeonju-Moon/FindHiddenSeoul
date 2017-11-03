package com.seoulapp.findhiddenseoul.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.seoulapp.findhiddenseoul.ApplicationController;
import com.seoulapp.findhiddenseoul.Model.Find;
import com.seoulapp.findhiddenseoul.NetworkService;
import com.seoulapp.findhiddenseoul.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HOME on 2017-10-31.
 */
public class ChangePwActivity extends Activity {

    // 키보드 내리기
    InputMethodManager imm;
    EditText find_pw_change, find_pw_check;

    // 서버 연동
    private NetworkService networkService;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw_change);

        find_pw_change = (EditText) findViewById(R.id.find_pw_change);
        find_pw_check = (EditText) findViewById(R.id.find_pw_check);

        find_pw_change.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                find_pw_change.setBackgroundResource(R.drawable.text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        find_pw_check.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                find_pw_check.setBackgroundResource(R.drawable.text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ImageButton change_btn_pw = (ImageButton) findViewById(R.id.change_btn_pw);
        change_btn_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(find_pw_change.getText().toString().equals(find_pw_check.getText().toString()))) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("새 비밀번호 입력 오류");

                    // AlertDialog 셋팅
                    alertDialogBuilder
                            .setMessage("새 비밀번호와 비밀번호 확인을 동일하게 입력해주세요.")
                            .setCancelable(false)
                            .setPositiveButton("확인",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {

                                            dialog.cancel();
                                            find_pw_change.requestFocus();
                                        }
                                    });

                    // 다이얼로그 생성
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // 다이얼로그 보여주기
                    alertDialog.show();

                } else {
                    Intent intent = getIntent();
                    String id = intent.getExtras().getString("id").toString();

                    Find find = new Find();
                    find.setNew_passwd(find_pw_change.getText().toString());
                    find.setUserId(id);

                    // ip, port 연결
                    ApplicationController application = ApplicationController.getInstance();
                    application.buildNetworkService();
                    networkService = ApplicationController.getInstance().getNetworkService();

                    Call<Find> thumbnailCall = networkService.post_change_pw(find);
                    thumbnailCall.enqueue(new Callback<Find>() {
                        @Override
                        public void onResponse(Call<Find> call, Response<Find> response) {
                            String check = response.body().getFind_pw2();
                            Log.i("서버 연동", "####서버에서 비번 잘 바뀌었나 " + check);

                            if (check.equals("true")) {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                alertDialogBuilder.setTitle("비밀번호 변경 성공");

                                // AlertDialog 셋팅
                                alertDialogBuilder
                                        .setMessage("비밀번호가 새로운 비밀번호로 변경되었습니다.")
                                        .setCancelable(false)
                                        .setPositiveButton("확인",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                        ChangePwActivity.this.finish();
                                                    }
                                                });

                                // 다이얼로그 생성
                                AlertDialog alertDialog = alertDialogBuilder.create();

                                // 다이얼로그 보여주기
                                alertDialog.show();

                            } else {
                                Log.i("서버 저장", "####서버에 비밀번호 저장 실패");
                            }

                        }

                        @Override
                        public void onFailure(Call<Find> call, Throwable t) {
                            Log.i("MyTag", "서버 onFailure 에러내용 : " + t.getMessage());
                        }
                    });


                }
            }
        });
    }

    public void joinClicked(View view) {

        final String str_pw_change = find_pw_change.getText().toString();
        final String str_pw_check = find_pw_check.getText().toString();

        if (str_pw_change.length() == 0 || str_pw_check.length() == 0) {
            //다이얼로그
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChangePwActivity.this);

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


        } else {

            //다이얼로그
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChangePwActivity.this);

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
            
        }

    }


    public void linearOnClick(View v) {
        imm.hideSoftInputFromWindow(find_pw_change.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(find_pw_check.getWindowToken(), 0);
    }
}