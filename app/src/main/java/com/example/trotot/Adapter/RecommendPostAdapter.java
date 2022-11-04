package com.example.trotot.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trotot.Fragment.HomeFragment;
import com.example.trotot.Model.Post;
import com.example.trotot.Model.User;
import com.example.trotot.R;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecommendPostAdapter extends RecyclerView.Adapter<RecommendPostAdapter.RecommendPostViewHolder> {

    ArrayList<Post> listPost;
    Context context;
    Bitmap bitmap;

    public RecommendPostAdapter(ArrayList<Post> listPost, Context context) {
        this.listPost = listPost;
        this.context = context;
    }

    @NonNull
    @Override
    public RecommendPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecommendPostViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.home_viewpaper_container,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendPostViewHolder holder, int position) {
        Post post = listPost.get(position);
        holder.tv_Title.setText(post.getTitle());
        holder.tv_Address.setText(post.getAddress());

        if(post.getPoster() != null){
            Uri uri;
            byte[] bytes = Base64.decode(post.getPoster(), Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            holder.kbvImg.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return listPost.size();
    }

    static class RecommendPostViewHolder extends RecyclerView.ViewHolder{
        private KenBurnsView kbvImg;
        private TextView tv_Title, tv_Address;

        public RecommendPostViewHolder(@NonNull View itemView) {
            super(itemView);

            kbvImg = itemView.findViewById(R.id.kbvImage);
            tv_Title = itemView.findViewById(R.id.home_post_title);
            tv_Address = itemView.findViewById(R.id.home_post_address);
        }
    }
}
