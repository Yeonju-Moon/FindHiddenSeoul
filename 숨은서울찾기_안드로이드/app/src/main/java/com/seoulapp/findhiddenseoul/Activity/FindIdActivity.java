package com.seoulapp.findhiddenseoul.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
public class FindIdActivity extends Activity {

    // 키보드 내리기
    InputMethodManager imm;
    EditText find_id_phone;

    // 서버 연동
    private NetworkService networkService;

    // 서버에서 받아오는 아이디
    String id;

    final Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        find_id_phone = (EditText) findViewById(R.id.find_id_phone);

        find_id_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                find_id_phone.setBackgroundResource(R.drawable.text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ImageButton find_btn_id = (ImageButton) findViewById(R.id.find_btn_id);
        find_btn_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // ip, port 연결
                ApplicationController application = ApplicationController.getInstance();
                application.buildNetworkService();
                networkService = ApplicationController.getInstance().getNetworkService();

                if (find_id_phone.getText().toString().equals("")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("전화번호 입력 오류");

                    // AlertDialog 셋팅
                    alertDialogBuilder
                            .setMessage("전화번호를 다시 입력해주세요.")
                            .setCancelable(false)
                            .setPositiveButton("확인",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {

                                            dialog.cancel();
                                            find_id_phone.requestFocus();
                                        }
                                    });

                    // 다이얼로그 생성
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // 다이얼로그 보여주기
                    alertDialog.show();
                } else {

                    Find find = new Find();
                    find.setPhone(find_id_phone.getText().toString());

                    Call<Find> thumbnailCall = networkService.post_find_id(find);
                    thumbnailCall.enqueue(new Callback<Find>() {
                        @Override
                        public void onResponse(Call<Find> call, Response<Find> response) {
                            id = response.body().getUserId();
                            Log.i("서버 연동", "####서버에서 id 받아오나 " + id);

                            if (id.equals("false")) {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                alertDialogBuilder.setTitle("아이디 찾기 실패");

                                // AlertDialog 셋팅
                                alertDialogBuilder
                                        .setMessage("해당하는 사용자 아이디가 존재하지 않습니다.")
                                        .setCancelable(false)
                                        .setPositiveButton("확인",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                        FindIdActivity.this.finish();
                                                    }
                                                });

                                // 다이얼로그 생성
                                AlertDialog alertDialog = alertDialogBuilder.create();

                                // 다이얼로그 보여주기
                                alertDialog.show();

                            } else {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                alertDialogBuilder.setTitle("아이디 찾기");

                                // AlertDialog 셋팅
                                alertDialogBuilder
                                        .setMessage("사용자 아이디 : " + id)
                                        .setCancelable(false)
                                        .setPositiveButton("확인",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                        FindIdActivity.this.finish();
                                                    }
                                                });

                                // 다이얼로그 생성
                                AlertDialog alertDialog = alertDialogBuilder.create();

                                // 다이얼로그 보여주기
                                alertDialog.show();
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

    public void linearOnClick(View v) {
        imm.hideSoftInputFromWindow(find_id_phone.getWindowToken(), 0);
    }
}