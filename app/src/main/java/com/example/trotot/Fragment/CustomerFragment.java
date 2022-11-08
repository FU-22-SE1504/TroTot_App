package com.example.trotot.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trotot.Adapter.CustomerAdapter;
import com.example.trotot.Adapter.HouseholderAdapter;
import com.example.trotot.Database.ConnectDatabase;
import com.example.trotot.Model.Post;
import com.example.trotot.Model.User;
import com.example.trotot.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class CustomerFragment extends Fragment {
    // View
    View view;

    // Data
    ArrayList<User> listUser;
    ArrayList<Post> list;
    int user_id;

    // Connect
    ConnectDatabase connectDatabase;
    Connection connection;
    Statement st;
    ResultSet rs;

    // Recyclerview
    RecyclerView recyclerView;
    CustomerAdapter customerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            user_id = bundle.getInt("user_id", 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_customer, container, false);
        displayItem();
        return view;
    }

    private void displayItem() {
        recyclerView = view.findViewById(R.id.customer_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        list = getAllListPost(2);
        listUser = getUserinfo();
        customerAdapter = new CustomerAdapter(listUser, list, getActivity());
        recyclerView.setAdapter(customerAdapter);
    }

    private ArrayList<User> getUserinfo() {
        ArrayList<User> list = new ArrayList<>();
        int role_id = 1;
        try {
            connectDatabase = new ConnectDatabase();
            connection = connectDatabase.ConnectToDatabase();

            if (connection != null) {
                // Check username is valid in database
                String query = "select user_id, username, avatar from [User] where role_id = " + role_id + ";";

                st = connection.createStatement();
                rs = st.executeQuery(query);

                int i = 0;
                while (rs.next()) {
                    User user = new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("avatar")
                    );
                    list.add(user);
                    ++i;
                }
            }
        } catch (Exception e) {
            Log.e("Error", "Get User info fail");
        }
        return list;
    }

    public ArrayList<Post> getAllListPost(int type_id) {
        ArrayList<Post> list = new ArrayList<>();
        try {
            try {
                connectDatabase = new ConnectDatabase();
                connection = connectDatabase.ConnectToDatabase();

                if (connection != null) {
                    String selectQuery = "Select * from [Post] where type_id = " + type_id + " order by post_id desc;";

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