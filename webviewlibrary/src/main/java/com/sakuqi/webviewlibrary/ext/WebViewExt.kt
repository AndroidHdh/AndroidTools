package com.sakuqi.webviewlibrary.ext

import android.os.Build
import android.webkit.WebSettings
import android.webkit.WebView
import com.sakuqi.webviewlibrary.BaseApplication

fun WebView.defaultSetting(){
    val webSettings = settings
    //5.0以上开启混合模式加载
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
    }
    webSettings.loadWithOverviewMode = true
    webSettings.useWideViewPort = true
    //允许js代码
    webSettings.javaScriptEnabled = true
    //允许SessionStorage/LocalStorage存储
    webSettings.domStorageEnabled = true
    //禁用放缩
    webSettings.displayZoomControls = false
    webSettings.builtInZoomControls = false
    //禁用文字缩放
    webSettings.textZoom = 100
    //10M缓存，api 18后，系统自动管理。
    webSettings.setAppCacheMaxSize((10 * 1024 * 1024).toLong())
    //允许缓存，设置缓存位置
    webSettings.setAppCacheEnabled(true)
    webSettings.setAppCachePath(BaseApplication.instance.getDir("sakuqi", 0).path)
    //允许WebView使用File协议
    webSettings.allowFileAccess = true
    //不保存密码
    webSettings.savePassword = false
    //设置UA
    webSettings.setUserAgentString(webSettings.userAgentString + " kaolaApp/")
    //自动加载图片
    webSettings.loadsImagesAutomatically = true
}