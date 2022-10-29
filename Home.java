package com.example.trotot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import RecentPost.Recentpost;
import RecentPost.RecentpostAdapter;
import TopPost.Toppost;
import TopPost.ToppostAdapter;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        RecyclerView recyclerView = findViewById(R.id.rcv_rcpost);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Recentpost[] recentposts = new Recentpost[]{
                new Recentpost("Hoang Loc Phat", "Ba lang, can tho", R.drawable.ava_1, "Một thiên đường dành cho giới trẻ tuổi từ 18-20", "1000 btc"),
                new Recentpost("Hoang Loc Phat", "Ninh Kieu, can tho", R.drawable.ava_1, "Một thiên đường dành cho giới trẻ tuổi từ 18-20", "1000 btc"),
                new Recentpost("1235554313532", "Ba lang, can tho", R.drawable.ava_2, "ballalbaalballab", "3tr200 vnd"),
                new Recentpost("Tong Loc Phat", "Phong dien , can tho", R.drawable.ava_1, "ballalbaalballab", "3tr200 vnd"),
                new Recentpost("Hoang  Phat", "Co do, can tho", R.drawable.ava_2, "ballalbaalballab", "3tr200 vnd"),
        };

        RecentpostAdapter recentpostAdapter = new RecentpostAdapter(recentposts, Home.this);
        recyclerView.setAdapter(recentpostAdapter);


        RecyclerView recyclerViewTop = findViewById(R.id.rcv_toppost);
        recyclerViewTop.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));


        Toppost[] topposts = new Toppost[]{
                new Toppost("Nhà nghỉ thiên đường", "BInh Thanh, Hp chi minh", R.drawable.ava_1),
                new Toppost("Nhà nghỉ đại ngục", "Ninh Kiều, Cần Thơ", R.drawable.ava_1),
                new Toppost("Nhà Trọ thiện phát", "Ba Láng, Cần Thơ", R.drawable.ava_2),
                new Toppost("Nhà Hoàng hà", "Cờ Đỏ, Cần Thơ", R.drawable.ava_1),
                new Toppost("Nhà nghỉ nghỉ", "BInh Thanh, Hp chi minh", R.drawable.ava_2),
        };

        ToppostAdapter toppostAdapter = new ToppostAdapter(topposts, Home.this);
        recyclerViewTop.setAdapter(toppostAdapter);
    }
}