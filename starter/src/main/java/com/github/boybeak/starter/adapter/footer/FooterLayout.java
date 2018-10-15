package com.github.boybeak.starter.adapter.footer;

import android.view.View;

import com.github.boybeak.adapter.extension.AbsFooterLayout;
import com.github.boybeak.adapter.extension.Footer;
import com.github.boybeak.starter.R;

/**
 * Created by gaoyunfei on 2018/3/9.
 */

public final class FooterLayout extends AbsFooterLayout {

    private View.OnClickListener actionListener;
    private String actionText;

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

    public View.OnClickListener getActionListener() {
        return actionListener;
    }

    public void setActionListener(View.OnClickListener actionListener) {
        this.actionListener = actionListener;
    }

    public String getActionText() {
        return actionText;
    }

    public void setActionText(String actionText) {
        this.actionText = actionText;
    }
}