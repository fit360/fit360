package com.app.spott.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.spott.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sshah on 3/6/16.
 */
public class CustomWindowAdapter implements GoogleMap.InfoWindowAdapter {
    @Bind(R.id.ivProfilePicture)
    ImageView ivProfilePicture;

    @Bind(R.id.tvUserName)
    TextView tvUserName;

    @Bind(R.id.tvAge)
    TextView tvAge;

    @Bind(R.id.tvGender)
    TextView tvGender;

    @Bind(R.id.tvTime)
    TextView tvTime;

    @Bind(R.id.tvFrequency)
    TextView tvFrequency;

    LayoutInflater mInflater;
    Context mContext;

    public CustomWindowAdapter(Context context, LayoutInflater i) {
        mContext = context;
        mInflater = i;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = mInflater.inflate(R.layout.custom_info_window, null);
        ButterKnife.bind(this, view);

        String contents = marker.getSnippet();
        String[] contentArray = contents.split(";");
        if(contentArray.length > 0){
            Glide.with(mContext).load(contentArray[0]).placeholder(R.drawable.drawable_placeholder).error(R.drawable.drawable_placeholder).dontAnimate().into(ivProfilePicture);
            tvUserName.setText(contentArray[1] + ",");
            tvAge.setText(contentArray[2]);
            tvGender.setText(contentArray[3]);
            tvTime.setText(contentArray[5]);
            tvFrequency.setText(contentArray[6]);
        }

        return view;
    }

    // This changes the frame of the info window; returning null uses the default frame.
    // This is just the border and arrow surrounding the contents specified above
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }
}