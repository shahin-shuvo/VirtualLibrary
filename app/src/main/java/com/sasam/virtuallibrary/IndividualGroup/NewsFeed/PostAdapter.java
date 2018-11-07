package com.sasam.virtuallibrary.IndividualGroup.NewsFeed;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.sasam.virtuallibrary.IndividualGroup.NewsFeed.models.Post;
import com.sasam.virtuallibrary.IndividualGroup.NewsFeed.utils.Constants;
import com.sasam.virtuallibrary.IndividualGroup.NewsFeed.utils.FirebaseUtils;
import com.sasam.virtuallibrary.MainActivity;
import com.sasam.virtuallibrary.R;
import com.sasam.virtuallibrary.Util.GlideApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shuvo on 10/17/17.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private List<Post> list;
    public List<String> GroupID = new ArrayList<>();
    private Context context;
    private DatabaseReference mDatabase ,mDatabaseCount,mRef;
    private FirebaseAuth mAuth;
    public static  int position;
    final  String userId = MainActivity.getUserID();
    public PostAdapter(List<Post> list, Context context) {
        this.list = list;

        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView postOwnerDisplayImageView;
        TextView postOwnerUsernameTextView;
        TextView postTimeCreatedTextView;
        ImageView postDisplayImageVIew;
        TextView postTextTextView;
        LinearLayout postLikeLayout;
        LinearLayout postCommentLayout;
        TextView postNumLikesTextView;
        TextView postNumCommentsTextView;
        public TextView text1,admin,text3,name;
        public TextView   grpCode;
        public ImageButton delete,edit;
        public Button viewTimeline;
        public CardView cv;
        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            postOwnerDisplayImageView = (ImageView) itemView.findViewById(R.id.iv_post_owner_display);
            postOwnerUsernameTextView = (TextView) itemView.findViewById(R.id.tv_post_username);
            postTimeCreatedTextView = (TextView) itemView.findViewById(R.id.tv_time);
            postDisplayImageVIew = (ImageView) itemView.findViewById(R.id.iv_post_display);
            postLikeLayout = (LinearLayout) itemView.findViewById(R.id.like_layout);
            postCommentLayout = (LinearLayout) itemView.findViewById(R.id.comment_layout);
            postNumLikesTextView = (TextView) itemView.findViewById(R.id.tv_likes);
            postNumCommentsTextView = (TextView) itemView.findViewById(R.id.tv_comments);
            postTextTextView = (TextView) itemView.findViewById(R.id.tv_post_text);
            cv=(CardView) itemView.findViewById(R.id.cardPost);
            //  edit = itemView.findViewById(R.id.edit_cvp);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = getAdapterPosition();

                }
            });

        }
        public void setUsername(String username) {
            postOwnerUsernameTextView.setText(username);
        }

        public void setTIme(CharSequence time) {
            postTimeCreatedTextView.setText(time);
        }

        public void setNumLikes(String numLikes) {
            postNumLikesTextView.setText(numLikes);
        }

        public void setNumCOmments(String numComments) {
            postNumCommentsTextView.setText(numComments);
        }

        public void setPostText(String text) {
            postTextTextView.setText(text);
        }

    }


    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_post, parent, false);
        PostAdapter.ViewHolder viewHolder = new PostAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,  final int position ) {
        final Post model=list.get(position);
        viewHolder.setNumCOmments(String.valueOf(model.getNumComments()));
        viewHolder.setNumLikes(String.valueOf(model.getNumLikes()));
        viewHolder.setTIme(DateUtils.getRelativeTimeSpanString(model.getTimeCreated()));
        viewHolder.setUsername(model.getUser().getUser());
        viewHolder.setPostText(model.getPostText());

        GlideApp.with(context)
                .load(model.getUser().getPhotoUrl())
                .into(viewHolder.postOwnerDisplayImageView);

        if (model.getPostImageUrl() != null) {
            //Toast.makeText(context,model.getPostImageUrl(),Toast.LENGTH_SHORT).show();
            viewHolder.postDisplayImageVIew.setVisibility(View.VISIBLE);
            //StorageReference storageReference = FirebaseStorage.getInstance()
             //       .getReference(model.getPostImageUrl());
            GlideApp.with(this.context).load(model.getPostImageUrl())
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(viewHolder.postDisplayImageVIew);
        } else {
            viewHolder.postDisplayImageVIew.setImageBitmap(null);
            viewHolder.postDisplayImageVIew.setVisibility(View.GONE);
        }

        viewHolder.postLikeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLikeClick(model.getPostId());
            }
        });

        viewHolder.postCommentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostCommentActivity.class);
                intent.putExtra(Constants.EXTRA_POST, model);
                context.startActivity(intent);
            }
        });


    }

    private void onLikeClick(final String postId) {
        FirebaseUtils.getPostLikedRef(postId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            //User liked
                            FirebaseUtils.getPostRef()
                                    .child(postId)
                                    .child(Constants.NUM_LIKES_KEY)
                                    .runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData mutableData) {
                                            long num = (long) mutableData.getValue();
                                            mutableData.setValue(num - 1);
                                            return Transaction.success(mutableData);
                                        }

                                        @Override
                                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                            FirebaseUtils.getPostLikedRef(postId)
                                                    .setValue(null);
                                        }
                                    });
                        } else {
                            FirebaseUtils.getPostRef()
                                    .child(postId)
                                    .child(Constants.NUM_LIKES_KEY)
                                    .runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData mutableData) {
                                            long num = (long) mutableData.getValue();
                                            mutableData.setValue(num + 1);
                                            return Transaction.success(mutableData);
                                        }

                                        @Override
                                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                            FirebaseUtils.getPostLikedRef(postId)
                                                    .setValue(true);
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }






    @Override
    public int getItemCount() {
        return list.size();
    }




}