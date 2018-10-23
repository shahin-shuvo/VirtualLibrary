package com.sasam.virtuallibrary.Groups;

import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.List;

public class GroupDetails implements Serializable {
    public static String  tName, tCode, tAdmin, tMembers,tGroupID;

    public GroupDetails() {

    }


    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public String getAdmin() {
        return Admin;
    }

    public void setAdmin(String Admin) {
        this.Admin = Admin;
    }


    private String Code;
    private String Admin;
    private String groupID;
    private String Name;
    private List Members;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }



    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }



    public List getMembers() {
        return Members;
    }

    public void setMembers(List members) {
        Members = members;
    }


    private FirebaseAuth mAuth;
    public static GroupDetails temporary;
    public GroupDetails(String Admin, String Code, String groupID , List Members, String Name)
    {
        this.Admin = Admin;
        this.Name = Name;
        this.Code = Code;
        this.Members = Members;
        this.groupID = groupID;


    }





}
