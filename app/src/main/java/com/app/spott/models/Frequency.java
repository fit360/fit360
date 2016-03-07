package com.app.spott.models;

import java.util.ArrayList;
import java.util.List;

public enum Frequency {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly");

    private String name;

    Frequency(String name){this.name = name;}

    public String getName() {
        return name;
    }

    public static List<String> getFriendlyNames(){
        ArrayList<String> result = new ArrayList<>();
        for(Frequency frequency : Frequency.values()){
            result.add(frequency.name);
        }
        return result;
    }
}
