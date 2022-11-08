package com.example.trotot.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trotot.Database.ConnectDatabase;
import com.example.trotot.Fragment.ProfileEditPostFragment;
import com.example.trotot.Fragment.ProfileFragment;
import com.example.trotot.Model.Post;
import com.example.trotot.Model.User;
import com.example.trotot.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostAdapter.ProfilePostViewHolder> {
    ArrayList<Post> list;
    User user;
    Context context;

    // Connect
    ConnectDatabase connectDatabase;
    Connection connection;
    Statement st;
    ResultSet rs;

    Bitmap bitmap;
    ProgressDialog progressDialog;

    public ProfilePostAdapter(ArrayList<Post> list, Context context, User user) {
        this.list = list;
        this.user = user;
        this.context = context;
    }

    @NonNull
    @Override
    public ProfilePostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_post_item, parent, false);
        return new ProfilePostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfilePostViewHolder holder, int position) {
        Post post = list.get(position);
        holder.tv_title.setText(post.getTitle());
        holder.tv_description.setText(post.getDescription());
        holder.tv_username.setText(user.getUsername());
        holder.tv_contact.setText("Contact: " + post.getContact());
        // Decode image
        if(post.getPoster() != null){
            byte[] bytes = Base64.decode(post.getPoster(), Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            // Set image
            holder.posterImg.setImageBitmap(bitmap);
        }else{
            holder.posterImg.setVisibility(View.GONE);
        }

        if(user.getAvatar() != null){
            byte[] bytes = Base64.decode(user.getAvatar(), Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            // Set image
            holder.avatarImg.setImageBitmap(bitmap);
        }

        // Edit post
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ProfileEditPostFragment();
                Bundle bundle = new Bundle();
                AppCompatActivity appCompatActivity = (AppCompatActivity) view.getContext();
                bundle.putSerializable("Post_Edit", post);
                fragment.setArguments(bundle);
                appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fragment).addToBackStack(null).commit()    ;
            }
        });

        // Delete post
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setMessage("Are you sure want to delete")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DeletePost(post.getPost_id());
                                Fragment fragment = new ProfileFragment();
                                AppCompatActivity appCompatActivity = (AppCompatActivity) view.getContext();
                                appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fragment).commit();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }

    private void DeletePost(int post_id) {
        connectDatabase = new ConnectDatabase();
        connection = connectDatabase.ConnectToDatabase();

        if (connection != null) {
            String selectQuery = "Delete from [Post] where post_id = " + post_id + ";";
            try{
                st = connection.createStatement();
                st.executeUpdate(selectQuery);
                Toast.makeText(context, "Delete success", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(context, "Delete Fail", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("Error: ", "Connect fail");
        }
    }

    @Override
    public int getItemCount() {
        Log.e("Size", String.valueOf(list.size()));
        return list.size();
    }

    public class ProfilePostViewHolder extends RecyclerView.ViewHolder{
        TextView tv_username, tv_title, tv_description, tv_contact;
        CircleImageView avatarImg;
        ImageView posterImg, btnDelete, btnEdit;

        public ProfilePostViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_username = itemView.findViewById(R.id.profile_post_username);
            tv_title = itemView.findViewById(R.id.profile_post_title);
            tv_description = itemView.findViewById(R.id.profile_post_description);
            tv_contact = itemView.findViewById(R.id.profile_post_contact);

            avatarImg = itemView.findViewById(R.id.profile_post_avatarImg);
            posterImg = itemView.findViewById(R.id.profile_post_posterImg);

            btnDelete = itemView.findViewById(R.id.profile_post_delete);
            btnEdit = itemView.findViewById(R.id.profile_post_edit);
        }
    }
}
