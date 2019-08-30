package com.sakuqi.webviewlibrary.client


import android.webkit.WebChromeClient
import android.webkit.WebView
import com.sakuqi.webviewlibrary.view.IWebViewUI

class MyWebChromeClient(private val viewImp: IWebViewUI?):WebChromeClient() {
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        viewImp?.progress(newProgress)
    }

    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
        viewImp?.setTitle(title)
    }
}