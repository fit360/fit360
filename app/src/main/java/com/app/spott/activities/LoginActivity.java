package com.app.spott.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.app.spott.R;
import com.app.spott.SpottApplication;
import com.app.spott.fragments.LoginScreenFragment;
import com.app.spott.fragments.LoginVideoFragment;
import com.app.spott.models.User;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements LoginVideoFragment.OnFragmentInteractionListener, LoginScreenFragment.LoginFragmentListener {

    @Bind(R.id.fragmentPlaceholder)
    FrameLayout fragmentPlaceHolder;

    private SpottApplication app;
    private static final String VIDEO_FRAGMENT_TAG = "FRAGMENT_LOGIN_VIDEO";
    private static final String LOGIN_FRAGMENT_TAG = "FRAGMENT_LOGIN_DETAIL";
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        app = (SpottApplication) getApplicationContext();
        ParseUser loggedInUser = ParseUser.getCurrentUser();
        if (loggedInUser == null)
            startLogin();
        else
            setPrimaryUser(loggedInUser);
    }

    private void startLoginVideoScreen() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentPlaceholder, new LoginVideoFragment(), VIDEO_FRAGMENT_TAG);
        ft.commit();
    }

    void setPrimaryUser(ParseUser loggedInUser) {
        Log.d(TAG, loggedInUser.getSessionToken());
        User.getByOwner(loggedInUser, new GetCallback<User>() {
            @Override
            public void done(User user, ParseException e) {
                if (e == null) {
                    app.setCurrentUser(user);
                    redirectHome();
                } else {
                    startLogin();
                }
            }
        });
    }

    @Override
    public void startLogin() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentPlaceholder, new LoginScreenFragment(), LOGIN_FRAGMENT_TAG);
        ft.addToBackStack(VIDEO_FRAGMENT_TAG);
        ft.commit();
    }

    private void redirectHome() {
        Intent intent = new Intent(LoginActivity.this, CommunityFeedActivity.class);
        startActivity(intent);
    }

    @Override
    public void startSignup() {

    }

    @Override
    public void onLoginSuccess(final ParseUser loggedInUser) {
        // update ParseUser.currentUser
        ParseUser.becomeInBackground(loggedInUser.getSessionToken(), new LogInCallback() {
            @Override
            public void done(ParseUser owner, ParseException e) {
                if (e == null) {
                    setPrimaryUser(owner);
                } else {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    @Override
    public void onLoginFailure(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
