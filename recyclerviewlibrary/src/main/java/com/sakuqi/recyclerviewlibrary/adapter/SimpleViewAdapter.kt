package com.sakuqi.recyclerviewlibrary.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sakuqi.corelibrary.BaseApplication
import com.sakuqi.recyclerviewlibrary.R
import com.sakuqi.viewpagerlibrary.holder.ViewPagerHolder
import com.sakuqi.viewpagerlibrary.holder.ViewPagerHolderCreator
import com.sakuqi.viewpagerlibrary.view.InfiniteViewPager
import java.io.File

class SimpleViewAdapter(datas: MutableList<String>) : BaseHeaderFooterViewAdapter<String>(datas) {
    override fun onCreateHeaderHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return SimpleHeaderHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_header,
                parent,
                false
            )
        )
    }

    override fun onCreateNormalHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return SimpleNormalHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_normal,
                parent,
                false
            )
        )
    }

    override fun onCreateFooterHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return SimpleFooterHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_footer,
                parent,
                false
            )
        )
    }

    override fun onBindHeaderHolder(holder: RecyclerView.ViewHolder) {
        if (holder is SimpleHeaderHolder) {
            holder.viewPagerAutoInfinite.setPage(mutableListOf(
                "img01.jpg",
                "img02.jpg",
                "img03.jpg",
                "img04.jpg",
                "img05.jpg"
            )
                ,
                object :
                    ViewPagerHolderCreator<ViewPagerHolder<String>> {
                    override fun createViewHolder(): StringHolder {
                        return StringHolder()
                    }
                })
            holder.viewPagerAutoInfinite.start()
        }
    }

    override fun onBindNormalHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SimpleNormalHolder) {
            holder.list_text.text = mDatas[getRealPosition(position)]
        }
    }

    override fun onBindFooterHolder(holder: RecyclerView.ViewHolder) {
        if (holder is SimpleFooterHolder) {
            holder.footerTv.text = BaseApplication.instance.getString(R.string.str_footer_text)
        }
    }

    class SimpleHeaderHolder(itemView: View) : HeaderViewHolder(itemView) {
        val viewPagerAutoInfinite =
            itemView.findViewById<InfiniteViewPager<String>>(R.id.viewPagerAutoInfinite)
    }

    class SimpleNormalHolder(itemView: View) : NormalViewHolder(itemView) {
        val list_text = itemView.findViewById<TextView>(R.id.list_text)
    }

    class SimpleFooterHolder(itemView: View) : FooterViewHolder(itemView) {
        val footerTv = itemView.findViewById<TextView>(R.id.footerTv)
    }

    class StringHolder : ViewPagerHolder<String> {
        lateinit var imageView: ImageView
        override fun createView(context: Context): View {
            val view = LayoutInflater.from(context).inflate(R.layout.item_head_viewpager, null)
            imageView = view.findViewById(R.id.viewpagerIv)
            return view
        }

        override fun onBind(context: Context, position: Int, data: String) {
            val stream = context.assets.open(data)
            imageView.setImageBitmap(BitmapFactory.decodeStream(stream))
        }
    }
}