package com.github.boybeak.starter.retrofit;

import android.support.annotation.NonNull;

import java.util.List;

import retrofit2.Callback;

public abstract class SimpleListCallback<T> extends SimpleCallback<List<T>>{

    public SimpleListCallback() {
    }

    public SimpleListCallback(Callback<List<T>> callback) {
        super(callback);
    }

    @Override
    public final void onResult(@NonNull List<T> ts) {
        if (ts.isEmpty()) {
            onEmpty();
        } else {
            onResultList(ts);
        }
    }

    public abstract void onResultList(@NonNull List<T> ts);
    public abstract void onEmpty();

}
