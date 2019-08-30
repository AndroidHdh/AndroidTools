package com.sakuqi.webviewlibrary

import android.app.Application

open class BaseApplication:Application() {
    companion object {
        lateinit var instance: Application
    }
    init {
        instance = this
    }
}