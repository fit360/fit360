package com.app.spott.models;

import com.app.spott.R;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum WorkoutType implements Illustrable {
    BASKETBALL("Basketball", R.drawable.ic_basketball),
    BICYCLING("Bicycling", R.drawable.ic_bicycling),
    BOXING("Boxing", R.drawable.ic_boxing),
    CROSSFIT("Crossfit", R.drawable.ic_crossfit),
    FOOTBALL("Football", R.drawable.ic_football),
    SOCCER("Soccer", R.drawable.ic_soccer),
    TENNIS("Tennis", R.drawable.ic_tennis),
    YOGA("Yoga", R.drawable.ic_yoga),
    RUNNING("Running", R.drawable.ic_running),
    MARTIAL_ARTS("Martial Arts", R.drawable.ic_martial_arts),
    SWIMMING("Swimming", R.drawable.ic_swimming);

    private String value;
    private int icon;

    private static final Map<String, WorkoutType> lookup = new HashMap<>();
    private static final ArrayList<String> readableStrings = new ArrayList<>();
    private static ArrayList<WorkoutType> all;

    static {
        for(WorkoutType workoutType : EnumSet.allOf(WorkoutType.class)) {
            lookup.put(workoutType.name(), workoutType);
            readableStrings.add(workoutType.toString());
            all.add(workoutType);
        }
    }

    WorkoutType(String value, int icon) {
        this.value = value;
        this.icon = icon;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public int getIcon(){return icon;}

    public static List<String> getReadableStrings(){
        return readableStrings;
    }

    public static WorkoutType getActivityType(String activityString){
        return lookup.get(activityString);
    }

    public static List<WorkoutType> getAll(){
        return all;
    }
}
