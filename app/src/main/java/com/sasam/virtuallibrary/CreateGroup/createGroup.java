package com.sasam.virtuallibrary.CreateGroup;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.sasam.virtuallibrary.Groups.GroupDetails;
import com.sasam.virtuallibrary.Groups.myGroupFragment;
import com.sasam.virtuallibrary.MainActivity;
import com.sasam.virtuallibrary.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class createGroup extends Fragment {

    public createGroup() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private  FragmentActivity myContext;
    private static DatabaseReference myRef;
    Button create ,cancel;
    public EditText groupName,groupCode;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_group, container, false);

        cancel = (Button) view.findViewById(R.id.cancel);
        create = (Button) view.findViewById(R.id.create);
        groupName = (EditText) view.findViewById(R.id.groupName);
        groupCode = (EditText) view.findViewById(R.id.groupCode);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String GroupName = (String) groupName.getText().toString();
                String GroupCode = groupCode.getText().toString();

                if (!GroupName.equals("") && !GroupCode.equals("")) {

                    myRef = MainActivity.Connection("Groups");

                    String[] members = {MainActivity.getUserID()};
                    List nameList = new ArrayList<String>(Arrays.asList(members));

                    String tGroupID = myRef.push().getKey();

                    GroupDetails groupDetails = new GroupDetails(MainActivity.getUserName(),  GroupCode ,tGroupID, nameList ,GroupName);
                    if (tGroupID != null)
                    {
                        myRef.child(tGroupID).setValue(groupDetails);
                        MainActivity.Connection("Users").child(MainActivity.getUserID()).child("userGroupList").push().setValue(tGroupID);
                    }
                        showAlert(view);

                    groupName.setText(null);
                    groupCode.setText(null);
                }
                else showAlertError(view);



            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }


    void showAlert(View view){
        new SweetAlertDialog(view.getContext(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Group created successfully !")
                .setConfirmText("Yes,Done!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                       // loadGroupPage();

                    }
                })
                .show();
    }
    void showAlertError(View view){
        new SweetAlertDialog(view.getContext(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText("Something went wrong!")
                .setConfirmText("Try again ?")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();

                    }
                })
                .show();
    }

    public void loadGroupPage() {

        myGroupFragment faid = new  myGroupFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                myContext.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, faid);
        fragmentTransaction.commit();
    }


}
