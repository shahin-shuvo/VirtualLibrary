package com.sasam.virtuallibrary.IndividualGroup.NewsFeed.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sasam.virtuallibrary.IndividualGroup.NewsFeed.GroupNewsFeed;
import com.sasam.virtuallibrary.IndividualGroup.NewsFeed.models.Post;
import com.sasam.virtuallibrary.IndividualGroup.NewsFeed.models.User;
import com.sasam.virtuallibrary.IndividualGroup.NewsFeed.utils.Constants;
import com.sasam.virtuallibrary.IndividualGroup.NewsFeed.utils.FirebaseUtils;
import com.sasam.virtuallibrary.MainActivity;
import com.sasam.virtuallibrary.R;
import com.sasam.virtuallibrary.Util.GlideApp;

import java.util.UUID;

import static android.app.Activity.RESULT_OK;

/**
 * Created by brad on 2017/02/05.
 */

public class PostCreateDialog extends DialogFragment implements View.OnClickListener {
    private static final int RC_PHOTO_PICKER = 1;
    private Post mPost;
    private ProgressDialog mProgressDialog;
    private Uri mSelectedUri;
    private ImageView mPostDisplay,close;
    private View mRootView;
    String downloadUrl="-1";
    private StorageReference storageReference;
    private FirebaseStorage storage;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mPost = new Post();
        mProgressDialog = new ProgressDialog(getContext());

        mRootView = getActivity().getLayoutInflater().inflate(R.layout.create_post_dialog, null);
        mPostDisplay = (ImageView) mRootView.findViewById(R.id.post_dialog_display);
        close = (ImageView) mRootView.findViewById(R.id.closePostTab);
        mRootView.findViewById(R.id.post_dialog_send_imageview).setOnClickListener(this);
        mRootView.findViewById(R.id.post_dialog_select_imageview).setOnClickListener(this);
        builder.setView(mRootView);
        close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               dismiss(); }
        });

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.post_dialog_send_imageview:
                sendPost();
                break;
            case R.id.post_dialog_select_imageview:
                selectImage();
                break;
        }
    }

    private void sendPost() {
        mProgressDialog.setMessage("Sending post...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

        FirebaseUtils.getUserRef(FirebaseUtils.getCurrentUser().getEmail().replace(".", ","))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        final String postId = FirebaseUtils.getUid();
                        TextView postDialogTextView = (TextView) mRootView.findViewById(R.id.post_dialog_edittext);
                        String text = postDialogTextView.getText().toString();

                        mPost.setUser(user);
                        mPost.setNumComments(0);
                        mPost.setNumLikes(0);
                        mPost.setTimeCreated(System.currentTimeMillis());
                        mPost.setPostId(postId);
                        mPost.setPostText(text);

                        if (mSelectedUri != null) {

                            uploadImage(postId,new MyCallback() {
                                @Override
                                public void onCallback(final String  list) {
                                   // mPost.setPostImageUrl(downloadUrl);
                                   // addToMyPostList(postId);

                                }
                            });
                        } else {
                            addToMyPostList(postId);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        mProgressDialog.dismiss();
                    }
                });
    }

    private void addToMyPostList(String postId) {
        FirebaseUtils.getPostRef().child(postId)
                .setValue(mPost);
        FirebaseUtils.getMyPostRef().child(postId).setValue(true)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mProgressDialog.dismiss();
                        dismiss();
                    }
                });

        FirebaseUtils.addToMyRecord(Constants.POST_KEY, postId);

        MainActivity.Connection("Groups").child(GroupNewsFeed.getGroupId()).child("thisGroupPost").push().setValue(postId);
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER) {
            if (resultCode == RESULT_OK) {
                mSelectedUri = data.getData();
                GlideApp.with(getContext()).load(mSelectedUri).into(mPostDisplay);
                mPostDisplay.setImageURI(mSelectedUri);
            }
        }
    }

    private void uploadImage(final String postId, final MyCallback myCallback) {

        if(mSelectedUri != null)
        {

            final StorageReference ref = storageReference.child("post_images/"+ UUID.randomUUID().toString());


            ref.putFile(mSelectedUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadUrl = uri.toString();
                            mPost.setPostImageUrl(downloadUrl);
                            addToMyPostList(postId);
                          //  System.out.println("####################"+mPost.getPostImageUrl());
                          //  progressDialog.dismiss();

                        }
                    });
                    myCallback.onCallback(downloadUrl);
                }
            }) .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                  //  progressDialog.dismiss();
                    Toast.makeText(getContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }


    }




}

interface MyCallback {
    void onCallback(String  list);
}
