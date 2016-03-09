package com.app.spott.models;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

public enum Time {
    EARLY_MORNING("Early morning"),
    LATE_MORNING("Late morning"),
    NOON("Noon"),
    LATE_NOON("Late noon"),
    EVENING("Evening"),
    NIGHT("Night");

    private String name;

    private static HashMap<String, Time> lookup = new HashMap<>();
    private static ArrayList<String> readableNames = new ArrayList<>();

    static {
        for(Time t: EnumSet.allOf(Time.class)){
            lookup.put(t.toString(), t);
            readableNames.add(t.toString());
        }
    }

    Time(String name){this.name = name;}

    public String getName() {
        return name;
    }

    public static List<String> getReadableNames(){
        return readableNames;
    }

    public static Time getTime(String value){
        return lookup.get(value);
    }
}
