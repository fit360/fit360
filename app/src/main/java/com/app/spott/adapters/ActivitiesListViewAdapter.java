package com.app.spott.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.spott.R;
import com.app.spott.models.Activity;
import com.app.spott.models.ActivityType;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sshah on 3/8/16.
 */
public class ActivitiesListViewAdapter extends ArrayAdapter<Activity> {

    Context mContext;

    public ActivitiesListViewAdapter(Context context, ArrayList<Activity> activites) {
        super(context, R.layout.item_activity, activites);
        mContext = context;
    }

    public static class ViewHolder {

        @Bind(R.id.ivProfilePicture)
        ImageView ivProfilePicture;

        @Bind(R.id.tvUserName)
        TextView tvUserName;

        @Bind(R.id.tvAge)
        TextView tvAge;

        @Bind(R.id.tvGender)
        TextView tvGender;

        @Bind(R.id.tvActivity)
        TextView tvActivity;

        @Bind(R.id.tvTime)
        TextView tvTime;

        @Bind(R.id.tvFrequency)
        TextView tvFrequency;

        @Bind(R.id.tvLocation)
        TextView tvLocation;

        @Bind(R.id.ivIconActivity)
        ImageView ivIconActivity;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Activity activity = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_activity, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        try {

            viewHolder.ivProfilePicture.setImageResource(0);
            Glide.with(mContext).load(activity.getUser().getProfileImageUrl()).error(R.drawable.drawable_placeholder).placeholder(R.drawable.drawable_placeholder).dontAnimate().into(viewHolder.ivProfilePicture);
            String text = String.format("%s %s", activity.getUser().getFirstName(), activity.getUser().getLastName());
            viewHolder.tvUserName.setText(text);
            viewHolder.tvAge.setText(String.valueOf(activity.getUser().getAge()));
            viewHolder.tvGender.setText(activity.getUser().getGender().getName());
            viewHolder.tvActivity.setText(activity.getActivityType().getName());
            viewHolder.tvTime.setText(activity.getTime().getName());
            viewHolder.tvFrequency.setText(activity.getFrequency().getName());
            viewHolder.tvLocation.setText(activity.getLocation().getAddress());
            ActivityType activityType = activity.getActivityType();
            viewHolder.ivIconActivity.setImageResource(activityType.getIcon());
        } catch(Exception e){
            e.printStackTrace();
        }

        return convertView;
    }


}
