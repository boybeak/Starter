package com.github.boybeak.starter.app.adapter;

import com.github.boybeak.adapter.AbsLayoutImpl;
import com.github.boybeak.starter.app.FileHolder;
import com.github.boybeak.starter.app.R;

import java.io.File;

public class FileImpl extends AbsLayoutImpl<File> {
    public FileImpl(File file) {
        super(file);
    }

    @Override
    public Class<FileHolder> getHolderClass() {
        return FileHolder.class;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_file;
    }
}
