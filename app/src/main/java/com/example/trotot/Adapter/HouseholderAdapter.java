package com.example.trotot.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trotot.Fragment.HomeFragment;
import com.example.trotot.Fragment.PostDetailFragment;
import com.example.trotot.LoginActivity;
import com.example.trotot.MainActivity;
import com.example.trotot.Model.Post;
import com.example.trotot.Model.User;
import com.example.trotot.R;

import java.util.ArrayList;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;

public class HouseholderAdapter extends RecyclerView.Adapter<HouseholderAdapter.HouseholderViewHolder> {
    ArrayList<User> listUser;
    ArrayList<Post> list;
    Context context;
    Bitmap bitmap;

    //Session
    SharedPreferences prefs;
    public static final String PREFERENCE_NAME = "PREFERENCE_DATA";
    Integer user_id;

    public HouseholderAdapter(ArrayList<User> listUser, ArrayList<Post> list, Context context) {
        this.listUser = listUser;
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HouseholderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.householder_item, parent, false);
        return new HouseholderViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull HouseholderViewHolder holder, int position) {
        Post post = list.get(position);
        // Get user by user id
        User user = listUser.stream().filter(a -> a.getUser_id() == post.getUser_id()).collect(Collectors.toList()).get(0);
        holder.tv_Title.setText(post.getTitle());
        holder.tv_Description.setText(post.getDescription());
        holder.tv_Price.setText(post.getPrice());
        //Poster
        if(post.getPoster() != null){
            byte[] bytes = Base64.decode(post.getPoster(), Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            // Set poster image
            holder.posterImg.setImageBitmap(bitmap);
        }

        //Avatar
        if(user.getAvatar() != null){
            byte[] bytes = Base64.decode(user.getAvatar(), Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            // Set avatar
            holder.avatarImg.setImageBitmap(bitmap);
        }

        holder.tv_Username.setText(user.getUsername());

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDetailPost(post, view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HouseholderViewHolder extends RecyclerView.ViewHolder{
        CircleImageView avatarImg;
        ImageView posterImg, btnDetail;
        TextView tv_Username, tv_Title, tv_Description, tv_Price;

        public HouseholderViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_Username = itemView.findViewById(R.id.householder_username);
            tv_Title = itemView.findViewById(R.id.householder_title);
            tv_Description = itemView.findViewById(R.id.householder_description);
            tv_Price = itemView.findViewById(R.id.householder_price);

            avatarImg = itemView.findViewById(R.id.householder_avatarImg);
            btnDetail = itemView.findViewById(R.id.householder_btn_detail);
            posterImg = itemView.findViewById(R.id.householder_posterImg);
        }
    }

    public void onClickDetailPost(Post post, View view){
        // Get id by session
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        user_id = prefs.getInt("user_id", 0);
        if (user_id != 0){
            Fragment fragment = new PostDetailFragment();
            AppCompatActivity appCompatActivity = (AppCompatActivity) view.getContext();
            Bundle bundle = new Bundle();
            bundle.putSerializable("Post_Detail", post);
            fragment.setArguments(bundle);
            appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fragment).addToBackStack(null).commit();
        }else {
            context.startActivity(new Intent(context, LoginActivity.class));
        }
    }
}
