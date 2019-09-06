package com.sakuqi.recyclerviewlibrary.refresh

import android.content.Context
import android.view.View
import android.view.ViewGroup

/**
 * 上拉加载更多辅助抽象类
 */
abstract class LoadViewCreator {
    abstract fun getLoadView(context: Context,parent:ViewGroup):View
    abstract fun onPull(currentDragHeight:Int,loadViewHeight:Int,currentLoadStatus:Int)
    abstract fun onLoading()
    abstract fun onStopLoad()
}