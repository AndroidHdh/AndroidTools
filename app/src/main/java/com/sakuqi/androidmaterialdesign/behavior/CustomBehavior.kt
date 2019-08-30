package com.sakuqi.androidmaterialdesign.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.sakuqi.androidmaterialdesign.AppApplication
import com.sakuqi.androidmaterialdesign.R

class CustomBehavior(context: Context, attributeSet: AttributeSet) :
    CoordinatorLayout.Behavior<View>(context, attributeSet) {
    override fun onLayoutChild(parent: CoordinatorLayout, child: View, layoutDirection: Int): Boolean {
        val params = child.layoutParams as CoordinatorLayout.LayoutParams
        if (params.height == CoordinatorLayout.LayoutParams.MATCH_PARENT) {
            child.layout(0, 0, parent.width, parent.height)
            child.translationY = getHeaderHeight()
            return true
        }
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return (axes and ViewCompat.SCROLL_AXIS_VERTICAL) != 0
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        if (dy < 0) {
            return
        }
        val transY = child.translationY - dy
        if (transY > 0) {
            child.translationY = transY
            consumed[1] = dy
        }
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
        if (dyUnconsumed > 0) {
            return
        }
        val transY = child.translationY - dyUnconsumed
        if (transY > 0 && transY < getHeaderHeight()) {
            child.translationY = transY
        }
    }

    private fun getHeaderHeight(): Float {
        return AppApplication.instance.resources.getDimensionPixelOffset(R.dimen.head_height).toFloat()
    }
}