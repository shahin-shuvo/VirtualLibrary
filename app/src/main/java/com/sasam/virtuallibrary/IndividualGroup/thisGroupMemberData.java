package com.sasam.virtuallibrary.IndividualGroup;

import android.app.Activity;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.sasam.virtuallibrary.Groups.GroupDetails;

import java.util.ArrayList;
import java.util.List;

public class thisGroupMemberData implements DataLoadInterface {

    private DatabaseReference groupDatabase,mDatabase;
    String code;
    private Activity activity;
    private View view;
    List<GroupDetails> listMyGroup = new ArrayList<>();
    List<String> myGroupList =  new ArrayList<>();
    ArrayList<String> listName = new ArrayList<>();
    ArrayList<String> memberList = new ArrayList<>();
    thisGroupMemberData(Activity activity,View view){

        this.activity = activity;
        this.view = view;
    }
    @Override
    public void loadData(String id) {

    }
}
