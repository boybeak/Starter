package com.github.boybeak.picker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IntDef;
import android.util.Log;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Camera extends Picker {

    private static final String TAG = Camera.class.getSimpleName();

    public static final int QUALITY_LOW = 0, QUALITY_HIGH = 1;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({QUALITY_LOW, QUALITY_HIGH})
    public @interface Quality{}

    private Uri output;
    private File outputFile;
    private int maxDuration = -1;
    private @Quality int quality = QUALITY_LOW;

    Camera() {

    }

    @Override
    public Camera image() {
        return (Camera) super.image();
    }

    @Override
    public Camera video() {
        return (Camera) super.video();
    }

    public Camera durationLimit(int timeInSeconds) {
        maxDuration = timeInSeconds;
        return this;
    }

    public Camera quality(@Quality int quality) {
        this.quality = quality;
        return this;
    }

    public Camera output(Uri uri, File file) {
//        Log.v(TAG, "uri=" + uri.getPath());
//        Log.v(TAG, "file=" + file.getAbsolutePath());
//        if (!uri.getPath().contains(file.getAbsolutePath())) {
//            throw new IllegalStateException("uri must from the file source");
//        }
        output = uri;
        outputFile = file;
        return this;
    }

    public String go(Context context, SingleCallback callback) {

        if (output == null || outputFile == null) {
            throw new IllegalStateException("You must define an output uri by output(uri) method");
        }

        String id = Picker.putCallback(callback);

        Intent it = new Intent(context, PickerAgentActivity.class);
        it.putExtra(Picker.KEY_ID, id);
        it.putExtra(Picker.KEY_MODE, Picker.MODE_CAMERA);
        it.putExtra(Picker.KEY_OUTPUT, output);
        it.putExtra(Picker.KEY_OUTPUT_FILE, outputFile.getAbsolutePath());
        it.putExtra(Picker.KEY_DURATION_LIMIT, maxDuration);
        it.putExtra(Picker.KEY_QUALITY, quality);
        it.putExtra(Picker.KEY_MIME, mime());
        context.startActivity(it);

        return id;
    }
}
