package com.github.boybeak.starter.retrofit;

import android.support.annotation.NonNull;

import com.github.boybeak.starter.ILife;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class SafeCallback<T> implements Callback<T> {

    private WeakReference<ILife> mLife = null;
    private Callback<T> mCallback = null;

    public SafeCallback(ILife life) {
        this(life, null);
    }

    public SafeCallback(ILife life, Callback<T> callback) {
        mLife = new WeakReference<>(life);
        mCallback = callback;
    }

    public void onPreResponse() {

    }

    public void onPostResponse() {

    }

    private boolean isAlive() {
        ILife life = mLife.get();
        return life != null && life.isAlive();
    }

    @Override
    public final void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {

        if (!isAlive()) {
            return;
        }

        onPreResponse();

        if (response.isSuccessful()) {
            T t = response.body();
            if (t != null) {
                onResult(t);
            } else {
                onError(new NullResultException("The result is null"));
            }
        } else {
            onError(new IllegalStateException(response.message()));
        }

        onPostResponse();

        if (mCallback != null) {
            mCallback.onResponse(call, response);
            mCallback = null;
        }
    }

    @Override
    public final void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {

        if (!isAlive()) {
            return;
        }

        onPreResponse();

        onError(t);

        onPostResponse();

        if (mCallback != null) {
            mCallback.onFailure(call, t);
            mCallback = null;
        }
    }

    public abstract void onResult(@NonNull T t);
    public abstract void onError(@NonNull Throwable t);
}
