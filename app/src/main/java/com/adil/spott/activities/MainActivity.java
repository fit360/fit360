package com.adil.spott.activities;

import android.app.Dialog;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.adil.spott.R;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    //TODO: Pull new activities data ONLY on location change based on new lat/long

    @Bind(R.id.spinnerActivities)
    Spinner spinnerActivities;

    @Bind(R.id.spinnerTime)
    Spinner spinnerTime;

    GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Toast.makeText(this, "Sorry, could not load the map. Please try again later", Toast.LENGTH_LONG).show();
        }

        ArrayAdapter<CharSequence> activitiesAdapter = ArrayAdapter.createFromResource(this, R.array.activities_array, R.layout.item_spinner);
        activitiesAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerActivities.setAdapter(activitiesAdapter);

        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(this, R.array.time_array, R.layout.item_spinner);
        timeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerTime.setAdapter(timeAdapter);

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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        connectClient();
        //TODO: move map to preferred activity location
        showActivitesOnMap();

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

    private void showActivitesOnMap() {
        BitmapDescriptor icBarbell =
                BitmapDescriptorFactory.fromResource(R.drawable.ic_benchpress);
        mMap.addMarker(new MarkerOptions().position(new LatLng(-34, 151))
                .title("Barbell")
                .icon(icBarbell));

        BitmapDescriptor icRun =
                BitmapDescriptorFactory.fromResource(R.drawable.ic_run);
        mMap.addMarker(new MarkerOptions().position(new LatLng(-34.25, 151))
                .title("Tread mill")
                .icon(icRun));
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}

