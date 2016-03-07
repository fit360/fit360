package com.app.spott.models;

import java.util.ArrayList;
import java.util.List;

public enum ActivityType {
    ARM_WRESTLING("Arm wrestling"),
    BMX_BIKE_RIDING("BMX bike riding"),
    BALLET("Ballet"),
    BASEBALL("Baseball"),
    BASKETBALL("Basketball"),
    BICYCLING("Bicycling"),
    BOXING("Boxing"),
    CALISTHENICS("Calisthenics"),
    CANOEING("Canoeing"),
    KAYAKING("Kayaking"),
    ROWING("Rowing"),
    CROSSFIT("Crossfit"),
    DIVING("Diving"),
    FOOTBALL("Football"),
    GYMNASTICS("Gymnastics"),
    HOCKEY("Hockey"),
    RUNNING("Running"),
    KUNG_FU("Kung-fu"),
    MARATHON_RUNNING("Marathon running"),
    POLE_DANCING("Pole dancing"),
    POLE_VAULTING("Pole vaulting"),
    POWERLIFTING("Powerlifting"),
    ROCK_CLIMBING("Rock climbing"),
    SKATEBOARDING("Skateboarding"),
    ICE_SKATING("Ice skating"),
    SKIING("Skiing"),
    SNOWBOARDING("Snowboarding"),
    SOCCER("Soccer"),
    SPRINTING("Sprinting"),
    STRETCHING("Stretching"),
    SUPER_SQUATS("Super squats"),
    SURFING("Surfing"),
    SWIMMING("Swimming"),
    SWORD_FIGHTING("Sword fighting"),
    TAI_CHI("Tai-chi"),
    TENNIS("Tennis"),
    VOLLEYBALL("Volleyball"),
    WALKING("Walking"),
    WATER_AEROBICS("Water aerobics"),
    WATER_POLO("Water polo"),
    WRESTLING("Wrestling"),
    YOGA("Yoga"),
    ZUMBA("Zumba");

    private String value;

    ActivityType(String value) {
        this.value = value;
    }

    public static List<String> getFriendlyNames(){
        ArrayList<String> result = new ArrayList<>();
        for(ActivityType activityType : ActivityType.values()){
            result.add(activityType.value);
        }
        return result;
    }

}
