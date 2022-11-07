package com.example.trotot.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trotot.Fragment.PostDetailFragment;
import com.example.trotot.LoginActivity;
import com.example.trotot.Model.Post;
import com.example.trotot.R;

import java.util.ArrayList;

public class RecentPostAdapter extends RecyclerView.Adapter<RecentPostAdapter.RecentPostViewHolder> {



    private Context context;
    private ArrayList<Post> recentPostData;
    Bitmap bitmap;
    SharedPreferences prefs;
    public static final String PREFERENCE_NAME = "PREFERENCE_DATA";
    Integer user_id;

    public RecentPostAdapter(ArrayList<Post> recentpostData, Context context){
        this.recentPostData = recentpostData;
        this.context = context;
    }



    @NonNull
    @Override
    public RecentPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_recentpost,parent,false);
        RecentPostViewHolder viewHolder = new RecentPostViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecentPostViewHolder holder, int position) {
        final Post recentPost= recentPostData.get(position);
        holder.rp_name.setText(recentPost.getTitle());
        holder.rp_location.setText(recentPost.getAddress());
        holder.rp_describe.setText(recentPost.getDescription());
        holder.rp_price.setText(recentPost.getPrice());
        if(recentPost.getPoster() != null){
            Uri uri;
            byte[] bytes = Base64.decode(recentPost.getPoster(), Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            holder.rp_image.setImageBitmap(bitmap);
        }
        holder.cv_rcpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDetailPost(recentPost, view);
            }
        });

    }

    @Override
    public int getItemCount() {
        return recentPostData.size();
    }

    public class RecentPostViewHolder extends  RecyclerView.ViewHolder{
        ImageView rp_image;
        TextView rp_name;
        TextView rp_location;
        TextView rp_describe;
        TextView rp_price;
        CardView cv_rcpost;

        public RecentPostViewHolder(@NonNull View itemView) {
            super(itemView);
            rp_image = itemView.findViewById(R.id.recentpost_img);
            rp_name = itemView.findViewById(R.id.recentpost_name);
            rp_location = itemView.findViewById(R.id.recentpost_location);
            rp_describe = itemView.findViewById(R.id.recentpost_describe);
            rp_price = itemView.findViewById(R.id.recentpost_price);
            cv_rcpost= itemView.findViewById(R.id.cv_rcpost);
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
            appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fragment).commit();
        }else {
            context.startActivity(new Intent(context, LoginActivity.class));
        }
    }

}
