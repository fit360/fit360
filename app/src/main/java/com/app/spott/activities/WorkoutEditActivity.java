package com.app.spott.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.app.spott.R;
import com.app.spott.SpottApplication;
import com.app.spott.interfaces.WorkoutEditFragmentListener;
import com.app.spott.models.User;
import com.app.spott.models.Workout;

import org.parceler.Parcels;

import butterknife.ButterKnife;

public class WorkoutEditActivity extends AppCompatActivity implements WorkoutEditFragmentListener {

    private User currentUser;
    private Workout workout;
    private boolean isNewWorkout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SpottApplication app = (SpottApplication) this.getApplicationContext();
        currentUser = app.getCurrentUser();
        setWorkout(getIntent());

        setContentView(R.layout.activity_workout_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);
    }

    private void setWorkout(Intent i) {
        if (i.hasExtra(ProfileActivity.WORKOUT_ID_INTENT_KEY)) {
            workout = Parcels.unwrap(i.getParcelableExtra(ProfileActivity.WORKOUT_ID_INTENT_KEY));
            isNewWorkout = false;
        } else {
            workout = new Workout();
            isNewWorkout = true;
        }
    }

    @Override
    public Workout getWorkout() {
        return workout;
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public boolean isLoggedInUser() {
        return (workout.getUser() == currentUser);
    }

    @Override
    public boolean isNewWorkout(){
        return isNewWorkout;
    }

    @Override
    public void setLatLng() {
//        set LatLng in Map fragment
    }

    @Override
    public void saveWorkout(Workout w){
        Intent i = new Intent();
        i.putExtra(ProfileActivity.WORKOUT_ID_INTENT_KEY, Parcels.wrap(w));
        setResult(RESULT_OK, i);
        finish();
    }
}
