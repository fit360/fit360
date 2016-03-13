package com.app.spott.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.spott.R;
import com.app.spott.adapters.ProfileActivitiesAdapter;
import com.app.spott.extensions.DividerItemDecoration;
import com.app.spott.models.Activity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileActivitiesFragment extends Fragment {

    private ProfileActivitiesAdapter activitiesAdapter;

    @Bind(R.id.rvProfileActivities)
    RecyclerView rvProfileActivities;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_activities, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        activitiesAdapter = new ProfileActivitiesAdapter(new ArrayList<Activity>());
        rvProfileActivities.setAdapter(activitiesAdapter);
        rvProfileActivities.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        rvProfileActivities.setLayoutManager(layoutManager);
    }
}
