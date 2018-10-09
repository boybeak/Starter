package com.github.boybeak.starter.adapter.footer;

import com.github.boybeak.adapter.AbsDataBindingHolder;
import com.github.boybeak.adapter.BaseLayoutImpl;

public abstract class AbsFooterLayout<VH extends AbsDataBindingHolder>
        extends BaseLayoutImpl<Footer, VH> {

    public AbsFooterLayout(Footer footer) {
        super(footer);
    }

    public AbsFooterLayout(String id, Footer footer) {
        super(id, footer);
    }

    @Override
    public final Footer getSource() {
        return super.getSource();
    }
}
