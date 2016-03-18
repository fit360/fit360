package com.app.spott.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.spott.R;
import com.app.spott.activities.ProfileActivity;
import com.app.spott.models.Workout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileWorkoutsAdapter extends RecyclerView.Adapter {

    private List<Workout> activities;
    private Context mContext;
    private int currentEditPosition = -1;
    private AdapterOnClickListener listener;

    public interface AdapterOnClickListener{
        void editWorkout(Workout workout);
    }

    public ProfileWorkoutsAdapter(Context context, List<Workout> activities) {
        this.activities = activities;
        this.mContext = context;
        this.listener = (AdapterOnClickListener)((ProfileActivity) context);
    }

    public void addActivities(List<Workout> activities) {
        int curSize = this.getItemCount();
        this.activities.addAll(activities);
        notifyItemRangeInserted(curSize, this.getItemCount() - 1);
    }

    public void addActivity(Workout workout) {
        this.activities.add(workout);
        notifyItemInserted(this.getItemCount() - 1);
    }

    private void setCurrentEditPosition(int pos){
        this.currentEditPosition = pos;
    }

    private void resetCurrentEditPosition(){
        this.currentEditPosition = -1;
    }

    public void updateActivity(Workout workout){
        if (currentEditPosition >= 0){
            this.activities.set(currentEditPosition, workout);
            notifyItemChanged(currentEditPosition);
            resetCurrentEditPosition();
        } else {
            addActivity(workout);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View activityView = li.inflate(R.layout.item_profile_workout, parent, false);
        RecyclerView.ViewHolder viewHolder = new ActivityViewHolder(activityView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Workout workout = activities.get(position);
        ActivityViewHolder vh = (ActivityViewHolder) holder;
        vh.ivActivityIcon.setImageResource(workout.getWorkoutType().getIcon());
        vh.tvActivityLocation.setText(workout.getLocation().getName());
        vh.tvActivityFrequency.setText(workout.getFrequency().getName());
        vh.tvActivityTime.setText(workout.getTime().getName());
    }

    @Override
    public int getItemCount() {
        return this.activities.size();
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
            Workout workout = activities.get(currentEditPosition);
//            FragmentManager fm = ((FragmentActivity) mContext).getSupportFragmentManager();
//            WorkoutEditFragment workoutEditFragment = WorkoutEditFragment.newInstance(workout.getObjectId());
//            workoutEditFragment.show(fm, "tag");
            listener.editWorkout(workout);
        }
    }
}
