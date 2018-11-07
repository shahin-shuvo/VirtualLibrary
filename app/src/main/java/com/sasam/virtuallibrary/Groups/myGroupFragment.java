package com.sasam.virtuallibrary.Groups;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sasam.virtuallibrary.MainActivity;
import com.sasam.virtuallibrary.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class myGroupFragment extends Fragment {

    private Button create,cancel;
    public EditText groupName,groupCode;
    public String CurrentUser ;
    public Dialog dialog1;
    //add Fir ebase Database stuff
    public DatabaseReference mDatabase, status;
    private DatabaseReference  groupDatabase,memberDatabase;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;



    List<GroupDetails> listAllGroup = new ArrayList<GroupDetails>();
    List<GroupDetails> listMyGroup = new ArrayList<>();
    List<String> myGroupList =  new ArrayList<>();
    ArrayList<String> listName = new ArrayList<>();
    ArrayList<String> memberList = new ArrayList<>();


    final Semaphore semaphore = new Semaphore(0);

    private static DatabaseReference myRef;
    private static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public myGroupFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_group, container, false);

        final SweetAlertDialog pDialog = new SweetAlertDialog(view.getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                pDialog.dismiss();
            }
        }, 2000); // 3000 milliseconds delay


        recyclerView = view.findViewById(R.id.recycler_view_group);
        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);



      //  System.out.println(listAllGroup.size());
        readData(new MyCallback() {
            @Override
            public void onCallback(List<GroupDetails> list) {




        mDatabase =MainActivity. Connection("Users").child(MainActivity.getUserID());
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listMyGroup.clear();
                for (DataSnapshot childSnapshot: dataSnapshot.child("userGroupList").getChildren()) {
                    String tempID = childSnapshot.getValue(String.class);
                    for(int i =0;i<listAllGroup.size();i++)
                    {
                        if(listAllGroup.get(i).getGroupID().toString().equals(tempID))
                        {
                            listMyGroup.add(listAllGroup.get(i));
                        }
                    }

                }
                adapter = new GroupAdapter(listMyGroup,getContext());
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

            }
        });


        return view;
    }


    public void readData(final MyCallback myCallback) {
        groupDatabase = MainActivity.Connection("Groups");
        groupDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listAllGroup.clear();

                for(DataSnapshot childSnapshot: dataSnapshot.getChildren())
                {
                    String groupID = (String) childSnapshot.child("groupID").getValue();
                    String groupName = (String) childSnapshot.child("name").getValue();
                    String groupCode = (String) childSnapshot.child("code").getValue();
                    String admin = (String) childSnapshot.child("admin").getValue();
                    String adminID = (String) childSnapshot.child("adminID").getValue();

                    GroupDetails temp = new GroupDetails(admin,  groupCode ,groupID ,groupName,adminID);
                    listAllGroup.add(temp);


                }
                myCallback.onCallback(listAllGroup);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

    }


    public interface MyCallback {
        void onCallback(List<GroupDetails> list);
    }




}
