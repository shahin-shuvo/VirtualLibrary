package com.sasam.virtuallibrary.Profile;

import android.telephony.PhoneNumberUtils;

public class User {

    private String name;
    private String email;
    private String Uid;
    private float rating;

    public User(String name,String email,String Uid){
        this.name=name;
        this.email=email;
        this.Uid=Uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return Uid;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
