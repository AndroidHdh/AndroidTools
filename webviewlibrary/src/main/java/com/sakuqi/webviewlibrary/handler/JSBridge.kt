package com.sakuqi.webviewlibrary.handler

import android.webkit.JavascriptInterface

/**
 * 原生提供给JS方法
 */

class JSBridge (private val bridgeListener: BridgeListener){
    interface BridgeListener{
        fun sendEvent(s:String)
    }

    @JavascriptInterface
    fun sendEvent(message:String){
        bridgeListener.sendEvent(message)
    }
}