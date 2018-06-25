package com.github.boybeak.starter.adapter;

import com.github.boybeak.starter.adapter.DataBindingAdapter;
import com.github.boybeak.starter.adapter.LayoutImpl;

public interface Converter<Data, Layout extends LayoutImpl> {
    Layout convert (Data data, DataBindingAdapter adapter);
}
