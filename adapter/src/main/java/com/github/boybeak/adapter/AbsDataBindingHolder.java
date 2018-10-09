package com.github.boybeak.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

/**
 * Created by gaoyunfei on 2018/3/8.
 */

public abstract class AbsDataBindingHolder<Layout extends LayoutImpl, B extends ViewDataBinding> extends BaseDataBindingHolder<B> {

    public AbsDataBindingHolder(@NotNull B binding) {
        super(binding);
    }

    public final void bindData (Layout layout, int position, RecyclerView.Adapter adapter) {
        onBindData(binding().getRoot().getContext(), layout, position, adapter);
    }
    public abstract void onBindData (@NonNull Context context, @NonNull Layout layout, int position,
                                     @NonNull RecyclerView.Adapter<RecyclerView.ViewHolder> adapter);

    public void onViewAttachedToWindow(){}
    public void onViewDetachedFromWindow() {}
}
