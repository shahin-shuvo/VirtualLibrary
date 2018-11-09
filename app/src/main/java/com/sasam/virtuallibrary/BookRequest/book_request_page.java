package com.sasam.virtuallibrary.BookRequest;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
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

public class book_request_page extends AppCompatActivity {

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



    List<BookRequestData> listAllRequest = new ArrayList<BookRequestData>();
    private static DatabaseReference myRef;
    private static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_request_page);
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Book Request");

        recyclerView = findViewById(R.id.recycler_view_request);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        //  System.out.println(listAllGroup.size());
        readRequestData(new MyCallback() {
            @Override
            public void onCallback(List<BookRequestData> list) {

                adapter = new RequestAdapter(listAllRequest);
                recyclerView.setAdapter(adapter);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void readRequestData(final MyCallback myCallback) {
        groupDatabase = MainActivity.Connection("Users").child(user.getUid()).child("pendingRequest");
        groupDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listAllRequest.clear();

                for(DataSnapshot childSnapshot: dataSnapshot.getChildren())
                {

                    BookRequestData temp = childSnapshot.getValue(BookRequestData.class);
                    listAllRequest.add(temp);


                }
                myCallback.onCallback(listAllRequest);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

    }


    public interface MyCallback {
        void onCallback(List<BookRequestData> list);
    }
}
