package com.sakuqi.viewpagerlibrary.utils

import android.content.res.Resources
import android.util.TypedValue

object DisplayUtils {
    fun dpToPx(dp:Float):Int{
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,Resources.getSystem().displayMetrics).toInt()
    }

    fun pxToDp(px:Float):Int{
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,px,Resources.getSystem().displayMetrics).toInt()
    }
}