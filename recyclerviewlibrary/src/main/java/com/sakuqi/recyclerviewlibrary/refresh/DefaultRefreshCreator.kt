package com.sakuqi.recyclerviewlibrary.refresh

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import com.sakuqi.recyclerviewlibrary.R


/**
 * 默认下拉刷新辅助类
 */
class DefaultRefreshCreator :RefreshViewCreator() {
    private var mRefresh:View?=null
    override fun getRefreshView(context: Context, parent: ViewGroup): View {
        val refreshView = LayoutInflater.from(context).inflate(R.layout.item_refresh_head,parent,false)
        mRefresh = refreshView.findViewById(R.id.refresh_head_iv)
        return refreshView
    }

    override fun onPull(currentFragHeight: Int, refreshViewHeight: Int, currentRefreshStatus: Int) {
        val rotate = currentFragHeight/refreshViewHeight.toFloat()
        mRefresh?.rotation = rotate*360
    }

    override fun onRefreshing() {
        val animation = RotateAnimation(0f,720f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f)
        animation.repeatCount = -1
        animation.duration = 1000
        mRefresh?.startAnimation(animation)
    }

    override fun onStopRefresh() {
        mRefresh?.rotation = 0f
        mRefresh?.clearAnimation()
    }
}