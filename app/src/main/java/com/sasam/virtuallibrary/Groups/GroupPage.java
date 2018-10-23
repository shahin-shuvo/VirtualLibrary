package com.sasam.virtuallibrary.Groups;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sasam.virtuallibrary.R;

import java.util.ArrayList;
import java.util.List;

import static com.sasam.virtuallibrary.MainActivity.Connection;
import static com.sasam.virtuallibrary.MainActivity.getUserName;


public class GroupPage extends Activity {

    private FirebaseAuth mAuth;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    public DatabaseReference mDatabase, status;
    private DatabaseReference  groupDatabase;
    List<GroupDetails> listEvent = new ArrayList<GroupDetails>();
    ArrayList<String> listName = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_group, container, false);
        super.onCreate(savedInstanceState);



        recyclerView =
                view.findViewById(R.id.card_view);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);



        final ProgressDialog dialog = null;
        //dialog = ProgressDialog.show(this.getContext(), "", "Loading", true);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = Connection("Users").child(getUserName());
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listName.clear();
                listEvent.clear();


                for (DataSnapshot childSnapshot: dataSnapshot.child("myGroup").getChildren()) {
                    //   temp = Connection("Groups").child(childSnapshot.getValue(String.class)).getValue(GroupDetails.class);
                   // Toast.makeText(getApplicationContext(), childSnapshot.getValue(String.class), Toast.LENGTH_SHORT).show();
                    listName.add(childSnapshot.getValue(String.class));
                    //  loadMyGroup(listName);

                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        groupDatabase = Connection("Groups");
        groupDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listEvent.clear();

                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    //   temp = Connection("Groups").child(childSnapshot.getValue(String.class)).getValue(GroupDetails.class);
                    // Toast.makeText(getApplicationContext(), childSnapshot.getValue(String.class), Toast.LENGTH_SHORT).show();
                    GroupDetails temp = new GroupDetails();
                    temp = childSnapshot.getValue(GroupDetails.class);

                    if (!temp.getName().equals("") ) {
                        for (int i=0; i<listName.size(); i++) {
                            if(temp.getName().equals(listName.get(i)))
                            {
                                listEvent.add(temp);
                                break;
                            }

                        }
                    }

                }

                adapter = new GroupAdapter(listEvent);
                recyclerView.setAdapter(adapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mAuth = FirebaseAuth.getInstance();
        final String userID = mAuth.getCurrentUser().getUid();





     return view;
    }
}
