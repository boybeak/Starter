package com.github.boybeak.starter.adapter;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewParent;
import android.widget.ProgressBar;

import com.github.boybeak.starter.R;
import com.github.boybeak.starter.adapter.DataBindingAdapter;
import com.github.boybeak.starter.adapter.footer.Footer;
import com.github.boybeak.starter.adapter.footer.FooterImpl;

/**
 * Created by gaoyunfei on 2018/3/9.
 */

public class FooterAdapter extends DataBindingAdapter {

    @BindingAdapter({"footerState", "footerMessage"})
    public static void setState (View view, @Footer.State int state, String message) {
        ProgressBar pb = view.findViewById(R.id.footer_pb);
        AppCompatTextView msgTv = view.findViewById(R.id.footer_msg);

        if (message == null) {
            Context context = view.getContext();
            switch (state) {
                case Footer.LOADING:
                    message = "";
                    break;
                case Footer.SUCCESS:
                    message = context.getString(R.string.text_done);
                    break;
                case Footer.FAILED:
                    message = context.getString(R.string.text_failed);
                    break;
                case Footer.EMPTY:
                    message = context.getString(R.string.text_no_more);
                    break;
            }
        }
        msgTv.setText(message);

        int pbV = View.GONE;
        int msgV = View.GONE;

        switch (state) {
            case Footer.LOADING:
                pbV = View.VISIBLE;
                msgV = View.GONE;
                break;
            case Footer.SUCCESS:
            case Footer.FAILED:
            case Footer.EMPTY:
                pbV = View.GONE;
                msgV = View.VISIBLE;
                break;
        }
        pb.setVisibility(pbV);
        msgTv.setVisibility(msgV);



    }

    private Footer mFooter;
    private FooterImpl mFooterImpl;

    public FooterAdapter(Context context) {
        super(context);
        mFooter = new Footer();
        mFooterImpl = new FooterImpl(mFooter);

        addFooter(mFooterImpl);

    }

    public void notifyFooter (@Footer.State int state, String message) {
        mFooter.setState(state);
        mFooter.setMessage(message);
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
        return mFooter.state == Footer.LOADING;
    }

}
