package com.app.spott.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.spott.R;
import com.app.spott.adapters.ProfileActivitiesAdapter;
import com.app.spott.extensions.DividerItemDecoration;
import com.app.spott.interfaces.ProfileFragment;
import com.app.spott.models.Activity;
import com.app.spott.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileActivitiesFragment extends Fragment implements ActivityEditFragment.OnSaveListener {

    private ProfileActivitiesAdapter activitiesAdapter;
    private User user;
    private boolean isLoggedInUser;

    @Bind(R.id.rvProfileActivities)
    RecyclerView rvProfileActivities;

    @Bind(R.id.btnAddActivity)
    Button btnAddActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_activities, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        activitiesAdapter = new ProfileActivitiesAdapter(getContext(), new ArrayList<Activity>());
        rvProfileActivities.setAdapter(activitiesAdapter);
        rvProfileActivities.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        rvProfileActivities.setLayoutManager(layoutManager);

        Activity.getForUser(user, new FindCallback<Activity>() {
            @Override
            public void done(List<Activity> objects, ParseException e) {
                activitiesAdapter.addActivities(objects);
            }
        });

        btnAddActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addActivity();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            ProfileFragment listener = (ProfileFragment) context;
            user = listener.getUser();
            isLoggedInUser = listener.isLoggedInUser();
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement ProfileFragment");
        }
    }

    @Override
    public void onActivitySave(Activity activity) {
        //add/update this activity in adapter
        activitiesAdapter.updateActivity(activity);
    }

    public void addActivity() {
        FragmentManager fm = ((FragmentActivity) getContext()).getSupportFragmentManager();
        ActivityEditFragment activityEditFragment = ActivityEditFragment.newInstance();
        activityEditFragment.show(fm, "tag");
    }
}
