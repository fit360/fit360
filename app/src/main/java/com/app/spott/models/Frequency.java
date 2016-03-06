package com.app.spott.models;

public enum Frequency {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly");

    private String name;

    Frequency(String name){this.name = name;}

    public String getName() {
        return name;
    }
}
