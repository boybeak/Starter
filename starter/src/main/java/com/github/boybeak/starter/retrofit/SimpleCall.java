package com.github.boybeak.starter.retrofit;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SimpleCall<R> implements Call<R> {

    private Call<R> workerCall;

    public SimpleCall(Call<R> workerCall) {
        this.workerCall = workerCall;
    }

    @Override
    public Response<R> execute() throws IOException {
        return workerCall.execute();
    }

    @Override
    public void enqueue(@NonNull Callback<R> callback) {
        if (callback instanceof CallbackImpl) {
            ((CallbackImpl)callback).onPreCall();
        }
        workerCall.enqueue(callback);
    }

    @Override
    public boolean isExecuted() {
        return workerCall.isExecuted();
    }

    @Override
    public void cancel() {
        workerCall.cancel();
    }

    @Override
    public boolean isCanceled() {
        return workerCall.isCanceled();
    }

    @Override
    public Call<R> clone() {
        return new SimpleCall<>(workerCall.clone());
    }

    @Override
    public Request request() {
        return workerCall.request();
    }
}
