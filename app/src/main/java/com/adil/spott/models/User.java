package com.adil.spott.models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("User")
public class User extends Model {

    private static String FIRST_NAME = "first_name";
    private static String LAST_NAME = "last_name";
    private static String AGE = "age";
    private static String GENDER = "gender";
    private static String OWNER = "owner";

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

    public List<User> getMatchedUsers() throws ParseException {
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.setLimit(10);

        return query.find();
    }


    @Override
    public String getLogTag() {
        return TAG;
    }
}
