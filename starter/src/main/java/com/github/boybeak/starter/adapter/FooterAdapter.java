package com.github.boybeak.starter.adapter;

import android.content.Context;
import android.support.annotation.StringRes;

import com.github.boybeak.adapter.DataBindingAdapter;
import com.github.boybeak.starter.R;
import com.github.boybeak.starter.adapter.footer.AbsFooterLayout;
import com.github.boybeak.starter.adapter.footer.Footer;
import com.github.boybeak.starter.adapter.footer.FooterLayout;

/**
 * Created by gaoyunfei on 2018/3/9.
 */

public class FooterAdapter extends DataBindingAdapter {

    private AbsFooterLayout mFooterImpl;

    public FooterAdapter(Context context, AbsFooterLayout footerLayout) {
        super(context);
        mFooterImpl = footerLayout;
        addFooter(mFooterImpl);
    }

    public FooterAdapter(Context context) {
        this(context, new FooterLayout(new Footer()));
    }

    public void notifyFooter (@Footer.State int state, String message) {
        mFooterImpl.getSource().setState(state);
        mFooterImpl.getSource().setMessage(message);
        notifyFooters();
    }

    public void notifyFooter (@Footer.State int state) {
        String msg = null;
        if (state == Footer.EMPTY && !isDataEmpty()) {
            msg = getContext().getString(R.string.text_no_more);
        }
        notifyFooter(state, msg);
    }

    public void notifyLoadingFooter () {
        notifyFooter(Footer.LOADING, null);
    }

    public void notifySuccessFooter(@StringRes int msgRes) {
        notifySuccessFooter(getContext().getString(msgRes));
    }

    public void notifySuccessFooter(@StringRes int msgRes, Object ... args) {
        notifySuccessFooter(getContext().getString(msgRes, args));
    }

    public void notifySuccessFooter (String message) {
        notifyFooter(Footer.SUCCESS, message);
    }

    public void notifySuccessFooter () {
        notifySuccessFooter(null);
    }

    public void notifyFailedFooter(@StringRes int msgRes) {
        notifyFailedFooter(getContext().getString(msgRes));
    }

    public void notifyFailedFooter(@StringRes int msgRes, Object ... args) {
        notifyFailedFooter(getContext().getString(msgRes, args));
    }

    public void notifyFailedFooter (String message) {
        notifyFooter(Footer.FAILED, message);
    }

    public void notifyFailedFooter () {
        notifyFailedFooter(null);
    }

    public void notifyEmptyFooter(@StringRes int msgRes) {
        notifyEmptyFooter(getContext().getString(msgRes));
    }

    public void notifyEmptyFooter(@StringRes int msgRes, Object ... args) {
        notifyEmptyFooter(getContext().getString(msgRes, args));
    }

    public void notifyEmptyFooter (String message) {
        notifyFooter(Footer.EMPTY, message);
    }

    public void notifyEmptyFooter () {
        notifyEmptyFooter(null);
    }

    public boolean isLoading () {
        return mFooterImpl.getSource().state == Footer.LOADING;
    }

}