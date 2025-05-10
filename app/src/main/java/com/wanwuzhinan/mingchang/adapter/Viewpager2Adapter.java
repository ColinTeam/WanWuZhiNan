package com.wanwuzhinan.mingchang.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class Viewpager2Adapter extends FragmentStateAdapter {
    public final List<Fragment> fragments;

    public Viewpager2Adapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> list) {
        super(fragmentActivity);
        fragments = list;
    }

    public Viewpager2Adapter(@NonNull Fragment fragment, List<Fragment> list) {
        super(fragment);
        fragments = list;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
