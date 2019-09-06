package com.sakuqi.recyclerviewlibrary.demo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sakuqi.recyclerviewlibrary.R

class SimpleAdapter(val datas: MutableList<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SimpleNormalHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_normal,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SimpleNormalHolder) {
            holder.list_text.text = datas[position]
        }
    }

    override fun getItemCount(): Int {
        return datas.size
    }


    class SimpleNormalHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val list_text = itemView.findViewById<TextView>(R.id.list_text)
    }
}