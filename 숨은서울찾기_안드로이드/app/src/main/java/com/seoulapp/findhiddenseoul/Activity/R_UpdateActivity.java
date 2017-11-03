package com.seoulapp.findhiddenseoul.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.seoulapp.findhiddenseoul.ApplicationController;
import com.seoulapp.findhiddenseoul.Model.Review;
import com.seoulapp.findhiddenseoul.NetworkService;
import com.seoulapp.findhiddenseoul.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.util.Log.v;

/**
 * Created by JIYOUNGKIM on 2017-10-19.
 */

public class R_UpdateActivity extends AppCompatActivity {

    private NetworkService networkService;

    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;

    private EditText review_content;
    private TextView strCount_view;
    private Button gallery_button, update_button;
    private ImageView review_image;
    private String token;
    private String review_index;
    private Uri mNewImageCaptureUri;
    private String imageFilePath;
    private Bitmap new_photo;

    private AppCompatDialog mProgressDialog;
    private Handler mHandler;
    private Message msg;

    private String review_heritage_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_update);

        // ip, port 연결
        ApplicationController applicationController = ApplicationController.getInstance();
        applicationController.buildNetworkService();
        networkService = ApplicationController.getInstance().getNetworkService();

        // 액션바 뒤로가기
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.backbutton);
        getSupportActionBar().setTitle("REVIEW");

        review_image = (ImageView) findViewById(R.id.Rupdate_iv_review_image);
        gallery_button = (Button) findViewById(R.id.Rupdate_btn_gallery);
        review_content = (EditText) findViewById(R.id.Rupdate_et_review_content);
        strCount_view = (TextView) findViewById(R.id.Rupdate_tv_strCount);

        // READ Review
        token = getIntent().getStringExtra("token");
        review_index = getIntent().getStringExtra("review_index");
        Review review = new Review();
        review.setToken(token);
        review.setReview_index(review_index);

        Call<List<Review>> call = networkService.getReadReview(review);
        call.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {

                // 기존 리뷰 이미지, 내용, 날짜출력
                if (response.isSuccessful()) {
                    List<Review> review_temp = response.body();
                    for (int i = 0; i < review_temp.size(); i++) {
                        Glide.with(R_UpdateActivity.this).load(review_temp.get(i).getReview_img()).into(review_image);
                        review_content.setText(review_temp.get(i).getReview_content());
                        review_heritage_index = review_temp.get(i).getReview_heritage_index();
                    }
                } else {
                    int statusCode = response.code();
                    v("read review code ", statusCode + "");
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                v("에러 ", t.getMessage());
            }
        });


        // 갤러리 불러오기 (새로운 사진)
        gallery_button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });

        // review_content 글자 수 세기
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                strCount_view.setText(s.length() + " / 140자");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 140) {
                    s.delete(s.length() - 2, s.length() - 1);
                }
            }
        };
        review_content.addTextChangedListener(textWatcher);


        // MODIFY Review
        update_button = (Button) findViewById(R.id.Rupdate_btn_update);
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressDialog = new AppCompatDialog(R_UpdateActivity.this);
                mProgressDialog.setCancelable(false);
                mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mProgressDialog.setContentView(R.layout.progress_loading);
                mProgressDialog.show();

                final ImageView img_loading_frame = (ImageView) mProgressDialog.findViewById(R.id.iv_frame_loading);
                final AnimationDrawable frameAnimation = (AnimationDrawable) img_loading_frame.getBackground();
                img_loading_frame.post(new Runnable() {
                    @Override
                    public void run() {
                        frameAnimation.start();
                    }
                });

                msg = new Message();

                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        // only modify review_content
                        if (new_photo == null) {
                            Review review = new Review();
                            review.setToken(token);
                            review.setReview_content(review_content.getText().toString());
                            review.setReview_img(null);
                            review.setReview_index(review_index);

                            Call<Review> call = networkService.getModifyOnlyReviewContent(review);
                            call.enqueue(new Callback<Review>() {
                                @Override
                                public void onResponse(Call<Review> call, Response<Review> response) {
                                    if (response.isSuccessful()) {
                                        msg.what = 1;
                                        mHandler.sendMessageDelayed(msg, 2000);
                                    }
                                    v("응답 : ", response.toString());
                                }

                                @Override
                                public void onFailure(Call<Review> call, Throwable t) {
                                    v("에러 : ", t.getMessage());
                                    msg.what = -1;
                                    mHandler.sendMessageDelayed(msg, 2000);
                                }
                            });
                        }
                        // modify All review
                        else {
                            Map<String, RequestBody> map = new HashMap<>();

                            // token
                            RequestBody requestBody1 = RequestBody.create(MediaType.parse("text/plain"), token);
                            map.put("token", requestBody1);

                            // reviewImage
                            RequestBody requestBody2;

                            File imgFile = new File(imageFilePath);
                            requestBody2 = RequestBody.create(MediaType.parse("image/jpg"), imgFile);
                            map.put("update_review_img\"; filename=\"" + imgFile.getName(), requestBody2);

                            // reviewText
                            RequestBody requestBody3 = RequestBody.create(MediaType.parse("text/plain"), review_content.getText().toString());
                            map.put("review_content", requestBody3);

                            // review_index
                            RequestBody requestBody4 = RequestBody.create(MediaType.parse("text/plain"), review_index);
                            map.put("review_index", requestBody4);

                            Call<Review> call = networkService.getModifyAllReview(map);
                            call.enqueue(new Callback<Review>() {

                                @Override
                                public void onResponse(Call<Review> call, Response<Review> response) {
                                    if (response.isSuccessful()) {
                                        msg.what = 100;
                                        mHandler.sendMessageDelayed(msg, 2000);
                                    }
                                    v("응답 : ", response.toString());
                                }

                                @Override
                                public void onFailure(Call<Review> call, Throwable t) {
                                    v("에러 : ", t.getMessage());
                                    msg.what = -100;
                                    mHandler.sendMessageDelayed(msg, 2000);
                                }
                            });
                        }
                    }
                }).start();

                mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        if (msg.what == 100) {
                            mProgressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "수정완료!", Toast.LENGTH_LONG).show();
                            startR_DetailActivity();
                        } else if (msg.what == -100) {
                            mProgressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "다시 시도해주세요.", Toast.LENGTH_LONG).show();
                        }
                        if (msg.what == 1) {
                            mProgressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "수정완료!", Toast.LENGTH_LONG).show();
                            startR_DetailActivity();
                        } else if (msg.what == -1) {
                            mProgressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "다시 시도해주세요.", Toast.LENGTH_LONG).show();
                        } else mProgressDialog.dismiss();
                    }
                };
            }
        });
    }

        // Start to Review Detail Activity
        public void startR_DetailActivity() {
            Bundle extras = new Bundle();
            extras.putString("token", token);
            extras.putString("review_index", review_index);
            Intent intent = new Intent(R_UpdateActivity.this, R_DetailActivity.class);
            intent.putExtras(extras);
            startActivity(intent);
            finish();
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            // 갤러리에서 사진 가져와서 crop하기
            case PICK_FROM_ALBUM: {
                mNewImageCaptureUri = data.getData();
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mNewImageCaptureUri, "image/*");
                intent.putExtra("outputX", 300);
                intent.putExtra("outputY", 300);
                intent.putExtra("aspectX", 4);
                intent.putExtra("aspectY", 4);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_IMAGE);
                break;
            }

            // crop한 이미지 imageView에 출력
            case CROP_FROM_IMAGE: {
                if (resultCode != RESULT_OK) {
                    return;
                }
                final Bundle extras = data.getExtras();

                String filePath = Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/FindHiddenSeoul/" + System.currentTimeMillis() + ".jpg";

                if (extras != null) {
                    Bitmap new_gallery_image = extras.getParcelable("data");
                    new_photo = new_gallery_image;
                    review_image.setImageBitmap(new_gallery_image);

                    storeCropImage(new_gallery_image, filePath);
                    imageFilePath = filePath;
                    break;
                }

                File f = new File(mNewImageCaptureUri.getPath());
                if (f.exists()) f.delete();
            }
        }
    }

    // crop한 사진 갤러리에 저장
    private void storeCropImage(Bitmap bitmap, String filePath) {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FindHiddenSeoul";
        File directory_FHS = new File(dirPath);
        if (!directory_FHS.exists()) directory_FHS.mkdir();

        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try {
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(copyFile)));

            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                Bundle extras = new Bundle();
                extras.putString("review_index", review_index);

                Intent intent = new Intent(R_UpdateActivity.this, R_DetailActivity.class);
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
    public void onBackPressed() {

    }
}
