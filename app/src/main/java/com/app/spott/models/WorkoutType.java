package com.app.spott.models;

import com.app.spott.R;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum WorkoutType implements Illustrable {
    ARM_WRESTLING("Arm wrestling", R.drawable.ic_arm_wrestling),
    BALLET("Ballet", R.drawable.ic_ballet),
    BASEBALL("Baseball", R.drawable.ic_baseball),
    BASKETBALL("Basketball", R.drawable.ic_basketball),
    BICYCLING("Bicycling", R.drawable.biking),
    BOXING("Boxing", R.drawable.ic_boxing),
    CALISTHENICS("Calisthenics", R.drawable.ic_calisthenics),
    CANOEING("Canoeing", R.drawable.ic_canoeing),
    CROSSFIT("Crossfit", R.drawable.ic_crossfit),
    DIVING("Diving", R.drawable.ic_diving),
    FOOTBALL("Football", R.drawable.ic_football),
    GYMNASTICS("Gymnastics", R.drawable.ic_gymnastics),
    HOCKEY("Hockey", R.drawable.ic_hockey),
    ICE_SKATING("Ice skating", R.drawable.ic_ice_skating),
    KAYAKING("Kayaking", R.drawable.ic_kayaking),
    KUNG_FU("Kung-fu", R.drawable.ic_kung_fu),
    POLE_VAULTING("Pole vaulting", R.drawable.ic_pole_vault),
    ROCK_CLIMBING("Rock climbing", R.drawable.ic_rock_climbing),
    ROLLERBLADING("Rollerblading", R.drawable.ic_rollerblading),
    ROWING("Rowing", R.drawable.ic_rowing),
    RUNNING("running", R.drawable.running),
    SKATEBOARDING("Skateboarding", R.drawable.ic_skateboarding),
    SKIING("Skiing", R.drawable.ic_skiing),
    SNOWBOARDING("Snowboarding", R.drawable.ic_snowboarding),
    SOCCER("Soccer", R.drawable.ic_soccer),
    SPINNING("Spinning", R.drawable.ic_spinning),
    STRETCHING("Stretching", R.drawable.ic_stretching),
    SURFING("Surfing", R.drawable.ic_surfing),
    SWIMMING("Swimming", R.drawable.ic_swimming),
    SWORD_FIGHTING("Sword fighting", R.drawable.ic_sword_fighting),
    TAI_CHI("Tai-chi", R.drawable.ic_tai_chi),
    TENNIS("Tennis", R.drawable.ic_tennis),
    VOLLEYBALL("Volleyball", R.drawable.ic_volleyball),
    WALKING("Walking", R.drawable.ic_walking),
    WATER_POLO("Water polo", R.drawable.ic_water_polo),
    WEIGHT_LIFTING("Weight Lifting", R.drawable.ic_weight_lifting),
    WRESTLING("Wrestling", R.drawable.ic_wrestling),
    YOGA("Yoga", R.drawable.ic_yoga),
    ZUMBA("Zumba", R.drawable.ic_zumba);

    private String value;
    private int icon;

    private static final Map<String, WorkoutType> lookup = new HashMap<>();
    private static final ArrayList<String> readableStrings = new ArrayList<>();
    private static ArrayList<WorkoutType> all;

    static {
        for(WorkoutType workoutType : EnumSet.allOf(WorkoutType.class)) {
            lookup.put(workoutType.name(), workoutType);
            readableStrings.add(workoutType.toString());
        }
        all = new ArrayList<>(lookup.values());
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
