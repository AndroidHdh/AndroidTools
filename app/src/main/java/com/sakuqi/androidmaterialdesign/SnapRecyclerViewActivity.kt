package com.sakuqi.androidmaterialdesign

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.sakuqi.androidmaterialdesign.snap.StartSnapHelper
import com.sakuqi.viewpagerlibrary.utils.DisplayUtils
import kotlinx.android.synthetic.main.activity_snap_recycler_view.*

/**
 * RecyclerView居中或者开始
 */
class SnapRecyclerViewActivity : AppCompatActivity() {
    var datas = mutableListOf("", "", "","","","","")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snap_recycler_view)
        toolBar.inflateMenu(R.menu.snap_menu)
        toolBar.setNavigationOnClickListener {
            finish()
        }
        toolBar.setOnMenuItemClickListener { item ->
            when {
                item?.itemId == R.id.item_hor -> {
                    horSnap()
                }
                item?.itemId == R.id.item_hor_viewPager -> {
                    horViewPagerSnap()
                }
                item?.itemId == R.id.item_hor_start -> {
                    horStartSnap()
                }
                item?.itemId == R.id.item_vertical_start -> {
                    veriticalStartSnap()
                }
                item?.itemId == R.id.item_vertical -> {
                    veriticalSnap()
                }
                item?.itemId == R.id.item_vertical_viewPager -> {
                    veriticalViewPagerSnap()
                }
            }
            true
        }

    }

    private fun veriticalSnap() {
        val manager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerview.layoutManager = manager
        recyclerview.adapter = SnapAdapter(datas, this, Type.VER)
        recyclerview.onFlingListener = null
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerview)
    }

    private fun horSnap() {
        val manager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        recyclerview.layoutManager = manager
        recyclerview.adapter = SnapAdapter(datas, this, Type.HOR)
        recyclerview.onFlingListener = null
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerview)
    }

    private fun veriticalStartSnap() {
        val manager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerview.layoutManager = manager
        recyclerview.adapter = SnapAdapter(datas, this, Type.VER)
        recyclerview.onFlingListener = null
        val snapHelper = StartSnapHelper()
        snapHelper.attachToRecyclerView(recyclerview)
    }

    private fun horStartSnap() {
        val manager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        recyclerview.layoutManager = manager
        recyclerview.adapter = SnapAdapter(datas, this, Type.HOR)
        recyclerview.onFlingListener = null
        val snapHelper = StartSnapHelper()
        snapHelper.attachToRecyclerView(recyclerview)
    }

    private fun veriticalViewPagerSnap() {
        val manager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerview.layoutManager = manager
        recyclerview.adapter = SnapAdapter(datas, this, Type.VER)
        recyclerview.onFlingListener = null
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerview)
    }

    private fun horViewPagerSnap() {
        val manager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        recyclerview.layoutManager = manager
        recyclerview.adapter = SnapAdapter(datas, this, Type.HOR)
        recyclerview.onFlingListener = null
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerview)
    }

    enum class Type {
        VER, HOR, SNAP_VER, SNAP_HOR
    }

    class SnapAdapter<T>(val datas: MutableList<T>, val context: Context, val type: Type) :
        RecyclerView.Adapter<SnapVH>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SnapVH {
            val view = LayoutInflater.from(context).inflate(R.layout.item_snap_view, parent, false)
            val layoutParams = view.layoutParams
            when (type) {
                Type.VER -> {
                    layoutParams.height = DisplayUtils.dpToPx(400f)
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                }
                Type.HOR -> {
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                    layoutParams.width = DisplayUtils.dpToPx(200f)
                }
                Type.SNAP_VER -> {
                    layoutParams.height = DisplayUtils.dpToPx(400f)
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                }
                Type.SNAP_HOR -> {
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                    layoutParams.width = DisplayUtils.dpToPx(200f)
                }
            }


            view.layoutParams = layoutParams
            return SnapVH(view)
        }

        override fun getItemCount(): Int {
            return datas.size
        }

        override fun onBindViewHolder(holder: SnapVH, position: Int) {

        }
    }

    class SnapVH(item: View) : RecyclerView.ViewHolder(item)
}
