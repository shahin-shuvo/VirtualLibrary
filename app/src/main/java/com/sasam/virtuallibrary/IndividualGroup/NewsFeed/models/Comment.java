package com.sasam.virtuallibrary.IndividualGroup.NewsFeed.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

/**
 * Created by pranto on 11/3/17.
 */

public class Comment implements Serializable {
    private FirebaseUser firebaseUser;
    private User user;
    private String commentId;
    private long timeCreated;
    private String comment;
    private String userID;
    private String userName;


    public Comment() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        user = new User(firebaseUser.getDisplayName(), firebaseUser.getEmail(), firebaseUser.getPhotoUrl().toString(), firebaseUser.getUid());
        //user = new User(account.getDisplayName(), account.getEmail(), account.getPhotoUrl().toString(), account.getId());
    }

    public Comment(User user, String commentId, long timeCreated, String comment, String userID, String userName) {

        this.user = user;
        this.commentId = commentId;
        this.timeCreated = timeCreated;
        this.comment = comment;
        this.userID = userID;
        this.userName = userName;
    }

    public void setUser(User user)
    {
        this.user = user;
        //System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^"+account.getDisplayName());
    }

    public User getUser() {
        return user;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setUserID(String userID)
    {
        this.userID = userID;
    }

    public String getUserID()
    {
        return userID;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return userName;
    }
}