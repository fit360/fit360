package com.app.spott.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.spott.R;
import com.app.spott.SpottApplication;
import com.app.spott.exceptions.ModelException;
import com.app.spott.interfaces.WorkoutEditFragmentListener;
import com.app.spott.models.Frequency;
import com.app.spott.models.Location;
import com.app.spott.models.Time;
import com.app.spott.models.User;
import com.app.spott.models.Workout;
import com.app.spott.models.WorkoutType;
import com.github.aakira.expandablelayout.ExpandableWeightLayout;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WorkoutEditActivity extends AppCompatActivity implements WorkoutEditFragmentListener {

    private User currentUser;
    private Workout workout;
    private boolean isNewWorkout;
    private Location location;
    private ParseGeoPoint geoPoint;
    private ArrayAdapter<Time> timeAdapter;
    private ArrayAdapter<Frequency> frequencyAdapter;
    private ArrayAdapter<WorkoutType> workoutTypeAdapter;
    private static final String TAG = WorkoutEditActivity.class.getSimpleName();


    @Bind(R.id.btnWorkout)
    Button btnWorkout;

    @Bind(R.id.etLocation)
    EditText etLocation;

    @Bind(R.id.spnTime)
    Spinner spinnerTime;

    @Bind(R.id.spnFrequency)
    Spinner spinnerFrequency;

    @Bind(R.id.expandableLayout)
    ExpandableWeightLayout expandableWorkouts;

    @OnClick(R.id.btnSave)
    void onSaveClick() {
        saveWorkout();
    }

    @OnClick(R.id.btnCancel)
    void onCancelClick() {
        finish();
    }

    @Bind(R.id.sampleButton)
    Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SpottApplication app = (SpottApplication) this.getApplicationContext();
        currentUser = app.getCurrentUser();

        setContentView(R.layout.activity_workout_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);

        initializeViews();
        setWorkout(getIntent());
        initPlaceFragment();
    }

    private void initializeViews() {
        geoPoint = new ParseGeoPoint();

        timeAdapter = new ArrayAdapter<Time>(this, android.R.layout.simple_list_item_1, Time.values());
        spinnerTime.setAdapter(timeAdapter);
        frequencyAdapter = new ArrayAdapter<Frequency>(this, android.R.layout.simple_list_item_1, Frequency.values());
        spinnerFrequency.setAdapter(frequencyAdapter);
        workoutTypeAdapter = new ArrayAdapter<WorkoutType>(this, android.R.layout.select_dialog_item, WorkoutType.values());

    public void selectWorkout(View view) {
        Toast.makeText(WorkoutEditActivity.this, "expanding...", Toast.LENGTH_SHORT).show();
        expandableWorkouts.toggle();
    }


    private void setWorkout(Intent i) {
        if (i.hasExtra(ProfileActivity.WORKOUT_ID_INTENT_KEY)) {
            String w_id = i.getStringExtra(ProfileActivity.WORKOUT_ID_INTENT_KEY);
            Workout.findOne(w_id, true, new GetCallback<Workout>() {
                @Override
                public void done(Workout object, ParseException e) {
                    if (e == null) {
                        workout = object;
                        location = workout.getLocation();
                        populateViews();
                    } else {
                        Log.e(TAG, e.getMessage());
                    }
                }
            });
        } else {
            workout = new Workout();
            location = new Location();
            isNewWorkout = true;
        }
    }

    private void populateViews() {
        btnWorkout.setText(workout.getWorkoutType().toString());
        spinnerFrequency.setSelection(frequencyAdapter.getPosition(workout.getFrequency()));
        spinnerTime.setSelection(timeAdapter.getPosition(workout.getTime()));
        etLocation.setText(workout.getLocation().getNameAddress());
    }

    private void saveWorkout() {
        if (!location.isSet()) {
            Toast.makeText(this, "Please select a location", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Location not set");
        }
        workout.setLocation(location);
        workout.setFrequency((Frequency) spinnerFrequency.getSelectedItem());
        workout.setTime((Time) spinnerTime.getSelectedItem());
        workout.setWorkoutType(workoutTypeAdapter.getItem(0));
        workout.setUser(currentUser);
        try {
            workout.saveModel();
            notifyListenerActivity(workout);
        } catch (ModelException e) {
            e.printStackTrace();
        }
    }

    private void initPlaceFragment() {
        SupportPlaceAutocompleteFragment f = (SupportPlaceAutocompleteFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentGplaces);
        f.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                etLocation.setText(place.getAddress());
                location.setName(place.getName().toString());
                location.setAddress(place.getAddress().toString());
                location.setPlaceId(place.getId());

                geoPoint.setLatitude(place.getLatLng().latitude);
                geoPoint.setLongitude(place.getLatLng().longitude);
                location.setPoint(geoPoint);
            }

            @Override
            public void onError(Status status) {
                etLocation.setText(status.toString());
            }
        });
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
    public boolean isNewWorkout() {
        return isNewWorkout;
    }

    @Override
    public void setLatLng() {
//        set LatLng in Map fragment
    }

    @Override
    public void notifyListenerActivity(Workout w) {
        Intent i = new Intent();
        i.putExtra(ProfileActivity.WORKOUT_ID_INTENT_KEY, w.getObjectId());
        setResult(RESULT_OK, i);
        finish();
    }
}
