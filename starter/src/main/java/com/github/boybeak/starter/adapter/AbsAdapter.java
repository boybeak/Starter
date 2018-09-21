package com.github.boybeak.starter.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class AbsAdapter extends RecyclerView.Adapter<AbsDataBindingHolder> {

    private SparseArrayCompat<Class<? extends AbsDataBindingHolder>> mTypeHolderMap = null; // key -- layout, value -- holderClass

    private LayoutInflater mInflater;

    public AbsAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mTypeHolderMap = new SparseArrayCompat<>();
    }

    @Override
    public AbsDataBindingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(mInflater, viewType, parent, false);
        return getHolder(viewType, binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AbsDataBindingHolder holder, int position) {
        holder.bindData(getItem(position), position, this);
    }

    private AbsDataBindingHolder getHolder (int viewType, ViewDataBinding binding) {
        if (mTypeHolderMap.indexOfKey(viewType) >= 0) {
            Class<? extends AbsDataBindingHolder> clz = mTypeHolderMap.get(viewType);
            if (clz != null) {
                try {
                    Constructor<? extends AbsDataBindingHolder> constructor = clz.getConstructor(binding.getClass().getSuperclass());
                    return constructor.newInstance(binding);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
        throw new IllegalStateException("Can not create ViewHolder for viewType=" + Integer.toHexString(viewType));
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        LayoutImpl layout = getItem(position);
        int type = layout.getLayout();
        Class<? extends AbsDataBindingHolder> holderClz = layout.getHolderClass();
        if (holderClz == null) {
            throw new IllegalStateException("class not be defined by class(" + layout.getClass().getName()
                    + "), please define a layout resource id by getHolderClass");
        }
        if (type <= 0) {
            throw new IllegalStateException("layout not be defined by class(" + layout.getClass().getName()
                    + "), please define a layout resource id by getLayout");
        }
        if (mTypeHolderMap.indexOfKey(type) < 0) {
            mTypeHolderMap.put(type, holderClz);
        }
        return layout.getLayout();
    }

    public abstract @NonNull LayoutImpl getItem (int position);
    public abstract int index(LayoutImpl layout);
}
