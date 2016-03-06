package com.adil.spott.models;

public class Activity extends Model {

    private static final String TAG = Activity.class.getSimpleName();
    private static final String USER = "user";
    private static final String ACTIVITY_TYPE = "activity_type";
    private static final String TIME = "time";
    private static final String FREQUENCY = "frequency";

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

    public Activities getActivityType() {
        return (Activities) get(ACTIVITY_TYPE);
    }

    public void setActivityType(Activities activityType) {
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
}
