package com.github.boybeak.permission;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PH {

    private static final String KEY_ = "com.github.boybeak.permission.";
    public static final String KEY_ID = KEY_ + "ID", KEY_PERMISSION_LIST = KEY_ + "PERMISSION_LIST";

    private static Map<String, Callback> sKeyCallbackMap = new HashMap<>();

    static void actionGranted(String id, List<String> permissions) {
        Callback callback = sKeyCallbackMap.get(id);
        if (callback != null) {
            callback.onGranted(permissions);
        }
        releaseCallback(id);
    }

    static void actionDenied(String id, String permission) {
        Callback callback = sKeyCallbackMap.get(id);
        if (callback != null) {
            callback.onDenied(permission);
        }
        releaseCallback(id);
    }

    public static boolean isPermissionGranted(Context context, String ... permissions) {
        for (String p : permissions) {
            int result = ContextCompat.checkSelfPermission(context, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private static void releaseCallback(String id) {
        sKeyCallbackMap.remove(id);
    }

    public static PH ask(String ... permissions) {
        return ask(Arrays.asList(permissions));
    }

    public static PH ask(List<String> permissions) {
        PH ph = new PH();
        ph.permissions.addAll(permissions);
        return ph;
    }

    private ArrayList<String> permissions;

    private PH() {
        this.permissions = new ArrayList<>();
    }

    public void go(Context context, Callback callback) {

        String id = UUID.randomUUID().toString();

        Intent it = new Intent(context, PermissionAgentActivity.class);
        it.putExtra(KEY_ID, id);
        it.putStringArrayListExtra(KEY_PERMISSION_LIST, permissions);
        context.startActivity(it);

        sKeyCallbackMap.put(id, callback);

    }

}