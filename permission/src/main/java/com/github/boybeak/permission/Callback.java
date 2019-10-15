package com.github.boybeak.permission;

import android.support.annotation.NonNull;

import java.util.List;

public interface Callback {
    void onGranted(@NonNull List<String> permissions);
    void onDenied(@NonNull String permission, boolean shouldShowRequestPermissionRationale);
}
