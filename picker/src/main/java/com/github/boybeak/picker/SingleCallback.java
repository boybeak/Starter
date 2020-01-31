package com.github.boybeak.picker;

import android.net.Uri;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public interface SingleCallback extends Callback {
    /**
     * @param id
     * @param uri
     * @param file
     * @param isMatch true if uri matches file, if can not get uri's real path, return a copy file path in cache/picker directory
     */
    void onGet(@NonNull String id, @NonNull Uri uri, @NonNull File file, boolean isMatch);
}
