package com.sasam.virtuallibrary;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sasam.virtuallibrary.Authentication.AuthActivity;
import com.sasam.virtuallibrary.CreateGroup.createGroup;
import com.sasam.virtuallibrary.Groups.GroupDetails;
import com.sasam.virtuallibrary.Groups.myGroupFragment;
import com.sasam.virtuallibrary.JoinGroup.join_group;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Button create,cancel;
    public EditText groupName,groupCode;
    public String CurrentUser ;
    public  Dialog dialog1;
    //add Fir ebase Database stuff
    public DatabaseReference mDatabase, status;
    private DatabaseReference  groupDatabase;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;



    List<GroupDetails> listMyGroup = new ArrayList<GroupDetails>();
    List<String> myGroupList =  new ArrayList<>();
    ArrayList<String> listName = new ArrayList<>();



    private static DatabaseReference myRef;
    private static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        loadNavigationDrawerHeader(navigationView.getHeaderView(0));

        loadMyGroupPage();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.main, menu);
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.join:
                Intent intent = new Intent(this, join_group.class);
                this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_groups) {
                loadMyGroupPage();
        }
        else if (id == R.id.nav_group_create) {
            createGroup faid = new   createGroup();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, faid);
            fragmentTransaction.commit();
        }
        else if (id == R.id.nav_join_group) {

        }
        else if (id == R.id.nav_notifications) {

        }
        else if (id == R.id.nav_settings) {

        }
        else if (id == R.id.nav_about) {

        }
        else if (id == R.id.nav_privacy_policy) {

        }
        else if (id == R.id.nav_privacy_policy) {

        }
        else if (id == R.id.nav_share) {

        }
        else if (id == R.id.nav_about) {

        }
        else if(id==R.id.sign_out){
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // user is now signed out
                            startActivity(new Intent(MainActivity.this, AuthActivity.class));
                            finish();
                        }
                    });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadNavigationDrawerHeader(View header) {
        TextView userNameTextViewNav = header.findViewById(R.id.current_user_name);
        TextView userEmailTextViewNav = header.findViewById(R.id.user_email);
        ImageView profile_imageView = header.findViewById(R.id.profile_imageView);

        if (user != null) {
            userNameTextViewNav.setText(user.getDisplayName());
            userEmailTextViewNav.setText(user.getEmail());
            Glide.with(this).load(user.getPhotoUrl()).into(profile_imageView);

        }
    }


    public static DatabaseReference Connection(String ref){
         FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
         myRef = mFirebaseDatabase.getReference(ref);
         return myRef;
    }

    public static String getUserName() {

        if (user != null) {
            return user.getDisplayName();
        }
        return null;
    }

    public static String getUserID()
    {
        String userId = "";
        if (user != null) {
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        return  userId;
    }

    public void loadMyGroupPage() {

        myGroupFragment faid = new  myGroupFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, faid);
        fragmentTransaction.commit();
    }



}
