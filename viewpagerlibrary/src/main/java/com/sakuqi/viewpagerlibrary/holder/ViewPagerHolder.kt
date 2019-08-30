package com.sakuqi.viewpagerlibrary.holder

import android.content.Context
import android.view.View

/**
 * 数据视图绑定接口
 */
interface ViewPagerHolder<T> {
    fun createView(context: Context): View
    fun onBind(context: Context, position: Int, data: T)
}
