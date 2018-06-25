package com.github.boybeak.starter.adapter.card;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.boybeak.starter.R;
import com.github.boybeak.starter.adapter.DataChange;
import com.github.boybeak.starter.adapter.card.CardAdapter;
import com.github.boybeak.starter.adapter.card.CardListHolder;
import com.github.boybeak.starter.adapter.expandable.Group;
import com.github.boybeak.starter.adapter.expandable.GroupList;
import com.github.boybeak.starter.adapter.footer.Footer;
import com.github.boybeak.starter.adapter.footer.FooterHolder;
import com.github.boybeak.starter.adapter.footer.FooterImpl;
import com.github.boybeak.starter.databinding.LayoutFooterBinding;

import java.util.ArrayList;
import java.util.List;

public class GroupCardAdapter extends RecyclerView.Adapter {

    private static final String TAG = GroupCardAdapter.class.getSimpleName();

    private static final int ITEM_TYPE_CARD_LIST = 0, ITEM_TYPE_FOOTER = 1;

    private Context mContext;

    private LayoutInflater mInflater = null;

    private GroupList mGroupList = null;

    private Footer mFooter;
    private FooterImpl mFooterImpl;

    public GroupCardAdapter(Context context, boolean footerEnable) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mGroupList = new GroupList();
        if (footerEnable) {
            mFooter = new Footer();
            mFooterImpl = new FooterImpl(mFooter);
        }
    }

    public GroupCardAdapter (Context context) {
        this(context, false);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_CARD_LIST:
                return new com.github.boybeak.starter.adapter.card.CardListHolder(mInflater.inflate(R.layout.layout_card_list_holder, parent, false));
            default:
                return new FooterHolder(LayoutFooterBinding.inflate(mInflater, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isCardPosition(position)) {
            com.github.boybeak.starter.adapter.card.CardListHolder cardListHolder = (CardListHolder)holder;
            Group group = mGroupList.get(position);
            com.github.boybeak.starter.adapter.card.CardAdapter adapter;
            if (cardListHolder.recyclerView.getAdapter() == null) {
                adapter = new com.github.boybeak.starter.adapter.card.CardAdapter(cardListHolder.recyclerView.getContext(), group);
            } else {
                adapter = (CardAdapter) cardListHolder.recyclerView.getAdapter();
                adapter.setGroup(group);
            }
            cardListHolder.recyclerView.setAdapter(adapter);
        } else {
            FooterHolder footerHolder = (FooterHolder)holder;
            footerHolder.onBindData(holder.itemView.getContext(), mFooterImpl, position, this);
        }
    }

    /*@Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }*/

    @Override
    public int getItemCount() {
        int count = mGroupList.size();
        if (isFooterEnable()) {
            count += 1;
        }
        return count;
    }

    public boolean isEmpty () {
        return mGroupList.isEmpty();
    }

    @Override
    public int getItemViewType(int position) {
        if (isCardPosition(position)) {
            return ITEM_TYPE_CARD_LIST;
        }
        return ITEM_TYPE_FOOTER;
    }

    private boolean isCardPosition (int position) {
        if (!isFooterEnable()) {
            return true;
        }
        return position < getItemCount() - 1;
    }

    private boolean isFooterEnable () {
        return mFooter != null;
    }

    public Group getGroup (int position) {
        return mGroupList.get(position);
    }

    public DataChange addGroup (Group group) {
        mGroupList.add(group);
        return new DataChange(this, mGroupList.size() - 1, DataChange.TYPE_ITEM_INSERTED);
    }

    public <T> DataChange addGroup (T t, GroupParser<T> parser) {
        return addGroup(parser.parse(t));
    }

    public DataChange removeGroup (int position) {
        mGroupList.remove(position);
        return new DataChange(this, position, DataChange.TYPE_ITEM_REMOVED);
    }

    public DataChange addGroups (List<Group> groups) {
        mGroupList.addAll(groups);
        return new DataChange(this, mGroupList.size() - groups.size(), groups.size(), DataChange.TYPE_ITEM_RANGE_INSERTED);
    }

    public <T> DataChange addGroups (List<T> groups, GroupParser<T> parser) {
        List<Group> groupList = new ArrayList<>();
        for (T t : groups) {
            groupList.add(parser.parse(t));
        }
        return addGroups(groupList);
    }

    public DataChange removeItemInGroup (int groupPosition, int itemPosition) {
        Group group = mGroupList.get(groupPosition);
        group.remove(itemPosition);
        return new DataChange (this, groupPosition, DataChange.TYPE_ITEM_CHANGED);
    }

    public interface GroupParser<T> {
        Group parse(T t);
    }

    public void notifyFooter (@Footer.State int state, String message) {
        if (!isFooterEnable()) {
            return;
        }
        mFooter.setState(state);
        mFooter.setMessage(message);
        notifyItemChanged(getItemCount() - 1);
    }

    public void notifyFooter (@Footer.State int state) {
        if (!isFooterEnable()) {
            return;
        }
        String msg = null;
        if (state == Footer.EMPTY && !mGroupList.isEmpty()) {
            msg = getContext().getString(R.string.text_no_more);
        }
        notifyFooter(state, msg);
    }

    public void notifyLoadingFooter () {
        notifyFooter(Footer.LOADING, null);
    }

    public void notifySuccessFooter (@StringRes int msgRes) {
        notifySuccessFooter(getContext().getString(msgRes));
    }

    public void notifySuccessFooter (String message) {
        notifyFooter(Footer.SUCCESS, message);
    }

    public void notifySuccessFooter () {
        notifySuccessFooter(null);
    }

    public void notifyFailedFooter (String message) {
        notifyFooter(Footer.FAILED, message);
    }

    public void notifyFailedFooter () {
        notifyFailedFooter(null);
    }

    public void notifyEmptyFooter (String message) {
        notifyFooter(Footer.EMPTY, message);
    }

    public void notifyEmptyFooter () {
        notifyEmptyFooter(null);
    }

    public Context getContext() {
        return mContext;
    }
}
