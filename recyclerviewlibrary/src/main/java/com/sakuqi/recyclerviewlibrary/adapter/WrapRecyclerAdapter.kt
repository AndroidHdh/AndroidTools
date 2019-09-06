package com.sakuqi.recyclerviewlibrary.adapter

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class WrapRecyclerAdapter(val adapter:RecyclerView.Adapter<RecyclerView.ViewHolder>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mHeaderViews:SparseArray<View> = SparseArray()
    private var mFooterViews:SparseArray<View> = SparseArray()

    companion object{
        var BASE_ITEM_TYPE_HEADER = 1000000
        var BASE_ITEM_TYPE_FOOTER = 2000000
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(isHeaderViewType(viewType)){
            val headerView = mHeaderViews.get(viewType)
            return createHeaderFooterViewHolder(headerView)
        }

        if(isFooterViewType(viewType)){
            val footerView = mFooterViews.get(viewType)
            return createHeaderFooterViewHolder(footerView)
        }
        return adapter.onCreateViewHolder(parent,viewType)
    }

    override fun getItemCount(): Int {
        return adapter.itemCount + mHeaderViews.size() +mFooterViews.size()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(isHeaderPosition(position) || isFooterPosition(position))
            return
        val realPosition = position - mHeaderViews.size()
        adapter.onBindViewHolder(holder,realPosition)
    }

    override fun getItemViewType(position: Int): Int {
        if(isHeaderPosition(position))
            return mHeaderViews.keyAt(position)
        if (isFooterPosition(position)) {
            val footerPosition = position - mHeaderViews.size() - adapter.itemCount
            return mFooterViews.keyAt(footerPosition)
        }

        val realPosition = position - mHeaderViews.size()
        return adapter.getItemViewType(realPosition)
    }

    /**
     * 是否是底部类型
     */
    private fun isFooterViewType(viewType: Int):Boolean{
        val position = mFooterViews.indexOfKey(viewType)
        return position >= 0
    }

    /**
     * 是否是头部类型
     */
    private fun isHeaderViewType(viewType: Int):Boolean{
        val position = mHeaderViews.indexOfKey(viewType)
        return position >= 0
    }

    /**
     * 创建顶部或者底部的Holder
     */
    private fun createHeaderFooterViewHolder(view:View):RecyclerView.ViewHolder{
        return object :RecyclerView.ViewHolder(view){}
    }

    /**
     * 是否是底部位置
     */
    private fun isFooterPosition(position: Int):Boolean{
        return position >= (mHeaderViews.size() + adapter.itemCount)
    }

    /**
     * 是否是头部位置
     */
    private fun isHeaderPosition(position: Int):Boolean{
        return position < mHeaderViews.size()
    }

    /**
     * 添加头部布局
     */
    fun addHeaderView(view:View){
        val position = mHeaderViews.indexOfValue(view)
        if (position < 0){
            mHeaderViews.put(BASE_ITEM_TYPE_HEADER++,view)
        }
        notifyDataSetChanged()
    }

    /**
     * 添加底部布局
     */
    fun addFooterView(view:View){
        val position = mFooterViews.indexOfValue(view)
        if(position < 0){
            mFooterViews.put(BASE_ITEM_TYPE_FOOTER++,view)
        }
        notifyDataSetChanged()
    }

    /**
     * 移除头部布局
     */
    fun removeHeaderView(view:View){
        val index = mHeaderViews.indexOfValue(view)
        if(index < 0) return
        mHeaderViews.removeAt(index)
        notifyDataSetChanged()
    }

    /**
     * 移除底部布局
     */
    fun removeFooterView(view:View){
        val index = mFooterViews.indexOfValue(view)
        if(index < 0) return
        mFooterViews.removeAt(index)
        notifyDataSetChanged()
    }

    fun adjustSpanSize(recyclerView: RecyclerView){
        if(recyclerView.layoutManager is GridLayoutManager){
            val layoutManager = recyclerView.layoutManager as GridLayoutManager
            layoutManager.spanSizeLookup = object :GridLayoutManager.SpanSizeLookup(){
                override fun getSpanSize(position: Int): Int {
                    val isHeaderOrFooter = isHeaderPosition(position) || isFooterPosition(position)
                    return if(isHeaderOrFooter)layoutManager.spanCount else 1
                }
            }
        }
    }
}