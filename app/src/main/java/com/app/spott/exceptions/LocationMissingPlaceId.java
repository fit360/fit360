package com.app.spott.exceptions;

public class LocationMissingPlaceId extends LocationModelException {
    public LocationMissingPlaceId(){
        super("Place Id is needed before saving object.");
    }
}