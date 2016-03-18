package com.app.spott.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.app.spott.R;
import com.app.spott.SpottApplication;
import com.app.spott.adapters.ProfileWorkoutsAdapter;
import com.app.spott.interfaces.ProfileFragmentListener;
import com.app.spott.models.User;
import com.app.spott.models.Workout;
import com.parse.GetCallback;
import com.parse.ParseException;

public class ProfileActivity extends AppCompatActivity implements ProfileWorkoutsAdapter.AdapterOnClickListener, ProfileFragmentListener {

    private User user;
    private boolean isLoggedInUser;
    public static final String WORKOUT_ID_INTENT_KEY = "workout_id";
    public static final int WORKOUT_EDIT_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SpottApplication app = (SpottApplication) this.getApplicationContext();

        Intent intent = getIntent();
        if (intent.hasExtra(MapActivity.INTENT_USER_ID)) {
//            setUser(intent.getStringExtra(MapActivity.INTENT_USER_ID));
            setUserOnUIThread(intent.getStringExtra(MapActivity.INTENT_USER_ID));
            isLoggedInUser = false;
        } else {
            user = app.getCurrentUser();
            isLoggedInUser = true;
        }

        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public boolean isLoggedInUser() {
        return isLoggedInUser;
    }

    public void setUser(String user_id) {
        User.findOne(User.class, user_id, new GetCallback<User>() {
            @Override
            public void done(User object, ParseException e) {
                if (e == null)
                    user = object;
                else
                    e.printStackTrace();
            }
        });
    }

    public void setUserOnUIThread(String user_id){
        try {
            user = User.findUserOnUIThread(user_id);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void editWorkout(Workout workout) {
//        start activity for result
        Intent intent = new Intent(this, WorkoutEditActivity.class);
        intent.putExtra(WORKOUT_ID_INTENT_KEY, workout.getObjectId());
        startActivityForResult(intent, WORKOUT_EDIT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == WORKOUT_EDIT_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK){
                String workoutId = data.getStringExtra(WORKOUT_ID_INTENT_KEY);
//                call update on profile workouts fragment list view
            }
        }
    }
}
