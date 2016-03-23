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

    private List<Workout> workouts;
    private Context mContext;
    private int currentEditPosition = -1;
    private AdapterOnClickListener listener;


    public interface AdapterOnClickListener{
        void editWorkout(Workout workout);
    }

    public ProfileWorkoutsAdapter(Context context, List<Workout> workouts) {
        this.workouts = workouts;
        this.mContext = context;
        this.listener = (AdapterOnClickListener)((ProfileActivity) context);
    }

    public void addWorkouts(List<Workout> workouts) {
        int curSize = this.getItemCount();
        this.workouts.addAll(workouts);
        notifyItemRangeInserted(curSize, this.getItemCount() - 1);
    }

    public void addWorkout(Workout workout) {
        this.workouts.add(workout);
        notifyItemInserted(this.getItemCount() - 1);
    }

    private void setCurrentEditPosition(int pos){
        this.currentEditPosition = pos;
    }

    private void resetCurrentEditPosition(){
        this.currentEditPosition = -1;
    }

    public void updateWorkout(Workout workout){
        if (currentEditPosition >= 0){
            this.workouts.set(currentEditPosition, workout);
            notifyItemChanged(currentEditPosition);
            resetCurrentEditPosition();
        } else {
            addWorkout(workout);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View activityView = li.inflate(R.layout.item_profile_workout, parent, false);
        RecyclerView.ViewHolder viewHolder = new WorkoutViewHolder(activityView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Workout workout = workouts.get(position);
        WorkoutViewHolder vh = (WorkoutViewHolder) holder;
        vh.ivActivityIcon.setImageResource(workout.getWorkoutType().getIcon());
        vh.tvActivityLocation.setText(workout.getLocation().getName());
        vh.tvActivityFrequency.setText(workout.getFrequency().toString());
        vh.tvActivityTime.setText(workout.getTime().toString());
        vh.tvActivity.setText(workout.getWorkoutType().toString());
    }

    @Override
    public int getItemCount() {
        return this.workouts.size();
    }

    public class WorkoutViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.ivActivityIcon)
        ImageView ivActivityIcon;

        @Bind(R.id.tvActivityLocation)
        TextView tvActivityLocation;

        @Bind(R.id.tvActivityFrequency)
        TextView tvActivityFrequency;

        @Bind(R.id.tvActivityTime)
        TextView tvActivityTime;

        @Bind(R.id.tvActivity)
        TextView tvActivity;

        public WorkoutViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            setCurrentEditPosition(getLayoutPosition());
            Workout workout = workouts.get(currentEditPosition);
            listener.editWorkout(workout);
        }
    }
}
