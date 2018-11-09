package com.sasam.virtuallibrary.IndividualGroup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.sasam.virtuallibrary.GroupLibrary.LibraryFragment;
import com.sasam.virtuallibrary.IndividualGroup.NewsFeed.GroupNewsFeed;
import com.sasam.virtuallibrary.R;

public class GroupTimeLine extends AppCompatActivity {
    BottomBar bottombar;
    Context context;
    String groupId,groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        groupId = intent.getExtras().getString("GroupID");
        groupName = intent.getExtras().getString("GroupName");
        this.setTitle(String.format(groupName,"GROUP"));

        setContentView(R.layout.activity_group_time_line);

        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        bottombar = findViewById(R.id.bottombarTimeline);
        bottombar = BottomBar.attach(this,savedInstanceState);
        bottombar.setActiveTabColor("#B03A2E");

        bottombar.setItemsFromMenu(R.menu.bottommenu_group, new OnMenuTabClickListener() {


            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if(menuItemId == R.id.timeline)
                {
                    GroupNewsFeed myFragment = new   GroupNewsFeed ();
                    Bundle b = new Bundle();
                    b.putString("groupID", groupId);
                    myFragment .setArguments(b);
                    //THEN NOW SHOW OUR FRAGMENT
                    android.support.v4.app.FragmentTransaction fragmentTransaction =
                            getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.bottomTabGroupPage, myFragment);
                    fragmentTransaction.commit();
                    // bottombar.getBar().setBackgroundColor(getResources().getColor(R.color.tab2));
                  //  bottombar.setActiveTabColor("#154360");

                }
                else if(menuItemId == R.id.peoples)
                {
                   sendData();


                }
                else if(menuItemId == R.id.allbooks){

                    LibraryFragment fragment = new LibraryFragment ();
                    Bundle bundle = new Bundle();
                    bundle.putString("groupID", groupId);
                    fragment .setArguments(bundle);
                    //THEN NOW SHOW OUR FRAGMENT
                    android.support.v4.app.FragmentTransaction fragmentTransaction =
                            getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.bottomTabGroupPage, fragment);
                    fragmentTransaction.commit();

                }



            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

            }
        });

    }
    

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return true;
    }


    private void sendData() {
        //PACK DATA IN A BUNDLE

        //PASS OVER THE BUNDLE TO OUR FRAGMENT
        groupPeoples myFragment = new   groupPeoples ();
        Bundle b = new Bundle();
        b.putString("groupID", groupId);
        myFragment .setArguments(b);
        //THEN NOW SHOW OUR FRAGMENT
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.bottomTabGroupPage, myFragment);
        fragmentTransaction.commit();

    }
}
