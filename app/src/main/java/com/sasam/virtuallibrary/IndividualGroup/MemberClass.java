package com.sasam.virtuallibrary.IndividualGroup;

public class MemberClass {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    private String name;
    private String mail;
    private String uid;
    private Float rating;

    MemberClass(String name, String mail, String uid, Float rating){
        this.name= name;
        this.mail= mail;
        this.uid= uid;
        this.rating=rating;

    }
}
