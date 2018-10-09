package com.github.boybeak.adapter;

/**
 * Created by gaoyunfei on 2018/3/7.
 */

public interface Parser<Data> {
    LayoutImpl<Data, ? extends AbsDataBindingHolder> parse (Data data, DataBindingAdapter adapter);
}
