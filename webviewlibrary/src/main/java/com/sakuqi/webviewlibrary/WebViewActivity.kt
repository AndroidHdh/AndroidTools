package com.sakuqi.webviewlibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_webview.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import com.sakuqi.webviewlibrary.handler.BridgeHandler
import com.sakuqi.webviewlibrary.view.BaseWebView
import com.sakuqi.webviewlibrary.view.IWebViewUI


class WebViewActivity : AppCompatActivity(), IWebViewUI {
    val url = "https://blog.csdn.net"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        val webView = WebViewBuilder.getWebView(this, BridgeHandler(),iWebViewUI = this)
        frameLayout.addView(webView)
        webView.loadUrl(url)
        webView.setOnScrollChangedCallBack {
            Toast.makeText(this,if(it == BaseWebView.DIRECTION_UP) "向上滚动" else "向下滚动",Toast.LENGTH_LONG).show()
        }
        webView.setOnScrollTopEvent {
            if(it) {
                Toast.makeText(this, "滚到顶部", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun setTitle(title: String?) {
        toolbar.title = title
    }

    override fun progress(progress: Int) {
        progress_horizontal.progress = progress
    }

    override fun startPage() {
        progress_horizontal.visibility = VISIBLE
    }

    override fun endPage() {
        progress_horizontal.visibility = GONE
    }
}
