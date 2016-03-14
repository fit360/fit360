package com.app.spott.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.spott.R;
import com.app.spott.fragments.ActivityEditFragment;
import com.app.spott.models.Activity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileActivitiesAdapter extends RecyclerView.Adapter implements ActivityEditFragment.OnSaveListener {

    private List<Activity> activities;
    private Context mContext;
    private int currentEditPosition = -1;

    public ProfileActivitiesAdapter(Context context, List<Activity> activities) {
        this.activities = activities;
        this.mContext = context;
    }

    public void addActivities(List<Activity> activities) {
        int curSize = this.getItemCount();
        this.activities.addAll(activities);
        notifyItemRangeInserted(curSize, this.getItemCount() - 1);
    }

    public void addActivity(Activity activity) {
        this.activities.add(activity);
        notifyItemInserted(this.getItemCount() - 1);
    }

    private void setCurrentEditPosition(int pos){
        this.currentEditPosition = pos;
    }

    private void resetCurrentEditPosition(){
        this.currentEditPosition = -1;
    }

    public void updateActivity(Activity activity){
        if (currentEditPosition >= 0){
            this.activities.add(currentEditPosition, activity);
            notifyItemChanged(currentEditPosition);
            resetCurrentEditPosition();
        } else {
            addActivity(activity);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View activityView = li.inflate(R.layout.item_profile_activity, parent, false);
        RecyclerView.ViewHolder viewHolder = new ActivityViewHolder(activityView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Activity activity = activities.get(position);
        ActivityViewHolder vh = (ActivityViewHolder) holder;
        vh.ivActivityIcon.setImageResource(activity.getActivityType().getIcon());
        vh.tvActivityLocation.setText(activity.getLocation().getName());
        vh.tvActivityFrequency.setText(activity.getFrequency().getName());
        vh.tvActivityTime.setText(activity.getTime().getName());
    }

    @Override
    public int getItemCount() {
        return this.activities.size();
    }

    @Override
    public void onActivitySave(Activity activity) {
        //get index of item to be updated
    }

    public class ActivityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.ivActivityIcon)
        ImageView ivActivityIcon;

        @Bind(R.id.tvActivityLocation)
        TextView tvActivityLocation;

        @Bind(R.id.tvActivityFrequency)
        TextView tvActivityFrequency;

        @Bind(R.id.tvActivityTime)
        TextView tvActivityTime;

        public ActivityViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            setCurrentEditPosition(getLayoutPosition());
            Activity activity = activities.get(currentEditPosition);
            FragmentManager fm = ((FragmentActivity) mContext).getSupportFragmentManager();
            ActivityEditFragment activityEditFragment = ActivityEditFragment.newInstance(activity.getObjectId());
            activityEditFragment.show(fm, "tag");
        }
    }
}
