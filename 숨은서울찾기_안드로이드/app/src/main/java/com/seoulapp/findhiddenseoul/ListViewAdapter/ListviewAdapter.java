package com.seoulapp.findhiddenseoul.ListViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.seoulapp.findhiddenseoul.ListViewItem.ListViewItem2;
import com.seoulapp.findhiddenseoul.R;

import java.util.ArrayList;

/**
 * Created by HOME on 2017-10-13.
 */
public class ListviewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<ListViewItem2> data;
    private int layout;

    public ListviewAdapter(Context context, int layout, ArrayList<ListViewItem2> data) {
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position).getNick_name();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if(convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        ListViewItem2 listviewitem = data.get(position);

        TextView rank_img = (TextView)convertView.findViewById(R.id.rank_img);
        ImageView rank_profile = (ImageView)convertView.findViewById(R.id.rank_profile);
        TextView rank_nick = (TextView)convertView.findViewById(R.id.rank_nick);
        TextView rank_level = (TextView)convertView.findViewById(R.id.rank_level);
        TextView rank_reviewnum = (TextView)convertView.findViewById(R.id.rank_reviewnum);

        // 아이템 내 각 위젯에 데이터 반영
        rank_img.setText(listviewitem.getRANK() + " .");
        Glide.with(context).load(listviewitem.getLevel_imgUrl()).override(70, 70).into(rank_profile);
        rank_nick.setText(listviewitem.getNick_name());
        rank_level.setText(listviewitem.getLevel() + "");
        rank_reviewnum.setText(listviewitem.getCounts() + "");

        return convertView;
    }
}