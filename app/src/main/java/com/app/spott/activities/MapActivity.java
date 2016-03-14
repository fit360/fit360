package com.app.spott.activities;

import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.spott.R;
import com.app.spott.adapters.ActivitiesListViewAdapter;
import com.app.spott.adapters.CustomWindowAdapter;
import com.app.spott.models.Activity;
import com.app.spott.models.ActivityType;
import com.app.spott.models.Frequency;
import com.app.spott.models.Time;
import com.app.spott.models.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
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
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    //TODO: Pull new activities data ONLY on location change based on new lat/long
    //TODO: Implement marker clustering
    //TODO: Multiselect spinner
    //TODO: group activites in same geo area for each user in the user listview

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

    @Bind(R.id.dragView)
    LinearLayout dragView;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<com.app.spott.models.Activity> mActivities;
    private ArrayList<com.app.spott.models.Activity> mFilteredActivities;
    private ActivitiesListViewAdapter mActivitiesListViewAdapter;
    private LatLng mRefLatLng;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public final static String INTENT_USER_ID = "userId";

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

        List<String> activityTypes = ActivityType.getReadableStrings();
        activityTypes.add(0, "Activities");
        ArrayAdapter<String> activitiesAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner, activityTypes);
        activitiesAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerActivities.setAdapter(activitiesAdapter);

        List<String> times = Time.getReadableNames();
        times.add(0, "Times");
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner, times);
        timeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerTime.setAdapter(timeAdapter);

        List<String> frequencies = Frequency.getReadableNames();
        frequencies.add(0, "Freqs");
        ArrayAdapter<String> frequencyAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner, frequencies);
        frequencyAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerFrequency.setAdapter(frequencyAdapter);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                moveMapToLocation(place.getLatLng());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(this.toString(), "An error occurred: " + status);
            }
        });

        mActivities = new ArrayList<>();
        mFilteredActivities = new ArrayList<>();
        mActivitiesListViewAdapter = new ActivitiesListViewAdapter(this, mFilteredActivities);
        lvActivities.setAdapter(mActivitiesListViewAdapter);
        slidingPanelLayout.setAnchorPoint(0.7f);
        lvActivities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Activity activity = mFilteredActivities.get(position);
                User user = activity.getUser();
                Intent intent = new Intent(MapActivity.this, ProfileActivity.class);
                intent.putExtra(INTENT_USER_ID, user.getObjectId());
                startActivity(intent);
            }
        });

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
                //TODO
                Intent intent = new Intent(MapActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                LatLng latLng = mMap.getCameraPosition().target;
                if (latLng != null && mRefLatLng != null &&
                        Math.abs(mRefLatLng.latitude - latLng.latitude) > 0.01 &&
                        Math.abs(mRefLatLng.longitude - latLng.longitude) > 0.01) {
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
            Toast.makeText(this, "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
            updateUsingChosenActivitiesFallBackCurrentLoc(null);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void updateMapAndUserList(final LatLng latLng) {
        Activity.getActivitiesAroundLatLng(latLng.latitude, latLng.longitude, new FindCallback<Activity>() {
            @Override
            public void done(List<Activity> activities, ParseException e) {
                mActivities.clear();
                if (activities != null && !activities.isEmpty()) {
                    mActivities.addAll(activities);
                    filterActivities();
                    slidingPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                } else {
                    slidingPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                }
                moveMapToLocation(latLng);
            }
        });
    }

    public void updateUsingChosenActivitiesFallBackCurrentLoc(final LatLng latLng) {
        User.getByOwner(ParseUser.getCurrentUser(), new GetCallback<User>() {
            @Override
            public void done(User user, ParseException e) {
                if (user != null) {
                    user.getChosenAcitivities(new FindCallback<Activity>() {
                        @Override
                        public void done(List<Activity> activities, ParseException e) {
                            LatLng finalLatLng = null;
                            if (activities != null && !activities.isEmpty()) {
                                Activity activity = activities.get(0);
                                double lat = activity.getLocation().getPoint().getLatitude();
                                double lng = activity.getLocation().getPoint().getLongitude();
                                finalLatLng = new LatLng(lat, lng);
                            } else {
                                finalLatLng = latLng;
                            }
                            updateMapAndUserList(finalLatLng);
                        }
                    });
                }
            }
        });
    }

    public void updateMapAndUserList() {
        showActivitesOnMap(mFilteredActivities);
        mActivitiesListViewAdapter.notifyDataSetChanged();
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
        int distanceFromOriginInMeters = 700;
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .include(SphericalUtil.computeOffset(latLng, distanceFromOriginInMeters, 0))
                .include(SphericalUtil.computeOffset(latLng, distanceFromOriginInMeters, 90))
                .include(SphericalUtil.computeOffset(latLng, distanceFromOriginInMeters, 180))
                .include(SphericalUtil.computeOffset(latLng, distanceFromOriginInMeters, 270))
                .build();

        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0));
    }

    private void filterActivities() {
        mFilteredActivities.clear();
        ActivityType activityType = ActivityType.values()[Math.max(0, spinnerActivities.getSelectedItemPosition() - 1)];
        Time time = Time.values()[Math.max(0, spinnerTime.getSelectedItemPosition() - 1)];
        Frequency frequency = Frequency.values()[Math.max(0, spinnerFrequency.getSelectedItemPosition() - 1)];

        for (Activity activity : mActivities) {
            if (spinnerActivities.getSelectedItemPosition() != 0 && activity.getActivityType() != activityType)
                continue;
            if (spinnerTime.getSelectedItemPosition() != 0 && activity.getTime() != time)
                continue;
            if (spinnerFrequency.getSelectedItemPosition() != 0 && activity.getFrequency() != frequency)
                continue;

            mFilteredActivities.add(activity);
        }
        updateMapAndUserList();
    }

    private void showActivitesOnMap(List<com.app.spott.models.Activity> activities) {
        mMap.clear();

        if (activities != null) {
            for (Activity activity : activities) {
                ActivityType activityType = activity.getActivityType();


                int icon = R.drawable.ic_fitness_default;
                if (activityType != null) {
                    icon = activityType.getIcon();
                }

                BitmapDescriptor marker =
                        BitmapDescriptorFactory.fromResource(icon);

                User user = activity.getUser();
                String profilePicUrl = user.getProfileImageUrl();
                String userName = String.format("%s %s", user.getFirstName(), user.getLastName());
                ;
                String age = String.valueOf(user.getAge());
                String gender = user.getGender().getName();
                String activityName = activityType.getName();
                String time = activity.getTime().getName();
                String frequency = activity.getFrequency().getName();

                String snippet = String.format("%s;%s;%s;%s;%s;%s;%s", profilePicUrl, userName, age, gender, activityName, time, frequency);
                LatLng latLng = new LatLng(activity.getLocation().getPoint().getLatitude(), activity.getLocation().getPoint().getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng)
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
}
