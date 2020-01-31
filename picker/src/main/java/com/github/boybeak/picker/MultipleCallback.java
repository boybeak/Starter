package com.github.boybeak.picker;

import android.net.Uri;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public interface MultipleCallback extends Callback {
    /**
     * @param id
     * @param uris
     * @param files
     * @param isMatchList true if uri matches file, if can not get uri's real path, return a copy file path in cache/picker directory
     */
    void onGet (@NonNull String id, @NonNull List<Uri> uris, @NonNull List<File> files, @NotNull List<Boolean> isMatchList);
}
