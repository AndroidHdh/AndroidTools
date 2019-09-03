package com.sakuqi.viewpagerlibrary.demo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.sakuqi.viewpagerlibrary.*
import com.sakuqi.viewpagerlibrary.adapter.CommonViewPagerAdapter
import com.sakuqi.viewpagerlibrary.adapter.InfiniteViewPagerAdapter
import com.sakuqi.viewpagerlibrary.holder.ViewPagerHolder
import com.sakuqi.viewpagerlibrary.holder.ViewPagerHolderCreator
import com.sakuqi.viewpagerlibrary.transformer.CustomTransformer
import com.sakuqi.viewpagerlibrary.view.InfiniteViewPager
import kotlinx.android.synthetic.main.activity_common_view_pager.*

class CommonViewPagerActivity : AppCompatActivity() {
    private lateinit var mViewPagerAutoInfinite:InfiniteViewPager<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_view_pager)
        viewPager.adapter =
            CommonViewPagerAdapter(mutableListOf("111", "222", "333")
                ,
                object :
                    ViewPagerHolderCreator<ViewPagerHolder<String>> {
                    override fun createViewHolder(): StringHolder {
                        return StringHolder()
                    }
                })
        viewPager.offscreenPageLimit = 3
        viewPager.setPageTransformer(true, CustomTransformer())
        circleIndicator.setUpWithViewPager(viewPager)
        circleIndicator1.setUpWithViewPager(viewPager)
        circleIndicator2.setUpWithViewPager(viewPager)

        val infiniteViewPagerAdapter =
            InfiniteViewPagerAdapter(mutableListOf("111", "222", "333")
                ,
                object :
                    ViewPagerHolderCreator<ViewPagerHolder<String>> {
                    override fun createViewHolder(): StringHolder {
                        return StringHolder()
                    }
                })
        infiniteViewPagerAdapter.setUpViewPager(viewPagerInfinite)
        viewPagerInfinite.offscreenPageLimit = 3
        viewPagerInfinite.setPageTransformer(true, CustomTransformer())


        mViewPagerAutoInfinite = findViewById(R.id.viewPagerAutoInfinite)
        mViewPagerAutoInfinite.setPage(mutableListOf("111", "222", "333")
            ,
            object :
                ViewPagerHolderCreator<ViewPagerHolder<String>> {
                override fun createViewHolder(): StringHolder {
                    return StringHolder()
                }
            })
        mViewPagerAutoInfinite.start()
    }

    class StringHolder : ViewPagerHolder<String> {
        lateinit var textView:TextView
        override fun createView(context: Context): View {
            val view = LayoutInflater.from(context).inflate(R.layout.viewpager_item_view,null)
            textView = view.findViewById(R.id.textView)
            return view
        }

        override fun onBind(context: Context, position: Int, data: String) {
            textView.text = data
            textView.setBackgroundColor(when(position) {
                0 -> ContextCompat.getColor(context, R.color.colorAccent)
                1 -> ContextCompat.getColor(context, R.color.colorPrimary)
                else -> ContextCompat.getColor(context, R.color.colorPrimaryDark)
            })
        }
    }
}
