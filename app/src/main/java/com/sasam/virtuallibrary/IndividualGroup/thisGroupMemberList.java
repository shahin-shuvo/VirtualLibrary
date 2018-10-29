package com.sasam.virtuallibrary.IndividualGroup;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sasam.virtuallibrary.MainActivity;
import com.sasam.virtuallibrary.R;

import java.util.ArrayList;
import java.util.List;

public class thisGroupMemberList implements DataLoadInterface {

    private DatabaseReference groupDatabase,mDatabase;
    String code;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    List<MemberClass> listGroupMember = new ArrayList<>();
    List<String> myGroupList =  new ArrayList<>();
    List<String> memberList = new ArrayList<>();
    private View view;
    private Activity activity;
    thisGroupMemberList(Activity activity, View view){
        this.activity = activity;
        this.view = view;
    }
    @Override
    public void loadData(String id) {
        recyclerView = view.findViewById(R.id.recycler_view_peoples);
        layoutManager = new LinearLayoutManager(activity);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        extractData(id ,new MyCallback() {
            @Override
            public void onCallback(List<String> list) {

                    mDatabase = MainActivity.Connection("UserInfo");
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            listGroupMember.clear();
                            for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                                String tempUID = childSnapshot.child("uid").getValue(String.class);
                                for(int i =0;i<memberList.size();i++)
                                {
                                    assert tempUID != null;
                                    if(tempUID.equals(memberList.get(i)) )
                                     {
                                         String tempID = childSnapshot.child("uid").getValue(String.class);
                                         String tempName = childSnapshot.child("name").getValue(String.class);
                                         Float tempRating = childSnapshot.child("rating").getValue(Float.class);
                                         String tempMail = childSnapshot.child("email").getValue(String.class);
                                         MemberClass memberClass = new MemberClass(tempName,tempMail,tempID,tempRating);
                                         listGroupMember.add(memberClass);
                                     }
                                }

                            }

                            adapter = new MemberAdapter(listGroupMember, activity);
                            recyclerView.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
            }
        });



    }







    /*=============================== For synchronization =============================*/
    public void extractData(String id ,final MyCallback myCallback) {

        groupDatabase = MainActivity.Connection("Groups").child(id).child("members");
        groupDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                memberList.clear();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    memberList.add(childSnapshot.getValue(String.class));

                }
                myCallback.onCallback(memberList);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

    }



    public interface MyCallback {
        void onCallback(List<String> list);
    }
}
