package com.app.spott.models;

import com.app.spott.R;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

public enum Frequency implements Illustrable {
    BLACK_FRIDAY("Black Friday",R.drawable.ic_calendar_bf),
    MONTHLY("Monthly",R.drawable.ic_calendar_monthly),
    WEEKLY("Weekly", R.drawable.ic_calendar_weekly),
    DAILY("Daily", R.drawable.ic_calendar_daily);

    private String value;
    private int icon;
    private static HashMap<String, Frequency> lookup = new HashMap<>();
    private static ArrayList<String> readableNames = new ArrayList<>();
    private static ArrayList<Frequency> all;

    static {
        for (Frequency f : EnumSet.allOf(Frequency.class)) {
            lookup.put(f.toString(), f);
            readableNames.add(f.toString());
            all.add(f);
        }
    }

    Frequency(String value, int icon) {
        this.value = value;
        this.icon = icon;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public int getIcon() {
        return this.icon;
    }

    public static List<String> getReadableNames() {
        return readableNames;
    }

    public static Frequency getFrequency(String f) {
        return lookup.get(f);
    }

    public static List<Frequency> getAll() {
        return all;
    }
}
