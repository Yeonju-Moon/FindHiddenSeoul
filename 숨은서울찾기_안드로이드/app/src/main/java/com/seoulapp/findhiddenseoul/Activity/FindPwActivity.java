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
public class FindPwActivity extends Activity {

    // 키보드 내리기
    InputMethodManager imm;
    EditText find_pw_id, find_pw_phone;

    // 서버 연동
    private NetworkService networkService;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        find_pw_id = (EditText)findViewById(R.id.find_pw_id);
        find_pw_phone = (EditText)findViewById(R.id.find_pw_phone);

        find_pw_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                find_pw_id.setBackgroundResource(R.drawable.text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        find_pw_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                find_pw_phone.setBackgroundResource(R.drawable.text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        ImageButton find_pw = (ImageButton)findViewById(R.id.find_pw);
        find_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // ip, port 연결
                ApplicationController application = ApplicationController.getInstance();
                application.buildNetworkService();
                networkService = ApplicationController.getInstance().getNetworkService();

                if (find_pw_id.getText().toString().equals("") || find_pw_phone.getText().toString().equals("")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("아이디/ 전화번호 입력 오류");

                    // AlertDialog 셋팅
                    alertDialogBuilder
                            .setMessage("아이디 / 전화번호를 다시 입력해주세요.")
                            .setCancelable(false)
                            .setPositiveButton("확인",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {

                                            dialog.cancel();
                                            find_pw_id.requestFocus();
                                        }
                                    });

                    // 다이얼로그 생성
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // 다이얼로그 보여주기
                    alertDialog.show();
                } else {

                    Find find = new Find();
                    find.setUserId(find_pw_id.getText().toString());
                    find.setPhone(find_pw_phone.getText().toString());

                    Call<Find> thumbnailCall = networkService.post_find_pw(find);
                    thumbnailCall.enqueue(new Callback<Find>() {

                        @Override
                        public void onResponse(Call<Find> call, Response<Find> response) {
                            String id = response.body().getUserId();
                            Log.i("서버 연동", "####서버에서 id 받아오나 " + id);

                            if (id.equals("false")) {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                alertDialogBuilder.setTitle("비밀번호 찾기 실패");

                                // AlertDialog 셋팅
                                alertDialogBuilder
                                        .setMessage("해당하는 사용자 아이디가 존재하지 않습니다.")
                                        .setCancelable(false)
                                        .setPositiveButton("확인",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });

                                // 다이얼로그 생성
                                AlertDialog alertDialog = alertDialogBuilder.create();

                                // 다이얼로그 보여주기
                                alertDialog.show();

                            } else {
                                Intent intent = new Intent(getApplicationContext(), ChangePwActivity.class);
                                intent.putExtra("id", id);
                                startActivity(intent);
                                finish();
                            }

                        }

                        @Override
                        public void onFailure(Call<Find> call, Throwable t) {

                        }

                    });
                }
            }

        });

    }

}
