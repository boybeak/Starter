package com.github.boybeak.starter.retrofit;

import android.support.annotation.NonNull;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;

public class SimpleCallAdapter<T> implements CallAdapter<T, SimpleCall<T>> {

    private final Type responseType;

    public SimpleCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public SimpleCall<T> adapt(@NonNull Call<T> call) {
        return new SimpleCall<>(call);
    }
}
