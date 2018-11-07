package com.sasam.virtuallibrary.IndividualGroup.NewsFeed;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.sasam.virtuallibrary.IndividualGroup.NewsFeed.models.Comment;
import com.sasam.virtuallibrary.IndividualGroup.NewsFeed.models.Post;
import com.sasam.virtuallibrary.IndividualGroup.NewsFeed.models.User;
import com.sasam.virtuallibrary.IndividualGroup.NewsFeed.utils.Constants;
import com.sasam.virtuallibrary.IndividualGroup.NewsFeed.utils.FirebaseUtils;
import com.sasam.virtuallibrary.R;
import com.sasam.virtuallibrary.Util.GlideApp;

public class PostCommentActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String BUNDLE_COMMENT = "comment";
    private Post mPost;
    private EditText mCommentEditTextView;
    private Comment mComment;
    private FirebaseUser firebaseUser;
    private String userID;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        if (savedInstanceState != null) {
            mComment = (Comment) savedInstanceState.getSerializable(BUNDLE_COMMENT);
        }
        this.setTitle("Comments");
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        mPost = (Post) intent.getSerializableExtra(Constants.EXTRA_POST);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();

        init();
        initPost();
        initCommentSection();
    }

    private void initCommentSection() {
        //final User user = new User();
        RecyclerView commentRecyclerView = (RecyclerView) findViewById(R.id.comment_recyclerview);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(PostCommentActivity.this));

        FirebaseRecyclerAdapter<Comment, CommentHolder> commentAdapter = new FirebaseRecyclerAdapter<Comment, CommentHolder>(
                Comment.class,
                R.layout.row_comment,
                CommentHolder.class,
                FirebaseUtils.getCommentRef(mPost.getPostId())
        ) {
            @Override
            protected void populateViewHolder(CommentHolder viewHolder, Comment model, int position) {

                viewHolder.setUsername(model.getUserName());   //gives error here *******************************************************************
                viewHolder.setComment(model.getComment());
                viewHolder.setTime(DateUtils.getRelativeTimeSpanString(model.getTimeCreated()));

                Glide.with(PostCommentActivity.this)
                        .load(model.getUser().getPhotoUrl())
                        .into(viewHolder.commentOwnerDisplay);
            }
        };

        commentRecyclerView.setAdapter(commentAdapter);
    }

    private void initPost() {
        ImageView postOwnerDisplayImageView = (ImageView) findViewById(R.id.iv_post_owner_display);
        TextView postOwnerUsernameTextView = (TextView) findViewById(R.id.tv_post_username);
        TextView postTimeCreatedTextView = (TextView) findViewById(R.id.tv_time);
        ImageView postDisplayImageView = (ImageView) findViewById(R.id.iv_post_display);
        LinearLayout postLikeLayout = (LinearLayout) findViewById(R.id.like_layout);
        LinearLayout postCommentLayout = (LinearLayout) findViewById(R.id.comment_layout);
        TextView postNumLikesTextView = (TextView) findViewById(R.id.tv_likes);
        TextView postNumCommentsTextView = (TextView) findViewById(R.id.tv_comments);
        TextView postTextTextView = (TextView) findViewById(R.id.tv_post_text);

        postOwnerUsernameTextView.setText(mPost.getUser().getUser());
        postTimeCreatedTextView.setText(DateUtils.getRelativeTimeSpanString(mPost.getTimeCreated()));
        postTextTextView.setText(mPost.getPostText());
        postNumLikesTextView.setText(String.valueOf(mPost.getNumLikes()));
        postNumCommentsTextView.setText(String.valueOf(mPost.getNumComments()));

        Glide.with(PostCommentActivity.this)
                .load(mPost.getUser().getPhotoUrl())
                .into(postOwnerDisplayImageView);

        if (mPost.getPostImageUrl() != null) {
            postDisplayImageView.setVisibility(View.VISIBLE);
           // StorageReference storageReference = FirebaseStorage.getInstance().getReference();

            GlideApp.with(PostCommentActivity.this)
                    .load(mPost.getPostImageUrl())
                    .apply(new RequestOptions().override(100, 100))
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(postDisplayImageView);
        } else {
            postDisplayImageView.setImageBitmap(null);
            postDisplayImageView.setVisibility(View.GONE);
        }
    }

    private void init() {
        mCommentEditTextView = (EditText) findViewById(R.id.et_comment);
        findViewById(R.id.iv_send).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_send: {
                sendComment();
                mCommentEditTextView.setText("");
            }
        }
    }

    private void sendComment() {
        final ProgressDialog progressDialog = new ProgressDialog(PostCommentActivity.this);

        progressDialog.setMessage("অপেক্ষা করুন, আপনার মন্তব্য পাঠানো হচ্ছে.....");
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        mComment = new Comment();
        final String uid = FirebaseUtils.getUid();
        String strComment = mCommentEditTextView.getText().toString();
        userName = FirebaseUtils.getCurrentUser().getDisplayName();

        mComment.setCommentId(uid);
        mComment.setComment(strComment);
        mComment.setTimeCreated(System.currentTimeMillis());
        mComment.setUserID(userID);
        mComment.setUserName(userName);

        FirebaseUtils.getUserRef(FirebaseUtils.getCurrentUser().getEmail().replace(".", ","))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        FirebaseUtils.getCommentRef(mPost.getPostId())
                                .child(uid)
                                .setValue(mComment);

                        FirebaseUtils.getPostRef().child(mPost.getPostId())
                                .child(Constants.NUM_COMMENTS_KEY)
                                .runTransaction(new Transaction.Handler() {
                                    @Override
                                    public Transaction.Result doTransaction(MutableData mutableData) {
                                        long num = (long) mutableData.getValue();
                                        mutableData.setValue(num + 1);
                                        return Transaction.success(mutableData);
                                    }

                                    @Override
                                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                        progressDialog.dismiss();
                                        FirebaseUtils.addToMyRecord(Constants.COMMENTS_KEY, uid);
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        progressDialog.dismiss();
                    }
                });
    }


    public static class CommentHolder extends RecyclerView.ViewHolder {
        ImageView commentOwnerDisplay;
        TextView usernameTextView;
        TextView timeTextView;
        TextView commentTextView;

        public CommentHolder(View itemView) {
            super(itemView);
            commentOwnerDisplay = (ImageView) itemView.findViewById(R.id.iv_comment_owner_display);
            usernameTextView = (TextView) itemView.findViewById(R.id.tv_username);
            timeTextView = (TextView) itemView.findViewById(R.id.tv_time);
            commentTextView = (TextView) itemView.findViewById(R.id.tv_comment);
        }

        public void setUsername(String username) {
            usernameTextView.setText(username);
        }

        public void setTime(CharSequence time) {
            timeTextView.setText(time);
        }

        public void setComment(String comment) {
            commentTextView.setText(comment);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return true;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(BUNDLE_COMMENT, mComment);
        super.onSaveInstanceState(outState);
    }
}

