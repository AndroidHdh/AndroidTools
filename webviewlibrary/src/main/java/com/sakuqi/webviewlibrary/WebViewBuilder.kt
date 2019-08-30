package com.sakuqi.webviewlibrary

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import com.sakuqi.webviewlibrary.client.MyWebChromeClient
import com.sakuqi.webviewlibrary.client.MyWebViewClient
import com.sakuqi.webviewlibrary.ext.defaultSetting
import com.sakuqi.webviewlibrary.handler.JSBridge
import com.sakuqi.webviewlibrary.view.BaseWebView
import com.sakuqi.webviewlibrary.view.IWebViewUI

object WebViewBuilder {

    fun getWebView(context:Context,
                   bridgeListener: JSBridge.BridgeListener,
                   webChromeClient: MyWebChromeClient? = null,
                   webViewClient: MyWebViewClient?= null,
                   iWebViewUI: IWebViewUI?=null):BaseWebView{
        var webChromeClientT = webChromeClient
        var webViewClientT = webViewClient
        val webView = LayoutInflater.from(context).inflate(R.layout.view_webview,null) as BaseWebView
        webView.setDownloadListener { url, userAgent, contentDisPosition, mimeType, contentLength ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            context.startActivity(intent)
        }
        webView.addJavascriptInterface(JSBridge(bridgeListener),"androidJS")
        if (webChromeClientT == null) {
            webChromeClientT = MyWebChromeClient(iWebViewUI)
        }
        if (webViewClientT == null){
            webViewClientT = MyWebViewClient(iWebViewUI)
        }

        webView.webChromeClient = webChromeClientT
        webView.webViewClient = webViewClientT
        webView.defaultSetting()
        return webView
    }
}