package com.sakuqi.recyclerviewlibrary.refresh

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.view.ViewCompat

class LoadRefreshRecyclerView(context: Context, attributeSet: AttributeSet?, deStyle: Int) :
    RefreshRecyclerView(context, attributeSet, deStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    companion object {
        const val LOAD_STATUS_NORMAL = 0x0011
        const val LOAD_STATUS_PULL_DOWN_REFRESH = 0x0022
        const val LOAD_STATUS_LOOSEN_LOADING = 0x0033
        const val LOAD_STATUS_LOADING = 0x0044
    }

    private var mLoadCreator: LoadViewCreator? = null
    private var mLoadViewHeight = 0
    private var mLoadView: View? = null
    private var mFingerDownY: Int = 0
    private var mCurrentDrag = false
    private var mCurrentLoadStatus = LOAD_STATUS_NORMAL
    private var onLoad: (() -> Unit)? = null

    override fun setAdapter(adapter: Adapter<ViewHolder>?) {
        super.setAdapter(adapter)
        addLoadView()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> mFingerDownY = ev.rawY.toInt()
            MotionEvent.ACTION_UP -> if (mCurrentDrag) restoreLoadView()
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun restoreLoadView() {
        val layoutParams = mLoadView?.layoutParams
        var currentBottomMargin = 0f
        if (layoutParams is MarginLayoutParams) {
            currentBottomMargin = layoutParams.bottomMargin.toFloat()
        }
        var finalBottomMargin = -mLoadViewHeight.toFloat()//0f
        if (mCurrentLoadStatus == LOAD_STATUS_LOOSEN_LOADING) {
            finalBottomMargin = 0f
            mCurrentLoadStatus = LOAD_STATUS_LOADING
            mLoadCreator?.onLoading()
            onLoad?.invoke()
        }
        var distance = currentBottomMargin - finalBottomMargin
        val animator = ObjectAnimator
            .ofFloat(currentBottomMargin, finalBottomMargin)
            .setDuration(distance.toLong())
        animator.addUpdateListener {
            val currentBottomMargin = it.animatedValue as Float
            setLoadViewMarginBottom(currentBottomMargin.toInt())
        }
        animator.start()
        mCurrentDrag = false
    }

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        when (e?.action) {
            MotionEvent.ACTION_MOVE -> {
                //如果是在最底部才处理，否则 不要处理
                if (mLoadCreator != null) {
                    mLoadViewHeight = mLoadView?.measuredHeight ?: 0
                }
                if (canScrollDown() || mCurrentLoadStatus == LOAD_STATUS_LOADING) {
                    //如果没有到达最顶端，也就是说还可以向上滚动就什么都不处理
                    mFingerDownY = e.rawY.toInt()
                    setLoadViewMarginBottom(-mLoadViewHeight)
                    return super.onTouchEvent(e)
                }

                //处理上拉加载更多自动滚动问题
                if (mCurrentDrag && adapter != null) {
                    scrollToPosition(adapter!!.itemCount - 1)
                }

                val distanceY = ((e.rawY - mFingerDownY) * mDragIndex).toInt()
                //如果是已经到达头部，并且不断的向下拉，那么不断的改变refreshView的marginTop的值
                if (distanceY < 0) {
                    val marginBottom = distanceY + mLoadViewHeight
                    setLoadViewMarginBottom(-marginBottom)
                    updateLoadStatus(-distanceY)
                    mCurrentDrag = true
                    return true
                }
            }
        }
        return super.onTouchEvent(e)
    }

    private fun updateLoadStatus(distanceY: Int) {
        mCurrentLoadStatus = when {
            distanceY <= 0 -> LOAD_STATUS_NORMAL
            distanceY < mLoadViewHeight -> LOAD_STATUS_PULL_DOWN_REFRESH
            else -> LOAD_STATUS_LOOSEN_LOADING
        }
        mLoadCreator?.onPull(distanceY, mLoadViewHeight, mCurrentLoadStatus)
    }

    private fun canScrollDown(): Boolean {
        return ViewCompat.canScrollVertically(this, 1)
    }

    private fun setLoadViewMarginBottom(bottomMargin: Int) {
        val params = mLoadView?.layoutParams
        if (params is MarginLayoutParams) {
            if (bottomMargin < -mLoadViewHeight) params.bottomMargin = -mLoadViewHeight
            params.bottomMargin = bottomMargin
            mLoadView?.layoutParams = params
        }
    }

    private fun addLoadView() {

        if (adapter != null && mLoadCreator != null) {
            val loadView = mLoadCreator?.getLoadView(context, this)
            if (loadView != null) {
                addFooterView(loadView)
                this.mLoadView = loadView
            }
        }
    }

    fun onStopLoad() {
        mCurrentLoadStatus = LOAD_STATUS_NORMAL
        restoreLoadView()
        mLoadCreator?.onStopLoad()
    }

    fun addOnLoadListener(onLoad: (() -> Unit)) {
        this.onLoad = onLoad
    }

    fun addLoadViewCreator(loadCreator: LoadViewCreator) {
        this.mLoadCreator = loadCreator
        addLoadView()
    }
}