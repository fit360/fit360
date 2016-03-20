package com.app.spott.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.spott.R;
import com.app.spott.adapters.ProfileWorkoutsAdapter;
import com.app.spott.extensions.SpacesItemDecoration;
import com.app.spott.interfaces.ProfileFragmentListener;
import com.app.spott.models.User;
import com.app.spott.models.Workout;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileWorkoutsFragment extends Fragment {

    private ProfileWorkoutsAdapter activitiesAdapter;
    private User mUser;
    private boolean isLoggedInUser;
    private AddWorkoutListener addWorkoutListener;

    @Bind(R.id.rvProfileActivities)
    RecyclerView rvProfileActivities;

    @Bind(R.id.ivAddActivity)
    ImageView ivAddActivity;

    public interface AddWorkoutListener {
        void addWorkout();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_workouts, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        activitiesAdapter = new ProfileWorkoutsAdapter(getContext(), new ArrayList<Workout>());
        rvProfileActivities.setAdapter(activitiesAdapter);
        rvProfileActivities.addItemDecoration(new SpacesItemDecoration(4));
        rvProfileActivities.setLayoutManager(layoutManager);

        Workout.getForUser(mUser, new FindCallback<Workout>() {
            @Override
            public void done(List<Workout> objects, ParseException e) {
                activitiesAdapter.addWorkouts(objects);
            }
        });

        ivAddActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWorkoutListener.addWorkout();
            }
        });

        if(mUser == null){
            User.getByOwner(ParseUser.getCurrentUser(), new GetCallback<User>() {
                @Override
                public void done(User user, ParseException e) {
                    mUser = user;
                    render();
                }
            });
        } else {
            render();
        }
    }

    private void render(){
        if(mUser.getOwner().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
            ivAddActivity.setVisibility(View.VISIBLE);
        } else {
            ivAddActivity.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            ProfileFragmentListener listener = (ProfileFragmentListener) context;
            mUser = listener.getUser();
            isLoggedInUser = listener.isLoggedInUser();
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement ProfileFragmentListener");
        }

        if (context instanceof AddWorkoutListener){
            addWorkoutListener = (AddWorkoutListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement AddWorkoutListener");
        }
    }

    public void onWorkoutSave(Workout workout) {
        //add/update this workout in adapter
        activitiesAdapter.updateWorkout(workout);
    }
}
