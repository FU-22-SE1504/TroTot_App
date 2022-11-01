package com.example.trotot.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.trotot.Database.ConnectDatabase;
import com.example.trotot.Model.User;
import com.example.trotot.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ProfileInfoFragment extends Fragment {
    // View
    View view;
    Button editProfile;
    EditText edtUsername, edtEmail, edtPhoneNumber, edtFullName;
    EditProfileFragment editProfileFragment;

    //Session
    SharedPreferences prefs;
    public static final String PREFERENCE_NAME = "PREFERENCE_DATA";
    Integer user_id;

    // Data
    ProgressDialog progressDialog;
    User user;
    ConnectDatabase connectDatabase;
    Connection connection;
    Statement st;
    ResultSet rs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_info, container, false);

        // Pass user id to edit profile screen
        prefs = this.getActivity().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        user_id = prefs.getInt("user_id", 0);
        Bundle bundle = new Bundle();
        bundle.putInt("user_id", user_id);

        editProfileFragment = new EditProfileFragment();
        editProfileFragment.setArguments(bundle);

        // Get view
        InitView();

        // Fetch data
        fetchUserData(user_id);

        // Handle onclick edit profile
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleEditProfile();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void fetchUserData(int userID) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching data...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        try {
            connectDatabase = new ConnectDatabase();
            connection = connectDatabase.ConnectToDatabase();

            if (connection != null) {
                String selectQuery = "Select * from [User] where user_id = " + userID;

                st = connection.createStatement();
                rs = st.executeQuery(selectQuery);
                if (rs.next()) {
                    user = new User(rs.getInt(1), rs.getString(2), rs.getString(3),
                            rs.getString(4), rs.getString(5), rs.getString(6), rs.getDate(7),
                            rs.getInt(8), rs.getString(9));
                    edtUsername.setText(user.getUsername());
                    edtEmail.setText(user.getEmail());
                    edtPhoneNumber.setText(user.getPhone_number());
                    edtFullName.setText(user.getFull_name());
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }

    private void InitView() {
        editProfile = view.findViewById(R.id.btn_EditProfile);
        edtUsername = view.findViewById(R.id.profile_info_username);
        edtEmail = view.findViewById(R.id.profile_info_email);
        edtFullName = view.findViewById(R.id.profile_info_fullName);
        edtPhoneNumber = view.findViewById(R.id.profile_info_phoneNumber);
    }

    private void handleEditProfile() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.body_container, editProfileFragment);
        ft.commit();
    }
}