package com.sakuqi.viewpagerlibrary

import android.content.Context
import android.view.animation.Interpolator
import android.widget.Scroller


class ViewPagerScroller : Scroller {
    var scrollDuration = 800
        private set// ViewPager默认的最大Duration 为600,我们默认稍微大一点。值越大越慢。
    var isUseDefaultDuration = false

    constructor(context: Context) : super(context)

    constructor(context: Context, interpolator: Interpolator) : super(context, interpolator)

    constructor(context: Context, interpolator: Interpolator, flywheel: Boolean) : super(
        context,
        interpolator,
        flywheel
    )

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
        super.startScroll(startX, startY, dx, dy, scrollDuration)
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, if (isUseDefaultDuration) duration else scrollDuration)
    }

    fun setDuration(duration: Int) {
        scrollDuration = duration
    }
}