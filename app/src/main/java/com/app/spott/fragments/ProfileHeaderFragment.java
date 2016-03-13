package com.app.spott.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.spott.R;
import com.app.spott.SpottApplication;
import com.app.spott.models.User;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileHeaderFragment extends Fragment {
    @Bind(R.id.tvUserName)
    TextView tvUserName;
    @Bind(R.id.tvGenderAge)
    TextView tvGenderAge;

    private User currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        SpottApplication app = (SpottApplication) this.getActivity().getApplicationContext();
        currentUser = app.getCurrentUser();
        return inflater.inflate(R.layout.fragment_profile_header, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        ButterKnife.bind(this, view);
        tvUserName.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
        tvGenderAge.setText(currentUser.getGender().getName() + ", "+ currentUser.getAge());
    }

}
