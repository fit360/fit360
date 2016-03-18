package com.app.spott;

import android.app.Application;

import com.app.spott.models.Workout;
import com.app.spott.models.Location;
import com.app.spott.models.Message;
import com.app.spott.models.Post;
import com.app.spott.models.User;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.interceptors.ParseLogInterceptor;

public class SpottApplication extends Application {

    public static final String PARSE_APPLICATION_ID = "spott_api";
    public static final String PARSE_CLIENT_KEY = "fit_360";
    public static final String TAG = SpottApplication.class.getSimpleName();
    private User currentUser;

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
//        Parse.initialize(this, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Workout.class);
        ParseObject.registerSubclass(Location.class);
        ParseObject.registerSubclass(Message.class);


        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(PARSE_APPLICATION_ID)
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("http://spott-api.herokuapp.com/parse/").build());
    }

    public void setCurrentUser(User user){
        currentUser = user;
    }

    public User getCurrentUser(){return currentUser;}

}
