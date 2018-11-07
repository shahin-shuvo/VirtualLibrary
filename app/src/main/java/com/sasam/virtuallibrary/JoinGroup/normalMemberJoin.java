package com.sasam.virtuallibrary.JoinGroup;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sasam.virtuallibrary.MainActivity;
import com.sasam.virtuallibrary.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class normalMemberJoin implements Join{

    public EditText joinCode;
    public String grpID;
    public Button joinButton;
    public static Activity object;
    private DatabaseReference groupDatabase,mDatabase;
    String code;
    private Activity activity;

    normalMemberJoin(Activity activity)
    {
        this.activity = activity;
    }
    @Override
    public void grpJoin() {

              joinCode = (EditText) activity.findViewById(R.id.inputCode);

                code = joinCode.getText().toString();
                if (!code.equals("") && !code.equals("")) {

                    groupDatabase = MainActivity.Connection("Groups");
                    groupDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                                if(childSnapshot.child("code").getValue(String.class).equals(code))
                                {
                                    if(!activity.isFinishing())
                                    {
                                        grpID = childSnapshot.child("groupID").getValue(String.class);
                                        System.out.println(grpID);
                                        MainActivity. Connection("Users").child(MainActivity.getUserID()).child("userGroupList").push().setValue(grpID);
                                        MainActivity. Connection("Groups").child(grpID).child("members").push().setValue(MainActivity.getUserID());

                                        showAlertSuccess();
                                    }

                                    joinCode.setText(null);
                                    break;
                                }

                            }


                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });

                }
                else {
                    if(!activity.isFinishing())
                    {
                        showAlertError();
                    }

                }



            }


    void showAlertSuccess(){
        new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Group join successfull !")
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
    void showAlertError(){
        new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
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



}
