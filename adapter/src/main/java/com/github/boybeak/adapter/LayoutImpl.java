package com.github.boybeak.adapter;

import android.support.annotation.LayoutRes;

/**
 * Created by gaoyunfei on 2018/3/8.
 */

public interface LayoutImpl<Data, VH extends AbsDataBindingHolder> {
    String id();
    void setId(String id);
    Data getSource();
    void setSource(Data data);
    <T> T getSourceUnSafe();
    Class<VH> getHolderClass();
    @LayoutRes int getLayout();

    void setSelected(boolean selected);
    boolean isSelected();

    void setSelectable(boolean selectable);
    boolean isSelectable();

    boolean supportSelect();

}