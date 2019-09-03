package com.sakuqi.recyclerviewlibrary.adapter

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

open class BaseHeaderFooterViewAdapter<T>(val mDatas: MutableList<T>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_NORMAL = 0x01
        const val TYPE_HEADER = 0x02
        const val TYPE_FOOTER = 0x03
    }

    open var mUseHeader = false
    open var mUseFooter = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> onCreateHeaderHolder(parent)
            TYPE_NORMAL -> onCreateNormalHolder(parent)
            else -> onCreateFooterHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            TYPE_HEADER -> onBindHeaderHolder(holder)
            TYPE_NORMAL -> onBindNormalHolder(holder,position)
            else ->onBindFooterHolder(holder)
        }
    }

    fun getRealPosition(position: Int): Int {
        return if (mUseHeader) {
            position - 1
        } else {
            position
        }
    }

    override fun getItemCount(): Int {
        var count = 0
        if (mUseHeader)
            count++
        if (mUseFooter)
            count++
        return count + mDatas.size
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0 && mUseHeader)
            return TYPE_HEADER
        if (position == itemCount - 1 && mUseFooter)
            return TYPE_FOOTER

        return TYPE_NORMAL
    }

    open fun onCreateHeaderHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return HeaderViewHolder(View(parent.context))
    }

    open fun onCreateNormalHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return NormalViewHolder(View(parent.context))
    }

    open fun onCreateFooterHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return FooterViewHolder(View(parent.context))
    }

    open fun onBindHeaderHolder(holder: RecyclerView.ViewHolder) {

    }

    open fun onBindNormalHolder(holder: RecyclerView.ViewHolder,position: Int) {

    }

    open fun onBindFooterHolder(holder: RecyclerView.ViewHolder) {

    }

    open class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    open class NormalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    open class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}