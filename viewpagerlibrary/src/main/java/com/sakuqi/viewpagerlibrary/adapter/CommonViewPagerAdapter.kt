package com.sakuqi.viewpagerlibrary.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.sakuqi.viewpagerlibrary.R
import com.sakuqi.viewpagerlibrary.holder.ViewPagerHolder
import com.sakuqi.viewpagerlibrary.holder.ViewPagerHolderCreator

/**
 * 通用ViewPager适配器
 */
class CommonViewPagerAdapter<T>(val datas:List<T>,val creator: ViewPagerHolderCreator<ViewPagerHolder<T>>):PagerAdapter(){
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return datas.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = getView(position,null,container)
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
    private fun getView(position: Int,view: View?,container: ViewGroup):View{
        var viewT = view
        val holder: ViewPagerHolder<T>?
        if(viewT == null){
            holder = creator.createViewHolder()
            viewT = holder.createView(container.context)
            viewT.setTag(R.id.common_view_pager_item_tag,holder)
        }else{
            holder = viewT.getTag(R.id.common_view_pager_item_tag) as ViewPagerHolder<T>
        }
        if(!datas.isNullOrEmpty()){
            holder.onBind(container.context,position,datas[position])
        }
        return viewT
    }
}