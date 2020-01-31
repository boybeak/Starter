package com.github.boybeak.safr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;
import java.util.UUID;
import java.util.WeakHashMap;

public class SAFR {

    private static final String TAG = SAFR.class.getSimpleName();

    static final String KEY_ID = "com.github.boybeak.safr.ID",
            KEY_INTENT = "com.github.boybeak.safr.INTENT",
            KEY_ACTION = "com.github.boybeak.safr.ACTION",
            KEY_TYPE = "com.github.boybeak.safr.TYPE",
            KEY_CLASS = "com.github.boybeak.safr.CLASS",
            KEY_REQUEST_CODE = "com.github.boybeak.safr.REQUEST_CODE";

    private static HashMap<String, Callback> sCallbackMap = new HashMap<>();

    static void onActivityResult(String id, int requestCode, int resultCode, Intent data) {
        Callback callback = sCallbackMap.get(id);

        if (callback != null) {
            callback.onResult(requestCode, resultCode, data);
            sCallbackMap.remove(id);
        }
    }

    public static SAFR newInstance() {
        return new SAFR();
    }

    private Intent mIntent = null;
    private Bundle mExtras = null;
    private String action, type;
    private Class<? extends Activity> aClass;

    private SAFR() {
        mExtras = new Bundle();
    }

    public SAFR byIntent(Intent intent) {
        mIntent = intent;
        mExtras = null;
        action = null;
        type = null;
        aClass = null;
        return this;
    }

    public SAFR byAction (String action) {
        this.action = action;
        aClass = null;
        return this;
    }

    public SAFR byClass (Class<? extends Activity> aClass) {
        this.aClass = aClass;
        action = null;
        return this;
    }

    public SAFR type(String type) {
        this.type = type;
        return this;
    }

    public SAFR extras(ExtraBuilder builder) {
        builder.onExtras(mExtras);
        return this;
    }

    public void startActivityForResult (Context context, int requestCode, Callback callback) {

        if (action == null && aClass == null && mIntent == null) {
            throw new IllegalStateException("You must set action or class by method byAction or byClass");
        }

        String id = UUID.randomUUID().toString();
        sCallbackMap.put(id, callback);

        Intent safrIt = new Intent(context, SAFRActivity.class);
        safrIt.putExtra(KEY_ID, id);
        if (mIntent != null) {
            safrIt.putExtra(KEY_INTENT, mIntent);
        } else {
            safrIt.putExtra(KEY_TYPE, type);
            if (action != null) {
                safrIt.putExtra(KEY_ACTION, action);
            } else if (aClass != null) {
                safrIt.putExtra(KEY_CLASS, aClass.getName());
            }
            safrIt.putExtras(mExtras);
        }
        safrIt.putExtra(KEY_REQUEST_CODE, requestCode);
        context.startActivity(safrIt);

    }

}
