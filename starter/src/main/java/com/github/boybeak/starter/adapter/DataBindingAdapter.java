package com.github.boybeak.starter.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.boybeak.selector.Selector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by gaoyunfei on 2018/3/7.
 */

public class DataBindingAdapter extends AbsAdapter {

    private static final String TAG = DataBindingAdapter.class.getSimpleName();

    private List<LayoutImpl> mHeaderList = null;
    private List<LayoutImpl> mDataList = null;
    private List<LayoutImpl> mFooterList = null;

    private Context mContext;

    public DataBindingAdapter (Context context) {
        super(context);
        mContext = context;

        mDataList = new ArrayList<>();

    }

    @Override
    public void onViewAttachedToWindow(@NonNull AbsDataBindingHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.onViewAttachedToWindow();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull AbsDataBindingHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.onViewDetachedFromWindow();
    }

    public Context getContext () {
        return mContext;
    }

    public List<LayoutImpl> getDataList () {
        return mDataList;
    }

    public Selector<LayoutImpl> dataSelector () {
        return dataSelector(LayoutImpl.class);
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
    public LayoutImpl getItem(int position) {
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

    public boolean containsInHeader (Class<? extends LayoutImpl> clz) {
        if (mHeaderList == null) {
            return false;
        }
        for (LayoutImpl layout : mHeaderList) {
            if (clz.isInstance(layout)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsInData (Class<? extends LayoutImpl> clz) {
        for (LayoutImpl layout : getDataList()) {
            if (clz.isInstance(layout)) {
                return true;
            }
        }
        return false;
    }

    public int indexOfData (LayoutImpl layout) {
        return mDataList.indexOf(layout);
    }

    public <T> Selector<T> getDataSelector (Class<T> tClass) {
        return Selector.selector(tClass, mDataList);
    }

    public Selector<LayoutImpl> getDataSelector () {
        return Selector.selector(LayoutImpl.class, mDataList);
    }

    public DataChange add (LayoutImpl layout) {
        mDataList.add(layout);
        return new DataChange(this, getHeaderSize() + getDataSize() - 1, DataChange.TYPE_ITEM_INSERTED);
    }

    public DataChange add (int position, LayoutImpl layout) {
        mDataList.add(position - getHeaderSize(), layout);
        return new DataChange(this, position, DataChange.TYPE_ITEM_INSERTED);
    }

    public DataChange addIfNotExist (LayoutImpl layout) {
        if (mDataList.contains(layout)) {
            return DataChange.doNothingInstance();
        }
        return add(layout);
    }

    public DataChange addAll (Collection<LayoutImpl> layouts) {
        int start = getHeaderSize() + getDataSize();
        mDataList.addAll(layouts);
        return new DataChange(this, start, layouts.size(),
                DataChange.TYPE_ITEM_RANGE_INSERTED);
    }

    public <Data> DataChange addAll (Collection<Data> dataList, Parser<Data> parser) {
        List<LayoutImpl> layouts = new ArrayList<>();
        for (Data data : dataList) {
            layouts.add(parser.parse(data, this));
        }
        return addAll(layouts);
    }

    public <Data, Layout extends LayoutImpl> DataChange addAll (Collection<Data> dataList, Converter<Data, Layout> converter) {
        List<LayoutImpl> layouts = new ArrayList<>();
        for (Data data : dataList) {
            layouts.add(converter.convert(data, this));
        }
        return addAll(layouts);
    }

    public <Data> DataChange replaceFirst (Data from, Data to) {
        for (int i = 0; i < getDataSize(); i++) {
            LayoutImpl layout = mDataList.get(i);
            if (layout.getSource().equals(from)) {
                layout.setSource(to);
                return new DataChange(this, getAdapterPositionOfData(i), DataChange.TYPE_ITEM_CHANGED);
            }
        }
        return DataChange.doNothingInstance();
    }

    public DataChange remove (LayoutImpl layout) {
        int index = mDataList.indexOf(layout);
        if (mDataList.remove(layout)) {
            return new DataChange(this, getAdapterPositionOfData(index), DataChange.TYPE_ITEM_REMOVED);
        }
        return DataChange.doNothingInstance();
    }

    public <Data> DataChange remove (Data data) {
        int removeIndex = -1;
        for (int i = 0; i < getDataSize(); i++) {
            if (mDataList.get(i).getSource().equals(data)) {
                removeIndex = i;
                break;
            }
        }
        if (removeIndex >= 0) {
            mDataList.remove(removeIndex);
            return new DataChange(this, removeIndex, DataChange.TYPE_ITEM_REMOVED);
        }
        return DataChange.doNothingInstance();
    }

    public DataChange addHeader (LayoutImpl layout) {
        if (mHeaderList == null) {
            mHeaderList = new ArrayList<>();
        }
        mHeaderList.add(layout);
        return new DataChange(this, mHeaderList.indexOf(layout), DataChange.TYPE_ITEM_INSERTED);
    }

    public DataChange addHeaders (Collection<LayoutImpl> layouts) {
        if (mHeaderList == null) {
            mHeaderList = new ArrayList<>();
        }
        int start = mHeaderList.size();
        mHeaderList.addAll(layouts);
        return new DataChange(this, start, layouts.size(), DataChange.TYPE_ITEM_RANGE_INSERTED);
    }

    public <Data, Layout extends LayoutImpl> DataChange addHeaders (Collection<Data> dataCollection, Converter<Data, Layout> converter) {
        List<LayoutImpl> layouts = new ArrayList<>();
        for (Data data : dataCollection) {
            layouts.add(converter.convert(data, this));
        }
        return addHeaders(layouts);
    }

    public DataChange clearHeaders() {
        if (mHeaderList == null) {
            return DataChange.doNothingInstance();
        }
        int count = mHeaderList.size();
        mHeaderList.clear();
        return new DataChange(this, 0, count);
    }

    public DataChange removeHeader (LayoutImpl layout) {
        if (mHeaderList == null) {
            return DataChange.doNothingInstance();
        }
        int index = mHeaderList.indexOf(layout);
        if (index < 0) {
            return DataChange.doNothingInstance();
        }
        mHeaderList.remove(layout);
        return new DataChange(this, index, DataChange.TYPE_ITEM_REMOVED);
    }

    public DataChange addFooter (LayoutImpl layout) {
        if (mFooterList == null) {
            mFooterList = new ArrayList<>();
        }
        mFooterList.add(layout);
        return new DataChange(this, getItemCount(),
                DataChange.TYPE_ITEM_INSERTED);
    }

    public DataChange clearData () {
        int dataSize = getDataSize();
        mDataList.clear();
        return new DataChange(this, getHeaderSize(), dataSize, DataChange.TYPE_ITEM_RANGE_REMOVED);
    }

    public DataChange clear () {
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
        return new DataChange(this, 0, itemCount, DataChange.TYPE_ITEM_RANGE_REMOVED);
    }

    public int getAdapterPositionOfData (int positionInDataList) {
        return getHeaderSize() + positionInDataList;
    }

    public int getAdapterPositionOfData (LayoutImpl layout) {
        return getAdapterPositionOfData(indexOfData(layout));
    }

    public int getAdapterPositionOfFooter(int positionInFooterList) {
        return getHeaderSize() + getDataSize() + positionInFooterList;
    }

    public int getAdapterPositionOfFooter(LayoutImpl layout) {
        if(mFooterList == null) {
            return -1;
        }
        if(mFooterList.isEmpty() || !mFooterList.contains(layout)) {
            return -1;
        }
        return getAdapterPositionOfFooter(mFooterList.indexOf(layout));
    }

    public void notifyFooters () {
        notifyItemRangeChanged(getFooterStartPosition(), getFooterSize());
    }

}
