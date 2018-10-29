package com.sasam.virtuallibrary.IndividualGroup;

import android.app.Activity;
import android.view.View;

import com.sasam.virtuallibrary.Groups.GroupDetails;

import java.util.ArrayList;
import java.util.List;

public class DataLoaderFacade {
    List<GroupDetails> listMyGroup = new ArrayList<>();
    List<String> myGroupList =  new ArrayList<>();
    ArrayList<String> listName = new ArrayList<>();
    List<String> mList = new ArrayList<>();

    public DataLoadInterface memberList;
    public  DataLoadInterface memberData;

    public DataLoaderFacade(Activity activity, View view){
        memberList = new thisGroupMemberList(activity,view);
        //memberData = new thisGroupMemberData(activity,view);
    }

    public void loadMemberList(String id){
        memberList.loadData(id);
    }

    public void loadMemberData(String id){
        memberData.loadData(id);
    }

}
