package com.example.trotot.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.trotot.Fragment.PostCustomerFragment;
import com.example.trotot.Fragment.PostHouseholderFragment;
import com.example.trotot.Fragment.ProfileInfoFragment;
import com.example.trotot.Fragment.ProfilePostFragment;

public class ProfileViewAdapter extends FragmentStateAdapter {

    public ProfileViewAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ProfileInfoFragment();
            case 1:
                return new ProfilePostFragment();
            default:
                return new ProfileInfoFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
