package com.app.spott.models;

import com.app.spott.exceptions.ModelException;
import com.app.spott.exceptions.LocationMissingPlaceId;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

import java.util.List;

@ParseClassName("Location")
public class Location extends Model {

    private static final String TAG = Location.class.getSimpleName();

    private static final String NAME = "name";
    private static final String ADDRESS = "address";
    private static final String POINT = "point";
    private static final String PLACE_ID = "place_id";

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

    public String getPlaceId(){
        return getString(PLACE_ID);
    }

    public void setPlaceId(String placeId){
        put(PLACE_ID, placeId);
    }

    public List<Location> getNearByLocations() throws ParseException {
        ParseQuery<Location> query = ParseQuery.getQuery(Location.class);
        query.whereNear(POINT, getPoint());
        query.setLimit(30);
        return query.find();
    }

    public static Location getByPlaceId(String placeId) throws ParseException {
        ParseQuery<Location> query = ParseQuery.getQuery(Location.class);
        query.whereEqualTo(PLACE_ID, placeId);
        return query.getFirst();
    }

    @Override
    public void saveModel() throws ModelException, ParseException {
        if (this.getPlaceId() == null) {
            throw new LocationMissingPlaceId();
        }
        Location existingLoc = getByPlaceId(this.getPlaceId());
        if (existingLoc == null){
            super.saveModel();
        } else {
            existingLoc.setName(this.getName());
            existingLoc.setAddress(this.getAddress());
            existingLoc.saveModel();
        }
    }
}
