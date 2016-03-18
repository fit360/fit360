package com.app.spott.models;

import com.app.spott.exceptions.WorkoutModelException;
import com.app.spott.exceptions.ModelException;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

import org.parceler.Parcel;

import java.util.List;

@Parcel
@ParseClassName("Workout")
public class Workout extends Model {

    private static final String TAG = Workout.class.getSimpleName();
    public static final String USER = "user";
    private static final String ACTIVITY_TYPE = "workout_type";
    private static final String TIME = "time";
    private static final String FREQUENCY = "frequency";
    public static final String LOCATION = "location";

    private static ParseQuery<Workout> query;

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

    public WorkoutType getWorkoutType() {
        return WorkoutType.valueOf(getString(ACTIVITY_TYPE));
    }

    public void setWorkoutType(WorkoutType workoutType) {
        put(ACTIVITY_TYPE, workoutType.toString());
    }

    public Time getTime() {
        return Time.valueOf(getString(TIME));
    }

    public void setTime(Time time) {
        put(TIME, time.toString());
    }

    public Frequency getFrequency() {
        return Frequency.valueOf(getString(FREQUENCY));
    }

    public void setFrequency(Frequency f) {
        put(FREQUENCY, f.toString());
    }

    public Location getLocation(){
        return (Location) get(LOCATION);
    }

    public void setLocation(Location loc){
        put(LOCATION, loc);
    }



    private static ParseQuery<Workout> getQuery(){
        ParseQuery<Workout> q = ParseQuery.getQuery(Workout.class);
        q.include(LOCATION);
        q.include(USER);
        return q;
    }

    public void getMatchedWorkouts(final FindCallback<Workout> findCallback) {
//        maybe use innerquery for optimization
        getLocation().getNearByLocations(new FindCallback<Location>() {
            @Override
            public void done(List<Location> nearByLocations, ParseException e) {
                query = getQuery();
                query.whereContainedIn(LOCATION, nearByLocations);
                query.setLimit(10);
                query.findInBackground(findCallback);
            }
        });
    }

    public static void getForUser(User user, FindCallback<Workout> findCallback) {
        query = getQuery();
        query.whereEqualTo(USER, user);
        query.findInBackground(findCallback);
    }

    public static void getAll(FindCallback<Workout> findCallback) {
        query = getQuery();
        query.findInBackground(findCallback);
    }

    public static void findOne(String id, GetCallback getCallback){
        query = getQuery();
        query.whereEqualTo(objectId, id);
        query.getFirstInBackground(getCallback);
    }

    @Override
    public void saveModel() throws ModelException {
        if (this.getWorkoutType() == null || this.getLocation() == null || this.getUser() == null)
            throw new WorkoutModelException();

        this.getLocation().saveModel();
        super.saveModel();
    }

    public static void getWorkoutsAroundLatLng(double lat, double lng, FindCallback<Workout> findCallback) {
        ParseGeoPoint parseGeoPoint = new ParseGeoPoint(lat, lng);
        ParseQuery<Location> locationParseQuery = ParseQuery.getQuery(Location.class);
        locationParseQuery.whereWithinKilometers(Location.POINT, parseGeoPoint, 1);
        ParseQuery<Workout> activityParseQuery = getQuery();
        activityParseQuery.whereMatchesQuery(LOCATION, locationParseQuery);
        activityParseQuery.findInBackground(findCallback);
    }
}
