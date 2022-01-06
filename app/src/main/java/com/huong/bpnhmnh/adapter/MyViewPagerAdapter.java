package com.huong.bpnhmnh.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.huong.bpnhmnh.fragment.FavoriteFragment;
import com.huong.bpnhmnh.fragment.HomeFragment;
import com.huong.bpnhmnh.fragment.MeFragment;
import com.huong.bpnhmnh.fragment.SearchFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {

    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity){
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new SearchFragment();
            case 2:
                return new FavoriteFragment();
            case 3:
                return new MeFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {

        return 4;
    }
}
