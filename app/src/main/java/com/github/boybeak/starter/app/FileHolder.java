package com.github.boybeak.starter.app;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.boybeak.adapter.AbsDataBindingHolder;
import com.github.boybeak.starter.app.adapter.FileImpl;
import com.github.boybeak.starter.app.databinding.LayoutFileBinding;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class FileHolder extends AbsDataBindingHolder<FileImpl, LayoutFileBinding> {

    @BindingAdapter({"thumb"})
    public static void thumb(AppCompatImageView iv, File file) {
        Glide.with(iv.getContext()).load(file).into(iv);
    }

    public FileHolder(@NotNull LayoutFileBinding binding) {
        super(binding);
    }

    @Override
    public void onBindData(Context context, FileImpl layout, int position, RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        binding().setFile(layout.getSource());
    }
}
