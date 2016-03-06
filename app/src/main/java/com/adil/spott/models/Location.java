package com.adil.spott.models;

import com.parse.ParseGeoPoint;

public class Location extends Model {

    private static final String TAG = Location.class.getSimpleName();

    private static final String NAME = "name";
    private static final String ADDRESS = "address";
    private static final String POINT = "point";

    @Override
    public String getLogTag() {
        return TAG;
    }

    public ParseGeoPoint getPoint(){
        return getParseGeoPoint(POINT);
    }

    public void setPoint(ParseGeoPoint point){
        put(POINT, point);
    }

    public String getName(){
        return getString(NAME);
    }

    public void setName(String name){
        put(NAME, name);
    }

    public String getAddress(){
        return getString(ADDRESS);
    }

    public void setAddress(String address){
        put(ADDRESS, address);
    }

}
