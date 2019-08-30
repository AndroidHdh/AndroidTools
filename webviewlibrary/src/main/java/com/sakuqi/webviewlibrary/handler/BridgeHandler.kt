package com.sakuqi.webviewlibrary.handler

import com.sakuqi.webviewlibrary.view.BaseWebView

class BridgeHandler(val webView: BaseWebView?=null) : JSBridge.BridgeListener {
    override fun sendEvent(s: String) {
        //TODO 做一些具体的业务交互逻辑
    }

    fun callJSMethod(method:String,callback:(String)->Unit){
        webView?.evaluateJavascript(method,callback)
    }
}