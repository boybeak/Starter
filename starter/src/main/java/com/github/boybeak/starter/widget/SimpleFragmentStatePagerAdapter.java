package com.github.boybeak.starter.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.github.boybeak.starter.fragment.BaseFragment;

import java.util.Arrays;
import java.util.List;

public class SimpleFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private Context context = null;
    private List<? extends BaseFragment> fragments = null;

    public SimpleFragmentStatePagerAdapter(Context context, FragmentManager fm, List<? extends BaseFragment> fragments) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
    }

    public SimpleFragmentStatePagerAdapter(Context context, FragmentManager fm, BaseFragment ... fragments) {
        this(context, fm, Arrays.asList(fragments));
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getTitle(context);
    }
}
