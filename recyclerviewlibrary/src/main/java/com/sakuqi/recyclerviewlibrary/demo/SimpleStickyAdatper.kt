package com.sakuqi.recyclerviewlibrary.demo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sakuqi.recyclerviewlibrary.R
import com.sakuqi.recyclerviewlibrary.adapter.BaseHeaderFooterViewAdapter
import com.sakuqi.recyclerviewlibrary.sticky.IStickyHeader
import com.sakuqi.recyclerviewlibrary.utils.PinYinUtil

class SimpleStickyAdatper(datas: MutableList<String>) :
    BaseHeaderFooterViewAdapter<String>(datas),
    IStickyHeader {
    override fun onCreateNormalHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return return SimpleStickyHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_normal,
                parent,
                false
            )
        )
    }

    override fun onBindNormalHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindNormalHolder(holder, position)
        if (holder is SimpleStickyHolder) {
            holder.list_text.text = mDatas[position]
        }
    }

    class SimpleStickyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val list_text = itemView.findViewById<TextView>(R.id.list_text)
    }

    override fun getHeaderId(position: Int): String {
        if(position >=mDatas.size){
            return ""
        }
        return PinYinUtil.getPinYinFirstString(mDatas[position])
    }

    fun getPositionForSection(section:String):Int{
        repeat(mDatas.count()) {
            if(section == PinYinUtil.getPinYinFirstString(mDatas[it])){
                return it
            }
        }
        return -1
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return return HeaderHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_sticky_head,
                parent,
                false
            )
        )
    }

    override fun onBindHeaderViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is HeaderHolder) {
            viewHolder.headTv.text = PinYinUtil.getPinYinFirstString(mDatas[position])
        }
    }

    class HeaderHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val headTv = itemView.findViewById<TextView>(R.id.headTv)
    }
}