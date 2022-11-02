package com.example.trotot.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trotot.Model.Post;
import com.example.trotot.Model.User;
import com.example.trotot.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostAdapter.ProfilePostViewHolder> {
    ArrayList<Post> list;
    User user;
    Context context;

    Bitmap bitmap;
    int user_id;
    Uri avatar;
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
        String description;
        Post post = list.get(position);
        holder.tv_username.setText(String.valueOf(post.getUser_id()));
        holder.tv_title.setText(subString(post.getTitle(), 20));
        holder.tv_description.setText(subString(post.getDescription(), 100));
        holder.tv_username.setText(user.getUsername());
        holder.tv_contact.setText("Contact: " + post.getContact());
        // Decode image
        if(post.getPoster() != null){
            byte[] bytes = Base64.decode(post.getPoster(), Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            // Set image
            holder.posterImg.setImageBitmap(bitmap);
        }

        if(user.getAvatar() != null){
            byte[] bytes = Base64.decode(user.getAvatar(), Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            // Set image
            holder.avatarImg.setImageBitmap(bitmap);
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
        ImageView posterImg, btnDelete;

        public ProfilePostViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_username = itemView.findViewById(R.id.profile_post_username);
            tv_title = itemView.findViewById(R.id.profile_post_title);
            tv_description = itemView.findViewById(R.id.profile_post_description);
            tv_contact = itemView.findViewById(R.id.profile_post_contact);

            avatarImg = itemView.findViewById(R.id.profile_post_avatarImg);
            posterImg = itemView.findViewById(R.id.profile_post_posterImg);

            btnDelete = itemView.findViewById(R.id.profile_post_delete);
        }
    }

    public String subString(String text, int length){
        String tmp = text;
        if(tmp.length() > length){
            tmp = tmp.substring(0, length);
            tmp += "...";
        }else{
            tmp = text;
        }
        return tmp;
    }
}
