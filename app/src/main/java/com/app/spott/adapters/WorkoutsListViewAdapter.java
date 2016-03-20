package com.app.spott.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.spott.R;
import com.app.spott.models.Workout;
import com.app.spott.models.WorkoutType;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sshah on 3/8/16.
 */
public class WorkoutsListViewAdapter extends ArrayAdapter<Workout> {

    Context mContext;

    public WorkoutsListViewAdapter(Context context, ArrayList<Workout> activites) {
        super(context, R.layout.item_workout, activites);
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

        Workout workout = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_workout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        try {

            viewHolder.ivProfilePicture.setImageResource(0);
            Glide.with(mContext).load(workout.getUser().getProfileImageUrl()).error(R.drawable.drawable_placeholder).placeholder(R.drawable.drawable_placeholder).dontAnimate().into(viewHolder.ivProfilePicture);
            String text = String.format("%s %s,", workout.getUser().getFirstName(), workout.getUser().getLastName());
            viewHolder.tvUserName.setText(text);
            viewHolder.tvAge.setText(String.valueOf(workout.getUser().getAge()));
            viewHolder.tvGender.setText(workout.getUser().getGender().getName());
            viewHolder.tvTime.setText(workout.getTime().toString());
            viewHolder.tvFrequency.setText(workout.getFrequency().toString());
            viewHolder.tvLocation.setText(workout.getLocation().getAddress());
            WorkoutType workoutType = workout.getWorkoutType();
            viewHolder.ivIconActivity.setImageResource(workoutType.getIcon());
        } catch(Exception e){
            e.printStackTrace();
        }

        return convertView;
    }


}
