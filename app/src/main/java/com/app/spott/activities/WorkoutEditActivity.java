package com.app.spott.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.app.spott.SpottApplication;
import com.app.spott.models.User;
import com.app.spott.models.Workout;

import org.parceler.Parcels;

import butterknife.ButterKnife;

public class WorkoutEditActivity extends AppCompatActivity {

    private User user;
    private Workout workout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        SpottApplication app = (SpottApplication) this.getApplicationContext();
        user = app.getCurrentUser();
        setWorkout(getIntent());
    }

    private void setWorkout(Intent i) {
        if (i.hasExtra(ProfileActivity.WORKOUT)) {
            workout = Parcels.unwrap(i.getParcelableExtra(ProfileActivity.WORKOUT));
        } else {
            workout = new Workout();
        }
    }

    public Workout getWorkout(){
        return workout;
    }

    public User getUser(){
        return user;
    }
}
