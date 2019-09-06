package com.sakuqi.recyclerviewlibrary.sticky

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * 黏贴头部接口
 */
interface IStickyHeader {
    fun isGroupLast(position: Int):Boolean{
        if(getHeaderId(position) != getHeaderId(position+1)){
            return true
        }
        return false
    }
    fun getHeaderId(position: Int):String

    fun onCreateHeaderViewHolder(parent:ViewGroup):RecyclerView.ViewHolder

    fun onBindHeaderViewHolder(viewHolder: RecyclerView.ViewHolder,position: Int)
}