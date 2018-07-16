package com.github.boybeak.starter.app.adapter;

import com.github.boybeak.starter.adapter.BaseLayoutImpl;
import com.github.boybeak.starter.app.FooterEvent;
import com.github.boybeak.starter.app.R;

public class FooterImpl extends BaseLayoutImpl<FooterEvent, FooterHolder> {
    public FooterImpl(FooterEvent event) {
        super(event);
    }

    @Override
    public Class<FooterHolder> getHolderClass() {
        return FooterHolder.class;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_add_footer;
    }
}
