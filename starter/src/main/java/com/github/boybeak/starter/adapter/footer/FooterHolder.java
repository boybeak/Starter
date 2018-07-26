package com.github.boybeak.starter.adapter.footer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import com.github.boybeak.starter.adapter.AbsAdapter;
import com.github.boybeak.starter.adapter.AbsDataBindingHolder;
import com.github.boybeak.starter.adapter.DataBindingAdapter;
import com.github.boybeak.starter.adapter.footer.FooterImpl;
import com.github.boybeak.starter.databinding.LayoutFooterBinding;

import org.jetbrains.annotations.NotNull;

/**
 * Created by gaoyunfei on 2018/3/9.
 */

public class FooterHolder extends AbsDataBindingHolder<com.github.boybeak.starter.adapter.footer.FooterImpl, LayoutFooterBinding> {

    public FooterHolder(@NotNull LayoutFooterBinding binding) {
        super(binding);
    }

    @Override
    public void onBindData(@NonNull Context context, @NonNull FooterImpl layout, int position, @NonNull RecyclerView.Adapter adapter) {
        binding().setFooter(layout.getSource());
    }
}
