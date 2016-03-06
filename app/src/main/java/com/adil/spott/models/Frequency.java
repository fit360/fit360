package com.adil.spott.models;

public enum Frequency {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly");

    private String value;

    Frequency(String value){this.value = value;}
}
