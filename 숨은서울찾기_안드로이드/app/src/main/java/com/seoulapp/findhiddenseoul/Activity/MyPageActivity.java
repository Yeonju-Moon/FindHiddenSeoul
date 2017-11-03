package com.seoulapp.findhiddenseoul.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.seoulapp.findhiddenseoul.ApplicationController;
import com.seoulapp.findhiddenseoul.ListViewAdapter.ListViewAdapter2;
import com.seoulapp.findhiddenseoul.ListViewAdapter.ListViewAdapter3;
import com.seoulapp.findhiddenseoul.ListViewItem.ListViewItem;
import com.seoulapp.findhiddenseoul.Model.UserInfo;
import com.seoulapp.findhiddenseoul.NetworkService;
import com.seoulapp.findhiddenseoul.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by BANGYURA on 2017-09-22.
 */

public class MyPageActivity extends AppCompatActivity {
    String shared_token, charURL, nickname;
    int level, rank;

    ApplicationController applicationController;
    private NetworkService networkService;

    UserInfo userInfo = new UserInfo();

    ImageView iv_char;
    TextView tv_name, tv_level, tv_rank;

    Button btn_ranking;

    ImageButton likedMore, reviewMore;

    TextView noLiked, noReview;
    AppCompatDialog mProgressDialog;
    Message msg;
    Handler mHandler;

    Bundle extras = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        // sharedPreference 선언
        final SharedPreferences setting = getSharedPreferences("setting", 0);
        final SharedPreferences.Editor editor = setting.edit();

        shared_token = setting.getString("token", "");
        charURL = setting.getString("charURL", "");
        nickname = setting.getString("nickname", "");
        level = setting.getInt("level", 0);
        rank = setting.getInt("rank", 0);
        userInfo.setToken(shared_token);

        iv_char = (ImageView) findViewById(R.id.M_iv_user_char);
        tv_name = (TextView) findViewById(R.id.M_tv_user_name);
        tv_level = (TextView) findViewById(R.id.M_tv_user_level);
        tv_rank = (TextView) findViewById(R.id.M_tv_user_ranking);

        Glide.with(this).load(charURL).into(iv_char);

        tv_name.setText("name. " + nickname);
        tv_name.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/NotoSansKR-Bold.mp3"));
        tv_name.setIncludeFontPadding(false);
        tv_name.setTextSize(15);

        tv_level.setText("Lv. " + level);
        tv_level.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/NotoSansKR-Bold.mp3"));
        tv_level.setIncludeFontPadding(false);
        tv_level.setTextSize(30);

        tv_rank.setText("랭킹. " + rank);
        tv_rank.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/NotoSansKR-Bold.mp3"));
        tv_rank.setIncludeFontPadding(false);
        tv_rank.setTextSize(30);

        btn_ranking = (Button) findViewById(R.id.M_btn_ranking);

        // 액션바 뒤로가기
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.backbutton);

        getSupportActionBar().setTitle("MY PAGE");

        likedMore = (ImageButton) findViewById(R.id.M_btn_more_liked);
        reviewMore = (ImageButton) findViewById(R.id.M_btn_more_review);

        noLiked = (TextView) findViewById(R.id.M_tv_noLiked);
        noReview = (TextView) findViewById(R.id.M_tv_noReview);

        applicationController = new ApplicationController();
        applicationController.onCreate();

        applicationController = ApplicationController.getInstance();
        applicationController.buildNetworkService();
        networkService = ApplicationController.getInstance().getNetworkService();

        TextView tv_liked = (TextView) findViewById(R.id.M_tv_liked);
        tv_liked.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/NotoSansKR-Bold.mp3"));
        tv_liked.setIncludeFontPadding(false);
        tv_liked.setTextSize(40);
        getLiked();

        TextView tv_review = (TextView) findViewById(R.id.M_tv_review);
        tv_review.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/NotoSansKR-Bold.mp3"));
        tv_review.setIncludeFontPadding(false);
        tv_review.setTextSize(40);
        getReview();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent();
                setResult(100, intent);
                finish();

                return true;

        }
        return false;
    }


    /********************************** 좋아요 2개 리스트 *****************************************/

    public void getLiked() {
        final ListView listView = (ListView) findViewById(R.id.M_listview_liked);
        final ListViewAdapter2 adapter = new ListViewAdapter2();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //list item 클릭 시
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //pos = position;
                ListViewItem listViewItem = (ListViewItem) parent.getItemAtPosition(position);

                //다음 페이지로 넘어가기 위해 클릭된 메모의 index를 넘긴다.
                //Bundle extras = new Bundle();
                extras.putString("heritage_index", listViewItem.getHeritage_index());

                Intent intent = new Intent(MyPageActivity.this, H_DetailActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        Call<List<ListViewItem>> thumbnailCall = networkService.get_user_like_recent(userInfo);
        thumbnailCall.enqueue(new Callback<List<ListViewItem>>() {
            @Override
            public void onResponse(Call<List<ListViewItem>> call, Response<List<ListViewItem>> response) {
                if (response.isSuccessful()) {
                    List<ListViewItem> listViewItem_temp = response.body();
                    likedMore.setClickable(true);
                    for (int i = 0; i < listViewItem_temp.size(); i++) {
                        adapter.addItem(listViewItem_temp.get(i).getHeritage_mainImg(), listViewItem_temp.get(i).getGu(),
                                listViewItem_temp.get(i).getHeritage_name(), listViewItem_temp.get(i).getHeritage_index());
                    }
                    listView.setAdapter(adapter);
                    setListViewHeightBasedOnChildren(listView);
                } else {
                    int statusCode = response.code();
                    Log.i("mytag", "응답코드 : " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<List<ListViewItem>> call, Throwable t) {
                //Log.i("MyTag", "서버 onFailure 에러내용 : " + t.getMessage());
                noLiked.setVisibility(View.VISIBLE);
                noLiked.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/NotoSansKR-Medium.mp3"));
                noLiked.setIncludeFontPadding(false);
                noLiked.setText("좋아요를 누른 장소가 없습니다.");
                noLiked.setTextSize(15);
                likedMore.setClickable(false);
            }
        });
    }

    /********************************** 리뷰 2개 리스트 *****************************************/
    public void getReview() {
        final ListView listView = (ListView) findViewById(R.id.M_listview_review);
        final ListViewAdapter3 adapter = new ListViewAdapter3();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //list item 클릭 시
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //pos = position;
                ListViewItem listViewItem = (ListViewItem) parent.getItemAtPosition(position);

                //다음 페이지로 넘어가기 위해 클릭된 메모의 index를 넘긴다.
                //Bundle extras = new Bundle();
                extras.putString("heritage_index", listViewItem.getReview_heritage_index());
                extras.putString("review_index", listViewItem.getReview_index());

                Intent intent = new Intent(MyPageActivity.this, R_DetailActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        Call<List<ListViewItem>> thumbnailCall = networkService.get_user_reivew_recent(userInfo);
        thumbnailCall.enqueue(new Callback<List<ListViewItem>>() {
            @Override
            public void onResponse(Call<List<ListViewItem>> call, Response<List<ListViewItem>> response) {
                if (response.isSuccessful()) {
                    List<ListViewItem> listViewItem_temp = response.body();
                    noLiked.setClickable(true);
                    for (int i = 0; i < listViewItem_temp.size(); i++) {
                        adapter.addItem(listViewItem_temp.get(i).getReview_img(), listViewItem_temp.get(i).getHeritage_name(),
                                listViewItem_temp.get(i).getReview_content(), listViewItem_temp.get(i).getReview_index());
                    }
                    listView.setAdapter(adapter);
                    setListViewHeightBasedOnChildren(listView);
                } else {
                    int statusCode = response.code();
                    Log.i("mytag", "응답코드 : " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<List<ListViewItem>> call, Throwable t) {
                //Log.i("MyTag", "서버 onFailure 에러내용 : " + t.getMessage());
                noReview.setClickable(false);
                noReview.setVisibility(View.VISIBLE);
                noReview.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/NotoSansKR-Medium.mp3"));
                noReview.setIncludeFontPadding(false);
                noReview.setText("리뷰를 남긴 장소가 없습니다.");
                noReview.setTextSize(15);
            }
        });
    }

    public void goDetailList() {
        Intent intent = new Intent(MyPageActivity.this, MyPageListActivity.class);
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void goRanking() {
        Intent intent = new Intent(MyPageActivity.this, ListviewActivity.class);
        startActivity(intent);
    }

    /********************************** 버튼 클릭 리스너 *****************************************/
    public void mOnClick(View view) {
        //다음 페이지로 넘어가기 위해 클릭된 메모의 index를 넘긴다.
        switch (view.getId()) {
            case R.id.M_btn_more_liked:
                extras = new Bundle();
                extras.putString("btn_name", "liked");
                goDetailList();
                break;
            case R.id.M_btn_more_review:
                extras = new Bundle();
                extras.putString("btn_name", "review");
                goDetailList();
                break;
            case R.id.M_btn_ranking:
                goRanking();
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