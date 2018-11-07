package com.sasam.virtuallibrary.IndividualGroup.NewsFeed.models;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

/**
 * Created by shuvo on 11/3/18.
 */

public class Post implements Serializable {
    private User user;
    private String postText;
    private String postImageUrl;
    private String postId;
    private long numLikes;
    private long numComments;
    private long timeCreated;
    private FirebaseUser firebaseUser;

    public Post() {

        //firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //user = new User(firebaseUser.getDisplayName(), firebaseUser.getEmail(), firebaseUser.getPhotoUrl().toString(), firebaseUser.getUid());
        //user = new User(account.getDisplayName(), account.getEmail(), account.getPhotoUrl().toString(), account.getId());

    }

    public Post(User user, String postText, String postImageUrl, String postId, long numLikes, long numComments, long timeCreated) {

        this.user = user;
        this.postText = postText;
        this.postImageUrl = postImageUrl;
        this.postId = postId;
        this.numLikes = numLikes;
        this.numComments = numComments;
        this.timeCreated = timeCreated;
    }

    public User getUser() {

        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public long getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(long numLikes) {
        this.numLikes = numLikes;
    }

    public long getNumComments() {
        return numComments;
    }

    public void setNumComments(long numComments) {
        this.numComments = numComments;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }
}
