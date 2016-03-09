package com.app.spott.exceptions;

public class LocationModelException extends ModelException {

    public LocationModelException(String message) {
        super(message);
    }

    public LocationModelException(){
        this("Invalid location model");
    }
}