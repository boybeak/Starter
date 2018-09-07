package com.github.boybeak.starter.adapter.footer;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ProgressBar;

import com.github.boybeak.starter.R;
import com.github.boybeak.starter.adapter.BaseLayoutImpl;

/**
 * Created by gaoyunfei on 2018/3/9.
 */

public final class FooterLayout extends AbsFooterLayout<FooterHolder> {

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


    public FooterLayout(Footer footer) {
        super(footer);
    }

    @Override
    public Class<FooterHolder> getHolderClass() {
        return FooterHolder.class;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_footer;
    }

}