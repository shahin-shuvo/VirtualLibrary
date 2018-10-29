package com.sasam.virtuallibrary.Groups;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sasam.virtuallibrary.IndividualGroup.GroupTimeLine;
import com.sasam.virtuallibrary.MainActivity;
import com.sasam.virtuallibrary.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by shuvo on 10/17/17.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private List<GroupDetails> list;
    public List<String> GroupID = new ArrayList<>();
    private Context context;
    private DatabaseReference mDatabase ,mDatabaseCount,mRef;
    private FirebaseAuth mAuth;
    public static  int position;
    final  String userId = MainActivity.getUserID();
    public GroupAdapter(List<GroupDetails> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView text1,admin,text3,name;
        public TextView   grpCode;
        public ImageButton delete,edit;
        public Button viewTimeline;
        public CardView cv;
        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            name = (TextView) itemView.findViewById(R.id.nameHeader);
            grpCode = (TextView) itemView.findViewById(R.id.grpCode);
           // disableEditText(grpCode);
            viewTimeline = (Button) itemView.findViewById(R.id.viewGroup) ;

            admin=(TextView) itemView.findViewById(R.id.admin);
            delete = (ImageButton) itemView.findViewById(R.id.leaveButton) ;
            cv=(CardView) itemView.findViewById(R.id.cardItem);
          //  edit = itemView.findViewById(R.id.edit_cvp);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     position = getAdapterPosition();

                }
            });

        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final GroupDetails temp=list.get(position);
        holder.admin.setText(temp.getAdmin());
        holder.name.setText(temp.getName());
        holder.grpCode.setText(temp.getCode());


/* ========================Leave button work ================================== */
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                final PopupMenu popupMenu = new PopupMenu(context,holder.delete);
                popupMenu.getMenuInflater().inflate(R.menu.leave_menu,popupMenu.getMenu());
                popupMenu.setGravity(Gravity.END);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        {
                            switch (item.getItemId()){
                                case R.id.confirmLeave:
                                    final String groupId = list.get(position).getGroupID();

                                    mDatabase = MainActivity.Connection("Users").child(MainActivity.getUserID()).child("userGroupList");
                                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot postsnapshot :dataSnapshot.getChildren()) {
                                                if(Objects.requireNonNull(postsnapshot.getValue()).toString().equals(groupId))
                                                {
                                                    String key = postsnapshot.getKey();
                                                    if (key != null) {
                                                        mDatabase.child(key).removeValue();

                                                        // ==========================Below part ========================
                                                        //====================Remove usewr from Group ================================
                                                        mRef = MainActivity.Connection("Groups").child(groupId).child("members");
                                                        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshotIn) {
                                                                for (DataSnapshot postsnapshotIn :dataSnapshotIn.getChildren()) {
                                                                    if(postsnapshotIn.getValue().toString().equals(MainActivity.getUserID()))
                                                                    {
                                                                        String keyUser = postsnapshotIn.getKey();
                                                                        if (keyUser != null) {
                                                                            mRef.child(keyUser).removeValue();
                                                                        }
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) { }
                                                        });

                                                        // ==========================Above  part ========================
                                                        //====================Remove usewr from Group ================================
                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                                    });
                            }
                        }
                        return false;
                    }
                });
                popupMenu.show();

            }
        });
        /* ========================Leave button work finished here================================== */


        /* ============================= Enter in the group started ===================*/
        holder.viewTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,GroupTimeLine.class);
                intent.putExtra("GroupID", list.get(position).getGroupID());
                intent.putExtra("GroupName", list.get(position).getName());
                context.startActivity(intent);
            }
        });

        /* ============================= Enter in the group finished ===================*/

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}