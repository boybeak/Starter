package com.github.boybeak.starter.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.view.ViewGroup;

public abstract class PagerStateAdapter<T extends Fragment> extends FragmentStatePagerAdapter {

    private SparseArrayCompat<T> registeredFragments = new SparseArrayCompat<>();

    public PagerStateAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public abstract T getItem(int position);

    @Override
    public T instantiateItem(ViewGroup container, int position) {
        T t = (T)super.instantiateItem(container, position);
        registeredFragments.put(position, t);
        return t;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        registeredFragments.remove(position);
    }

    public T getFragmentAt (int position) {
        return registeredFragments.get(position);
    }

}