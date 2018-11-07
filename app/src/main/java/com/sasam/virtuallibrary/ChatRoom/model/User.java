package com.sasam.virtuallibrary.ChatRoom.model;


import java.util.ArrayList;

public class User {
    public String name;
    public String email;
    public String avata;
    public Status status;
    public Message message;
   // public ArrayList<Group> group;
    //public ArrayList<GroupUser> group;


    public User(){
        status = new Status();
        message = new Message();
        status.isOnline = false;
        status.timestamp = 0;
        message.idReceiver = "0";
        message.idSender = "0";
        message.text = "";
        message.timestamp = 0;
        //group=new ArrayList<Group>();
        //group=new ArrayList<>();
    }

    public void setAvata(String avata) {
        this.avata = avata;
    }
}
