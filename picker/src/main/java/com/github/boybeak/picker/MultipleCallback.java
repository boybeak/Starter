package com.github.boybeak.picker;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.List;

public interface MultipleCallback extends Callback {
    void onGet (@NonNull String id, @NonNull List<Uri> uris, @NonNull List<File> files);
}
