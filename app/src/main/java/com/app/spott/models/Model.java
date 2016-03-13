package com.app.spott.models;

import android.util.Log;

import com.app.spott.exceptions.ModelException;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public abstract class Model extends ParseObject {

    public abstract String getLogTag();

    private static String objectId = "_id";

    public void saveModel() throws ModelException {
        this.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null)
                    Log.v(getLogTag(), "object saved");
                else
                    Log.e(getLogTag(), e.getMessage());
            }
        });
    }

    public static void findOne(Class modelClass, String id, GetCallback getCallback) {
        ParseQuery query = ParseQuery.getQuery(modelClass);
        query.whereEqualTo(objectId, id);
        query.getFirstInBackground(getCallback);
    }
}
