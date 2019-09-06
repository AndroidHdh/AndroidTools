package com.sakuqi.recyclerviewlibrary.refresh

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.marginTop
import com.sakuqi.recyclerviewlibrary.view.WrapRecyclerView

open class RefreshRecyclerView(context: Context, attributeSet: AttributeSet?, defStyle: Int) :
    WrapRecyclerView(context, attributeSet, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    companion object {
        const val REFRESH_STATUS_NORMAL = 0x0011
        const val REFRESH_STATUS_PULL_DOWN_REFRESH = 0x0022
        const val REFRESH_STATUS_LOOSEN_REFRESHING = 0x0033
        const val REFRESH_STATUS_REFRESHING = 0x0044
    }

    /**下拉刷新辅助类*/
    private var mRefreshCreator: RefreshViewCreator? = null
    /**下拉刷新头部的高度*/
    private var mRefreshViewHeight = 0
    /**下拉刷新的头部View*/
    private var mRefreshView: View? = null
    /**手指按下的Y位置*/
    private var mFingerDownY = 0
    /**手指拖拽的阻尼指数*/
    protected var mDragIndex = 0.35f
    /**当前是否在拖动*/
    private var mCurrentDrag = false
    /**当前的状态*/
    private var mCurrentRefreshStatus = REFRESH_STATUS_NORMAL

    private var onRefresh:(()->Unit)?=null


    override fun setAdapter(adapter: Adapter<ViewHolder>?) {
        super.setAdapter(adapter)
        addRefreshView()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                mFingerDownY = ev.rawY.toInt()
            }
            MotionEvent.ACTION_UP -> {
                if(mCurrentDrag){
                    restoreRefreshView()
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 重置当前的刷新状态
     */
    private fun restoreRefreshView() {
        val layoutParams = mRefreshView?.layoutParams
        var currentTopMargin = 0f
        if(layoutParams is MarginLayoutParams){
            currentTopMargin = layoutParams.topMargin.toFloat()
        }
        var finalTopMargin = -mRefreshViewHeight + 1f
        if(mCurrentRefreshStatus == REFRESH_STATUS_LOOSEN_REFRESHING){
            finalTopMargin = 0f
            mCurrentRefreshStatus = REFRESH_STATUS_REFRESHING
            mRefreshCreator?.onRefreshing()
            onRefresh?.invoke()
        }

        val distance = currentTopMargin - finalTopMargin
        val animator = ObjectAnimator
            .ofFloat(currentTopMargin,finalTopMargin)
            .setDuration(distance.toLong())
        animator.addUpdateListener {
            val currentTopMargin = it.animatedValue as Float
            setRefreshViewMarginTop(currentTopMargin.toInt())
        }
        animator.start()
        mCurrentDrag = false
    }

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        when(e?.action){
            MotionEvent.ACTION_MOVE -> {
                //如果是在最顶端才处理，否则不需要处理
                if(canScrollUp() || mCurrentRefreshStatus == REFRESH_STATUS_REFRESHING){
                    //如果没有到达最顶端，也就是说还可以向上滚动就什么都不处理
                    return super.onTouchEvent(e)
                }

                //解决下拉刷新自动滚动问题
                if(mCurrentDrag){
                    scrollToPosition(0)
                }
                //获取手指触摸拖拽的距离
                val distanceY = (e.rawY -mFingerDownY) * mDragIndex
                //如果是已经到达头部，并且不断的向下拉那么不断的改变refreshView的marginTop的值
                if(distanceY>0){
                    val marginTop = distanceY - mRefreshViewHeight
                    setRefreshViewMarginTop(marginTop.toInt())
                    updateRefreshStatus(marginTop.toInt())
                    mCurrentDrag = true
                    return false
                }
            }
        }
        return super.onTouchEvent(e)
    }

    /**
     * 更新刷新状态
     */
    private fun updateRefreshStatus(marginTop: Int) {
        mCurrentRefreshStatus = when {
            marginTop <= -mRefreshViewHeight -> REFRESH_STATUS_NORMAL
            marginTop < 0 -> REFRESH_STATUS_PULL_DOWN_REFRESH
            else -> REFRESH_STATUS_LOOSEN_REFRESHING
        }

        if(mRefreshCreator != null){
            mRefreshCreator?.onPull(marginTop,mRefreshViewHeight,mCurrentRefreshStatus)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if(changed){
            if (mRefreshView != null && mRefreshViewHeight <= 0){
                mRefreshViewHeight = mRefreshView?.measuredHeight?:0
                if(mRefreshViewHeight >0){
                    setRefreshViewMarginTop(-mRefreshViewHeight +1)
                }
            }
        }
    }

    private fun canScrollUp(): Boolean {
        return ViewCompat.canScrollVertically(this,-1)
    }

    /**
     * 设置刷新的View的marginTop
     */
    private fun setRefreshViewMarginTop(topMargin: Int) {
        val params = mRefreshView?.layoutParams
        if(params is MarginLayoutParams) {
            if (topMargin < -mRefreshViewHeight + 1) {
                params.topMargin = -mRefreshViewHeight+1
            }
            params.topMargin = topMargin
            mRefreshView?.layoutParams = params
        }
    }

    /**
     * 设置添加的刷新的View
     */
    private fun addRefreshView() {
        if (adapter != null && mRefreshCreator != null) {
            val refreshView = mRefreshCreator?.getRefreshView(context, this)
            if (refreshView != null) {
                addHeaderView(refreshView)
                this.mRefreshView = refreshView
            }
        }
    }

    fun onStopRefresh(){
        mCurrentRefreshStatus = REFRESH_STATUS_NORMAL
        restoreRefreshView()
        mRefreshCreator?.onStopRefresh()
    }

    fun addOnRefreshListener(onRefresh:(()->Unit)){
        this.onRefresh = onRefresh
    }

    fun addRefreshViewCreator(refreshCreator: RefreshViewCreator) {
        mRefreshCreator = refreshCreator
        addRefreshView()
    }

}