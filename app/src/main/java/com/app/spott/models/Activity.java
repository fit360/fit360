package com.app.spott.models;

import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class Activity extends Model {

    private static final String TAG = Activity.class.getSimpleName();
    private static final String USER = "user";
    private static final String ACTIVITY_TYPE = "activity_type";
    private static final String TIME = "time";
    private static final String FREQUENCY = "frequency";
    private static final String LOCATION = "location";

    private static ParseQuery<Activity> query;

    @Override
    public String getLogTag() {
        return TAG;
    }

    public User getUser() {
        return (User) get(USER);
    }

    public void setUser(User user) {
        put(USER, user);
    }

    public ActivityType getActivityType() {
        return (ActivityType) get(ACTIVITY_TYPE);
    }

    public void setActivityType(ActivityType activityType) {
        put(ACTIVITY_TYPE, activityType);
    }

    public Time getTime() {
        return (Time) get(TIME);
    }

    public void setTime(Time time) {
        put(TIME, time);
    }

    public Frequency getFrequency() {
        return (Frequency) get(FREQUENCY);
    }

    public void setFrequency(Frequency f) {
        put(FREQUENCY, f);
    }

    public Location getLocation(){
        return (Location) get(LOCATION);
    }

    public void setLocation(Location loc){
        put(LOCATION, loc);
    }

    public List<Activity> getMatchedActivities() throws ParseException {
//        maybe use innerquery for optimization
        List<Location> nearByLocations = getLocation().getNearByLocations();
        query = ParseQuery.getQuery(Activity.class);
        query.whereContainedIn(LOCATION, nearByLocations);
        query.setLimit(10);
        return query.find();
    }

    public static List<Activity> getForUser(User user) throws ParseException {
        query = ParseQuery.getQuery(Activity.class);
        query.whereEqualTo(USER, user);
        return query.find();
    }

    public static List<Activity> getAll() throws ParseException {
        query = ParseQuery.getQuery(Activity.class);
        return query.find();
    }
}
