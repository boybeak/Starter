package com.github.boybeak.starter.adapter;

import android.support.annotation.NonNull;

import java.util.Collection;

public interface ListConverter<Data> {
    Collection<LayoutImpl> convert(Data data, @NonNull DataBindingAdapter adapter);
}
