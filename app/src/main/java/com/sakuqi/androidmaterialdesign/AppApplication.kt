package com.sakuqi.androidmaterialdesign

import android.app.Application
import com.sakuqi.corelibrary.BaseApplication

class AppApplication: BaseApplication() {
    companion object {
        lateinit var instance: Application
    }
    init {
        instance = this
    }
}