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
import android.widget.Spinner;
import android.widget.Toast;

import com.app.spott.R;
import com.app.spott.adapters.CustomWindowAdapter;
import com.app.spott.models.ActivityType;
import com.app.spott.models.Frequency;
import com.app.spott.models.Time;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

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

    @Bind(R.id.spinnerActivities)
    Spinner spinnerActivities;

    @Bind(R.id.spinnerTime)
    Spinner spinnerTime;

    @Bind(R.id.spinnerFrequency)
    Spinner spinnerFrequency;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private List<com.app.spott.models.Activity> mActivities;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Toast.makeText(this, "Sorry, could not load the map. Please try again later", Toast.LENGTH_LONG).show();
        }

        ArrayAdapter<String> activitiesAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner, ActivityType.getReadableStrings());
        activitiesAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerActivities.setAdapter(activitiesAdapter);

        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner, Time.getReadableNames());
        timeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerTime.setAdapter(timeAdapter);

        ArrayAdapter<String> frequencyAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner, Frequency.getReadableNames());
        frequencyAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerFrequency.setAdapter(frequencyAdapter);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);


        spinnerActivities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<com.app.spott.models.Activity> activities = filterActivities();
                showActivitesOnMap(activities);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerFrequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<com.app.spott.models.Activity> activities = filterActivities();
                showActivitesOnMap(activities);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<com.app.spott.models.Activity> activities = filterActivities();
                showActivitesOnMap(activities);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setInfoWindowAdapter(new CustomWindowAdapter(getLayoutInflater()));
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

        //TODO: move map to preferred activity location
        fetchActivities();
        List<com.app.spott.models.Activity> activities = filterActivities();
        showActivitesOnMap(activities);
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
            moveMapToLocation(latLng);
        } else {
            Toast.makeText(this, "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

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
                //TODO
                // Create a new DialogFragment for the error dialog
//                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
//                errorFragment.setDialog(errorDialog);
//                errorFragment.show(getSupportFragmentManager(), "Location Updates");
            }

            return false;
        }
    }

    private void moveMapToLocation(LatLng latLng) {
        int distanceFromOriginInMeters = 2000;
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .include(SphericalUtil.computeOffset(latLng, distanceFromOriginInMeters, 0))
                .include(SphericalUtil.computeOffset(latLng, distanceFromOriginInMeters, 90))
                .include(SphericalUtil.computeOffset(latLng, distanceFromOriginInMeters, 180))
                .include(SphericalUtil.computeOffset(latLng, distanceFromOriginInMeters, 270))
                .build();

        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0));
    }

    private void fetchActivities() {
        //TODO:
    }

    private List<com.app.spott.models.Activity> filterActivities() {
        //TODO:
        ActivityType activityType = ActivityType.values()[spinnerActivities.getSelectedItemPosition()];
//        Time time = Time.values()[spinnerTime.getSelectedItemPosition()];
//        Frequency frequency = Frequency.values()[spinnerFrequency.getSelectedItemPosition()];

        ArrayList<com.app.spott.models.Activity> result = new ArrayList<>();
//        for (Activity activity : mActivities) {
//            if (activity.getActivityType() == activityType &&
//                    activity.getTime() == time &&
//                    activity.getFrequency() == frequency) {
//                result.add(activity);
//            }
//        }
        return result;
    }

    private void showActivitesOnMap(List<com.app.spott.models.Activity> activities) {
        mMap.clear();

        BitmapDescriptor icon =
                BitmapDescriptorFactory.fromResource(R.drawable.ic_fitness_default);
        mMap.addMarker(new MarkerOptions().position(new LatLng(37.774, -122.439))
                .title("Foo Bar")
                .snippet("General Strength")
                .icon(icon));

        icon =
                BitmapDescriptorFactory.fromResource(R.drawable.ic_yoga);
        mMap.addMarker(new MarkerOptions().position(new LatLng(37.771, -122.438))
                .title("Foo Bar")
                .snippet("Yoga")
                .icon(icon));

        icon =
                BitmapDescriptorFactory.fromResource(R.drawable.ic_boxing);
        mMap.addMarker(new MarkerOptions().position(new LatLng(37.777, -122.446))
                .title("Foo Bar")
                .snippet("Boxing")
                .icon(icon));

        icon =
                BitmapDescriptorFactory.fromResource(R.drawable.ic_yoga);
        mMap.addMarker(new MarkerOptions().position(new LatLng(37.764, -122.430))
                .title("Foo Bar")
                .snippet("Yoga")
                .icon(icon));

        icon =
                BitmapDescriptorFactory.fromResource(R.drawable.ic_yoga);
        mMap.addMarker(new MarkerOptions().position(new LatLng(37.781, -122.454))
                .title("Foo Bar")
                .snippet("General Strength")
                .icon(icon));

        icon =
                BitmapDescriptorFactory.fromResource(R.drawable.ic_yoga);
        mMap.addMarker(new MarkerOptions().position(new LatLng(37.760, -122.454))
                .title("Foo Bar")
                .snippet("Yoga")
                .icon(icon));

        icon =
                BitmapDescriptorFactory.fromResource(R.drawable.ic_fitness_default);
        mMap.addMarker(new MarkerOptions().position(new LatLng(37.781, -122.428))
                .title("Foo Bar")
                .snippet("General Strength")
                .icon(icon));

        icon =
                BitmapDescriptorFactory.fromResource(R.drawable.ic_basketball);
        mMap.addMarker(new MarkerOptions().position(new LatLng(37.790, -122.424))
                .title("Foo Bar")
                .snippet("Basketball")
                .icon(icon));

        icon =
                BitmapDescriptorFactory.fromResource(R.drawable.ic_cycling);
        mMap.addMarker(new MarkerOptions().position(new LatLng(37.788, -122.446))
                .title("Foo Bar")
                .snippet("Cycling")
                .icon(icon));

//TODO
//        if (activities != null) {
//            for (Activity activity : activities) {
//                ActivityType activityType = activity.getActivityType();
//

//                int resourceId = R.drawable.ic_benchpress;
//                if (activityType.getResourceId() != -1) {
//                    resourceId = activityType.getResourceId();
//                }

//                BitmapDescriptor icon =
//                        BitmapDescriptorFactory.fromResource(resourceId);
//                mMap.addMarker(new MarkerOptions().position(new LatLng(-34, 151))
//                        .title("Barbell")
//                        .icon(icon));
//            }
//        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
