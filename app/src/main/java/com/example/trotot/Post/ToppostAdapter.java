package com.example.trotot.Post;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trotot.Fragment.HomeFragment;
import com.example.trotot.R;

import java.util.ArrayList;


public class ToppostAdapter extends RecyclerView.Adapter<ToppostAdapter.ToppostViewHolder> {


    private ArrayList<Toppost> ToppostData;
    private Context context;
    public ToppostAdapter(ArrayList<Toppost> toppostData, Context context) {
        this.ToppostData = toppostData;
        this.context = context;
    }

    @NonNull
    @Override
    public ToppostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_toppost,parent,false);
        ToppostViewHolder viewHolder = new ToppostViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ToppostViewHolder holder, int position) {
        final Toppost Toppostlist= ToppostData.get(position);
        holder.tp_name.setText(Toppostlist.getTop_name());
        holder.tp_location.setText(Toppostlist.getTop_location());
        holder.tp_image.setImageResource(Toppostlist.getTop_img());

    }

    @Override
    public int getItemCount() {
        return ToppostData.size();
    }

    public class ToppostViewHolder extends RecyclerView.ViewHolder{
        ImageView tp_image;
        TextView tp_name;
        TextView tp_location;

        public ToppostViewHolder(@NonNull View itemView) {
            super(itemView);
            tp_image = itemView.findViewById(R.id.top_img);
            tp_name = itemView.findViewById(R.id.top_name);
            tp_location = itemView.findViewById(R.id.top_location);
        }
    }
}
