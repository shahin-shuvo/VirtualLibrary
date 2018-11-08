package com.sasam.virtuallibrary;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
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
import com.sasam.virtuallibrary.Books.BookListActivity;
import com.sasam.virtuallibrary.ChatRoom.ChatListActivity;
import com.sasam.virtuallibrary.ChatRoom.data.FriendDB;
import com.sasam.virtuallibrary.ChatRoom.data.GroupDB;
import com.sasam.virtuallibrary.ChatRoom.service.ServiceUtils;
import com.sasam.virtuallibrary.CreateGroup.createGroup;
import com.sasam.virtuallibrary.Groups.GroupDetails;
import com.sasam.virtuallibrary.Groups.myGroupFragment;
import com.sasam.virtuallibrary.IndividualGroup.NewsFeed.models.User;
import com.sasam.virtuallibrary.IndividualGroup.NewsFeed.utils.FirebaseUtils;
import com.sasam.virtuallibrary.JoinGroup.join_group;
import com.sasam.virtuallibrary.UI.About;

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

    private BottomNavigationView bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupBottomNavigation();
        User user = new User();
        String photoUrl = null;


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
                View menuItemView = findViewById(R.id.join);
                PopupMenu popupMenu = new PopupMenu(this, menuItemView);
                popupMenu.setGravity(Gravity.END);
                popupMenu.inflate(R.menu.menu_list);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        {
                            int id = item.getItemId();
                            if(id ==  R.id.joinGroupMenu){
                                Intent intent = new Intent(getApplicationContext(), join_group.class);
                                startActivity(intent); }



                            if(id== R.id.createGroupMenu){
                                createGroup faid = new createGroup();
                                android.support.v4.app.FragmentTransaction fragmentTransaction =
                                        getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_container, faid);
                                fragmentTransaction.commit();}

                        }
                        return false;
                    }
                });
                popupMenu.show();
                return true;
            default:
                return true;
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
        else if (id == R.id.nav_notifications) {

        }
        else if (id == R.id.nav_settings) {

        }
        else if (id == R.id.nav_about) {
            Intent intentAbout = new Intent(MainActivity.this, About.class);
            startActivity(intentAbout);
        }
        else if (id == R.id.nav_privacy_policy) {

        }
        else if (id == R.id.nav_privacy_policy) {

        }
        else if (id == R.id.nav_share) {

        }
        else if(id==R.id.nav_message){
            Intent intent = new Intent(this, ChatListActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_about) {

        }
        else if(id==R.id.sign_out){
            FriendDB.getInstance(getApplicationContext()).dropDB();
            GroupDB.getInstance(getApplicationContext()).dropDB();
            ServiceUtils.stopServiceFriendChat(getApplicationContext().getApplicationContext(), true);
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
            User userApp = new User();
            String photoUrl = null;
            if (user.getPhotoUrl() != null) {
                userApp.setPhotoUrl(user.getPhotoUrl().toString());
            }

            userApp.setEmail(user.getEmail());
            userApp.setUser(user.getDisplayName());
            userApp.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
            FirebaseUtils.getUserRef(user.getEmail().replace(".", ",")).setValue(userApp);

        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();

                switch (item.getItemId()) {
                    case R.id.navigation_home:
//                        fragmentManager.beginTransaction()
//                                .replace(R.id.content_main, new HomeFragment())
//                                .commit();
                        return true;
                    case R.id.navigation_events:
//                        fragmentManager.beginTransaction()
//                                .replace(R.id.content_main, new EventFragment())
//                                .commit();
                        return true;
                    case R.id.navigation_chat:
                         Intent intent = new Intent(MainActivity.this, ChatListActivity.class);
                         startActivity(intent);
                        return true;
                    case R.id.navigation_books:
                        Intent intent2 = new Intent(MainActivity.this, BookListActivity.class);
                        startActivity(intent2);

                        return true;
                }
                return false;
            }

        };
        bottomNavigation = findViewById(R.id.navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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
