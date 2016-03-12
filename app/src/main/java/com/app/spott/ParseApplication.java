package com.app.spott;

import android.app.Application;
import android.util.Log;

import com.app.spott.models.Post;
import com.app.spott.models.User;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.parse.interceptors.ParseLogInterceptor;

public class ParseApplication extends Application {

    public static final String PARSE_APPLICATION_ID = "spott_api";
    public static final String PARSE_CLIENT_KEY = "fit_360";
    public static final String TAG = ParseApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
//        Parse.initialize(this, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(PARSE_APPLICATION_ID)
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("http://spott-api.herokuapp.com/parse/").build());
    }

    private void test_update(){
        ParseObject testObject = new ParseObject("test_collection");
        testObject.put("name", "pitbull");
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
