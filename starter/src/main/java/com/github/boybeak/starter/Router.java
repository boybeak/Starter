package com.github.boybeak.starter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Router {

    public static Router with(Context context) {
        return new Router(context);
    }

    public static Router with(View view) {
        return with(view.getContext());
    }

    public static Router with(Fragment fragment) {
        return with(fragment.getActivity());
    }

    private Context context;

    private Intent it = new Intent();
    private List<Pair<View, String>> sharedElements = new ArrayList<>();

    private Router(Context context) {
        this.context = context;
    }

    public Router category(String ... categories) {
        for (String c : categories) {
            it.addCategory(c);
        }
        return this;
    }

    public Router data(Uri uri) {
        it.setData(uri);
        return this;
    }

    public Router withParcelable(String key, Parcelable parcelable) {
        it.putExtra(key, parcelable);
        return this;
    }

    public Router withParcelableList(String key, List<Parcelable> parcelableList) {
        ArrayList<Parcelable> list = new ArrayList<>(parcelableList);
        it.putExtra(key, list);
        return this;
    }

    public Router withBoolean(String key, Boolean bool) {
        it.putExtra(key, bool);
        return this;
    }

    public Router withInt(String key, int value) {
        it.putExtra(key, value);
        return this;
    }

    public Router withString(String key, String value) {
        it.putExtra(key, value);
        return this;
    }

    public Router withSharedElement(View view, String translationName) {
        sharedElements.add(new Pair<View, String>(view, translationName));
        return this;
    }

    public Router withSharedElement(View view, @StringRes int translationNameRes) {
        return withSharedElement(view, view.getContext().getString(translationNameRes));
    }

    public void goTo(Class clz) {
        it.setClass(context, clz);
        this.context.startActivity(it, makeOptionBundle());
        recycle();
    }

    public void goTo(String action) {
        it.setAction(action);
        this.context.startActivity(it, makeOptionBundle());
        recycle();
    }

    private void recycle() {
        if (!sharedElements.isEmpty()) {
            sharedElements.clear();
        }
        context = null;
    }

    private Bundle makeOptionBundle() {
        if (sharedElements.isEmpty()) {
            return null;
        }
        Pair[] pairs = new Pair[sharedElements.size()];

        return ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context, pairs).toBundle();
    }

}
