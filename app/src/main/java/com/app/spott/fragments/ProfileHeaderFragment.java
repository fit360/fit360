package com.app.spott.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.spott.R;
import com.app.spott.interfaces.ProfileFragmentListener;
import com.app.spott.models.User;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileHeaderFragment extends Fragment {
    @Bind(R.id.tvUserName)
    TextView tvUserName;

    @Bind(R.id.tvGenderAge)
    TextView tvGenderAge;

    @Bind(R.id.ivProfilePicture)
    ImageView ivProfilePicture;
    private User user;
    private boolean isLoggedInUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_header, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        tvUserName.setText(user.getFirstName() + " " + user.getLastName());
        tvGenderAge.setText(user.getGender().getName() + ", " + user.getAge());
        Glide.with(this.getActivity()).load(user.getProfileImageUrl()).centerCrop().into(ivProfilePicture);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            ProfileFragmentListener listener = (ProfileFragmentListener) context;
            user = listener.getUser();
            isLoggedInUser = listener.isLoggedInUser();
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement ProfileFragmentListener");
        }
    }
}
