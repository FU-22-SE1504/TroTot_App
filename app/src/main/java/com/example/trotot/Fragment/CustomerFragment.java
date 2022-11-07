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
            list = (ArrayList<Post>) bundle.get("List_CustomerPost");
            listUser = (ArrayList<User>) bundle.get("List_UserInfo");
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
        customerAdapter = new CustomerAdapter(listUser, list, getActivity());
        recyclerView.setAdapter(customerAdapter);
    }
}