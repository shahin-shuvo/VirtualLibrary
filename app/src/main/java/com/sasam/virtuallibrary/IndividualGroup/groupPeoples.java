package com.sasam.virtuallibrary.IndividualGroup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sasam.virtuallibrary.R;


public class groupPeoples extends Fragment {
    String groupId;
    public groupPeoples() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_peoples, container, false);
        groupId = getArguments().getString("groupID");
        System.out.println(groupId);
        return  view;
    }


}
