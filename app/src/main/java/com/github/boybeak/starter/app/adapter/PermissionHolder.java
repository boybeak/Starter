package com.github.boybeak.starter.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.boybeak.adapter.AbsDataBindingHolder;
import com.github.boybeak.starter.app.databinding.LayoutPermissionBinding;

import org.jetbrains.annotations.NotNull;

public class PermissionHolder extends AbsDataBindingHolder<PermissionImpl, LayoutPermissionBinding> {
    public PermissionHolder(@NotNull LayoutPermissionBinding binding) {
        super(binding);
    }

    @Override
    public void onBindData(Context context, PermissionImpl layout, int position, RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        binding().setPermission(layout.getSource());
        binding().setIsChecked(layout.isChecked());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setChecked(!layout.isChecked());
                adapter.notifyItemChanged(position);
            }
        });
    }
}
