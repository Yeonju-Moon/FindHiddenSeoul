package com.seoulapp.findhiddenseoul.ListViewAdapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.seoulapp.findhiddenseoul.ListViewItem.ListViewItem;
import com.seoulapp.findhiddenseoul.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BANGYURA on 2017-10-27.
 */

/***********************************************리뷰리스트***************************************/
public class ListViewAdapter3 extends BaseAdapter {
    Context context;
    private List<ListViewItem> list = null;
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>() ;


    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item3, parent, false);
        }

        // 화면에 표시될 View로부터 위젯에 대한 참조 획득
        ImageView reviewImageView = (ImageView) convertView.findViewById(R.id.list_item3_iv) ;
        TextView nickTextView = (TextView) convertView.findViewById(R.id.list_item3_tv_nickname) ;
        TextView contentTextView = (TextView) convertView.findViewById(R.id.list_item3_tv_content);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = (ListViewItem) getItem(position);

        // 아이템 내 각 위젯에 데이터 반영
        Glide.with(context).load(listViewItem.getReview_img()).bitmapTransform(new CenterCrop(context)).into(reviewImageView);

        nickTextView.setText(listViewItem.getNickname());
        nickTextView.setTypeface(Typeface.createFromAsset(convertView.getContext().getAssets(), "fonts/NotoSansKR-Bold.mp3"));
        nickTextView.setIncludeFontPadding(false);
        nickTextView.setTextSize(20);

        contentTextView.setText(listViewItem.getReview_content());
        contentTextView.setTypeface(Typeface.createFromAsset(convertView.getContext().getAssets(), "fonts/NotoSansKR-Medium.mp3"));
        contentTextView.setIncludeFontPadding(false);
        contentTextView.setTextSize(17);

        return convertView;
    }

    public void addItem(String img, String nickname, String content, String index) {
        ListViewItem item = new ListViewItem();

        item.setReview_img(img);
        item.setNickname(nickname);
        item.setReview_content(content);
        item.setReview_index(index);

        listViewItemList.add(item);

        //notifyDataSetChanged();
    }
}