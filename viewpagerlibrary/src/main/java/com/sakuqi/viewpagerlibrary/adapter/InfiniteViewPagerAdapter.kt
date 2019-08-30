package com.sakuqi.viewpagerlibrary.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.sakuqi.viewpagerlibrary.R
import com.sakuqi.viewpagerlibrary.holder.ViewPagerHolder
import com.sakuqi.viewpagerlibrary.holder.ViewPagerHolderCreator
import java.lang.IllegalStateException

/**
 * 无限轮播适配器
 */
class InfiniteViewPagerAdapter <T>(val datas:List<T>,val creator: ViewPagerHolderCreator<ViewPagerHolder<T>>,var canLoop:Boolean = true):
    PagerAdapter(){

    private var mViewPager:ViewPager?=null
    var mPageClickListener:((View,Int)->Unit)?=null
    private val mLooperCountFactor = 500

    fun setUpViewPager(viewPager: ViewPager){
        mViewPager = viewPager
        mViewPager?.adapter = this
        mViewPager?.adapter?.notifyDataSetChanged()
        val currentItem = if(canLoop){getStartSelectItem()}else{0}
        mViewPager?.currentItem = currentItem
    }

    private fun getStartSelectItem(): Int {
        if(getRealCount() == 0){
            return 0
        }
        var currentItem = getRealCount() * mLooperCountFactor /2
        if(currentItem % getRealCount() == 0){
            return currentItem
        }
        while (currentItem % getRealCount() != 0){
            currentItem++
        }
        return currentItem
    }

    private fun getRealCount(): Int {
        return datas.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return if(canLoop) getRealCount() * mLooperCountFactor else getRealCount()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = getView(position,null,container)
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun finishUpdate(container: ViewGroup) {
        super.finishUpdate(container)
        if(canLoop){
            var position = mViewPager?.currentItem
            if(position == count -1){
                position = 0
                setCurrentItem(position)
            }
        }
    }

    private fun setCurrentItem(position:Int){
        try {
            mViewPager?.setCurrentItem(position, false)
        }catch (e:IllegalStateException){
            e.printStackTrace()
        }
    }
    private fun getView(position: Int, view: View?, container: ViewGroup): View {
        val realPosition = position % getRealCount()
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
            holder.onBind(container.context,realPosition,datas[realPosition])
        }
        viewT.setOnClickListener {
            mPageClickListener?.invoke(viewT,realPosition)
        }
        return viewT
    }
}