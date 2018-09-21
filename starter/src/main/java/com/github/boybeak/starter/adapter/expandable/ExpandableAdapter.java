package com.github.boybeak.starter.adapter.expandable;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.boybeak.starter.adapter.AbsAdapter;
import com.github.boybeak.starter.adapter.AbsDataBindingHolder;
import com.github.boybeak.starter.adapter.LayoutImpl;
import com.github.boybeak.starter.adapter.expandable.Group;
import com.github.boybeak.starter.adapter.expandable.GroupList;

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