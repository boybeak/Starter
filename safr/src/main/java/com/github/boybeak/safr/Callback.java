package com.github.boybeak.safr;

import android.content.Intent;

public interface Callback {
    void onResult(int requestCode, int resultCode, Intent data);
}