package com.github.boybeak.picker;

import android.content.ComponentName;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.IntDef;
import android.support.annotation.StringDef;
import android.util.Log;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class Picker {

    private static final String TAG = Picker.class.getSimpleName();

    private static Map<String, Callback> sIdCallbackMap = new HashMap<>();

    public static Gallery gallery() {
        return new Gallery();
    }

    public static Camera camera() {
        return new Camera();
    }

    static final int MODE_GALLERY = 1, MODE_CAMERA = 2;

    private static final String KEY_ = "com.github.boybeak.picker.";
    public static final String KEY_ID = KEY_ + "ID", KEY_ALLOW_MULTIPLE = KEY_ + "ALLOW_MULTIPLE",
            KEY_MIME = KEY_ + "MIME", KEY_MIME_TYPE = KEY_ + "MIME_TYPE", KEY_MODE = KEY_ + "MODE",
            KEY_OUTPUT = KEY_ + "OUTPUT", KEY_OUTPUT_FILE = KEY_ + "OUTPUT_FILE",
            KEY_DURATION_LIMIT = "DURATION_LIMIT", KEY_QUALITY = KEY_ + "QUALITY";

    public static final String IMAGE = "image", VIDEO = "video";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({IMAGE, VIDEO})
    @interface Mime{}

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MODE_GALLERY, MODE_CAMERA})
    @interface Mode{}

    static String putCallback (Callback callback) {
        final String id = UUID.randomUUID().toString();
        sIdCallbackMap.put(id, callback);
        return id;
    }

    static void actionCallback (String id, Uri uri, File file, boolean isMatch) {
        Callback callback = sIdCallbackMap.get(id);
        Log.v(TAG, "actionCallback id=" + id + " callback=" + callback);
        if (callback != null && callback instanceof SingleCallback) {
            ((SingleCallback) callback).onGet(id, uri, file, isMatch);
        }
        releaseCallback(id);
    }

    static void actionCallback (String id, List<Uri> uris, List<File> files, List<Boolean> isMatchList) {
        Callback callback = sIdCallbackMap.get(id);
        if (callback != null && callback instanceof MultipleCallback) {
            ((MultipleCallback) callback).onGet(id, uris, files, isMatchList);
        }
        releaseCallback(id);
    }

    static void cancelCallback (String id) {
        releaseCallback(id);
    }

    private static void releaseCallback (String id) {
        sIdCallbackMap.remove(id);
    }

    private @Mime String mime = IMAGE;

    public Picker image() {
        mime = IMAGE;
        return this;
    }

    public Picker video() {
        mime = VIDEO;
        return this;
    }

    @Mime String mime() {
        return mime;
    }

}
