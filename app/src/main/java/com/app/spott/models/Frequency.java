package com.app.spott.models;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

public enum Frequency {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly");

    private String name;
    private static HashMap<String, Frequency> lookup = new HashMap<>();
    private static ArrayList<String> readableNames = new ArrayList<>();

    static {
        for(Frequency f: EnumSet.allOf(Frequency.class)){
            lookup.put(f.getName(), f);
            readableNames.add(f.getName());
        }
    }

    Frequency(String name){this.name = name;}

    public String getName() {
        return name;
    }

    public static List<String> getReadableNames(){
        return readableNames;
    }

    public static Frequency getFrequency(String f){
        return lookup.get(f);
    }
}
