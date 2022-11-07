package com.example.trotot.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.trotot.Adapter.ProfileViewAdapter;
import com.example.trotot.Database.ConnectDatabase;
import com.example.trotot.Model.User;
import com.example.trotot.Adapter.PostViewAdapter;
import com.example.trotot.R;
import com.google.android.material.tabs.TabLayout;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    // View
    User user;
    View view;
    CircleImageView avatarImg;
    TextView txtUsername, txtEmail;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    Bitmap bitmap;
    ProfileViewAdapter postViewAdapter;

    //Session
    SharedPreferences prefs;
    public static final String PREFERENCE_NAME = "PREFERENCE_DATA";
    Integer user_id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            user = (User) bundle.get("User_Info");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Get id by session
        prefs = this.getActivity().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        user_id = prefs.getInt("user_id", 0);

        // Init view
        InitView();

        // Set data
        txtUsername.setText(user.getUsername());
        txtEmail.setText(user.getEmail());
        // Decode image
        if(user.getAvatar() != null){
            byte[] bytes = Base64.decode(user.getAvatar(), Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            // Set image
            avatarImg.setImageBitmap(bitmap);
        }

        postViewAdapter = new ProfileViewAdapter(getActivity());
        viewPager2.setAdapter(postViewAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

        return view;
    }

    private void InitView() {
        avatarImg = view.findViewById(R.id.profile_avatar_image);

        txtUsername = view.findViewById(R.id.profile_txt_username);
        txtEmail = view.findViewById(R.id.profile_txt_email);

        tabLayout = view.findViewById(R.id.profile_tab_layout);
        viewPager2 = view.findViewById(R.id.profile_view_paper);
    }
}