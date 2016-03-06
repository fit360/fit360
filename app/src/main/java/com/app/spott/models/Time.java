package com.app.spott.models;

public enum Time {
    EARLY_MORNING("Early morning"),
    LATE_MORNING("Late morning"),
    NOON("Noon"),
    LATE_NOON("Late noon"),
    EVENING("Evening"),
    NIGHT("Night");

    private String name;

    Time(String value){this.name = name;}

    public String getName() {
        return name;
    }
}
