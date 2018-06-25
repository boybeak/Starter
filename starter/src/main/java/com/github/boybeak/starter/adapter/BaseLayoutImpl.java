package com.github.boybeak.starter.adapter;

import com.github.boybeak.starter.adapter.AbsDataBindingHolder;
import com.github.boybeak.starter.adapter.LayoutImpl;

/**
 * Created by gaoyunfei on 2018/3/8.
 */

public abstract class BaseLayoutImpl<Data, VH extends AbsDataBindingHolder>
        implements com.github.boybeak.starter.adapter.LayoutImpl<Data, VH> {

    private Data data;

    public BaseLayoutImpl(Data data) {
        this.data = data;
    }

    @Override
    public Data getSource() {
        return data;
    }

    @Override
    public void setSource (Data data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof com.github.boybeak.starter.adapter.LayoutImpl) {
            return data.equals(((LayoutImpl) obj).getSource());
        }
        return false;
    }
}
