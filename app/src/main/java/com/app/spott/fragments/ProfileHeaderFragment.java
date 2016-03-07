package com.app.spott.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.spott.R;
import com.app.spott.models.User;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileHeaderFragment extends Fragment {
    @Bind(R.id.tvUserName)
    TextView tvUserName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_profile_header, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        ButterKnife.bind(this, view);

        // Specify which class to query
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        //TODO: change the obj id later// Specify the object id
        query.getInBackground("ZEOha5PCG0", new GetCallback<User>() {
            public void done(User user, ParseException e) {
                if (e == null) {
                    // Access data using the `get` methods for the object
                    tvUserName.setText(user.getFirstName());
                    // Do whatever you want with the data...
                } else {
                    // something went wrong
                }
            }
        });
    }

}
