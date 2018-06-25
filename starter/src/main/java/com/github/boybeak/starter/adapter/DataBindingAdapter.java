package com.github.boybeak.starter.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.boybeak.selector.Selector;
import com.github.boybeak.starter.adapter.AbsAdapter;
import com.github.boybeak.starter.adapter.AbsDataBindingHolder;
import com.github.boybeak.starter.adapter.Converter;
import com.github.boybeak.starter.adapter.DataChange;
import com.github.boybeak.starter.adapter.LayoutImpl;
import com.github.boybeak.starter.adapter.Parser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * Created by gaoyunfei on 2018/3/7.
 */

public class DataBindingAdapter extends AbsAdapter {

    private static final String TAG = DataBindingAdapter.class.getSimpleName();

    private List<com.github.boybeak.starter.adapter.LayoutImpl> mHeaderList = null;
    private List<com.github.boybeak.starter.adapter.LayoutImpl> mDataList = null;
    private List<com.github.boybeak.starter.adapter.LayoutImpl> mFooterList = null;

    private Context mContext;

    public DataBindingAdapter (Context context) {
        super(context);
        mContext = context;

        mDataList = new ArrayList<>();

    }

    @Override
    public void onViewAttachedToWindow(AbsDataBindingHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.onViewAttachedToWindow();
    }

    @Override
    public void onViewDetachedFromWindow(AbsDataBindingHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.onViewDetachedFromWindow();
    }

    public Context getContext () {
        return mContext;
    }

    public List<com.github.boybeak.starter.adapter.LayoutImpl> getDataList () {
        return mDataList;
    }

    public Selector<com.github.boybeak.starter.adapter.LayoutImpl> dataSelector () {
        return dataSelector(com.github.boybeak.starter.adapter.LayoutImpl.class);
    }

    public <Layout> Selector<Layout> dataSelector (Class<Layout> clz) {
        return Selector.selector(clz, mDataList);
    }

    @Override
    public int getItemCount() {
        return getHeaderSize() + getDataSize() + getFooterSize();
    }

    public int getHeaderSize() {
        if (mHeaderList == null) {
            return 0;
        }
        return mHeaderList.size();
    }

    public int getDataSize() {
        return mDataList.size();
    }

    public int getFooterSize() {
        if (mFooterList == null) {
            return 0;
        }
        return mFooterList.size();
    }

    /*@Override
    public long getItemId(int position) {
        return position;
    }*/

    @Override
    public com.github.boybeak.starter.adapter.LayoutImpl getItem(int position) {
        if (position >= 0 && position < getHeaderSize()) {
            return mHeaderList.get(position);
        } else if (position >= getHeaderSize() && position < getHeaderSize() + getDataSize()) {
            return mDataList.get(position - getHeaderSize());
        } else {
            return mFooterList.get(position - getHeaderSize() - getDataSize());
        }
    }

    private int getFooterStartPosition () {
        return getHeaderSize() + getDataSize();
    }

    public boolean isEmpty () {
        return getItemCount() == 0;
    }

    public boolean isDataEmpty () {
        return mDataList.isEmpty();
    }

    public boolean containsInHeader (Class<? extends com.github.boybeak.starter.adapter.LayoutImpl> clz) {
        if (mHeaderList == null) {
            return false;
        }
        for (com.github.boybeak.starter.adapter.LayoutImpl layout : mHeaderList) {
            if (clz.isInstance(layout)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsInData (Class<? extends com.github.boybeak.starter.adapter.LayoutImpl> clz) {
        for (com.github.boybeak.starter.adapter.LayoutImpl layout : getDataList()) {
            if (clz.isInstance(layout)) {
                return true;
            }
        }
        return false;
    }

    public int indexOfData (com.github.boybeak.starter.adapter.LayoutImpl layout) {
        return mDataList.indexOf(layout);
    }

    public <T> Selector<T> getDataSelector (Class<T> tClass) {
        return Selector.selector(tClass, mDataList);
    }

    public Selector<com.github.boybeak.starter.adapter.LayoutImpl> getDataSelector () {
        return Selector.selector(com.github.boybeak.starter.adapter.LayoutImpl.class, mDataList);
    }

    public com.github.boybeak.starter.adapter.DataChange add (com.github.boybeak.starter.adapter.LayoutImpl layout) {
        mDataList.add(layout);
        return new com.github.boybeak.starter.adapter.DataChange(this, getHeaderSize() + getDataSize() - 1, com.github.boybeak.starter.adapter.DataChange.TYPE_ITEM_INSERTED);
    }

    public com.github.boybeak.starter.adapter.DataChange add (int position, com.github.boybeak.starter.adapter.LayoutImpl layout) {
        mDataList.add(position - getHeaderSize(), layout);
        return new com.github.boybeak.starter.adapter.DataChange(this, position, com.github.boybeak.starter.adapter.DataChange.TYPE_ITEM_INSERTED);
    }

    public com.github.boybeak.starter.adapter.DataChange addIfNotExist (com.github.boybeak.starter.adapter.LayoutImpl layout) {
        if (mDataList.contains(layout)) {
            return com.github.boybeak.starter.adapter.DataChange.doNothingInstance();
        }
        return add(layout);
    }

    public com.github.boybeak.starter.adapter.DataChange addAll (Collection<com.github.boybeak.starter.adapter.LayoutImpl> layouts) {
        int start = getHeaderSize() + getDataSize();
        mDataList.addAll(layouts);
        return new com.github.boybeak.starter.adapter.DataChange(this, start, layouts.size(),
                com.github.boybeak.starter.adapter.DataChange.TYPE_ITEM_RANGE_INSERTED);
    }

    public <Data> com.github.boybeak.starter.adapter.DataChange addAll (Collection<Data> dataList, Parser<Data> parser) {
        List<com.github.boybeak.starter.adapter.LayoutImpl> layouts = new ArrayList<>();
        for (Data data : dataList) {
            layouts.add(parser.parse(data, this));
        }
        return addAll(layouts);
    }

    public <Data, Layout extends com.github.boybeak.starter.adapter.LayoutImpl> com.github.boybeak.starter.adapter.DataChange addAll (List<Data> dataList, Converter<Data, Layout> converter) {
        List<com.github.boybeak.starter.adapter.LayoutImpl> layouts = new ArrayList<>();
        for (Data data : dataList) {
            layouts.add(converter.convert(data, this));
        }
        return addAll(layouts);
    }

    public <Data> com.github.boybeak.starter.adapter.DataChange replaceFirst (Data from, Data to) {
        for (int i = 0; i < getDataSize(); i++) {
            com.github.boybeak.starter.adapter.LayoutImpl layout = mDataList.get(i);
            if (layout.getSource().equals(from)) {
                layout.setSource(to);
                return new com.github.boybeak.starter.adapter.DataChange(this, getAdapterPositionOfData(i), com.github.boybeak.starter.adapter.DataChange.TYPE_ITEM_CHANGED);
            }
        }
        return com.github.boybeak.starter.adapter.DataChange.doNothingInstance();
    }

    public com.github.boybeak.starter.adapter.DataChange remove (com.github.boybeak.starter.adapter.LayoutImpl layout) {
        int index = mDataList.indexOf(layout);
        if (mDataList.remove(layout)) {
            return new com.github.boybeak.starter.adapter.DataChange(this, getAdapterPositionOfData(index), com.github.boybeak.starter.adapter.DataChange.TYPE_ITEM_REMOVED);
        }
        return com.github.boybeak.starter.adapter.DataChange.doNothingInstance();
    }

    public <Data> com.github.boybeak.starter.adapter.DataChange remove (Data data) {
        int removeIndex = -1;
        for (int i = 0; i < getDataSize(); i++) {
            if (mDataList.get(i).getSource().equals(data)) {
                removeIndex = i;
                break;
            }
        }
        if (removeIndex >= 0) {
            mDataList.remove(removeIndex);
            return new com.github.boybeak.starter.adapter.DataChange(this, removeIndex, com.github.boybeak.starter.adapter.DataChange.TYPE_ITEM_REMOVED);
        }
        return com.github.boybeak.starter.adapter.DataChange.doNothingInstance();
    }

    public com.github.boybeak.starter.adapter.DataChange addHeader (com.github.boybeak.starter.adapter.LayoutImpl layout) {
        if (mHeaderList == null) {
            mHeaderList = new ArrayList<>();
        }
        mHeaderList.add(layout);
        return new com.github.boybeak.starter.adapter.DataChange(this, mHeaderList.indexOf(layout), com.github.boybeak.starter.adapter.DataChange.TYPE_ITEM_INSERTED);
    }

    public com.github.boybeak.starter.adapter.DataChange removeHeader (com.github.boybeak.starter.adapter.LayoutImpl layout) {
        if (mHeaderList == null) {
            return com.github.boybeak.starter.adapter.DataChange.doNothingInstance();
        }
        int index = mHeaderList.indexOf(layout);
        if (index < 0) {
            return com.github.boybeak.starter.adapter.DataChange.doNothingInstance();
        }
        mHeaderList.remove(layout);
        return new com.github.boybeak.starter.adapter.DataChange(this, index, com.github.boybeak.starter.adapter.DataChange.TYPE_ITEM_REMOVED);
    }

    public com.github.boybeak.starter.adapter.DataChange addFooter (com.github.boybeak.starter.adapter.LayoutImpl layout) {
        if (mFooterList == null) {
            mFooterList = new ArrayList<>();
        }
        mFooterList.add(layout);
        return new com.github.boybeak.starter.adapter.DataChange(this, getItemCount(),
                com.github.boybeak.starter.adapter.DataChange.TYPE_ITEM_INSERTED);
    }

    public com.github.boybeak.starter.adapter.DataChange clearData () {
        int dataSize = getDataSize();
        mDataList.clear();
        return new com.github.boybeak.starter.adapter.DataChange(this, getHeaderSize(), dataSize, com.github.boybeak.starter.adapter.DataChange.TYPE_ITEM_RANGE_REMOVED);
    }

    public com.github.boybeak.starter.adapter.DataChange clear () {
        int itemCount = getItemCount();
        if (mHeaderList != null && !mHeaderList.isEmpty()) {
            mHeaderList.clear();
        }
        if (!mDataList.isEmpty()) {
            mDataList.clear();
        }
        if (mFooterList != null && !mFooterList.isEmpty()) {
            mFooterList.clear();
        }
        return new com.github.boybeak.starter.adapter.DataChange(this, 0, itemCount, DataChange.TYPE_ITEM_RANGE_REMOVED);
    }

    public int getAdapterPositionOfData (int positionInDataList) {
        return getHeaderSize() + positionInDataList;
    }

    public int getAdapterPositionOfData (LayoutImpl layout) {
        return getAdapterPositionOfData(indexOfData(layout));
    }

    public void notifyFooters () {
        notifyItemRangeChanged(getFooterStartPosition(), getFooterSize());
    }

}
