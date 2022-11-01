package com.example.trotot.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trotot.Post.DetailPost;
import com.example.trotot.Post.DetailPostAdapter;
import com.example.trotot.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DetailPost[] detailPosts;
    private ArrayList<DetailPost> detailPostsData;
    private int[] image_detail;
    private RecyclerView recycleview;

    public PostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostFragment newInstance(String param1, String param2) {
        PostFragment fragment = new PostFragment();
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
        return inflater.inflate(R.layout.detail_post, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataInitialize();

        recycleview = view.findViewById(R.id.pvr_photo);
        recycleview.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        recycleview.setHasFixedSize(true);
        DetailPostAdapter detailPostAdapter = new DetailPostAdapter(getContext(), detailPostsData);
        recycleview.setAdapter(detailPostAdapter);
        detailPostAdapter.notifyDataSetChanged();
    }
    private void dataInitialize(){
        detailPostsData = new ArrayList<>();

        image_detail = new int[]{
                R.drawable.ava_1,
                R.drawable.ava_2,
                R.drawable.ava_2,
                R.drawable.ava_1,
                R.drawable.ava_2,
                R.drawable.ava_1,
        };
        for (int i = 0; i < image_detail.length ; i++) {
            DetailPost detailPost = new DetailPost(image_detail[i]);
            detailPostsData.add(detailPost);
        }
    }
}
