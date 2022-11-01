package com.example.trotot.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.trotot.Database.ConnectDatabase;
import com.example.trotot.Post.DetailPost;
import com.example.trotot.Post.DetailPostAdapter;
import com.example.trotot.Post.Recentpost;
import com.example.trotot.Post.RecentpostAdapter;
import com.example.trotot.Post.Toppost;
import com.example.trotot.Post.ToppostAdapter;
import com.example.trotot.R;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class HomeFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ConnectDatabase connectDatabase;
    Button connect;
    private RecyclerView recycleviewtop;
    private ArrayList<Toppost> TopppostData;
    private int[] image_top, img_rc;
    private String[] top_name;
    private String[] top_location;
    private String[] recentpost_location;
    private String[] recentpost_debscribe;
    private String[] recentpost_price;
    private RecyclerView recycleviewrc;
    private ArrayList<Recentpost> RecentpostData;


    public HomeFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataInitialize();
        recycleviewtop = view.findViewById(R.id.rcv_toppost);
        recycleviewtop.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        recycleviewtop.setHasFixedSize(true);
        ToppostAdapter toppostAdapter = new ToppostAdapter(TopppostData, getContext());
        recycleviewtop.setAdapter(toppostAdapter);
        toppostAdapter.notifyDataSetChanged();

//        dataInitialize2();
//        recycleviewrc = view.findViewById(R.id.rcv_rcpost);
//        recycleviewrc.setLayoutManager(new LinearLayoutManager(getContext()));
//        recycleviewrc.setHasFixedSize(true);
//        RecentpostAdapter recentpostAdapter = new RecentpostAdapter(RecentpostData, getContext());
//        recycleviewrc.setAdapter(recentpostAdapter);
//        recentpostAdapter.notifyDataSetChanged();

    }

    private void dataInitialize() {
        TopppostData = new ArrayList<>();
        top_name = new String[]{
                String.valueOf(R.id.top_name),
                String.valueOf(R.id.top_name),
        };

        top_location = new String[]{
                String.valueOf(R.id.top_location),
                String.valueOf(R.id.top_location),
        };

        image_top = new int[]{
                R.drawable.ava_2,
                R.drawable.ava_1,
        };
        for (int i = 0; i < image_top.length ; i++) {
            Toppost toppost = new Toppost(top_name[i], top_location[i], image_top[i]);
            TopppostData.add(toppost);
        }
    }
//    private void dataInitialize2(){
//        RecentpostData = new ArrayList<>();
//        String[] recentpost_name = new String[]{
//                String.valueOf(R.id.recentpost_name),
//                String.valueOf(R.id.recentpost_name),
//                String.valueOf(R.id.recentpost_name),
//        };
//
//        recentpost_location = new String[]{
//                String.valueOf(R.id.recentpost_location),
//                String.valueOf(R.id.recentpost_location),
//                String.valueOf(R.id.recentpost_location),
//        } ;
//        recentpost_debscribe = new String[]{
//                String.valueOf(R.id.recentpost_describe),
//                String.valueOf(R.id.recentpost_describe),
//                String.valueOf(R.id.recentpost_describe),
//
//        } ;
//
//        recentpost_price = new String[]{
//                String.valueOf(R.id.recentpost_price)
//        } ;
//        img_rc = new int[]{
//                R.drawable.ava_2,
//                R.drawable.ava_1,
//                R.drawable.ava_1,
//        };
//        for (int i = 0; i < img_rc.length ; i++) {
//            Recentpost recentpost = new Recentpost(recentpost_name[i], recentpost_location[i], img_rc[i],recentpost_debscribe[i],recentpost_price[i]);
//            RecentpostData.add(recentpost);
//        }
//    }
}