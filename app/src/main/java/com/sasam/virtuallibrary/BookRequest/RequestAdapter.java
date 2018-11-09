package com.sasam.virtuallibrary.BookRequest;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.sasam.virtuallibrary.MainActivity;
import com.sasam.virtuallibrary.R;

import java.util.ArrayList;
import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private List<BookRequestData> list;
    public List<String> GroupID = new ArrayList<>();
    private Context context;
    private DatabaseReference mDatabase ,mDatabaseCount,mRef;
    private FirebaseAuth mAuth;
    public static  int position;
    final  String userId = MainActivity.getUserID();
    public RequestAdapter(List<BookRequestData> list) {
        this.list = list;

    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView sender,book;

        public ImageButton accept;
        public CardView cv;
        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            sender=(TextView) itemView.findViewById(R.id.reqSender);
            book=(TextView) itemView.findViewById(R.id.reqBook);
            accept= (ImageButton) itemView.findViewById(R.id.acptButton);

            cv=(CardView) itemView.findViewById(R.id.cardReq);
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
    public RequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_req_card, parent, false);
        RequestAdapter.ViewHolder viewHolder = new RequestAdapter.ViewHolder(v);
       return viewHolder;
    }



    @Override
    public void onBindViewHolder(final RequestAdapter.ViewHolder holder, final int position) {

        final BookRequestData temp=list.get(position);
        holder.sender.setText(temp.getReqUserName());
        holder.book.setText(temp.getReqBookName());

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popupMenu = new PopupMenu(context,holder.accept);
                popupMenu.getMenuInflater().inflate(R.menu.accept_menu,popupMenu.getMenu());
                popupMenu.setGravity(Gravity.END);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        {
                            switch (item.getItemId()){
                                case R.id.confirmAccept:
                                    MainActivity.Connection("Books").child(temp.getReqBookId()).
                                            child("currentOwner").setValue("+1");
                                    Toast.makeText(context, "Request Accepted",Toast.LENGTH_SHORT).show();


                            }
                        }
                        return false;
                    }
                });
                popupMenu.show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
