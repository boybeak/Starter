package com.github.boybeak.starter.retrofit;

import android.support.annotation.NonNull;

import com.github.boybeak.starter.ILife;

import java.util.List;

import retrofit2.Callback;

public abstract class SafeListCallback<T> extends SafeCallback<List<T>> {

    public SafeListCallback(ILife life) {
        super(life);
    }

    public SafeListCallback(ILife life, Callback<List<T>> callback) {
        super(life, callback);
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
