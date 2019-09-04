package com.sakuqi.recyclerviewlibrary

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DividerItemDecoration(val context:Context,
                            val orientation:Int,
                            val paddingLeft:Int = 0,
                            val paddingRight:Int = 0,
                            var driverHeight:Int = 0,
                            @ColorInt val colorInt:Int = Color.LTGRAY) :RecyclerView.ItemDecoration() {
    companion object{
        val ATTRS = intArrayOf(android.R.attr.listDivider)
        val HORIZONTAL = LinearLayoutManager.HORIZONTAL
        val VERTICAL = LinearLayoutManager.VERTICAL
    }
    private var mDivider:Drawable?=null

    init {
        val a = context.obtainStyledAttributes(ATTRS)
        mDivider = a.getDrawable(0)
        mDivider?.colorFilter = PorterDuffColorFilter(colorInt,PorterDuff.Mode.ADD)
        if(driverHeight == 0) driverHeight = mDivider?.intrinsicHeight?:2
        a.recycle()
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if(orientation == VERTICAL){
            drawVertical(c,parent)
        }else if(orientation == HORIZONTAL){
            drawHorizontal(c,parent)
        }
    }

    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom
        val childCount = parent.childCount
        for (i in 0 until childCount){
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin
            val right = left + driverHeight
            mDivider?.setBounds(left,top,right,bottom)
            mDivider?.draw(c)
        }
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        var left = parent.paddingLeft
        var right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount){
            val child = parent.getChildAt(i)
            child.tag = i
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = if(i == childCount-1) top else top+driverHeight
            mDivider?.setBounds(left+paddingLeft,top,right-paddingRight,bottom)
            mDivider?.draw(c)
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if(orientation == VERTICAL){
            outRect.set(0, 0, 0, driverHeight)
        }else{
            outRect.set(0,0,driverHeight,0)
        }
    }

}