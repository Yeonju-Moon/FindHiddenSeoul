package com.seoulapp.findhiddenseoul.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.seoulapp.findhiddenseoul.ApplicationController;
import com.seoulapp.findhiddenseoul.Fragment.ImageFragment;
import com.seoulapp.findhiddenseoul.ListViewAdapter.ListViewAdapter3;
import com.seoulapp.findhiddenseoul.ListViewItem.ListViewItem;
import com.seoulapp.findhiddenseoul.Model.UserInfo;
import com.seoulapp.findhiddenseoul.NetworkService;
import com.seoulapp.findhiddenseoul.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by BANGYURA on 2017-10-25.
 */

public class H_DetailActivity extends AppCompatActivity {
    String heritage_index;
    // sharedPreference 선언
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    ApplicationController applicationController;
    NetworkService networkService;
    UserInfo userInfo = new UserInfo();
    String get_gu, get_heritage_name;
    int count;
    ViewPager viewPager;
    pagerAdapter mPagerAdapter;
    String showPart, showAll;
    int pos;
    final Context context = this;
    Button like, more;
    String shared_token;
    TextView detailTextView;
    int y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_detail);

        setting = getSharedPreferences("setting", 0);
        editor = setting.edit();
        shared_token = setting.getString("token", "");

        final TextView guAndName = (TextView) findViewById(R.id.Hdetail_tv_guAndName);

        final ImageButton r = (ImageButton) findViewById(R.id.Hdetail_btn_right);
        final ImageButton l = (ImageButton) findViewById(R.id.Hdetail_btn_left);

        final TextView h_name = (TextView) findViewById(R.id.Hdetail_tv_h_name);
        like = (Button) findViewById(R.id.Hdetail_btn_liked);

        // 액션바 뒤로가기
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.backbutton);

        // 앞 페이지에서 받아오는 heritage_index
        heritage_index = getIntent().getStringExtra("heritage_index");

        detailTextView = (TextView) findViewById(R.id.Hdetail_tv_details);

        final TextView reviewTextView = (TextView) findViewById(R.id.Hdetail_tv_review);
        reviewTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/NotoSansKR-Bold.mp3"));
        reviewTextView.setIncludeFontPadding(false);
        reviewTextView.setTextSize(36);

        more = (Button) findViewById(R.id.Hdetail_btn_more);

        final TextView noReview = (TextView) findViewById(R.id.Hdetail_tv_noReview);

        applicationController = ApplicationController.getInstance();
        applicationController.buildNetworkService();
        networkService = ApplicationController.getInstance().getNetworkService();

/************************************ heritage 설명 *************************************/
        final Call<ResponseBody> detail = networkService.getDetail(heritage_index);
        detail.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        Log.v("Test", result);

                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String heritage_name = jsonObject.getString("heritage_name");
                                String gu = jsonObject.getString("gu");
                                String locate = jsonObject.getString("location");
                                String gunlipsigi = jsonObject.getString("gunlipsigi");
                                String gunlipyongdo = jsonObject.getString("gunlipyongdo");
                                String eeryeok = jsonObject.getString("gunlipsigi");
                                String bozone = jsonObject.getString("bozone");
                                String bus_station = jsonObject.getString("bus_station");
                                String bus_number = jsonObject.getString("bus_number");
                                String bus_walk = jsonObject.getString("bus_walk");
                                String subway_station = jsonObject.getString("subway_station");
                                String subway_walk = jsonObject.getString("subway_walk");
                                String parkinglot = jsonObject.getString("parkinglot");
                                String max_parking = jsonObject.getString("max_parking");
                                String parking_fee = jsonObject.getString("parking_fee");
                                String parking_time = jsonObject.getString("parking_time");
                                String heritage_detail = jsonObject.getString("heritage_detail");

                                get_gu = gu;
                                get_heritage_name = heritage_name;

                                // 이미지 위 구+유산이름
                                Log.v("guAndname", get_gu + get_heritage_name);
                                guAndName.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/NotoSansKR-Bold.mp3"));
                                guAndName.setIncludeFontPadding(false);
                                guAndName.setTextSize(30);
                                guAndName.setText(get_gu + "\n" + get_heritage_name);

                                //유산이름
                                h_name.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/NotoSansKR-Bold.mp3"));
                                h_name.setIncludeFontPadding(false);
                                h_name.setTextSize(36);
                                h_name.setText(get_heritage_name);
                                h_name.setSingleLine(true);
                                h_name.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                                //포커스를 받아야 문자가 흐르기 때문에
                                //포커스를 받을 수 없는 상황에서는 선택된 것으로 처리하면 마키 동작
                                h_name.setSelected(true);

                                //String category = jsonObject.getString("category");

                                showPart = "- 위치 : " + locate + "\n- 건립시기 : " + gunlipsigi
                                        + "\n- 건립용도 : " + gunlipyongdo + "\n- 이력 : " + eeryeok + "\n- 보존 : " + bozone.trim();

                                showAll = "- 위치 : " + locate + "\n- 건립시기 : " + gunlipsigi
                                        + "\n- 건립용도 : " + gunlipyongdo + "\n- 이력 : " + eeryeok + "\n- 보존 : " + bozone.trim()
                                        + "\n- " + bus_station + "\n- " + bus_number + "\n- " + bus_walk
                                        + "\n- " + subway_station + "\n- " + subway_walk
                                        + "\n- 주차장 : " + parkinglot + "\n- 주차 가능 수" + max_parking + "\n- 주차요금 : " + parking_fee + "\n- 주차시간 : " + parking_time
                                        + "\n- 설명 : " + heritage_detail.trim();

                                detailTextView.setText(showPart);
                                //detailTextView.setMaxLines(5);
                                detailTextView.setEllipsize(TextUtils.TruncateAt.END);
                                detailTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/NotoSansKR-Regular.mp3"));
                                detailTextView.setIncludeFontPadding(false);
                                detailTextView.setTextSize(13);

                                Log.v("Test", response.body().toString());

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    int statusCode = response.code();
                    Log.i("mytag", "응답코드 : " + statusCode);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        viewPager = (ViewPager) findViewById(R.id.Hdetail_viewPager);
        //final LinearLayout curPageMarkLayout = (LinearLayout) findViewById(R.id.page_mark);
        mPagerAdapter = new pagerAdapter(getSupportFragmentManager());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pos = position;

                if (position == 0) {
                    l.setEnabled(false);
                    l.setImageResource(R.drawable.leftgray);
                    viewPager.setAlpha(0.8F);
                    guAndName.setVisibility(View.VISIBLE);
                } else if (position == (count - 1)) {
                    r.setEnabled(false);
                    r.setImageResource(R.drawable.rightgray);
                    viewPager.setAlpha(1.0F);
                    guAndName.setVisibility(View.INVISIBLE);
                } else {
                    l.setEnabled(true);
                    l.setImageResource(R.drawable.leftblack);
                    r.setEnabled(true);
                    r.setImageResource(R.drawable.rightblack);
                    viewPager.setAlpha(1.0F);
                    guAndName.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        /************************************ Image Detail *************************************/
        Call<ResponseBody> detail_img = networkService.getDetail_img(heritage_index);
        detail_img.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        Log.v("Test", result);

                        try {
                            JSONArray jsonArray = new JSONArray(result);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String img_url = jsonObject.getString("img_url");

                                mPagerAdapter.addImg(img_url);
                                //Log.v("Test", img_url);
                            }
                            viewPager.setAdapter(mPagerAdapter);
                            viewPager.setCurrentItem(pos);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    int statusCode = response.code();
                    Log.i("mytag", "응답코드 : " + statusCode);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });

        /************************************ Image Count *************************************/
        final Call<ResponseBody> img_count = networkService.getImg_count(heritage_index);
        img_count.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        Log.v("Test", result);

                        try {
                            JSONArray jsonArray = new JSONArray(result);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String img_count_str = jsonObject.getString("img_count");
                                //Log.v("Test", img_url);

                                count = Integer.parseInt(img_count_str);
                                Log.v("count", img_count_str);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    int statusCode = response.code();
                    Log.i("mytag", "응답코드 : " + statusCode);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });


        /************************************ Review list *************************************/
        final ListView reviewListView = (ListView) findViewById(R.id.Hdetail_listview_review);
        reviewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //list item 클릭 시
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                ListViewItem listViewItem = (ListViewItem) parent.getItemAtPosition(position);

                //다음 페이지로 넘어가기 위해 클릭된 메모의 index를 넘긴다.
                Bundle extras = new Bundle();
                extras.putString("heritage_index", heritage_index);
                extras.putString("review_index", listViewItem.getReview_index());
                Intent intent = new Intent(H_DetailActivity.this, R_DetailActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
                //Toast.makeText(H_DetailActivity.this, listViewItem.getReview_index(), Toast.LENGTH_SHORT).show();
            }
        });

        final ListViewAdapter3 reviewAdapter = new ListViewAdapter3();

        Call<ResponseBody> heritage_review = networkService.getHeritage_review(heritage_index);
        heritage_review.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();

                        if (result.equals("")) {
                            //Toast.makeText(H_DetailActivity.this, "no", Toast.LENGTH_SHORT).show();
                            noReview.setVisibility(View.VISIBLE);
                            noReview.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/NotoSansKR-Medium.mp3"));
                            noReview.setIncludeFontPadding(false);
                            noReview.setText(get_heritage_name + "의 첫 방문자가 되어보세요");
                            noReview.setTextSize(15);
                        } else {
                            try {
                                JSONArray jsonArray = new JSONArray(result);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String review_img = jsonObject.getString("review_img");
                                    String nickname = jsonObject.getString("nickname");
                                    String review_content = jsonObject.getString("review_content");
                                    String review_index = jsonObject.getString("review_index");

                                    /*if (review_content.length() > 10) {
                                        review_content = review_content.substring(0, 10);
                                    }*/

                                    reviewAdapter.addItem(review_img, nickname, review_content, review_index);
                                    Log.v("Test2", review_img + nickname + review_content + review_index);
                                }
                                reviewListView.setAdapter(reviewAdapter);
                                setListViewHeightBasedOnChildren(reviewListView);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    int statusCode = response.code();
                    Log.i("mytag", "응답코드 : " + statusCode);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        /************************************ like_status *************************************/
        userInfo.setToken(shared_token);
        userInfo.setHeritage_index(heritage_index);

        Call<UserInfo> thumbnailCall = networkService.getLike_status(userInfo);
        thumbnailCall.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.isSuccessful()) {
                    UserInfo userInfo_temp = response.body();
                    if (userInfo_temp.getLike_check().equals("unlike")) {
                        //Toast.makeText(H_DetailActivity.this, "눌렸음", Toast.LENGTH_SHORT).show();
                        like.setSelected(false);
                    } else if (userInfo_temp.getLike_check().equals("like")) {
                        //Toast.makeText(H_DetailActivity.this, "해제", Toast.LENGTH_SHORT).show();
                        like.setSelected(true);
                    }
                } else {
                    int statusCode = response.code();
                    Log.i("mytag", "응답코드 : " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Log.i("MyTag", "서버 onFailure 에러내용 : " + t.getMessage());
            }
        });

        final ScrollView scrollView = (ScrollView) findViewById(R.id.Hdetail_scrollView);

        final Button btn_top = (Button) findViewById(R.id.Hdetail_btn_top);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = scrollView.getScrollX();
                y = scrollView.getScrollY();

                Log.v("scroll", "X = " + x + ", Y = " + y);

                if (y != 0) {
                    btn_top.setVisibility(View.VISIBLE);
                } else if (y == 0) {
                    btn_top.setVisibility(View.INVISIBLE);
                    //Toast.makeText(H_DetailActivity.this, "y=0", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });

        btn_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.smoothScrollTo(0, 0);
                // scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    private class pagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<String> imgUrl2 = new ArrayList<>();

        public pagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }


        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return new ImageFragment().newInstance(imgUrl2.get(position).toString(), imgUrl2.size(), pos);
        }

        @Override
        public int getCount() {
            return imgUrl2.size();
        }

        public void addImg(String img) {

            imgUrl2.add(img);

            notifyDataSetChanged();
        }
    }


    public void postingLiked() {
        userInfo.setToken(shared_token);
        userInfo.setHeritage_index(heritage_index);

        Call<UserInfo> thumbnailCall = networkService.postClicked_like(userInfo);
        thumbnailCall.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.isSuccessful()) {
                    UserInfo userInfo_temp = response.body();
                    if (userInfo_temp.getLike().equals("true")) {
                        //Toast.makeText(H_DetailActivity.this, "눌렸음", Toast.LENGTH_SHORT).show();
                    } else if (userInfo_temp.getLike().equals("false")) {
                        //Toast.makeText(H_DetailActivity.this, "해제", Toast.LENGTH_SHORT).show();
                    }
                } else {
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

    public void postingChecked() {
        userInfo.setToken(shared_token);
        userInfo.setHeritage_index(heritage_index);

        if (userInfo.getToken().equals("")) {
            //다이얼로그
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            // 제목셋팅
            alertDialogBuilder.setTitle("알림");

            // AlertDialog 셋팅
            alertDialogBuilder
                    .setMessage("로그인 해주시길 바랍니다.")
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

        Call<UserInfo> thumbnailCall = networkService.get_review_check(userInfo);
        thumbnailCall.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.isSuccessful()) {
                    UserInfo userInfo_temp = response.body();
                    Log.v("review_check", userInfo_temp.getWrite_review());
                    if (userInfo_temp.getWrite_review().equals("true")) {
                        //리뷰작성가능
                        //다음 페이지로 넘어가기 위해 클릭된 메모의 index를 넘긴다.
                        Bundle extras = new Bundle();
                        extras.putString("heritage_index", heritage_index);
                        Intent intent = new Intent(H_DetailActivity.this, R_WriteActivity.class);
                        intent.putExtras(extras);
                        startActivity(intent);

                    } else if (userInfo_temp.getWrite_review().equals("false")) {
                        //다이얼로그
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                context);

                        // 제목셋팅
                        alertDialogBuilder.setTitle("알림");

                        // AlertDialog 셋팅
                        alertDialogBuilder
                                .setMessage("리뷰를 이미 작성하셨습니다.")
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
                } else {
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

    public void nOnClick(View view) {
        switch (view.getId()) {
            case R.id.Hdetail_btn_left:
                viewPager.setCurrentItem(pos - 1, true);
                break;
            case R.id.Hdetail_btn_right:
                viewPager.setCurrentItem(pos + 1, true);
                break;
            case R.id.Hdetail_btn_liked:
                like.setSelected(!like.isSelected());
                if (like.isSelected()) {
                    //Handle selected state change
                    like.setSelected(true);
                } else {
                    //Handle de-select state change
                    like.setSelected(false);
                }

                postingLiked();
                break;
            case R.id.Hdetail_btn_addReview:
                postingChecked();
                break;
            case R.id.Hdetail_btn_more:
                more.setSelected(!more.isSelected());
                if (more.isSelected()) {
                    //Handle selected state change
                    more.setSelected(true);
                    detailTextView.setText(showAll);
                } else {
                    //Handle de-select state change
                    more.setSelected(false);
                    detailTextView.setText(showPart);
                    /*detailTextView.setMaxLines(5);
                    detailTextView.setEllipsize(TextUtils.TruncateAt.END);*/
                }
                break;
            default:
                break;
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                // sharedPreference 선언
                final SharedPreferences setting = getSharedPreferences("setting", 0);
                final SharedPreferences.Editor editor = setting.edit();

                Intent intent = new Intent(H_DetailActivity.this, H_ListActivity.class);
                intent.putExtra("guText", get_gu);
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