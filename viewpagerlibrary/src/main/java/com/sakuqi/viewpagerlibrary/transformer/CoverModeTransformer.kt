package com.sakuqi.viewpagerlibrary.transformer

import androidx.core.view.ViewCompat.setTranslationX
import androidx.core.view.ViewCompat.setScaleY
import androidx.core.view.ViewCompat.setScaleX
import android.opengl.ETC1.getWidth
import android.view.View
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs


class CoverModeTransformer(private val mViewPager: ViewPager) : ViewPager.PageTransformer {

    private var reduceX = 0.0f
    private var itemWidth = 0f
    private var offsetPosition = 0f
    private val mCoverWidth: Int = 0
    private val mScaleMax = 1.0f
    private val mScaleMin = 0.9f

    override fun transformPage(view: View, position: Float) {
        if (offsetPosition == 0f) {
            val paddingLeft = mViewPager.paddingLeft.toFloat()
            val paddingRight = mViewPager.paddingRight.toFloat()
            val width = mViewPager.measuredWidth.toFloat()
            offsetPosition = paddingLeft / (width - paddingLeft - paddingRight)
        }
        val currentPos = position - offsetPosition
        if (itemWidth == 0f) {
            itemWidth = view.width.toFloat()
            //由于左右边的缩小而减小的x的大小的一半
            reduceX = (2.0f - mScaleMax - mScaleMin) * itemWidth / 2.0f
        }
        if (currentPos <= -1.0f) {
            view.translationX = reduceX + mCoverWidth
            view.scaleX = mScaleMin
            view.scaleY = mScaleMin
        } else if (currentPos <= 1.0) {
            val scale = (mScaleMax - mScaleMin) * abs(1.0f - abs(currentPos))
            val translationX = currentPos * -reduceX
            when {
                currentPos <= -0.5 -> //两个view中间的临界，这时两个view在同一层，左侧View需要往X轴正方向移动覆盖的值()
                    view.translationX = translationX + mCoverWidth * abs(abs(currentPos) - 0.5f) / 0.5f
                currentPos <= 0.0f -> view.translationX = translationX
                currentPos >= 0.5 -> //两个view中间的临界，这时两个view在同一层
                    view.translationX = translationX - mCoverWidth * abs(abs(currentPos) - 0.5f) / 0.5f
                else -> view.translationX = translationX
            }
            view.scaleX = scale + mScaleMin
            view.scaleY = scale + mScaleMin
        } else {
            view.scaleX = mScaleMin
            view.scaleY = mScaleMin
            view.translationX = -reduceX - mCoverWidth
        }

    }
}
