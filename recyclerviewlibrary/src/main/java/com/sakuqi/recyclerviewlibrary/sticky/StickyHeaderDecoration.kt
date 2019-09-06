package com.sakuqi.recyclerviewlibrary.sticky

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max

/**
 * 头部浮动的Header
 */
class StickyHeaderDecoration(val mISticky: IStickyHeader) :
    RecyclerView.ItemDecoration() {

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val count = parent.childCount
        for (layoutPos in 0 until count){
            val child = parent.getChildAt(layoutPos)
            val adapterPos = parent.getChildAdapterPosition(child)
            if(adapterPos != RecyclerView.NO_POSITION && (layoutPos == 0 ||  hasHeader(adapterPos))){
                val header = getHeader(parent,adapterPos).itemView
                c.save()
                val left:Float = child.left.toFloat()
                val top:Float = getHeaderTop(parent,child,header,adapterPos,layoutPos).toFloat()
                c.translate(left,top)
                header.translationX = left
                header.translationY = top
                header.draw(c)
                c.restore()
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        var headerHeight = 0
        if(position != RecyclerView.NO_POSITION && hasHeader(position)){
            val header = getHeader(parent,position).itemView
            headerHeight = header.height
        }
        outRect.set(0,headerHeight,0,0)
    }
    fun hasHeader(position:Int):Boolean{
        if(position == 0){
            return true
        }
        val previous = position -1
        return mISticky.getHeaderId(position) != mISticky.getHeaderId(previous)
    }

    private fun getHeader(parent:RecyclerView,position: Int):RecyclerView.ViewHolder{
        val holder = mISticky.onCreateHeaderViewHolder(parent)
        val header = holder.itemView
        mISticky.onBindHeaderViewHolder(holder,position)
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width,View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height,View.MeasureSpec.UNSPECIFIED)
        val childWidth = ViewGroup.getChildMeasureSpec(widthSpec,parent.paddingLeft+parent.paddingRight,header.layoutParams.width)
        val childHeight = ViewGroup.getChildMeasureSpec(heightSpec,parent.paddingTop+parent.paddingBottom,header.layoutParams.height)
        header.measure(childWidth,childHeight)
        header.layout(0,0,header.measuredWidth,header.measuredHeight)
        return holder
    }

    private fun getHeaderTop(parent:RecyclerView,child:View,header:View,adatperPos:Int,layoutPos:Int):Int{
        val headerHeight = header.height
        var top = child.y -headerHeight
        if(layoutPos == 0){
            val count = parent.childCount
            val currentId = mISticky.getHeaderId(adatperPos)
            for (i in 1 until count){
                val adapterPosHere = parent.getChildAdapterPosition(parent.getChildAt(i))
                if(adapterPosHere != RecyclerView.NO_POSITION){
                    val nextId = mISticky.getHeaderId(adapterPosHere)
                    if(nextId != currentId){
                        val next = parent.getChildAt(i)
                        val offset = next.y - (headerHeight + getHeader(parent,adapterPosHere).itemView.height)
                        if(offset < 0){
                            return offset.toInt()
                        }else{
                            break
                        }
                    }
                }
            }
            top = max(0f,top)
        }
        return top.toInt()
    }
}