package com.app.spott.models;

import java.util.ArrayList;
import java.util.List;

public enum Time {
    EARLY_MORNING("Early morning"),
    LATE_MORNING("Late morning"),
    NOON("Noon"),
    LATE_NOON("Late noon"),
    EVENING("Evening"),
    NIGHT("Night");

    private String name;

    Time(String value){this.name = value;}

    public String getName() {
        return name;
    }

    public static List<String> getFriendlyNames(){
        ArrayList<String> result = new ArrayList<>();
        for(Time time : Time.values()){
            result.add(time.name);
        }
        return result;
    }
}
