package com.github.boybeak.starter.retrofit;

import android.support.annotation.NonNull;

import com.github.boybeak.starter.ILife;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class SafeCallback<T> extends SimpleCallback<T> {

    private WeakReference<ILife> mLifeWeakRef = null;

    public SafeCallback(ILife life) {
        this(life, null);
    }

    public SafeCallback(ILife life, Callback<T> callback) {
        super(callback);
        mLifeWeakRef = new WeakReference<>(life);
    }

    private boolean isAlive() {
        ILife life = mLifeWeakRef.get();
        return life != null && life.isAlive();
    }

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        if (!isAlive()) {
            return;
        }
        super.onResponse(call, response);
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        if (!isAlive()) {
            return;
        }
        super.onFailure(call, t);
    }
}
