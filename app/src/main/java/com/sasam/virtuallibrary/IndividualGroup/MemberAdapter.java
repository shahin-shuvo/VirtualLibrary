package com.sasam.virtuallibrary.IndividualGroup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sasam.virtuallibrary.MainActivity;
import com.sasam.virtuallibrary.R;

import java.util.ArrayList;
import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    private List<MemberClass> list;
    public List<String> GroupID = new ArrayList<>();
    private Context context;
    private DatabaseReference mDatabase ,mDatabaseCount,mRef;
    public Float newGiveRating = Float.valueOf(2);
    private FirebaseAuth mAuth;
    public  Float newRating;
    public static  int position;
    final  String userId = MainActivity.getUserID();
    public MemberAdapter(List<MemberClass> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView mail,admin,name;
        public ImageButton giveRating;
        public RatingBar ratingBar;
        public TextView   grpCode;

        public ImageButton delete,edit;
        public PopupWindow mPopupWindow;
        public Button viewProfile;
        public CardView cv;
        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            name = (TextView) itemView.findViewById(R.id.nameUser);
            mail = (TextView) itemView.findViewById(R.id.mailUser);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rating);
            giveRating =  (ImageButton) itemView.findViewById(R.id.ratingButton) ;


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




        holder.giveRating.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(final View view) {
                final Dialog myDialog = new Dialog(context);
                myDialog.setContentView(R.layout.custom_popup);
                final PopupMenu popupMenu = new PopupMenu(context,holder.giveRating);

                popupMenu.getMenuInflater().inflate(R.menu.rating_menu,popupMenu.getMenu());
                popupMenu.setGravity(Gravity.END);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        {
                            switch (item.getItemId()){
                                case R.id.ratingIcon:
                                    final String uId = list.get(position).getUid();

                                    ImageButton btnClose = (ImageButton)myDialog.findViewById(R.id.closePopup);
                                    Button btnSend = (Button)myDialog.findViewById(R.id.btnsend);

                                    RatingBar ratingBar = (RatingBar)myDialog.findViewById(R.id.ratingBarCustom);


                                    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

                                        public void onRatingChanged(RatingBar ratingBar, float rating,	boolean fromUser) {

                                            newGiveRating = rating;

                                        }
                                    });
                                    btnClose.setOnClickListener(new Button.OnClickListener(){

                                        @Override
                                        public void onClick(View v) {
                                            myDialog.dismiss();
                                        }});
                                    btnSend.setOnClickListener(new Button.OnClickListener(){

                                        @Override
                                        public void onClick(View v) {
                                            calculateRating(uId ,new MyCallback() {
                                                @Override
                                                public void onCallback(Float  list) {
                                                    MainActivity.Connection("UserInfo").child(uId).child("rating").setValue(newRating);
                                                }
                                            });
                                            myDialog.dismiss();
                                        }});

                                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.rgb(23, 32, 42)));
                                    myDialog.show();




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


    /*=============================== For rainf calculation =============================*/
    private void calculateRating(String uId, final MyCallback myCallback) {

        mRef = MainActivity.Connection("UserInfo").child(uId);
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Float curRating = dataSnapshot.child("rating").getValue(Float .class);
                newRating = (curRating + newGiveRating)/2;
                myCallback.onCallback(newRating);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });


    }



    public interface MyCallback {
        void onCallback(Float list);
    }


}
