package com.github.boybeak.starter.app.adapter;

import android.content.Context;
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
    public void onBindData(Context context, FooterImpl layout, int position, RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        binding().setEvent(layout.getSource());
    }
}
