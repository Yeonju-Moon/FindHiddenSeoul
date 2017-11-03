package com.seoulapp.findhiddenseoul.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.seoulapp.findhiddenseoul.ApplicationController;
import com.seoulapp.findhiddenseoul.Model.Review;
import com.seoulapp.findhiddenseoul.NetworkService;
import com.seoulapp.findhiddenseoul.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class R_DetailActivity extends AppCompatActivity {

    private NetworkService networkService;
    private ImageView review_image;
    private TextView review_content, review_date, review_nickname;
    private String isMineReview;
    private Button update_button, delete_button;
    private String review_index, heritage_index, review_heritage_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_detail);

        // connection
        ApplicationController applicationController = ApplicationController.getInstance();
        applicationController.buildNetworkService();
        networkService = ApplicationController.getInstance().getNetworkService();

        // actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.backbutton);
        getSupportActionBar().setTitle("REVIEW");

        // sharedPreference
        final SharedPreferences setting = getSharedPreferences("setting", 0);
        final String shared_token = setting.getString("token", "");

        review_image = (ImageView) findViewById(R.id.Rdetail_iv_review_image);
        review_content = (TextView) findViewById(R.id.Rdetail_tv_review_content);
        review_date = (TextView) findViewById(R.id.Rdetail_tv_review_date);
        review_nickname = (TextView) findViewById(R.id.Rdetail_tv_nickname);
        update_button = (Button) findViewById(R.id.Rdetail_btn_update);
        delete_button = (Button) findViewById(R.id.Rdetail_btn_delete);
        heritage_index = getIntent().getStringExtra("heritage_index");

        // Read
        review_index = getIntent().getStringExtra("review_index");
        Review review = new Review();
        review.setToken(shared_token);
        review.setReview_index(review_index);

        Call<List<Review>> call = networkService.getReadReview(review);
        call.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {

                // 기존 리뷰 이미지, 내용, 날짜출력
                if (response.isSuccessful()) {
                    List<Review> review_temp = response.body();
                    for (int i = 0; i < review_temp.size(); i++) {
                        Glide.with(R_DetailActivity.this).load(review_temp.get(i).getReview_img()).into(review_image);
                        review_content.setText(review_temp.get(i).getReview_content());
                        review_date.setText(review_temp.get(i).getReview_date().substring(0, 10));
                        review_nickname.setText(review_temp.get(i).getNick_name() + "님 리뷰");
                        isMineReview = review_temp.get(i).getMine();
                        review_heritage_index = review_temp.get(i).getReview_heritage_index();

                        // review_token값과 현재 token값이 다른경우 (내가 쓴 글이 아닌경우)
                        if (isMineReview.equals("false")) {
                            update_button.setVisibility(View.INVISIBLE);
                            delete_button.setVisibility(View.INVISIBLE);
                        } else if (isMineReview.equals("true")) { // 같은 경우 수정,삭제 버튼 활성화 (내가 쓴 글인 경우)
                            update_button.setVisibility(View.VISIBLE);
                            delete_button.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    int statusCode = response.code();
                    Log.v("code ", statusCode + "");
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                Log.v("에러 ", t.getMessage());
            }
        });


        // Update
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = new Bundle();
                extras.putString("token", shared_token);
                extras.putString("review_index", review_index);
                Intent intent = new Intent(R_DetailActivity.this, R_UpdateActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
                finish();
            }
        });


        // Delete
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Review review = new Review();

                // set token, review_index
                review.setToken(shared_token);
                review.setReview_index(review_index);

                Call<Review> call2 = networkService.getDeleteReview(review);
                call2.enqueue(new Callback<Review>() {
                    @Override
                    public void onResponse(Call<Review> call, Response<Review> response) {
                        Log.v("코드 : ", response.toString());
                        Toast.makeText(getBaseContext(), "삭제완료", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(R_DetailActivity.this, H_DetailActivity.class);
                        intent.putExtra("heritage_index", review_heritage_index);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Review> call, Throwable t) {
                        Log.v("에러 : ", t.getMessage());
                    }
                });


            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                Bundle extras = new Bundle();
                extras.putString("heritage_index", review_heritage_index);

                Intent intent = new Intent(R_DetailActivity.this, H_DetailActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onPause() {
        super.onPause();

        // Remove the activity when its off the screen
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

    }
}
