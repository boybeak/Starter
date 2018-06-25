package com.github.boybeak.starter.adapter;

import android.support.annotation.LayoutRes;

import com.github.boybeak.starter.adapter.AbsDataBindingHolder;

/**
 * Created by gaoyunfei on 2018/3/8.
 */

public interface LayoutImpl<Data, VH extends AbsDataBindingHolder> {
    Data getSource();
    void setSource(Data data);
    Class<VH> getHolderClass();
    @LayoutRes int getLayout();
}