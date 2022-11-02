package com.example.trotot.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trotot.Model.Post;
import com.example.trotot.Model.User;
import com.example.trotot.R;

import java.util.ArrayList;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {
    ArrayList<User> listUser;
    ArrayList<Post> list;
    Context context;
    Bitmap bitmap;

    public CustomerAdapter(ArrayList<User> listUser, ArrayList<Post> list, Context context) {
        this.listUser = listUser;
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_item, parent, false);
        return new CustomerViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Post post = list.get(position);
        // Get user by user id
        User user = listUser.stream().filter(a -> a.getUser_id() == post.getUser_id()).collect(Collectors.toList()).get(0);
        holder.tv_Title.setText(subString(post.getTitle(), 25));
        holder.tv_Description.setText(subString(post.getDescription(), 110));

        //Avatar
        if(user.getAvatar() != null){
            byte[] bytes = Base64.decode(user.getAvatar(), Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            // Set avatar
            holder.avatarImg.setImageBitmap(bitmap);
        }

        holder.tv_Username.setText(user.getUsername());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder {
        CircleImageView avatarImg;
        TextView tv_Username, tv_Title, tv_Description;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_Username = itemView.findViewById(R.id.customer_username);
            tv_Title = itemView.findViewById(R.id.customer_title);
            tv_Description = itemView.findViewById(R.id.customer_description);

            avatarImg = itemView.findViewById(R.id.customer_avatarImg);
        }
    }

    public String subString(String text, int length) {
        String tmp = text;
        if (tmp.length() > length) {
            tmp = tmp.substring(0, length);
            tmp += "...";
        } else {
            tmp = text;
        }
        return tmp;
    }
}
