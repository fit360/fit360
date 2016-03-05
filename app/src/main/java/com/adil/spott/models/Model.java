package com.adil.spott.models;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public abstract class Model extends ParseObject {

    public abstract String getLogTag();

    public void save_(){
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
}
