package com.sakuqi.webviewlibrary.view


interface IWebViewUI {
    fun setTitle(title:String?)
    fun progress(progress:Int)
    fun startPage(){}
    fun endPage(){}
}