package com.example.trotot.Post;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trotot.R;

import java.util.ArrayList;

public class DetailPostAdapter extends RecyclerView.Adapter<DetailPostAdapter.DetailPostViewHolder> {

    private Context context;
    private ArrayList<DetailPost> detailPostsData;

        public DetailPostAdapter( Context context, ArrayList<DetailPost>  detailPostsData){
            this.detailPostsData=detailPostsData;
            this.context =context;
        }

    public DetailPostAdapter(DetailPost[] detailPosts) {
    }

    @NonNull
    @Override
    public DetailPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_post_detail,parent,false);
        DetailPostViewHolder viewHolder = new DetailPostViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailPostViewHolder holder, int position) {
        final DetailPost detailPost = detailPostsData.get(position);
        holder.img_detal.setImageResource(detailPost.getImgPost());
    }

    @Override
    public int getItemCount() {
        return detailPostsData.size();
    }

    public class DetailPostViewHolder extends RecyclerView.ViewHolder{

        private ImageView img_detal;

        public DetailPostViewHolder(@NonNull View itemView) {
            super(itemView);
            img_detal= itemView.findViewById(R.id.post_img);
        }
    }
}
