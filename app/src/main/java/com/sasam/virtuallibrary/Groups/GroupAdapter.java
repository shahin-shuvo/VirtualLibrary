package com.sasam.virtuallibrary.Groups;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sasam.virtuallibrary.R;

import java.util.List;

/**
 * Created by shuvo on 10/17/17.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private List<GroupDetails> list;
    private Context context;
    private DatabaseReference mDatabase ,mDatabaseCount;
    private FirebaseAuth mAuth;
    public static  int position;
    public GroupAdapter(List<GroupDetails> list) {
        this.list = list;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView text1,admin,text3,name;
        public TextView   grpCode;
        public ImageButton delete,arrow,edit;
        public CardView cv;
        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            name = (TextView) itemView.findViewById(R.id.nameHeader);
            grpCode = (TextView) itemView.findViewById(R.id.grpCode);
           // disableEditText(grpCode);

            admin=(TextView) itemView.findViewById(R.id.admin);
            delete = (ImageButton) itemView.findViewById(R.id.delete_cvp) ;
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

//        Toast.makeText(context.getApplicationContext(), (CharSequence) temp.getMembers().get(0), Toast.LENGTH_SHORT).show();
        holder.grpCode.setText(temp.getCode());


        mAuth = FirebaseAuth.getInstance();
        String userID =  mAuth.getCurrentUser().getUid();

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase = FirebaseDatabase.getInstance().getReference().child("Events");

                mAuth = FirebaseAuth.getInstance();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Log.d("none",userId);
                final String openerID = mAuth.getCurrentUser().getUid();

               // mDatabase.child(userId).child(eventID).removeValue();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }
}