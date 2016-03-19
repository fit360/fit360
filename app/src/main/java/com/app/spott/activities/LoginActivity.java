package com.app.spott.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.app.spott.R;
import com.app.spott.SpottApplication;
import com.app.spott.fragments.LoginDetailFragment;
import com.app.spott.fragments.LoginVideoFragment;

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
        startLoginVideoScreen();
    }

    private void startLoginVideoScreen(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentPlaceholder, new LoginVideoFragment(), "FRAGMENT_LOGIN_VIDEO");
        ft.commit();
    }

    @Override
    public void startLogin() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentPlaceholder, new LoginDetailFragment(), "FRAGMENT_LOGIN_DETAIL");
        ft.addToBackStack("FRAGMENT_LOGIN_VIDEO");
        ft.commit();
    }

    @Override
    public void startSignup() {

    }


}
