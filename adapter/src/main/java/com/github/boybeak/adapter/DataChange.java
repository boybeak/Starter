package com.github.boybeak.adapter;

import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * Created by gaoyunfei on 2017/4/27.
 */

public final class DataChange {

    public static final int
            TYPE_ITEM_JUST_NOTIFY = 0,
            TYPE_ITEM_INSERTED = 1,
            TYPE_ITEM_RANGE_INSERTED = 2,
            TYPE_ITEM_CHANGED = 3,
            TYPE_ITEM_RANGE_CHANGED = 4,
            TYPE_ITEM_REMOVED = 5,
            TYPE_ITEM_RANGE_REMOVED = 6,
            TYPE_ITEM_MOVED = 7;

    public static DataChange doNothingInstance () {
        return new DataChange(true);
    }

    public static DataChange notifyDataSetChangeInstance (RecyclerView.Adapter adapter) {
        return new DataChange(adapter, 0, TYPE_ITEM_JUST_NOTIFY);
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            TYPE_ITEM_JUST_NOTIFY,
            TYPE_ITEM_INSERTED,
            TYPE_ITEM_RANGE_INSERTED,
            TYPE_ITEM_CHANGED,
            TYPE_ITEM_RANGE_CHANGED,
            TYPE_ITEM_REMOVED,
            TYPE_ITEM_RANGE_REMOVED,
            TYPE_ITEM_MOVED
    })
    public @interface Type{}

    private RecyclerView.Adapter mAdapter;
    private int fromPosition, itemCountOrToPosition;
    private @Type int type;
    private boolean doNothing = false;

    private List payloads;

    /**
     * @param adapter the adapter
     * @param from the from position
     * @param countOrToPosition if {@link DataChange#type} is {@link DataChange#TYPE_ITEM_MOVED},
     *                          this params is toPosition, else is itemCount. And this will not work for
     *                          {@link DataChange#TYPE_ITEM_INSERTED},{@link DataChange#TYPE_ITEM_CHANGED},
     *                          and {@link DataChange#TYPE_ITEM_REMOVED}
     * @param type type of change
     */
    public DataChange (RecyclerView.Adapter adapter, int from, int countOrToPosition, @Type int type) {
        this (adapter, from, countOrToPosition, type, null);
    }

    public DataChange (RecyclerView.Adapter adapter, int from, int countOrToPosition, @Type int type, List payloads) {
        mAdapter = adapter;
        this.fromPosition = from;
        this.itemCountOrToPosition = countOrToPosition;
        this.type = type;
        this.payloads = payloads;
    }

    public DataChange(RecyclerView.Adapter adapter, int from, @Type int type) {
        this(adapter, from, 0, type);
    }

    private DataChange (boolean doNothing) {
        this.doNothing = doNothing;
    }

    public RecyclerView.Adapter autoNotify () {
        RecyclerView.Adapter adapter = mAdapter;
        mAdapter = null;
        if (doNothing) {
            return adapter;
        }
        switch (type) {
            case TYPE_ITEM_INSERTED:
                adapter.notifyItemInserted(fromPosition);
                break;
            case TYPE_ITEM_RANGE_INSERTED:
                adapter.notifyItemRangeInserted(fromPosition, itemCountOrToPosition);
                break;
            case TYPE_ITEM_CHANGED:
                adapter.notifyItemChanged(fromPosition, payloads);
                break;
            case TYPE_ITEM_RANGE_CHANGED:
                adapter.notifyItemRangeChanged(fromPosition, itemCountOrToPosition, payloads);
                break;
            case TYPE_ITEM_REMOVED:
                adapter.notifyItemRemoved(fromPosition);
                break;
            case TYPE_ITEM_RANGE_REMOVED:
                adapter.notifyItemRangeRemoved(fromPosition, itemCountOrToPosition);
                break;
            case TYPE_ITEM_MOVED:
                adapter.notifyItemMoved(fromPosition, itemCountOrToPosition);
                break;
            default:
                adapter.notifyDataSetChanged();
                break;
        }

        return adapter;
    }

    public RecyclerView.Adapter notifyDataSetChanged () {
        mAdapter.notifyDataSetChanged();
        return mAdapter;
    }

    public RecyclerView.Adapter dontNotifyChanged () {
        return mAdapter;
    }
}