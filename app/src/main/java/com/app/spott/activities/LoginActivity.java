package com.app.spott.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.app.spott.R;
import com.app.spott.SpottApplication;
import com.app.spott.fragments.LoginDetailFragment;
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

public class LoginActivity extends AppCompatActivity implements LoginVideoFragment.OnFragmentInteractionListener, LoginDetailFragment.OnFragmentInteractionListener {

    @Bind(R.id.fragmentPlaceholder)
    FrameLayout fragmentPlaceHolder;

    private SpottApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (ParseUser.getCurrentUser() != null) { // start with existing user
            startWithCurrentUser();
        } else { // If not logged in, login as a new anonymous user
            login();
        }

        startLoginVideoScreen();
    }

    private void startLoginVideoScreen(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentPlaceholder, new LoginVideoFragment(), "FRAGMENT_LOGIN_VIDEO");
        ft.commit();
    }
    // Get the userId from the cached currentUser object
    void startWithCurrentUser() {
        ParseUser loggedInUser = ParseUser.getCurrentUser();
        Log.d("login", loggedInUser.getSessionToken());
        User.getByOwner(loggedInUser, new GetCallback<User>() {
            @Override
            public void done(User user, ParseException e) {
                if (e == null) {
                    app.setCurrentUser(user);
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
        ft.replace(R.id.fragmentPlaceholder, new LoginDetailFragment(), "FRAGMENT_LOGIN_DETAIL");
        ft.addToBackStack("FRAGMENT_LOGIN_VIDEO");
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


    @Override
    public void startSignup() {

    }


}
