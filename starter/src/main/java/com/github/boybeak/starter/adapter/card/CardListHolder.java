package com.github.boybeak.starter.adapter.card;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.boybeak.starter.R;

public class CardListHolder extends RecyclerView.ViewHolder {

    public RecyclerView recyclerView;

    public CardListHolder(View itemView) {
        super(itemView);

        recyclerView = itemView.findViewById(R.id.recycler_view);
    }
}
