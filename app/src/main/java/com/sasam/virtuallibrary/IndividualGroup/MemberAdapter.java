package com.sasam.virtuallibrary.IndividualGroup;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.sasam.virtuallibrary.MainActivity;
import com.sasam.virtuallibrary.R;

import java.util.ArrayList;
import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    private List<MemberClass> list;
    public List<String> GroupID = new ArrayList<>();
    private Context context;
    private DatabaseReference mDatabase ,mDatabaseCount,mRef;
    private FirebaseAuth mAuth;
    public static  int position;
    final  String userId = MainActivity.getUserID();
    public MemberAdapter(List<MemberClass> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView mail,admin,name;
        public RatingBar ratingBar;
        public TextView   grpCode;
        public ImageButton delete,edit;
        public Button viewProfile;
        public CardView cv;
        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            name = (TextView) itemView.findViewById(R.id.nameUser);
            mail = (TextView) itemView.findViewById(R.id.mailUser);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rating);
          //  viewProfile = (Button) itemView.findViewById(R.id.viewProfile) ;

            cv=(CardView) itemView.findViewById(R.id.card_view_people);

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
                .inflate(R.layout.group_card_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final MemberClass temp=list.get(position);
        holder.name.setText(temp.getName());
        holder.mail.setText(temp.getMail());
        holder.ratingBar.setRating(temp.getRating());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
