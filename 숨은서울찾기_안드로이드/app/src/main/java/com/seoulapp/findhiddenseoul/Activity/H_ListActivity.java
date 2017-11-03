package com.seoulapp.findhiddenseoul.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.seoulapp.findhiddenseoul.Fragment.ListFragment1;
import com.seoulapp.findhiddenseoul.Fragment.ListFragment2;
import com.seoulapp.findhiddenseoul.R;

/**
 * Created by BANGYURA on 2017-10-25.
 */

public class H_ListActivity extends AppCompatActivity {
    // MainActivity에서 받아오는 gu이름
    String guText;

    // 리스트를 붙일 뷰페이저
    ViewPager viewPager;

    ImageButton btn_first, btn_second, btn_third, btn_fourth, btn_fifth, btn_sixth;

    // 현재 선택된 페이지 위치
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_list);

        TextView likes = (TextView) findViewById(R.id.Hlist_tv_likes);
        likes.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/NotoSansKR-Bold.mp3"));
        likes.setIncludeFontPadding(false);
        likes.setTextSize(36);

        // 뷰페이지
        viewPager = (ViewPager) findViewById(R.id.Hlist_viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pos = position;
                switch (position) {
                    case 0:
                        btn_first.setImageResource(R.drawable.category01yellow);
                        btn_second.setImageResource(R.drawable.category02);
                        btn_third.setImageResource(R.drawable.category03);
                        btn_fourth.setImageResource(R.drawable.category04);
                        btn_fifth.setImageResource(R.drawable.category05);
                        btn_sixth.setImageResource(R.drawable.category06);
                        break;
                    case 1:
                        btn_first.setImageResource(R.drawable.category01);
                        btn_second.setImageResource(R.drawable.category02yellow);
                        btn_third.setImageResource(R.drawable.category03);
                        btn_fourth.setImageResource(R.drawable.category04);
                        btn_fifth.setImageResource(R.drawable.category05);
                        btn_sixth.setImageResource(R.drawable.category06);
                        break;
                    case 2:
                        btn_first.setImageResource(R.drawable.category01);
                        btn_second.setImageResource(R.drawable.category02);
                        btn_third.setImageResource(R.drawable.category03yellow);
                        btn_fourth.setImageResource(R.drawable.category04);
                        btn_fifth.setImageResource(R.drawable.category05);
                        btn_sixth.setImageResource(R.drawable.category06);
                        break;
                    case 3:
                        btn_first.setImageResource(R.drawable.category01);
                        btn_second.setImageResource(R.drawable.category02);
                        btn_third.setImageResource(R.drawable.category03);
                        btn_fourth.setImageResource(R.drawable.category04yellow);
                        btn_fifth.setImageResource(R.drawable.category05);
                        btn_sixth.setImageResource(R.drawable.category06);
                        break;
                    case 4:
                        btn_first.setImageResource(R.drawable.category01);
                        btn_second.setImageResource(R.drawable.category02);
                        btn_third.setImageResource(R.drawable.category03);
                        btn_fourth.setImageResource(R.drawable.category04);
                        btn_fifth.setImageResource(R.drawable.category05yellow);
                        btn_sixth.setImageResource(R.drawable.category06);
                        break;
                    case 5:
                        btn_first.setImageResource(R.drawable.category01);
                        btn_second.setImageResource(R.drawable.category02);
                        btn_third.setImageResource(R.drawable.category03);
                        btn_fourth.setImageResource(R.drawable.category04);
                        btn_fifth.setImageResource(R.drawable.category05);
                        btn_sixth.setImageResource(R.drawable.category06yellow);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        btn_first = (ImageButton) findViewById(R.id.Hlist_btn_first);
        btn_second = (ImageButton) findViewById(R.id.Hlist_btn_second);
        btn_third = (ImageButton) findViewById(R.id.Hlist_btn_third);
        btn_fourth = (ImageButton) findViewById(R.id.Hlist_btn_fourth);
        btn_fifth = (ImageButton) findViewById(R.id.Hlist_btn_fifth);
        btn_sixth = (ImageButton) findViewById(R.id.Hlist_btn_sixth);

        viewPager.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(0);

        btn_first.setOnClickListener(movePageListener);
        btn_first.setTag(0);
        btn_second.setOnClickListener(movePageListener);
        btn_second.setTag(1);
        btn_third.setOnClickListener(movePageListener);
        btn_third.setTag(2);
        btn_fourth.setOnClickListener(movePageListener);
        btn_fourth.setTag(3);
        btn_fifth.setOnClickListener(movePageListener);
        btn_fifth.setTag(4);
        btn_sixth.setOnClickListener(movePageListener);
        btn_sixth.setTag(5);

        // Main -> Sub 받아온 것
        guText = getIntent().getStringExtra("guText");

        // 액션바 뒤로가기
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.backbutton);

        getSupportActionBar().setTitle(guText);

        /*if(pos)*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        //ViewPagerAdapter.notifyDataSetChanged();
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

    View.OnClickListener movePageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int tag = (int) v.getTag();
            viewPager.setCurrentItem(tag);

            switch (v.getId()) {
                case R.id.Hlist_btn_first:
                    btn_first.setImageResource(R.drawable.category01yellow);
                    btn_second.setImageResource(R.drawable.category02);
                    btn_third.setImageResource(R.drawable.category03);
                    btn_fourth.setImageResource(R.drawable.category04);
                    btn_fifth.setImageResource(R.drawable.category05);
                    btn_sixth.setImageResource(R.drawable.category06);
                    break;
                case R.id.Hlist_btn_second:
                    btn_first.setImageResource(R.drawable.category01);
                    btn_second.setImageResource(R.drawable.category02yellow);
                    btn_third.setImageResource(R.drawable.category03);
                    btn_fourth.setImageResource(R.drawable.category04);
                    btn_fifth.setImageResource(R.drawable.category05);
                    btn_sixth.setImageResource(R.drawable.category06);
                    break;
                case R.id.Hlist_btn_third:
                    btn_first.setImageResource(R.drawable.category01);
                    btn_second.setImageResource(R.drawable.category02);
                    btn_third.setImageResource(R.drawable.category03yellow);
                    btn_fourth.setImageResource(R.drawable.category04);
                    btn_fifth.setImageResource(R.drawable.category05);
                    btn_sixth.setImageResource(R.drawable.category06);
                    break;
                case R.id.Hlist_btn_fourth:
                    btn_first.setImageResource(R.drawable.category01);
                    btn_second.setImageResource(R.drawable.category02);
                    btn_third.setImageResource(R.drawable.category03);
                    btn_fourth.setImageResource(R.drawable.category04yellow);
                    btn_fifth.setImageResource(R.drawable.category05);
                    btn_sixth.setImageResource(R.drawable.category06);
                    break;
                case R.id.Hlist_btn_fifth:
                    btn_first.setImageResource(R.drawable.category01);
                    btn_second.setImageResource(R.drawable.category02);
                    btn_third.setImageResource(R.drawable.category03);
                    btn_fourth.setImageResource(R.drawable.category04);
                    btn_fifth.setImageResource(R.drawable.category05yellow);
                    btn_sixth.setImageResource(R.drawable.category06);
                    break;
                case R.id.Hlist_btn_sixth:
                    btn_first.setImageResource(R.drawable.category01);
                    btn_second.setImageResource(R.drawable.category02);
                    btn_third.setImageResource(R.drawable.category03);
                    btn_fourth.setImageResource(R.drawable.category04);
                    btn_fifth.setImageResource(R.drawable.category05);
                    btn_sixth.setImageResource(R.drawable.category06yellow);
                    break;
            }
        }
    };


    private class pagerAdapter extends FragmentStatePagerAdapter {
        public pagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ListFragment1().newInstance(guText);
                case 1:
                    return new ListFragment2().newInstance(guText, "정치역사");
                case 2:
                    return new ListFragment2().newInstance(guText, "시민생활");
                case 3:
                    return new ListFragment2().newInstance(guText, "산업노동");
                case 4:
                    return new ListFragment2().newInstance(guText, "도시관리");
                case 5:
                    return new ListFragment2().newInstance(guText, "문화예술");
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 6;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // sharedPreference 선언
                final SharedPreferences setting = getSharedPreferences("setting", 0);
                final SharedPreferences.Editor editor = setting.edit();
                String shared_token = setting.getString("token", "");
                Intent intent = new Intent();
                setResult(100,intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
