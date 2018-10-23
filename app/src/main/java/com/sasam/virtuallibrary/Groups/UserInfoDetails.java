package com.sasam.virtuallibrary.Groups;

public class UserInfoDetails {

    private String  userName;
    private String usermMail;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUsermMail() {
        return usermMail;
    }

    public void setUsermMail(String usermMail) {
        this.usermMail = usermMail;
    }



    public UserInfoDetails(String userName, String usermMail)
    {
        this.usermMail = usermMail;
        this.userName = userName;
    }


}
