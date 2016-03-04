package com.adil.spott.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.adil.spott.R;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.parse.interceptors.ParseLogInterceptor;

public class MainActivity extends AppCompatActivity {

    public static final String PARSE_APPLICATION_ID = "spott_api";
    public static final String PARSE_CLIENT_KEY = "fit_360";
    public static final String TAG = "ParseApplication";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.initialize(new Parse.Configuration.Builder(this)
        .applicationId(PARSE_APPLICATION_ID)
        .addNetworkInterceptor(new ParseLogInterceptor())
        .server("http://spott-api.herokuapp.com/parse/").build());

        ParseObject testObject = new ParseObject("test_collection");
        testObject.put("name", "splat");
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null)
                    Log.v(TAG, "object saved");
                else
                    Log.e(TAG, e.getMessage());
            }
        });
    }
}
