package com.example.mhealthapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private TextView userName, userEmail;
    private DatabaseReference ref;
    private String curUid;


//    GridView gridView;
//    String[] funcNames = {"Activity", "Heart Rate", "Steps", "Food&\nNutrition", "BMI", "Exercise", "Profile", "Reminders", "Sleep", "Settings", "About Us"};
//    int[] funcImg = {R.drawable.activity1, R.drawable.heartbeat, R.drawable.steps, R.drawable.mealplan, R.drawable.bmi, R.drawable.exercise, R.drawable.profile, R.drawable.notification
//            , R.drawable.sleep, R.drawable.settings, R.drawable.aboutus};

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);


        firebaseAuth = FirebaseAuth.getInstance();

        curUid = firebaseAuth.getCurrentUser().getUid();

        ref = FirebaseDatabase.getInstance().getReference().child(curUid);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       /* FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View navHeaderView = navigationView.getHeaderView(0);


        userName = (TextView) navHeaderView.findViewById(R.id.userNameTV);
        userEmail = (TextView) navHeaderView.findViewById(R.id.UserEmailTV);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("username").getValue().toString();
                    String email = dataSnapshot.child("useremail").getValue().toString();
                    userName.setText(name);
                    userEmail.setText(email);


                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("UserListActivity", "Error occured");
            }
        });

        // default fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flMain, new newHomeFragment());
        ft.commit();

        navigationView.setCheckedItem(R.id.nav_home);


    }


    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }



    public void Logout() {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(HomeActivity2.this, MainActivity.class));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.


        int id = item.getItemId();
        if (id == R.id.nav_home) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flMain, new HomeFragment());
            ft.commit();
        } else if (id == R.id.nav_profile) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flMain, new ProfileActivityFrag());
            ft.commit();
//            Toast.makeText(HomeActivity2.this, "Profile ", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(HomeActivity2.this, ProfileActivity.class));
        } else if (id == R.id.nav_HeartRateMenu) {
            Toast.makeText(HomeActivity2.this, "Heart Rate is clicked ", Toast.LENGTH_SHORT).show();


        } else if (id == R.id.nav_ActivityMenu) {
            Toast.makeText(HomeActivity2.this, "ActivityMenu is clicked ", Toast.LENGTH_SHORT).show();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flMain, new Steps());
            ft.commit();

        } else if (id == R.id.nav_BMIMenu) {
            Toast.makeText(HomeActivity2.this, "BMIMenu is clicked ", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_MealPlanMenu) {
            Toast.makeText(HomeActivity2.this, "MealPlanMenu is clicked ", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_ReminderMenu) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flMain, new ListViewFragment());
            ft.commit();
//            Toast.makeText(HomeActivity2.this, "ReminderMenu is clicked ", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_settings) {
            Toast.makeText(HomeActivity2.this, "settings is clicked ", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_logoutMenu) {
            Toast.makeText(HomeActivity2.this, "logout is clicked ", Toast.LENGTH_SHORT).show();
            Logout();
        }else if (id == R.id.nav_action_FAQ) {
            Toast.makeText(HomeActivity2.this, "FAQ is clicked ", Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
