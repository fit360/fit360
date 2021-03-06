package com.app.spott.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.spott.R;
import com.app.spott.SpottApplication;
import com.app.spott.interfaces.ProfileFragmentListener;
import com.app.spott.models.User;
import com.app.spott.utils.DeviceDimensionsHelper;
import com.bumptech.glide.Glide;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileHeaderFragment extends Fragment {
    @Bind(R.id.tvUserName)
    TextView tvUserName;

    @Bind(R.id.tvGenderAge)
    TextView tvGenderAge;

    @Bind(R.id.ivProfilePicture)
    ImageView ivProfilePicture;

    @Bind(R.id.ivCoverPhoto)
    ImageView ivCoverPhoto;

    private User mUser;
    private boolean isLoggedInUser;
    private SpottApplication app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        app = (SpottApplication)getActivity().getApplicationContext();
        return inflater.inflate(R.layout.fragment_profile_header, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
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

    private void animateHeaderAndProfilePic() {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator moveToBottom = ObjectAnimator.ofFloat(ivProfilePicture, "y", DeviceDimensionsHelper.convertDpToPixel(120f, this.getContext()));
        moveToBottom.setDuration(1000);
        moveToBottom.setInterpolator(new AccelerateInterpolator());
        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(ivCoverPhoto, "alpha", 1.0f);
        moveToBottom.setDuration(1000);
        set.playTogether(moveToBottom, fadeAnim);
        set.start();
    }

    private void render(){
        tvUserName.setText(mUser.getFirstName() + " " + mUser.getLastName() + ",");
        tvGenderAge.setText(mUser.getGender().getName() + " " + mUser.getAge());
        Glide.with(this).load(mUser.getCoverImageUrl()).placeholder(R.drawable.drawable_placeholder).error(R.drawable.drawable_placeholder).dontAnimate().into(ivCoverPhoto);
        Glide.with(this).load(mUser.getProfileImageUrl()).centerCrop().into(ivProfilePicture);
        animateHeaderAndProfilePic();
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
    }
}
