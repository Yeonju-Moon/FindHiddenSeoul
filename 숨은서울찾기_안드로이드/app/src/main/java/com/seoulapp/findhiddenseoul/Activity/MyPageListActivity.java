package com.seoulapp.findhiddenseoul.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.seoulapp.findhiddenseoul.ApplicationController;
import com.seoulapp.findhiddenseoul.ListViewAdapter.ListViewAdapter2;
import com.seoulapp.findhiddenseoul.ListViewItem.ListViewItem;
import com.seoulapp.findhiddenseoul.Model.UserInfo;
import com.seoulapp.findhiddenseoul.NetworkService;
import com.seoulapp.findhiddenseoul.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by BANGYURA on 2017-10-20.
 */
// 인터넷 접속이 안되서 이미지가 뜨지 않을때 디폴트 이미지? 가튼거 넣기
public class MyPageListActivity extends AppCompatActivity{
    ApplicationController applicationController;
    private NetworkService networkService;

    UserInfo userInfo = new UserInfo();

    // Mypage에서 눌린 버튼의 이름
    String btn_name;

    String shared_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        // 액션바 뒤로가기
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.backbutton);

        // sharedPreference 선언
        final SharedPreferences setting = getSharedPreferences("setting", 0);
        final SharedPreferences.Editor editor = setting.edit();

        shared_token = setting.getString("token", "");

        userInfo.setToken(shared_token);

        applicationController = new ApplicationController();
        applicationController.onCreate();

        applicationController = ApplicationController.getInstance();
        applicationController.buildNetworkService();
        networkService = ApplicationController.getInstance().getNetworkService();

        btn_name = getIntent().getStringExtra("btn_name");

        if(btn_name.equals("liked")){
            getLiked();
        }else if(btn_name.equals("review")){
            getReview();
        }

        getSupportActionBar().setTitle(btn_name);

        ListView li = (ListView) findViewById(R.id.listview);

        /*변경하고 싶은 레이아웃의 파라미터 값을 가져 옴*/
        LinearLayout.LayoutParams plControl = (LinearLayout.LayoutParams) li.getLayoutParams();

        /*해당 margin값 변경*/
        plControl.leftMargin = 14;
        plControl.topMargin = 15;

        /*변경된 값의 파라미터를 해당 레이아웃 파라미터 값에 셋팅*/
        li.setLayoutParams(plControl);
    }

    public void getLiked(){
        final ListView listView = (ListView) findViewById(R.id.listview);
        final ListViewAdapter2 adapter = new ListViewAdapter2();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //list item 클릭 시
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewItem listViewItem = (ListViewItem) parent.getItemAtPosition(position);

                //다음 페이지로 넘어가기 위해 클릭된 메모의 index를 넘긴다.
                Bundle extras = new Bundle();
                extras.putString("heritage_index", listViewItem.getHeritage_index());

                Intent intent = new Intent(MyPageListActivity.this, H_DetailActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        Call<List<ListViewItem>> thumbnailCall = networkService.get_user_like(userInfo);
        thumbnailCall.enqueue(new Callback<List<ListViewItem>>() {
            @Override
            public void onResponse(Call<List<ListViewItem>> call, Response<List<ListViewItem>> response) {
                if(response.isSuccessful()){
                    List<ListViewItem> listViewItem_temp = response.body();
                    for (int i = 0; i < listViewItem_temp.size(); i++) {
                        adapter.addItem(listViewItem_temp.get(i).getHeritage_mainImg(), listViewItem_temp.get(i).getGu(),
                                            listViewItem_temp.get(i).getHeritage_name(), listViewItem_temp.get(i).getHeritage_index());
                    }
                    listView.setAdapter(adapter);
                }else{
                    int statusCode = response.code();
                    Log.i("mytag", "응답코드 : " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<List<ListViewItem>> call, Throwable t) {
                Log.i("MyTag", "서버 onFailure 에러내용 : " + t.getMessage());
            }
        });
    }

    public void getReview(){
        final ListView listView = (ListView) findViewById(R.id.listview);
        final ListViewAdapter2 adapter = new ListViewAdapter2();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //list item 클릭 시
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewItem listViewItem = (ListViewItem) parent.getItemAtPosition(position);

                //다음 페이지로 넘어가기 위해 클릭된 메모의 index를 넘긴다.
                Bundle extras = new Bundle();
                extras.putString("heritage_index", listViewItem.getReview_heritage_index());
                extras.putString("review_index", listViewItem.getReview_index());

                Intent intent = new Intent(MyPageListActivity.this, R_DetailActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        Call<List<ListViewItem>> thumbnailCall = networkService.get_user_review(userInfo);
        thumbnailCall.enqueue(new Callback<List<ListViewItem>>() {
            @Override
            public void onResponse(Call<List<ListViewItem>> call, Response<List<ListViewItem>> response) {
                if(response.isSuccessful()){
                    List<ListViewItem> listViewItem_temp = response.body();
                    for (int i = 0; i < listViewItem_temp.size(); i++) {
                        adapter.addItem(listViewItem_temp.get(i).getReview_img(), listViewItem_temp.get(i).getReview_content(),
                                            null, listViewItem_temp.get(i).getReview_index());
                    }
                    listView.setAdapter(adapter);
                }else{
                    int statusCode = response.code();
                    Log.i("mytag", "응답코드 : " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<List<ListViewItem>> call, Throwable t) {
                Log.i("MyTag", "서버 onFailure 에러내용 : " + t.getMessage());
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                Intent intent = new Intent(MyPageListActivity.this, MyPageActivity.class);
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
