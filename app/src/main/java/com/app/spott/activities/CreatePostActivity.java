package com.app.spott.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.app.spott.R;
import com.app.spott.fragments.PostComposeFragment;
import com.app.spott.fragments.PostImageCaptureFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CreatePostActivity extends AppCompatActivity
        implements PostImageCaptureFragment.OnFragmentInteractionListener,
        PostComposeFragment.OnFragmentInteractionListener {


    @Bind(R.id.fragmentPlaceholder)
    FrameLayout fragmentPlaceholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        startImageCaptureFragment();
    }

    @Override
    public void startComposeFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentPlaceholder, new PostComposeFragment(), "FRAGMENT_POST_COMPOSE");
        ft.commit();
    }

    @Override
    public void startImageCaptureFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentPlaceholder, new PostImageCaptureFragment(), "FRAGMENT_POST_IMAGE");
        ft.commit();
    }


}
