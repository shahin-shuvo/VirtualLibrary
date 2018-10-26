package com.sasam.virtuallibrary.CreateGroup;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sasam.virtuallibrary.CodeGenerator.Iterator;
import com.sasam.virtuallibrary.CodeGenerator.NameRepository;
import com.sasam.virtuallibrary.Groups.GroupDetails;
import com.sasam.virtuallibrary.MainActivity;
import com.sasam.virtuallibrary.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class createGroup extends Fragment {

    public createGroup() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    List<String > existCodeList  = new ArrayList<>();
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
       // groupCode = (EditText) view.findViewById(R.id.groupCode);



        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extractCodeList(new MyCallback() {
                    @Override
                    public void onCallback(List<String> list) {


                String GroupName = (String) groupName.getText().toString();
             //   String GroupCode = groupCode.getText().toString();

                if (!GroupName.equals("")) {

                    String  GroupCode = generateCode(existCodeList);

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
                        showAlert(getView(),GroupCode);

                    groupName.setText(null);

                }
                else showAlertError(getView());

                    }
                });

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


    void showAlert(View view,String string){
        new SweetAlertDialog(view.getContext(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Group Code:- " + string)
                .setConfirmText("Yes,Done!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();


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

    // This part is for unique code generator ----------------------------
    // ----------------------------------------------------------------------
    //======================================================================
    //===========================CREATE UNIQUE GROUP CODE ===================

    public void extractCodeList(final MyCallback myCallback) {
        DatabaseReference groupDatabase = MainActivity.Connection("GivenGroupCode");
        groupDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                existCodeList.clear();
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren())
                {
                    existCodeList.add(childSnapshot.getValue(String.class));

                }
                myCallback.onCallback(existCodeList);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

    }


    public static String generateCode(List<String> existCodeList) {
        char[] chars1 = "stuABCpqrDEzFGmnoHIJKjklLMyNOghiPQvRSdefTUwVWabcXYZx".toCharArray();
        Integer i;
        String string = null;
        boolean flag = true;
        while (flag) {
            StringBuilder sb1 = new StringBuilder();
            Random random1 = new Random();
            for (i = 0; i < 6; i++) {
                char c1 = chars1[random1.nextInt(chars1.length)];
                sb1.append(c1);
            }
            string = sb1.toString();
            System.out.println(string);
            NameRepository namesRepository = new NameRepository(existCodeList, string);
            Integer count = 0;
            for (Iterator iter = namesRepository.getIterator(); iter.hasNext(); ) {
                String result = (String) iter.next();
                if(result.equals("exist"))
                {
                    break;
                }
                else if (result.equals("no"))
                {
                    count++;
                }
            }
            if(count==existCodeList.size()) flag =false;

        }

       return string;
    }

    public interface MyCallback {
        void onCallback(List<String> list);
    }



}
