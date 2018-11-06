package com.sasam.virtuallibrary.IndividualGroup.NewsFeed;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sasam.virtuallibrary.IndividualGroup.NewsFeed.dialogs.PostCreateDialog;
import com.sasam.virtuallibrary.IndividualGroup.NewsFeed.models.Post;
import com.sasam.virtuallibrary.MainActivity;
import com.sasam.virtuallibrary.R;

import java.util.ArrayList;
import java.util.List;


public class GroupNewsFeed extends Fragment {
    private View mRootVIew;
    public DatabaseReference mDatabase, status;
    private RecyclerView mPostRecyclerView;
    static String groupId;
    List<Post> listPost = new ArrayList<>();
    List<String > listThisGrpPost = new ArrayList<>();

    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;



    public GroupNewsFeed() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (FirebaseApp.getApps(getActivity()).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        mRootVIew = inflater.inflate(R.layout.fragment_group_news_feed, container, false);
        this.groupId = getArguments().getString("groupID");

        FloatingActionButton fab = (FloatingActionButton) mRootVIew.findViewById(R.id.fab_post);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostCreateDialog dialog = new PostCreateDialog();
                dialog.show(getFragmentManager(), null);
            }
        });

        recyclerView =mRootVIew.findViewById(R.id.recyclerview_post);
        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        readPostId(new MyCallback() {
            @Override
            public void onCallback(final List<String> list) {

                    MainActivity.Connection("posts").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            listPost.clear();
                            for(int i =0;i<listThisGrpPost.size();i++) {
                                String tempPostID = listThisGrpPost.get(i);
                                Post post = dataSnapshot.child(tempPostID).getValue(Post.class);
                                listPost.add(post);
                            }
                            adapter = new PostAdapter(listPost,getContext());
                            recyclerView.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

            }
        });

        return mRootVIew;
    }


    public static String getGroupId(){
        return groupId;
    }


    public void readPostId(final MyCallback myCallback) {
        mDatabase =MainActivity. Connection("Groups").child(groupId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listThisGrpPost.clear();
                for (DataSnapshot childSnapshot: dataSnapshot.child("thisGroupPost").getChildren()) {
                    String tempPostID = childSnapshot.getValue(String.class);
                    listThisGrpPost.add(tempPostID);
                }
                myCallback.onCallback(listThisGrpPost);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

    }

    public interface MyCallback {
        void onCallback(List<String> list);
    }
}
