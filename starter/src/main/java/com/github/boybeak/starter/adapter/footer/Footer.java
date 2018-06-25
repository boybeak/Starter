package com.github.boybeak.starter.adapter.footer;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by gaoyunfei on 2018/3/10.
 */

public class Footer {
    public static final int LOADING = 1, SUCCESS = 2, FAILED = 3, EMPTY = 4;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LOADING, SUCCESS, FAILED, EMPTY})
    public @interface State{}

    public @State int state = EMPTY;
    public String message = "";

    public @State int getState() {
        return state;
    }

    public void setState(@State int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
