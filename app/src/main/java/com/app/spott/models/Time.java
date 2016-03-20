package com.app.spott.models;

import com.app.spott.R;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

public enum Time implements EnumModel {
    EARLY_MORNING("Early morning", R.drawable.ic_early_morning),
    LATE_MORNING("Late morning", R.drawable.ic_late_morning),
    NOON("Noon", R.drawable.ic_noon),
    LATE_NOON("Late noon", R.drawable.ic_late_noon),
    EVENING("Evening", R.drawable.ic_evening),
    NIGHT("Night", R.drawable.ic_night);

    private String name;
    private int icon;

    private static HashMap<String, Time> lookup = new HashMap<>();
    private static ArrayList<String> readableNames = new ArrayList<>();
    private static ArrayList<Time> all;

    static {
        for (Time t : EnumSet.allOf(Time.class)) {
            lookup.put(t.toString(), t);
            readableNames.add(t.getName());
        }
        all = new ArrayList<>(lookup.values());
    }

    Time(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public static List<String> getReadableNames() {
        return readableNames;
    }

    public static Time getTime(String value) {
        return lookup.get(value);
    }

    @Override
    public int getIcon() {
        return icon;
    }

    public static List<Time> getAll() {
        return all;
    }

}
