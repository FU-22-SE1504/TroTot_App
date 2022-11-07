package com.example.trotot.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.trotot.Adapter.ListAllPostAdapter;
import com.example.trotot.Adapter.RecommendPostAdapter;
import com.example.trotot.Database.ConnectDatabase;
import com.example.trotot.Model.Post;
import com.example.trotot.Model.User;
import com.example.trotot.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    // View
    View view;

    // Data
    ArrayList<Post> list;
    ArrayList<Post> listAllPost;
    ArrayList<User> listUser;

    // Connect
    ConnectDatabase connectDatabase;
    Connection connection;
    Statement st;
    ResultSet rs;

    // Recyclerview
    RecyclerView recyclerView;
    ListAllPostAdapter listAllPostAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        displayItem();
        displayAllPost();
        return view;
    }

    private void displayAllPost() {
        recyclerView = view.findViewById(R.id.list_all_post_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        listAllPost = getAllPost();
        listUser = getUserinfo();
        listAllPostAdapter = new ListAllPostAdapter(listUser, listAllPost, getActivity());
        recyclerView.setAdapter(listAllPostAdapter);
    }

    private void displayItem() {
        ViewPager2 viewPager2 = view.findViewById(R.id.home_viewPaper2);
        list = getTopPost(1);
        viewPager2.setAdapter(new RecommendPostAdapter(list, getActivity()));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.95f + r * 0.05f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);
    }

    public ArrayList<Post> getTopPost(int type_id) {
        ArrayList<Post> list = new ArrayList<>();
        try {
            try {
                connectDatabase = new ConnectDatabase();
                connection = connectDatabase.ConnectToDatabase();

                if (connection != null) {
                    String selectQuery = "select top 3 * from Post where type_id = " + type_id + " order by create_at desc;";

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

    public ArrayList<Post> getAllPost() {
        ArrayList<Post> list = new ArrayList<>();
        try {
            try {
                connectDatabase = new ConnectDatabase();
                connection = connectDatabase.ConnectToDatabase();

                if (connection != null) {
                    String selectQuery = "Select * from [Post];";

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
                Log.e("Bug: ",ex.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private ArrayList<User> getUserinfo() {
        ArrayList<User> list = new ArrayList<>();
        int role_id = 1;
        try {
            connectDatabase = new ConnectDatabase();
            connection = connectDatabase.ConnectToDatabase();

            if (connection != null) {
                // Check username is valid in database
                String query = "select * from [User] where role_id = " + role_id + ";";

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

}