package com.example.trotot.RecentPost;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trotot.Home;
import com.example.trotot.R;

public class RecentpostAdapter extends RecyclerView.Adapter<RecentpostAdapter.RecentpostViewHolder> {

    Recentpost[] RecentpostData;
    Context context;
    public RecentpostAdapter(Recentpost[] recentpostData, Home activity){
        this.RecentpostData = recentpostData;
        this.context= activity;
    }



    @NonNull
    @Override
    public RecentpostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_recentpost,parent,false);
        RecentpostViewHolder viewHolder = new RecentpostViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecentpostViewHolder holder, int position) {
        final Recentpost Recentpostlist= RecentpostData[position];
        holder.rp_name.setText(Recentpostlist.getRecentpost_name());
        holder.rp_location.setText(Recentpostlist.getRecentpost_location());
        holder.rp_image.setImageResource(Recentpostlist.getRecentpost_img());
        holder.rp_describe.setText(Recentpostlist.getRecentpost_describe());
        holder.rp_price.setText(Recentpostlist.getRecentpost_price());


    }

    @Override
    public int getItemCount() {
        return RecentpostData.length;
    }

    public class RecentpostViewHolder extends  RecyclerView.ViewHolder{
        ImageView rp_image;
        TextView rp_name;
        TextView rp_location;
        TextView rp_describe;
        TextView rp_price;


        public RecentpostViewHolder(@NonNull View itemView) {
            super(itemView);
            rp_image = itemView.findViewById(R.id.recentpost_img);
            rp_name = itemView.findViewById(R.id.recentpost_name);
            rp_location = itemView.findViewById(R.id.recentpost_location);
            rp_describe = itemView.findViewById(R.id.recentpost_describe);
            rp_price = itemView.findViewById(R.id.recentpost_price);

        }
    }

}
