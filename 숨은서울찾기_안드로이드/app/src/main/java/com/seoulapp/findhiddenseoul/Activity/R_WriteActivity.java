package com.seoulapp.findhiddenseoul.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seoulapp.findhiddenseoul.ApplicationController;
import com.seoulapp.findhiddenseoul.Model.Review;
import com.seoulapp.findhiddenseoul.NetworkService;
import com.seoulapp.findhiddenseoul.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class R_WriteActivity extends AppCompatActivity {

    // retrofit2.3.0 networkService
    private NetworkService networkService;

    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;


    private ImageView review_image;
    private Button gallery_button, store_button, cancel_button;
    private EditText review_content;
    private TextView strCount_View;
    private Uri mImageCaptureUri;
    private String imageFilePath;
    private AppCompatDialog mProgressDialog;
    private Handler mHandler;
    private Bitmap gallery_image;
    private String isLevelup;
    private String heritage_index;
    private Message msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_write);

        // ip, port 연결
        ApplicationController applicationController = ApplicationController.getInstance();
        applicationController.buildNetworkService();
        networkService = ApplicationController.getInstance().getNetworkService();

        // 액션바
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.backbutton);
        getSupportActionBar().setTitle("REVIEW");

        // sharedPreference
        final SharedPreferences setting = getSharedPreferences("setting", 0);
        final String shared_token = setting.getString("token", "");

        review_image = (ImageView) findViewById(R.id.Rwrite_iv_review_image);
        gallery_button = (Button) findViewById(R.id.Rwrite_btn_gallery);
        review_content = (EditText) findViewById(R.id.Rwrite_et_review_content);
        strCount_View = (TextView) findViewById(R.id.Rwrite_tv_strCount);
        store_button = (Button) findViewById(R.id.Rwrite_btn_create);
        cancel_button = (Button) findViewById(R.id.Rwrite_btn_cancel);
        heritage_index = getIntent().getStringExtra("heritage_index");
        mHandler = new Handler();


        // CANCEL
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startH_DetailActivity();
            }
        });

        // STORE
        store_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog = new AppCompatDialog(R_WriteActivity.this);
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

                // Handler Message
                msg = new Message();

                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        // no review_img
                        if (gallery_image == null) {
                            Review review = new Review();
                            review.setToken(shared_token);
                            review.setReview_content(review_content.getText().toString());
                            review.setReview_img(null);
                            review.setHeritage_index(heritage_index);

                            Call<Review> call = networkService.getCreateReviewNoImage(review);
                            call.enqueue(new Callback<Review>() {
                                @Override
                                public void onResponse(Call<Review> call, Response<Review> response) {
                                    if (response.isSuccessful()) {
                                        msg.what = 1;
                                        mHandler.sendMessageDelayed(msg, 2000);
                                        Review review_temp = response.body();
                                        isLevelup = review_temp.getLevelup();
                                        Log.v("no_image: levelup 값", isLevelup);
                                    } else {
                                        Log.v("no_image:levelup code", response.toString());
                                    }
                                }

                                @Override
                                public void onFailure(Call<Review> call, Throwable t) {
                                    msg.what = -1;
                                    mHandler.sendMessageDelayed(msg, 2000);
                                    Log.v("no_image:levelup error ", t.getMessage());
                                }
                            });
                        }

                        // all review
                        else {
                            Map<String, RequestBody> map = new HashMap<>();

                            // token
                            RequestBody requestBody1 = RequestBody.create(MediaType.parse("text/plain"), shared_token);
                            map.put("token", requestBody1);

                            // heritage_index
                            RequestBody requestBody2 = RequestBody.create(MediaType.parse("text/plain"), heritage_index);
                            map.put("heritage_index", requestBody2);

                            // reviewImage
                            RequestBody requestBody3;
                            File imgFile = new File(imageFilePath);
                            requestBody3 = RequestBody.create(MediaType.parse("image/jpg"), imgFile);
                            map.put("review_img\"; filename=\"" + imgFile.getName(), requestBody3);

                            // reviewText
                            RequestBody requestBody4 = RequestBody.create(MediaType.parse("text/plain"), review_content.getText().toString());
                            map.put("review_content", requestBody4);


                            Call<Review> call = networkService.getCreateReview(map);
                            call.enqueue(new Callback<Review>() {
                                @Override
                                public void onResponse(Call<Review> call, Response<Review> response) {
                                    if (response.isSuccessful()) {
                                        msg.what = 100;
                                        mHandler.sendMessageDelayed(msg, 2000);
                                        Review review_temp = response.body();
                                        isLevelup = review_temp.getLevelup();
                                        Log.v("yes_image: levelup 값", isLevelup);
                                    } else {
                                        Log.v("yes_image: levelup code", response.toString());
                                    }
                                }

                                @Override
                                public void onFailure(Call<Review> call, Throwable t) {
                                    msg.what = -100;
                                    mHandler.sendMessageDelayed(msg, 2000);
                                    Log.v("yes_image:error", t.getMessage());
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
                            Toast.makeText(getApplicationContext(), "작성완료!", Toast.LENGTH_LONG).show();
                            startH_DetailActivity();
                        } else if (msg.what == -100) {
                            mProgressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "다시 시도해주세요.", Toast.LENGTH_LONG).show();
                        }
                        if (msg.what == 1) {
                            mProgressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "작성완료!", Toast.LENGTH_LONG).show();
                            startH_DetailActivity();

                        } else if (msg.what == -1) {
                            mProgressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "다시 시도해주세요.", Toast.LENGTH_LONG).show();
                        } else mProgressDialog.dismiss();
                    }
                };


            }
        });

        // strCount_view 글자 수 세기
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                strCount_View.setText(s.length() + " / 140자");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 140) {
                    s.delete(s.length() - 2, s.length() - 1);
                }
            }
        };

        review_content.addTextChangedListener(textWatcher);

        // 갤러리 불러오기
        gallery_button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });

    }

    public void startH_DetailActivity() {
        Intent intent = new Intent(R_WriteActivity.this, H_DetailActivity.class);
        intent.putExtra("heritage_index", heritage_index);
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
                mImageCaptureUri = data.getData();
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");
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
                    gallery_image = extras.getParcelable("data");
                    review_image.setImageBitmap(gallery_image);

                    storeCropImage(gallery_image, filePath);
                    imageFilePath = filePath;
                    break;
                }

                File f = new File(mImageCaptureUri.getPath());
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
                extras.putString("heritage_index", heritage_index);

                Intent intent = new Intent(R_WriteActivity.this, H_DetailActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

/*
    @Override
    public void onPause() {
        super.onPause();

        // Remove the activity when its off the screen
        finish();
    }
*/

    @Override
    public void onBackPressed() {

    }
}



