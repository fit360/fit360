package com.app.spott.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.app.spott.R;
import com.app.spott.SpottApplication;
import com.app.spott.fragments.LoginScreenFragment;
import com.app.spott.fragments.LoginVideoFragment;
import com.app.spott.models.Gender;
import com.app.spott.models.User;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        app = (SpottApplication) getApplicationContext();
        if (ParseUser.getCurrentUser() != null) {
            startWithCurrentUser();
        } else {
            startLogin();
        }
    }

    private void startLoginVideoScreen() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentPlaceholder, new LoginVideoFragment(), VIDEO_FRAGMENT_TAG);
        ft.commit();
    }

    void startWithCurrentUser() {
        ParseUser loggedInUser = ParseUser.getCurrentUser();
        Log.d("login", loggedInUser.getSessionToken());
        User.getByOwner(loggedInUser, new GetCallback<User>() {
            @Override
            public void done(User user, ParseException e) {
                if (e == null) {
                    onLoginSuccess(user);
                } else {
                    app.setCurrentUser(setupAdil());
                }
            }
        });
    }

    private User setupAdil() {
        User user = new User();
        user.setFirstName("Adil");
        user.setAge(25);
        user.setLastName("Ansari");
        user.setGender(Gender.MALE);
        user.setOwner(ParseUser.getCurrentUser());
        try {
            user.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void startLogin() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentPlaceholder, new LoginScreenFragment(), LOGIN_FRAGMENT_TAG);
        ft.addToBackStack(VIDEO_FRAGMENT_TAG);
        ft.commit();
    }

    // Create an anonymous user using ParseAnonymousUtils and set sUserId
    void login() {
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.e("DEBUG", "Anonymous login failed: ", e);
                } else {
                    startWithCurrentUser();
                }
            }
        });

    }

    private void redirectHome(){
        Intent intent = new Intent(LoginActivity.this, CommunityFeedActivity.class);
        startActivity(intent);
    }


    @Override
    public void startSignup() {

    }


    @Override
    public void onLoginSuccess(User user) {
        app.setCurrentUser(user);
        redirectHome();
    }

    @Override
    public void onLoginFailure() {

    }
}
