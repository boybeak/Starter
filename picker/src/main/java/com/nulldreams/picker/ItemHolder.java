package com.nulldreams.picker;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemHolder extends RecyclerView.ViewHolder {

    public AppCompatImageView thumb, flag;

    public ItemHolder(View itemView) {
        super(itemView);

        thumb = itemView.findViewById(R.id.item_thumb);
        flag = itemView.findViewById(R.id.item_check_flag);
    }
}
