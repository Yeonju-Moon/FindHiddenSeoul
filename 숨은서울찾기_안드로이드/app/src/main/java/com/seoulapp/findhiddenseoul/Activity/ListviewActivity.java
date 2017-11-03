package com.seoulapp.findhiddenseoul.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.seoulapp.findhiddenseoul.ApplicationController;
import com.seoulapp.findhiddenseoul.ListViewAdapter.ListviewAdapter;
import com.seoulapp.findhiddenseoul.ListViewItem.ListViewItem2;
import com.seoulapp.findhiddenseoul.Model.Rank;
import com.seoulapp.findhiddenseoul.NetworkService;
import com.seoulapp.findhiddenseoul.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HOME on 2017-10-13.
 */
public class ListviewActivity extends ActionBarActivity {

    // 서버 연동
    private NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // 액션바 뒤로가기
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.backbutton);

        getSupportActionBar().setTitle("RANKING");

        // 쉐어드
        final SharedPreferences setting = getSharedPreferences("setting", 0);
        String shared_token = setting.getString("token", "");
        Log.e("Main onCreate token", "token 확인 " + shared_token);

        // ip, port 연결
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        networkService = ApplicationController.getInstance().getNetworkService();

        Rank rank = new Rank();
        rank.setToken(shared_token);

        final ListView listView = (ListView)findViewById(R.id.listview);
        final ArrayList<ListViewItem2> data = new ArrayList<>();
        final ListviewAdapter adapter = new ListviewAdapter(this, R.layout.item, data);

        final TextView rank_img = (TextView)findViewById(R.id.rank_img1);
        final ImageView rank_profile = (ImageView)findViewById(R.id.rank_profile1);
        final TextView rank_nick = (TextView)findViewById(R.id.rank_nick1);
        final TextView rank_level = (TextView)findViewById(R.id.rank_level1);
        final TextView rank_reviewnum = (TextView)findViewById(R.id.rank_reviewnum1);

        Call<List<ListViewItem2>> thumbnailCall = networkService.post_rank(rank);
        thumbnailCall.enqueue(new Callback<List<ListViewItem2>>() {
            @Override
            public void onResponse(Call<List<ListViewItem2>> call, Response<List<ListViewItem2>> response) {
                if (response.isSuccessful()) {
                    List<ListViewItem2> listviewitem_temp = response.body();
                    for (int i = 0; i < listviewitem_temp.size(); i++) {
                        Log.e("리스트뷰", "@@@@@@@@@리스트뷰 : " + listviewitem_temp.get(i).getRANK());
                        if (i == 0) {
                            Log.e("리스트뷰", "@@@@@@@@@리스트뷰 1등 : " + listviewitem_temp.get(i).getRANK());

                            rank_nick.setText(listviewitem_temp.get(i).getNick_name());
                            rank_img.setText(listviewitem_temp.get(i).getRANK() + " .");
                            Glide.with(ListviewActivity.this).load(listviewitem_temp.get(i).getLevel_imgUrl()).into(rank_profile);
                            rank_level.setText(listviewitem_temp.get(i).getLevel() + "");
                            rank_reviewnum.setText(listviewitem_temp.get(i).getCounts() + "");

                        } else {
                            ListViewItem2 temp = new ListViewItem2(listviewitem_temp.get(i).getRANK(), listviewitem_temp.get(i).getLevel_imgUrl(), listviewitem_temp.get(i).getNick_name(),
                                    listviewitem_temp.get(i).getLevel(), listviewitem_temp.get(i).getCounts());

                            data.add(temp);
                        }
                    }

                    listView.setAdapter(adapter);

                } else {
                    int statusCode = response.code();
                    Log.i("mytag", "응답코드 : " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<List<ListViewItem2>> call, Throwable t) {
                Log.e("리스트뷰 서버 오류", "@@@@@@@@@@@서버연결 실패" + t);
            }
        });

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

                Intent intent = new Intent(ListviewActivity.this, MyPageActivity.class);
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