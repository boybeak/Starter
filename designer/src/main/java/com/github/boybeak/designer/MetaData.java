package com.github.boybeak.designer;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class MetaData {

    public static <T> T getValue(Context context, String key, T defaultValue) {
        ApplicationInfo ai = null;
        try {
            ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            Object obj = bundle.get(key);
            return (T)obj;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static String getClientId(Context context) {
        return getValue(context, "CLIENT_ID", "");
    }

    public static String getClientSecret(Context context) {
        return getValue(context, "CLIENT_SECRET", "");
    }

    public static String getRedirectUri(Context context) {
        return getValue(context, "REDIRECT_URI", "");
    }

}
