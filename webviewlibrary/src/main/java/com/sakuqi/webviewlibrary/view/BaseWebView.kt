package com.sakuqi.webviewlibrary.view

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.webkit.WebView

class BaseWebView:WebView{
    companion object{
        const val DIRECTION_UP = 0
        const val DIRECTION_DOWN = 1
    }
    private var isTop:Boolean = true
    private var directionCallBack:((Int) -> Unit)? = null
    private var topEventCallback:((Boolean) -> Unit)? = null
    constructor(context: Context):super(context)
    constructor(context: Context,attrs:AttributeSet):super(context,attrs)
    constructor(context: Context,attrs:AttributeSet,defStyle:Int):super(context,attrs,defStyle)
    var lastDirection = DIRECTION_UP
    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        directionCallBack?.let {
            if(t > oldt){
                if(lastDirection == DIRECTION_DOWN){
                    return@let
                }
                lastDirection = DIRECTION_DOWN
                it.invoke(DIRECTION_DOWN)
            }else if(t < oldt){
                if(lastDirection == DIRECTION_UP){
                    return@let
                }
                lastDirection = DIRECTION_UP
                it.invoke(DIRECTION_UP)
            }
        }
        topEventCallback?.let {
            if(t > 0){
                if(isTop){
                    isTop = false
                    it.invoke(isTop)
                }
            }else {
                if(!isTop){
                    isTop = true
                    it.invoke(isTop)
                }
            }
        }
    }

    fun setOnScrollChangedCallBack(callBack:(direction:Int) -> Unit){
        this.directionCallBack = callBack
    }

    fun setOnScrollTopEvent(callBack: (isTop: Boolean) -> Unit){
        this.topEventCallback = callBack
    }

}