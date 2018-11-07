package com.sasam.virtuallibrary.Authentication;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sasam.virtuallibrary.ChatRoom.patterns.BitmapToStringAdapter;
import com.sasam.virtuallibrary.ChatRoom.data.SharedPreferenceHelper;
import com.sasam.virtuallibrary.ChatRoom.data.StaticConfig;
import com.sasam.virtuallibrary.MainActivity;
import com.sasam.virtuallibrary.Profile.User;
import com.sasam.virtuallibrary.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static com.sasam.virtuallibrary.Util.LoggerTags.AUTHENTIC;

public class AuthActivity extends AppCompatActivity {

    public static final int RC_SIGN_IN = 1000;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();
        //getFacebookKeyHash();

        Log.d(AUTHENTIC ,"form auth");
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    StaticConfig.UID=user.getUid();
                    // Toast.makeText(AuthActivity.this, "You're now signed in. Welcome", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AuthActivity.this,MainActivity.class));
                    finish();
                } else {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setLogo(R.drawable.app_logo)
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(),
                                                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                                                    new AuthUI.IdpConfig.FacebookBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                final FirebaseUser firebaseUser=mFirebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            Log.d(AUTHENTIC,firebaseUser.getDisplayName()+firebaseUser.getEmail()+firebaseUser.getUid()
                    +"  "+firebaseUser.getMetadata()+" "+firebaseUser.getProviderData()+" "+firebaseUser.getProviderId());
            User user=new User(firebaseUser.getDisplayName(),firebaseUser.getEmail(),firebaseUser.getUid());
            DatabaseReference db=FirebaseConnection.databaseRef("UserInfo");
            db.child(user.getUid()).setValue(user);


            final com.sasam.virtuallibrary.ChatRoom.model.User user1=new com.sasam.virtuallibrary.ChatRoom.model.User();
            user1.email=firebaseUser.getEmail();
            user1.name=firebaseUser.getDisplayName();


            if(firebaseUser.getPhotoUrl()==null){
                //Log.d("authentic","null found");
                user1.avata="default";
                addNewuser(user1,firebaseUser);

            }
            else{
                //Log.d("authentic","not found");
                Glide.with(this)
                        .asBitmap()
                        .load(firebaseUser.getPhotoUrl())
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap imgBitmap, Transition<? super Bitmap> transition) {
                                //imageView.setImageBitmap(resource);

                                String imageBase64 = new BitmapToStringAdapter(imgBitmap).getStringBase64();


                                if(imageBase64.length()>0){
                                    user1.avata=imageBase64;
                                }else{
                                    user1.avata="default";
                                }

                                //user1.group.add()

                                addNewuser(user1,firebaseUser);

                                SharedPreferenceHelper preferenceHelper = SharedPreferenceHelper.getInstance(getApplicationContext());
                                preferenceHelper.saveUserInfo(user1);

                            }
                        });
            }

            SharedPreferenceHelper preferenceHelper = SharedPreferenceHelper.getInstance(getApplicationContext());
            preferenceHelper.saveUserInfo(user1);
            //Log.d("authentic",firebaseUser.getPhotoUrl().toString());


        }



                // Sign-in succeeded, set up the UI
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    void addNewuser(final com.sasam.virtuallibrary.ChatRoom.model.User user1, final FirebaseUser firebaseUser){

        DatabaseReference db=FirebaseConnection.databaseRef("user");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(firebaseUser.getUid())){
                    Log.d(AUTHENTIC,"no child exist");
                    DatabaseReference db2=FirebaseConnection.databaseRef("user");
                    db2.child(firebaseUser.getUid()).setValue(user1);
                }else{
                    Log.d("authentic","email");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
    public void getFacebookKeyHash(){
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.tsm.way.ui.common.activities", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {     }
    }



}
