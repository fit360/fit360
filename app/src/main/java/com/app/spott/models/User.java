package com.app.spott.models;

import com.app.spott.exceptions.ModelException;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("User")
public class User extends Model {

    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String AGE = "age";
    private static final String GENDER = "gender";
    private static final String OWNER = "owner";
    private static final String PROFILE_IMAGE_URL = "profile_image_url";

    private static final String TAG = User.class.getSimpleName();

    private static ParseQuery<User> query;

    public User(){super();}

    public String getFirstName(){
        return getString(FIRST_NAME);
    }
    public String getProfileImageUrl(){
        return getString(PROFILE_IMAGE_URL);
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
        return Gender.getGender(getString(GENDER));
    }

    public void setGender(Gender gender){
        put(GENDER, gender.getName());
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

    public static void getByOwner(ParseUser owner, GetCallback<User> getCallback) {
        query = ParseQuery.getQuery(User.class);
        query.whereEqualTo(OWNER, owner);
        query.getFirstInBackground(getCallback);
    }

    @Override
    public void saveModel() throws ModelException, ParseException {
        super.saveModel();
    }


    public void getChosenAcitivities(FindCallback<Activity> findCallback){
        ParseQuery<Activity> parseQuery = ParseQuery.getQuery(Activity.class);
        parseQuery.whereEqualTo(Activity.USER, this);
        parseQuery.orderByAscending("createdAt");
        parseQuery.include(Activity.USER);
        parseQuery.include(Activity.LOCATION);
        parseQuery.findInBackground(findCallback);
    }

}
