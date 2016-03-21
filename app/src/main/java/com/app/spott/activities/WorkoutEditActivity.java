package com.app.spott.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.spott.R;
import com.app.spott.SpottApplication;
import com.app.spott.adapters.GridTileAdapter;
import com.app.spott.exceptions.ModelException;
import com.app.spott.interfaces.WorkoutEditFragmentListener;
import com.app.spott.models.Frequency;
import com.app.spott.models.Illustrable;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WorkoutEditActivity extends AppCompatActivity implements WorkoutEditFragmentListener,
        GridTileAdapter.TileTouchInterceptor {

    private User currentUser;
    private Workout workout;
    private boolean isNewWorkout;
    private Location location;
    private ParseGeoPoint geoPoint;
    private GoogleMap map;
    private GridTileAdapter timeAdapter;
    private GridTileAdapter frequencyAdapter;
    private GridTileAdapter workoutTypeAdapter;
    private static final String TAG = WorkoutEditActivity.class.getSimpleName();
    private SupportPlaceAutocompleteFragment placeFragment;


    @Bind(R.id.ivWorkoutIcon)
    ImageView ivWorkoutIcon;

    @Bind(R.id.ibTimeIcon)
    ImageView ibTimeIcon;

    @Bind(R.id.ibFrequencyIcon)
    ImageView ibFreqIcon;

    @Bind(R.id.btnWorkout)
    Button btnWorkout;

    @Bind(R.id.btnTime)
    Button btnTime;

    @Bind(R.id.btnFrequency)
    Button btnFreq;

    @Bind(R.id.expWorkoutSelector)
    ExpandableWeightLayout expandableWorkouts;

    @Bind(R.id.expTimeSelector)
    ExpandableWeightLayout expandableTime;

    @Bind(R.id.expMap)
    ExpandableWeightLayout expandableMap;

    @Bind(R.id.expFrequencySelector)
    ExpandableWeightLayout expandableFreq;

    @Bind(R.id.mapView)
    MapView mapView;

    @Bind(R.id.rvWorkoutSelector)
    RecyclerView rvWorkoutSelector;

    @Bind(R.id.rvTimeSelector)
    RecyclerView rvTimeSelector;

    @Bind(R.id.rvFreqSelector)
    RecyclerView rvFreqSelector;

    @OnClick(R.id.btnSave)
    void onSaveClick() {
        saveWorkout();
    }

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

        initializeViews(savedInstanceState);
        initializeData(getIntent());
    }

    private void initializeViews(Bundle savedInstanceState) {
        workoutTypeAdapter = new GridTileAdapter(this, WorkoutType.getAll());
        StaggeredGridLayoutManager lmWorkout = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rvWorkoutSelector.setAdapter(workoutTypeAdapter);
        rvWorkoutSelector.setLayoutManager(lmWorkout);
        rvWorkoutSelector.setHasFixedSize(true);

        timeAdapter = new GridTileAdapter(this, Time.getAll());
        StaggeredGridLayoutManager lmTime = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rvTimeSelector.setAdapter(timeAdapter);
        rvTimeSelector.setLayoutManager(lmTime);
        rvTimeSelector.setHasFixedSize(true);

        frequencyAdapter = new GridTileAdapter(this, Frequency.getAll());
        StaggeredGridLayoutManager lmFreq = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        rvFreqSelector.setAdapter(frequencyAdapter);
        rvFreqSelector.setLayoutManager(lmFreq);
        rvFreqSelector.setHasFixedSize(true);

        placeFragment = (SupportPlaceAutocompleteFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentGplaces);
        initPlaceFragment(placeFragment);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.getUiSettings().setMyLocationButtonEnabled(true);
                map.getUiSettings().setAllGesturesEnabled(true);
                map.setMyLocationEnabled(true);
                MapsInitializer.initialize(WorkoutEditActivity.this);
            }
        });
    }

    private void animateMap(ParseGeoPoint point) {
        animateMap(new LatLng(point.getLatitude(), point.getLongitude()));
    }

    private void animateMap(LatLng latLng) {
        map.clear();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latLng);
        map.animateCamera(cameraUpdate);
        map.addMarker(new MarkerOptions().position(latLng));
    }

    public void toggleWorkout(View view) {
        expandableWorkouts.toggle();
    }

    public void toggleTime(View view) {
        expandableTime.toggle();
    }

    public void toggleMap(View v) {
        expandableMap.toggle();

        if (location != null && location.isSet())
            animateMap(location.getPoint());
    }

    public void toggleFreq(View v){
        expandableFreq.toggle();
    }

    private void initializeData(Intent i) {
        if (i.hasExtra(ProfileActivity.WORKOUT_ID_INTENT_KEY)) {
            String w_id = i.getStringExtra(ProfileActivity.WORKOUT_ID_INTENT_KEY);
            Workout.findOne(w_id, true, new GetCallback<Workout>() {
                @Override
                public void done(Workout object, ParseException e) {
                    if (e == null) {
                        workout = object;
                        location = workout.getLocation();
                        geoPoint = location.getPoint();
                        populateViews(workout);
                    } else {
                        Log.e(TAG, e.getMessage());
                    }
                }
            });
        } else {
            workout = new Workout();
            location = new Location();
            geoPoint = new ParseGeoPoint();
            isNewWorkout = true;
        }
    }

    private void populateViews(Workout w) {
        btnWorkout.setText(w.getWorkoutType().toString());
        ivWorkoutIcon.setImageResource(w.getWorkoutType().getIcon());

        btnTime.setText(w.getTime().toString());
        ibTimeIcon.setImageResource(w.getTime().getIcon());

        btnFreq.setText(w.getFrequency().toString());
        ibFreqIcon.setImageResource(w.getFrequency().getIcon());

        placeFragment.setText(location.getName());
    }

    private void saveWorkout() {
        if (!location.isSet()) {
            Toast.makeText(this, "Please select a location", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Location not set");
        }
        workout.setLocation(location);
        workout.setUser(currentUser);
        try {
            workout.saveModelLive();
            notifyListenerActivity(workout);
        } catch (ModelException e) {
            Toast.makeText(this, "Select all fields", Toast.LENGTH_SHORT);
            e.printStackTrace();
        } catch (ParseException e) {
            Toast.makeText(this, "Some error while saving model", Toast.LENGTH_SHORT);
            e.printStackTrace();
        }
    }

    private void initPlaceFragment(final SupportPlaceAutocompleteFragment frag) {
        frag.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                frag.setText(place.getName());
                location.setName(place.getName().toString());
                location.setAddress(place.getAddress().toString());
                location.setPlaceId(place.getId());

                geoPoint.setLatitude(place.getLatLng().latitude);
                geoPoint.setLongitude(place.getLatLng().longitude);
                location.setPoint(geoPoint);
                expandableMap.expand();
                animateMap(geoPoint);
            }

            @Override
            public void onError(Status status) {
                frag.setText(status.toString());
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void notifyListenerActivity(Workout w) {
        Intent i = new Intent();
        i.putExtra(ProfileActivity.WORKOUT_ID_INTENT_KEY, w.getObjectId());
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public void onTileSelect(Illustrable obj) {
        if (obj instanceof WorkoutType)
            setWorkoutType((WorkoutType) obj);
        else if (obj instanceof Time)
            setWorkoutTime((Time) obj);
        else if (obj instanceof Frequency)
            setWorkoutFrequency((Frequency) obj);
    }

    private void setWorkoutType(WorkoutType wt) {
        btnWorkout.setText(wt.toString());
        ivWorkoutIcon.setImageResource(wt.getIcon());
        workout.setWorkoutType(wt);
        expandableWorkouts.collapse();
    }

    private void setWorkoutTime(Time t) {
        btnTime.setText(t.toString());
        ibTimeIcon.setImageResource(t.getIcon());
        workout.setTime(t);
        expandableTime.collapse();
    }

    private void setWorkoutFrequency(Frequency f){
        btnFreq.setText(f.toString());
        ibFreqIcon.setImageResource(f.getIcon());
        workout.setFrequency(f);
        expandableFreq.collapse();
    }
}
