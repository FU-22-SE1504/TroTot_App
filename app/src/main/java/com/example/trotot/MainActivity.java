package com.example.trotot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trotot.Database.ConnectDatabase;
import com.example.trotot.Fragment.CustomerFragment;
import com.example.trotot.Fragment.EditProfileFragment;
import com.example.trotot.Fragment.HomeFragment;
import com.example.trotot.Fragment.HouseholderFragment;
import com.example.trotot.Fragment.PostFragment;
import com.example.trotot.Fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // Navigation bar
    BottomNavigationView navigationView;

    //Toolbar
    Toolbar toolbar;
    TextView toolbarTitle;

    //Logout
    ImageView btnLogout;


    //Session
    SharedPreferences prefs;
    public static final String PREFERENCE_NAME = "PREFERENCE_DATA";
    int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set session
        prefs = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

        // Check connect internet
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbar = (Toolbar) findViewById(R.id.toolbarCustom);
        setSupportActionBar(toolbar);

        // Hide status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Binding view
        navigationView = findViewById(R.id.bottom_navigation);
        btnLogout = findViewById(R.id.btn_Logout);

        // Get user id form session
        user_id = prefs.getInt("user_id", 0);

        // Set visible button logout
        if(user_id == 0){
            btnLogout.setVisibility(View.INVISIBLE);
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.edit().clear().apply();
                startActivity(new Intent(MainActivity.this,MainActivity .class));
                btnLogout.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();
            }
        });

        // Set home fragment is default page
        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new HomeFragment()).commit();
        // Set default select item
        navigationView.setSelectedItemId(R.id.nav_home);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                String headerTitle = "Home Page";
                Intent intent = null;
                int user_id = prefs.getInt("user_id", 0);
                String selectedItem = null;
                Fragment fragment = new HomeFragment();
                // Pass user id to edit profile screen
                Bundle bundle = new Bundle();
                bundle.putInt("user_id", user_id);
                switch (item.getItemId()){
                    case R.id.nav_customer:
                        headerTitle = "Customer Page";
                        fragment = new CustomerFragment();
                        break;
                    case R.id.nav_householder:
                        headerTitle = "Householder Page";
                        fragment = new HouseholderFragment();
                        break;
                    case R.id.nav_home:
                        headerTitle = "Home Page";
                        fragment = new HomeFragment();
                        break;
                    case R.id.nav_addpost:
                        if(user_id == 0){
                            startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        }else{
                            headerTitle = "Create New Post";
                            fragment = new PostFragment();
                        }
                        break;
                    default:
                        if(user_id == 0){
                            startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        }else{
                            headerTitle = "User Profile";
                            fragment = new ProfileFragment();
                        }
                        break;
                }
                toolbarTitle.setText(headerTitle);
                // Change activity
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fragment).commit();
                return true;
            }
        });

    }


}