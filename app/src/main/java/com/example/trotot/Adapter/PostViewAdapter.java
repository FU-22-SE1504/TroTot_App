package com.example.trotot.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.trotot.Fragment.PostCustomerFragment;
import com.example.trotot.Fragment.PostHouseholderFragment;

public class PostViewAdapter extends FragmentStateAdapter {


    public PostViewAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new PostCustomerFragment();
            case 1:
                return new PostHouseholderFragment();
            default:
                return new PostCustomerFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
