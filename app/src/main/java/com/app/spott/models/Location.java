package com.app.spott.models;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

import java.util.List;

public class Location extends Model {

    private static final String TAG = Location.class.getSimpleName();

    private static final String NAME = "name";
    private static final String ADDRESS = "address";
    private static final String POINT = "point";

    private ParseQuery<Location> query;

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

    public List<Location> getNearByLocations() throws ParseException {
        query = ParseQuery.getQuery(Location.class);
        query.whereNear(POINT, getPoint());
        query.setLimit(30);
        return query.find();
    }

}
