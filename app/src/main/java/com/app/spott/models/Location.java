package com.app.spott.models;

import com.app.spott.exceptions.LocationMissingPlaceId;
import com.app.spott.exceptions.ModelException;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

@ParseClassName("Location")
public class Location extends Model {

    private static final String TAG = Location.class.getSimpleName();

    private static final String NAME = "name";
    private static final String ADDRESS = "address";
    private static final String PLACE_ID = "place_id";
    public static final String POINT = "point";

    @Override
    public String getLogTag() {
        return TAG;
    }

    public ParseGeoPoint getPoint() {
        return getParseGeoPoint(POINT);
    }

    public void setPoint(ParseGeoPoint point) {
        put(POINT, point);
    }

    public String getName() {
        return getString(NAME);
    }

    public void setName(String name) {
        put(NAME, name);
    }

    public String getAddress() {
        return getString(ADDRESS);
    }

    public void setAddress(String address) {
        put(ADDRESS, address);
    }

    public String getPlaceId() {
        return getString(PLACE_ID);
    }

    public void setPlaceId(String placeId) {
        put(PLACE_ID, placeId);
    }

    public String getNameAddress(){
        return getName() + ", " + getAddress();
    }

    public void getNearByLocations(FindCallback<Location> findCallback) {
        ParseQuery<Location> query = ParseQuery.getQuery(Location.class);
        query.whereNear(POINT, getPoint());
        query.setLimit(30);
        query.findInBackground(findCallback);
    }

    public static void getByPlaceId(String placeId, GetCallback<Location> getCallback) {
        ParseQuery<Location> query = ParseQuery.getQuery(Location.class);
        query.whereEqualTo(PLACE_ID, placeId);
        query.getFirstInBackground(getCallback);
    }

    @Override
    public void saveModel() throws ModelException {
        if (this.getPlaceId() == null) {
            throw new LocationMissingPlaceId();
        }

        getByPlaceId(this.getPlaceId(), new GetCallback<Location>() {
            @Override
            public void done(Location object, ParseException e) {
                if (object != null)
                    return;
            }
        });
        super.saveModel();
    }
}
