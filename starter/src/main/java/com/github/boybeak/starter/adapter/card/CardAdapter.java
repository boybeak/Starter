package com.github.boybeak.starter.adapter.card;

import android.content.Context;

import com.github.boybeak.starter.adapter.AbsAdapter;
import com.github.boybeak.starter.adapter.LayoutImpl;
import com.github.boybeak.starter.adapter.expandable.Group;

public class CardAdapter extends AbsAdapter {

    private Group mGroup;

    public CardAdapter(Context context, Group group) {
        super(context);
        mGroup = group;
    }

    public CardAdapter(Context context) {
        super(context);
        mGroup = null;
    }

    @Override
    public LayoutImpl getItem(int position) {
        return mGroup.get(position);
    }

    @Override
    public int index(LayoutImpl layout) {
        return mGroup.indexOf(layout);
    }


    @Override
    public int getItemCount() {
        return mGroup.size();
    }

    public void setGroup (Group group) {
        mGroup = group;
    }
}