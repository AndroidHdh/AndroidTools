package com.sakuqi.recyclerviewlibrary.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sakuqi.recyclerviewlibrary.adapter.WrapRecyclerAdapter

/**
 * RecyclerView的子类
 * 实现添加头部和底部的功能
 */
open class WrapRecyclerView(context: Context,attributeSet: AttributeSet?,defStyle:Int) :RecyclerView(context,attributeSet,defStyle){
    constructor(context: Context):this(context,null,0)
    constructor(context: Context,attributeSet: AttributeSet?):this(context,attributeSet,0)
    private var mWrapRecyclerAdapter:WrapRecyclerAdapter?=null
    private var mAdapter:Adapter<ViewHolder>? = null

    override fun setAdapter(adapter: Adapter<ViewHolder>?) {
        if (adapter == null) {
            return
        }
        if(mAdapter != null){
            mAdapter?.unregisterAdapterDataObserver(mDataObserver)
            mAdapter = null
        }

        this.mAdapter = adapter
        mWrapRecyclerAdapter = if(adapter is WrapRecyclerAdapter){
            adapter
        }else{
            WrapRecyclerAdapter(adapter)
        }
        super.setAdapter(mWrapRecyclerAdapter)

        mAdapter?.registerAdapterDataObserver(mDataObserver)
        mWrapRecyclerAdapter?.adjustSpanSize(this)
    }

    fun addHeaderView(view:View){
        mWrapRecyclerAdapter?.addHeaderView(view)
    }

    fun addFooterView(view:View){
        mWrapRecyclerAdapter?.addFooterView(view)
    }

    fun removeFooterView(view:View){
        mWrapRecyclerAdapter?.removeFooterView(view)
    }

    fun removeHeaderView(view: View){
        mWrapRecyclerAdapter?.removeHeaderView(view)
    }
    private val mDataObserver = object :AdapterDataObserver(){
        override fun onChanged() {
            if(mAdapter == null) return
            if(mWrapRecyclerAdapter != mAdapter)
                mWrapRecyclerAdapter?.notifyDataSetChanged()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            if(mAdapter == null) return
            if(mWrapRecyclerAdapter != mAdapter)
                mWrapRecyclerAdapter?.notifyItemRemoved(positionStart)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            if(mAdapter == null) return
            if(mWrapRecyclerAdapter != mAdapter)
                mWrapRecyclerAdapter?.notifyItemMoved(fromPosition,toPosition)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            if(mAdapter == null) return
            if(mWrapRecyclerAdapter != mAdapter){
                mWrapRecyclerAdapter?.notifyItemInserted(positionStart)
            }
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            if(mAdapter == null) return
            if(mWrapRecyclerAdapter != mAdapter){
                mWrapRecyclerAdapter?.notifyItemChanged(positionStart)
            }
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            if(mAdapter == null) return
            if(mWrapRecyclerAdapter != mAdapter){
                mWrapRecyclerAdapter?.notifyItemChanged(positionStart,payload)
            }
        }
    }
}