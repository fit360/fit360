package com.adil.spott.models;

public class Activity extends Model{

    private static final String TAG = Activity.class.getSimpleName();
    private static final String ACTIVITY = "activity";
    private static final String USER = "user";
    private static final String TYPE = "type";
    private static final String TIME = "time";
    private static final String FREQUENCY = "frequency";

    @Override
    public String getLogTag() {
        return TAG;
    }
}
