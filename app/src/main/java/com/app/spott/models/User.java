package com.app.spott.models;

import com.parse.ParseClassName;
import com.parse.ParseUser;

@ParseClassName("User")
public class User extends Model {

    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String AGE = "age";
    private static final String GENDER = "gender";
    private static final String OWNER = "owner";
    private static final String ACTIVITIES = "activities";

    private static final String TAG = User.class.getSimpleName();

    public User(){super();}

    public String getFirstName(){
        return getString(FIRST_NAME);
    }

    public void setFirstName(String firstName){
        put(FIRST_NAME, firstName);
    }

    public String getLastName(){
        return getString(LAST_NAME);
    }

    public void setLastName(String lastName){
        put(LAST_NAME, lastName);
    }

    public int getAge(){
        return getInt(AGE);
    }

    public void setAge(int age){
        put(AGE, age);
    }

    public Gender getGender(){
        return (Gender) get(GENDER);
    }

    public void setGender(Gender gender){
        put(GENDER, gender);
    }

    public String getId(String id){
        return getObjectId();
    }

    public ParseUser getOwner(){
        return getParseUser(OWNER);
    }

    public void setOwner(ParseUser owner){
        put(OWNER, owner);
    }

    @Override
    public String getLogTag() {
        return TAG;
    }
}
