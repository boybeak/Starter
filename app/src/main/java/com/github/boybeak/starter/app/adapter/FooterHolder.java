package com.github.boybeak.starter.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.github.boybeak.starter.adapter.AbsDataBindingHolder;
import com.github.boybeak.starter.app.adapter.FooterImpl;
import com.github.boybeak.starter.app.databinding.LayoutAddFooterBinding;

import org.jetbrains.annotations.NotNull;

public class FooterHolder extends AbsDataBindingHolder<FooterImpl, LayoutAddFooterBinding>{

    public FooterHolder(@NotNull LayoutAddFooterBinding binding) {
        super(binding);
    }

    @Override
    public void onBindData(@NonNull Context context, @NonNull FooterImpl layout, int position, @NonNull RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        binding().setEvent(layout.getSource());
    }
}
