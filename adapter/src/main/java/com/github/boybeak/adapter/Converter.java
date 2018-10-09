package com.github.boybeak.adapter;

import android.support.annotation.NonNull;

public interface Converter<Data, Layout extends LayoutImpl> {
    Layout convert (Data data, @NonNull DataBindingAdapter adapter);
}
