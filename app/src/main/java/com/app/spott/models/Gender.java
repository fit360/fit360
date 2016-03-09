package com.app.spott.models;

import java.util.EnumSet;
import java.util.HashMap;

public enum Gender {
    MALE("Male"), FEMALE("Female");

    private String value;
    private static HashMap<String, Gender> lookup = new HashMap<>();

    static {
        for(Gender gender: EnumSet.allOf(Gender.class)) {
            lookup.put(gender.getName(), gender);
        }
    }

    Gender(String value){this.value = value;}

    public String getName() {
        return value;
    }

    public static Gender get(String gender){
        return lookup.get(gender);
    }
}
