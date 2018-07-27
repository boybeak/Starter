package com.github.boybeak.designer

import android.app.Application
import com.github.boybeak.designer.manager.AccountManager

class App : Application(){
    override fun onCreate() {
        super.onCreate()

        AccountManager.startUp(this)
    }
}