package com.sakuqi.androidmaterialdesign

import android.app.Application
import com.sakuqi.webviewlibrary.BaseApplication

class AppApplication:BaseApplication() {
    companion object {
        lateinit var instance: Application
    }
    init {
        instance = this
    }
}