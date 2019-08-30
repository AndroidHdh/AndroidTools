package com.sakuqi.viewpagerlibrary.transformer

import android.view.View
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs
import kotlin.math.max

/**
 * 转场动画
 */
class CustomTransformer:ViewPager.PageTransformer {
    private val MIN_SCALE = 0.9f
    override fun transformPage(page: View, position: Float) {
        if(position < -1){
            page.scaleY = MIN_SCALE
        }else if(position<=1){
            val scale = max(MIN_SCALE, 1- abs(position))
            page.scaleY = scale
        }else{
            page.scaleY = MIN_SCALE
        }
    }
}