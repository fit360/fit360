package com.app.spott.activities;

import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.spott.R;
import com.app.spott.adapters.CustomWindowAdapter;
import com.app.spott.adapters.LocationsSpinnerAdapter;
import com.app.spott.adapters.WorkoutsListViewAdapter;
import com.app.spott.models.Frequency;
import com.app.spott.models.Time;
import com.app.spott.models.User;
import com.app.spott.models.Workout;
import com.app.spott.models.WorkoutType;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    //TODO: Pull new activities data ONLY on location change based on new lat/long
    //TODO: Implement marker clustering
    //TODO: Multiselect spinner
    //TODO: group activites in same geo area for each user in the user listview

    @Bind(R.id.spinnerLocations)
    Spinner spinnerLocations;

    @Bind(R.id.spinnerActivities)
    Spinner spinnerActivities;

    @Bind(R.id.spinnerTime)
    Spinner spinnerTime;

    @Bind(R.id.spinnerFrequency)
    Spinner spinnerFrequency;

    @Bind(R.id.lvActivities)
    ListView lvActivities;

    @Bind(R.id.slidingPanelLayout)
    com.sothree.slidinguppanel.SlidingUpPanelLayout slidingPanelLayout;


    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<Workout> mWorkouts;
    private ArrayList<Workout> mFilteredWorkouts;
    private WorkoutsListViewAdapter mWorkoutsListViewAdapter;
    private LatLng mRefLatLng;
    private User mUser;
    private List<Workout> mPreferredWorkouts;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public final static String INTENT_USER_ID = "userId";
    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Toast.makeText(this, "Sorry, could not load the map. Please try again later", Toast.LENGTH_LONG).show();
        }

        mPreferredWorkouts = new ArrayList<>();

        List<String> activityTypes = new ArrayList<String>();
        activityTypes.addAll(WorkoutType.getReadableStrings());
        activityTypes.add(0, "Activities");
        ArrayAdapter<String> activitiesAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner, activityTypes);
        activitiesAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerActivities.setAdapter(activitiesAdapter);

        List<String> times = new ArrayList<>();
        times.addAll(Time.getReadableNames());
        times.add(0, "Times");
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner, times);
        timeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerTime.setAdapter(timeAdapter);

        List<String> frequencies = new ArrayList<>();
        frequencies.addAll(Frequency.getReadableNames());
        frequencies.add(0, "Freqs");
        ArrayAdapter<String> frequencyAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner, frequencies);
        frequencyAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerFrequency.setAdapter(frequencyAdapter);

        mWorkouts = new ArrayList<>();
        mFilteredWorkouts = new ArrayList<>();
        mWorkoutsListViewAdapter = new WorkoutsListViewAdapter(this, mFilteredWorkouts);
        lvActivities.setAdapter(mWorkoutsListViewAdapter);
        slidingPanelLayout.setAnchorPoint(0.7f);
        lvActivities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Workout workout = mFilteredWorkouts.get(position);
                User user = workout.getUser();
                Intent intent = new Intent(MapActivity.this, ProfileActivity.class);
                intent.putExtra(INTENT_USER_ID, user.getObjectId());
                startActivity(intent);
            }
        });
        slidingPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setInfoWindowAdapter(new CustomWindowAdapter(this, getLayoutInflater()));
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        connectClient();
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(MapActivity.this, ProfileActivity.class);
                intent.putExtra(INTENT_USER_ID, marker.getTitle());
                startActivity(intent);
            }
        });
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                LatLng latLng = mMap.getCameraPosition().target;
                if (latLng != null && mRefLatLng != null &&
                        Math.abs(mRefLatLng.latitude - latLng.latitude) > 0.002 &&
                        Math.abs(mRefLatLng.longitude - latLng.longitude) > 0.002) {
                    mRefLatLng = latLng;
                    updateMapAndUserList(latLng);
                }
            }
        });
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        spinnerActivities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterActivities();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerFrequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterActivities();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterActivities();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    protected void connectClient() {
        // Connect the client.
        if (isGooglePlayServicesAvailable() && mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mRefLatLng = latLng;
            updateUsingChosenActivitiesFallBackCurrentLoc(latLng);
        } else {
            Log.e(this.toString(), "Current location was null, enable GPS on emulator!");
            updateUsingChosenActivitiesFallBackCurrentLoc(null);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void updateMapAndUserList(final LatLng latLng) {
        Workout.getWorkoutsAroundLatLng(latLng.latitude, latLng.longitude, new FindCallback<Workout>() {
            @Override
            public void done(List<Workout> workouts, ParseException e) {
                mWorkouts.clear();
                if (workouts != null && !workouts.isEmpty()) {
                    mWorkouts.addAll(workouts);
                }
                filterActivities();
            }
        });
    }

    public void updateUsingChosenActivitiesFallBackCurrentLoc(final LatLng latLng) {
        User.getByOwner(ParseUser.getCurrentUser(), new GetCallback<User>() {
            @Override
            public void done(User user, ParseException e) {
                if (user != null) {
                    mUser = user;
                    user.getChosenAcitivities(new FindCallback<Workout>() {
                        @Override
                        public void done(List<Workout> workouts, ParseException e) {
                            if (workouts != null && !workouts.isEmpty()) {

                                mPreferredWorkouts.addAll(workouts);
                                final Set<com.app.spott.models.Location> preferredLocations = new HashSet<>();
                                for (Workout workout : workouts) {
                                    com.app.spott.models.Location location = workout.getLocation();
                                    preferredLocations.add(location);
                                }

                                final List<com.app.spott.models.Location> locations = new ArrayList<com.app.spott.models.Location>();
                                locations.addAll(preferredLocations);
                                spinnerLocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        com.app.spott.models.Location location = locations.get(position);
                                        LatLng latLngLocation = new LatLng(location.getPoint().getLatitude(), location.getPoint().getLongitude());
                                        updateMapAndUserList(latLngLocation);
                                        moveMapToLocation(latLngLocation);

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                                LocationsSpinnerAdapter locationsAdapter = new LocationsSpinnerAdapter(MapActivity.this, locations);
                                locationsAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                                spinnerLocations.setAdapter(locationsAdapter);
                                spinnerLocations.setVisibility(View.VISIBLE);

                            } else {
                                updateMapAndUserList(latLng);
                                moveMapToLocation(latLng);
                                spinnerLocations.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });
    }

    public void updateMapAndUserList() {
        showActivitesOnMap(mFilteredWorkouts);
        mWorkoutsListViewAdapter.notifyDataSetChanged();
        if (mFilteredWorkouts.size() > 0) {
            slidingPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            slidingPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        }
    }


    private boolean isGooglePlayServicesAvailable() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                Toast.makeText(this, "Google Play Services not available", Toast.LENGTH_LONG).show();
            }

            return false;
        }
    }

    private void moveMapToLocation(LatLng latLng) {
        int distanceFromOriginInMeters = 800;
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .include(SphericalUtil.computeOffset(latLng, distanceFromOriginInMeters, 0))
                .include(SphericalUtil.computeOffset(latLng, distanceFromOriginInMeters, 90))
                .include(SphericalUtil.computeOffset(latLng, distanceFromOriginInMeters, 180))
                .include(SphericalUtil.computeOffset(latLng, distanceFromOriginInMeters, 270))
                .build();

        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0));
    }

    private void filterActivities() {
        mFilteredWorkouts.clear();
        WorkoutType activityType = WorkoutType.values()[Math.max(0, spinnerActivities.getSelectedItemPosition() - 1)];
        Time time = Time.values()[Math.max(0, spinnerTime.getSelectedItemPosition() - 1)];
        Frequency frequency = Frequency.values()[Math.max(0, spinnerFrequency.getSelectedItemPosition() - 1)];

        for (Workout workout : mWorkouts) {
            if (workout.getUser().getObjectId().equals(mUser.getObjectId())) {
                continue;
            }
            if (spinnerActivities.getSelectedItemPosition() != 0 && workout.getWorkoutType() != activityType)
                continue;
            if (spinnerTime.getSelectedItemPosition() != 0 && workout.getTime() != time)
                continue;
            if (spinnerFrequency.getSelectedItemPosition() != 0 && workout.getFrequency() != frequency)
                continue;

            mFilteredWorkouts.add(workout);
        }
        updateMapAndUserList();
    }

    private void showActivitesOnMap(List<Workout> workouts) {
        mMap.clear();

        if (workouts != null) {
            for (Workout workout : workouts) {
                WorkoutType activityType = workout.getWorkoutType();
                int icon = R.drawable.ic_fitness_default;
                if (activityType != null) {
                    icon = activityType.getIcon();
                }

                BitmapDescriptor marker =
                        BitmapDescriptorFactory.fromResource(icon);

                User user = workout.getUser();
                String profilePicUrl = user.getProfileImageUrl();
                String userName = String.format("%s %s", user.getFirstName(), user.getLastName());
                ;
                String age = String.valueOf(user.getAge());
                String gender = user.getGender().getName();
                String activityName = activityType.toString();
                String time = workout.getTime().toString();
                String frequency = workout.getFrequency().toString();

                String snippet = String.format("%s;%s;%s;%s;%s;%s;%s", profilePicUrl, userName, age, gender, activityName, time, frequency);
                LatLng latLng = new LatLng(workout.getLocation().getPoint().getLatitude(), workout.getLocation().getPoint().getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng)
                        .title(user.getObjectId())
                        .snippet(snippet)
                        .icon(marker));

            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_place_autocomplete) {
            try {
                Intent intent =
                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                .build(this);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            } catch (GooglePlayServicesRepairableException e) {
                // TODO: Handle the error.
            } catch (GooglePlayServicesNotAvailableException e) {
                // TODO: Handle the error.
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                updateMapAndUserList(place.getLatLng());
                moveMapToLocation(place.getLatLng());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}
