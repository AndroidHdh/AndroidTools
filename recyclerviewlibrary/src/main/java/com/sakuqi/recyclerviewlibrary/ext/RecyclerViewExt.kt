package com.sakuqi.recyclerviewlibrary.ext

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.moveToPosition(position:Int){
    if(position != -1){
        this.scrollToPosition(position)
        val layoutManager = this.layoutManager as LinearLayoutManager
        layoutManager.scrollToPositionWithOffset(position,0)
    }
}

fun RecyclerView.smoothMoveToPosition(position: Int,shouldScroll:((Boolean,Int)->Unit)?=null){
    var firstItemPosition = -1
    var lastItemPosition = -1
    firstItemPosition = this.getChildLayoutPosition(this.getChildAt(0))
    lastItemPosition = this.getChildLayoutPosition(this.getChildAt(this.childCount -1))
    if(position < firstItemPosition){
        this.smoothScrollToPosition(position)
    }else if(position <= lastItemPosition){
        var movePosition = position - firstItemPosition
        if(movePosition >= 0 && movePosition < this.childCount){
            val top = this.getChildAt(movePosition).top
            this.smoothScrollBy(0,top)
        }
    }else{
        this.smoothScrollToPosition(position)
        shouldScroll?.invoke(true,position)
    }
}