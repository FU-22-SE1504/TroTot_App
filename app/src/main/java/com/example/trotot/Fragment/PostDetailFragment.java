package com.example.trotot.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.trotot.LoginActivity;
import com.example.trotot.MainActivity;
import com.example.trotot.Model.Post;
import com.example.trotot.R;

public class PostDetailFragment extends Fragment {
    View view;
    Post post;
    TextView tv_title, tv_description, tv_contact, tv_address, tv_price;
    LinearLayout lo_address, lo_poster;
    ImageView imgPoster;

    //Session
    SharedPreferences prefs;
    public static final String PREFERENCE_NAME = "PREFERENCE_DATA";
    Integer user_id;

    Bitmap bitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null){
            post =  (Post) bundle.get("Post_Detail");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_post_detail, container, false);

        InitView();

        tv_title.setText(post.getTitle());
        tv_description.setText(post.getDescription());
        tv_contact.setText(post.getContact());


        if(post.getType_id() == 1){
            tv_address.setText(post.getAddress());
            tv_price.setText(post.getPrice());
            // Set poster image
            if(post.getPoster() != null){
                byte[] bytes = Base64.decode(post.getPoster(), Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                // Set image
                imgPoster.setImageBitmap(bitmap);
            }
        }else{
            lo_poster.setVisibility(View.GONE);
            lo_address.setVisibility(View.GONE);
        }

        return view;
    }

    public void InitView(){
        tv_title = view.findViewById(R.id.detail_title);
        tv_description = view.findViewById(R.id.detail_description);
        tv_address = view.findViewById(R.id.detail_address);
        tv_contact = view.findViewById(R.id.detail_contact);
        tv_price = view.findViewById(R.id.detail_price);

        imgPoster = view.findViewById(R.id.detail_poster);

        lo_address = view.findViewById(R.id.detail_layout_address);
        lo_poster = view.findViewById(R.id.detail_layout_poster);
    }
}