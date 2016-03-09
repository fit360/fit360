package com.app.spott.models;

import com.app.spott.exceptions.ActivityModelException;
import com.app.spott.exceptions.ModelException;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

@ParseClassName("Activity")
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
        return ActivityType.getActivityType(getString(ACTIVITY_TYPE));
    }

    public void setActivityType(ActivityType activityType) {
        put(ACTIVITY_TYPE, activityType.getName());
    }

    public Time getTime() {
        return Time.getTime(getString(TIME));
    }

    public void setTime(Time time) {
        put(TIME, time.getName());
    }

    public Frequency getFrequency() {
        return Frequency.getFrequency(getString(FREQUENCY));
    }

    public void setFrequency(Frequency f) {
        put(FREQUENCY, f.getName());
    }

    public Location getLocation(){
        return (Location) get(LOCATION);
    }

    public void setLocation(Location loc){
        put(LOCATION, loc);
    }

    private static ParseQuery<Activity> getQuery(){
        ParseQuery<Activity> q = ParseQuery.getQuery(Activity.class);
        q.include(LOCATION);
        q.include(USER);
        return q;
    }
    public List<Activity> getMatchedActivities() throws ParseException {
//        maybe use innerquery for optimization
        List<Location> nearByLocations = getLocation().getNearByLocations();
        query = getQuery();
        query.whereContainedIn(LOCATION, nearByLocations);
        query.setLimit(10);
        return query.find();
    }

    public static List<Activity> getForUser(User user) throws ParseException {
        query = getQuery();
        query.whereEqualTo(USER, user);
        return query.find();
    }

    public static List<Activity> getAll() throws ParseException {
        query = getQuery();
        return query.find();
    }

    @Override
    public void saveModel() throws ModelException, ParseException {
        if (this.getActivityType() == null || this.getLocation() == null || this.getUser() == null)
            throw new ActivityModelException();

        this.getLocation().saveModel();
        super.saveModel();
    }
}
