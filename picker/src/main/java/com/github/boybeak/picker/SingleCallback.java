package com.github.boybeak.picker;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;

public interface SingleCallback extends Callback {
    void onGet(@NonNull String id, @NonNull Uri uri, @NonNull File file);
}
