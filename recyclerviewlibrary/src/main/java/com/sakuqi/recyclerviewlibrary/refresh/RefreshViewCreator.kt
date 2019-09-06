package com.sakuqi.recyclerviewlibrary.refresh

import android.content.Context
import android.view.View
import android.view.ViewGroup

/**
 * 下拉刷新辅助类
 */
abstract class RefreshViewCreator {
    abstract fun getRefreshView(context: Context,parent:ViewGroup):View
    abstract fun onPull(currentFragHeight:Int,refreshViewHeight:Int,currentRefreshStatus:Int)
    abstract fun onRefreshing()
    abstract fun onStopRefresh()
}