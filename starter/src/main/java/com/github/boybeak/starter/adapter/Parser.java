package com.github.boybeak.starter.adapter;

import com.github.boybeak.starter.adapter.AbsDataBindingHolder;
import com.github.boybeak.starter.adapter.DataBindingAdapter;
import com.github.boybeak.starter.adapter.LayoutImpl;

/**
 * Created by gaoyunfei on 2018/3/7.
 */

public interface Parser<Data> {
    LayoutImpl<Data, ? extends AbsDataBindingHolder> parse (Data data, DataBindingAdapter adapter);
}
