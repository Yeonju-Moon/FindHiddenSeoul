package com.seoulapp.findhiddenseoul.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.seoulapp.findhiddenseoul.R;

/**
 * Created by BANGYURA on 2017-09-29.
 */

public class ImageFragment extends Fragment {
    Button btn;
    TextView tv, tv2;

    public static ImageFragment newInstance(String imgUrl, int size, int pos){
        Bundle args = new Bundle();
        args.putString("imgUrl", imgUrl);
        args.putInt("size", size);
        args.putInt("pos", pos);

        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_img, container, false);

        final Context context = container.getContext();

        ImageView iv = (ImageView)view.findViewById(R.id.detail_imgs);

        Glide.with(this).load(getArguments().getString("imgUrl")).into(iv);

        Log.v("sizepos", getArguments().getInt("size") + ", " + getArguments().getInt("pos"));

        return view;
    }
}
