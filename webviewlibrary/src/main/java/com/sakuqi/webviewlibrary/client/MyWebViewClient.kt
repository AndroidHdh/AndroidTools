package com.sakuqi.webviewlibrary.client

import android.graphics.Bitmap
import android.net.http.SslError
import android.util.Log
import android.webkit.*
import com.sakuqi.webviewlibrary.view.IWebViewUI

class MyWebViewClient(private val iWebViewUI: IWebViewUI?) : WebViewClient(){
    companion object{
        val TAG = MyWebViewClient::class.java.simpleName
    }
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        iWebViewUI?.startPage()
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        iWebViewUI?.endPage()
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        Log.d(TAG,"可进行拦截操作 $url")
        return false
    }

    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
        super.onReceivedError(view, request, error)
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        super.onReceivedSslError(view, handler, error)
    }
}