package com.github.boybeak.starter.app.adapter;

import com.github.boybeak.starter.adapter.BaseLayoutImpl;
import com.github.boybeak.starter.app.R;

public class PermissionImpl extends BaseLayoutImpl<String, PermissionHolder> {

    private boolean isChecked;

    public PermissionImpl(String s) {
        super(s);
    }

    @Override
    public Class<PermissionHolder> getHolderClass() {
        return PermissionHolder.class;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_permission;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
