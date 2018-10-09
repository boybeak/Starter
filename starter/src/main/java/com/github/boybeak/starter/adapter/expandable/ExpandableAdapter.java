package com.github.boybeak.starter.adapter.expandable;

import android.content.Context;

import com.github.boybeak.adapter.AbsAdapter;
import com.github.boybeak.adapter.LayoutImpl;

/**
 * Created by gaoyunfei on 2018/3/28.
 */

public class ExpandableAdapter extends AbsAdapter {


    private GroupList mGroupList = null;

    public ExpandableAdapter(Context context) {
        super(context);
        mGroupList = new GroupList();
    }

    @Override
    public int getItemCount() {
        return mGroupList.getItemCount();
    }

    @Override
    public LayoutImpl getItem(int position) {
        return mGroupList.getItem(position);
    }

    @Override
    public int index(LayoutImpl layout) {
        return mGroupList.indexOf(layout);
    }

    public void addGroup (Group group) {
        mGroupList.add(group);
    }

}