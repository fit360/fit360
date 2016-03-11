package com.app.spott.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.app.spott.R;
import com.app.spott.models.Gender;
import com.app.spott.models.Location;
import com.app.spott.models.User;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);
        if (ParseUser.getCurrentUser() != null) { // start with existing user
            startWithCurrentUser();
        } else { // If not logged in, login as a new anonymous user
            login();
        }

    }
    // Get the userId from the cached currentUser object
    void startWithCurrentUser() {
        ParseUser loggedInUser = ParseUser.getCurrentUser();
        User user;
        try {
            user = User.getByOwner(loggedInUser);
        } catch (ParseException e) {
            e.printStackTrace();
            user = setupNewUser();
        }

        Log.d("User", user.getFirstName());
    }

    private User setupNewUser(){
        return null;
    }

    private User setupAdil(){
        User user = new User();
        user.setFirstName("Adil");
        user.setAge(25);
        user.setLastName("Ansari");
        user.setGender(Gender.MALE);
        user.setOwner(ParseUser.getCurrentUser());
        try {
            user.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return user;
    }

    private Location addLocation(String placeId){
        Location loc = new Location();
        try {
            loc = Location.getByPlaceId(placeId);
        } catch (ParseException e) {
            e.printStackTrace();
            loc.setPoint(new ParseGeoPoint(37.78, -122.40));
            loc.setName("Equinox");
            loc.setPlaceId(placeId);
            loc.setAddress("747 Market St, San Francisco, CA 94103, United States");
            try {
                loc.save();
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }
        return loc;
    }

    // Create an anonymous user using ParseAnonymousUtils and set sUserId
    void login() {
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.e("DEBUG", "Anonymous login failed: ", e);
                } else {
                    startWithCurrentUser();
                }
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
        if (id == R.id.action_map) {
            Intent mapIntent = new Intent(this, MapActivity.class);
            startActivity(mapIntent);
            return true;
        }else if (id == R.id.action_profile) {
            Intent i = new Intent(this, ProfileActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}

