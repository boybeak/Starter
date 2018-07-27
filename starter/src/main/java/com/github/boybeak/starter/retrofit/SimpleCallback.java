package com.github.boybeak.starter.retrofit;

import android.support.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class SimpleCallback<T> implements Callback<T> {

    private Callback<T> mCallback = null;

    public SimpleCallback() {
        this(null);
    }

    public SimpleCallback(Callback<T> callback) {
        mCallback = callback;
    }

    public void onPreResponse() {

    }

    public void onPostResponse() {

    }

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {

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
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {

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
