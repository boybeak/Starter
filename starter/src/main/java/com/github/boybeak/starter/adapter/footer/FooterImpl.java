package com.github.boybeak.starter.adapter.footer;

import com.github.boybeak.starter.R;
import com.github.boybeak.starter.adapter.BaseLayoutImpl;
import com.github.boybeak.starter.adapter.footer.Footer;
import com.github.boybeak.starter.adapter.footer.FooterHolder;

/**
 * Created by gaoyunfei on 2018/3/9.
 */

public class FooterImpl extends BaseLayoutImpl<com.github.boybeak.starter.adapter.footer.Footer, FooterHolder> {

    public FooterImpl(Footer footer) {
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
