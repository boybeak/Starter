package com.github.boybeak.picker;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

public class Gallery extends Picker {

    public static final String ANY = "*";

    public interface ImageType {
        String ANY = Gallery.ANY, BMP = "bmp", GIF = "gif", JPEG = "jpeg", JPG = JPEG,
                PNG = "png", TIFF = "tiff", TIF = TIFF, WEB_P = "webp";
    }

    public interface VideoType {
        String ANY = Gallery.ANY, THREE_GP = "3gpp", MP4 = "mp4", MPEG = "mpeg", OGG = "ogg",
                WEB_M = "webm";
    }

    private boolean multipleSelect = false;
    private String type = ANY;

    Gallery() {

    }

    public Gallery multiple(boolean multipleSelect) {
        this.multipleSelect = multipleSelect;
        return this;
    }

    @Override
    public Gallery image() {
        return (Gallery) super.image();
    }

    @Override
    public Gallery video() {
        return (Gallery) super.video();
    }

    public Gallery type(String type) {
        this.type = type;
        return this;
    }

    public String go(Context context, Callback callback) {

        if (TextUtils.isEmpty(mime())) {
            throw new IllegalStateException("type not define, call image() or video() method before go");
        }
        if (multipleSelect && !(callback instanceof MultipleCallback)) {
            throw new IllegalStateException("you should use MultipleCallback for a multiple select request");
        }
        if (!multipleSelect && !(callback instanceof SingleCallback)) {
            throw new IllegalStateException("single request must work with a SingleCallback");
        }
        if (TextUtils.isEmpty(type)) {
            type = ANY;
        }

        String id = Picker.putCallback(callback);

        Intent it = new Intent(context, PickerAgentActivity.class);
        it.putExtra(Picker.KEY_MODE, Picker.MODE_GALLERY);
        it.putExtra(Picker.KEY_ID, id);
        it.putExtra(Picker.KEY_ALLOW_MULTIPLE, multipleSelect);
        it.putExtra(Picker.KEY_MIME_TYPE, mime() + "/" + type);
        context.startActivity(it);

        return id;
    }
}
