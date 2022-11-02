package com.example.trotot.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.trotot.Adapter.ProfilePostAdapter;
import com.example.trotot.Database.ConnectDatabase;
import com.example.trotot.LoginActivity;
import com.example.trotot.MainActivity;
import com.example.trotot.Model.Post;
import com.example.trotot.Model.User;
import com.example.trotot.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class ProfilePostFragment extends Fragment {
    View view;

    //Session
    SharedPreferences prefs;
    public static final String PREFERENCE_NAME = "PREFERENCE_DATA";
    Integer user_id;
    User user;
    // Data
    ConnectDatabase connectDatabase;
    Connection connection;
    Statement st;
    ResultSet rs;
    // Recyclerview
    RecyclerView recyclerView;
    ArrayList<Post> list;
    ProfilePostAdapter profilePostAdapter;

    ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_post, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching data...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        prefs = this.getActivity().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        user_id = prefs.getInt("user_id", 0);

        displayItem();

        // Inflate the layout for this fragment
        return view;
    }

    private void displayItem() {
        recyclerView = view.findViewById(R.id.profile_post_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        list = getAllListPost();
        user = getUserinfo();
        profilePostAdapter = new ProfilePostAdapter(list, getActivity(), user);
        recyclerView.setAdapter(profilePostAdapter);
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private User getUserinfo() {
        User user = new User();
        try {
            connectDatabase = new ConnectDatabase();
            connection = connectDatabase.ConnectToDatabase();
            if(connection != null) {
                // Check username is valid in database
                String query = "select username, avatar from [User] where user_id = " + user_id + ";";
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(query);
                if (rs.next()) {
                    user = new User(rs.getString(1), rs.getString(2));
                    return user;
                }
            }
        }catch (Exception e){
            Log.e("Error", "Get User info fail");
        }
        return user;
    }

    public ArrayList<Post> getAllListPost() {
        ArrayList<Post> list = new ArrayList<>();
        try {
            try {
                connectDatabase = new ConnectDatabase();
                connection = connectDatabase.ConnectToDatabase();

                if (connection != null) {
                    String selectQuery = "Select * from [Post] where user_id = " + user_id +";";

                    st = connection.createStatement();
                    rs = st.executeQuery(selectQuery);

                    int i = 0;
                    while (rs.next()) {
                        Post post = new Post(
                                rs.getInt("post_id"),
                                rs.getInt("user_id"),
                                rs.getString("title"),
                                rs.getString("description"),
                                rs.getString("address"),
                                rs.getString("price"),
                                rs.getInt("type_id"),
                                rs.getString("poster"),
                                rs.getString("contact"));
                        list.add(post);
                        ++i;
                    }
                } else {
                    Log.e("Error: ", "Connect fail");
                }
            } catch (Exception ex) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}