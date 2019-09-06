package com.sakuqi.recyclerviewlibrary.demo

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sakuqi.corelibrary.BaseActvity
import com.sakuqi.recyclerviewlibrary.R
import com.sakuqi.recyclerviewlibrary.decoration.DividerItemDecoration
import com.sakuqi.recyclerviewlibrary.ext.smoothMoveToPosition
import com.sakuqi.recyclerviewlibrary.refresh.DefaultLoadCreator
import com.sakuqi.recyclerviewlibrary.refresh.DefaultRefreshCreator
import com.sakuqi.recyclerviewlibrary.sticky.StickyHeaderDecoration
import com.sakuqi.recyclerviewlibrary.utils.PinYinUtil
import com.sakuqi.recyclerviewlibrary.view.WrapRecyclerView
import com.sakuqi.viewpagerlibrary.holder.ViewPagerHolder
import com.sakuqi.viewpagerlibrary.holder.ViewPagerHolderCreator
import com.sakuqi.viewpagerlibrary.view.InfiniteViewPager
import kotlinx.android.synthetic.main.activity_recycler_view.*

class RecyclerViewActivity : BaseActvity() {
    lateinit var itemDecorationH: RecyclerView.ItemDecoration
    lateinit var itemDecorationV: RecyclerView.ItemDecoration
    lateinit var recyclerView: WrapRecyclerView
    val datas = mutableListOf(
        "1",
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
        "10",
        "11",
        "12"
    )
    val stickyDatas = mutableListOf(
        "阿魏八味丸",
        "阿昔洛韦眼膏",
        "艾司洛尔",
        "安吖啶注射液",
        "阿达帕林",
        "参茸追风酒",
        "草乌",
        "石斛夜光丸",
        "骨质增生片",
        "乌鸡白凤丸",
        "人参益母丸",
        "补脾益肠丸",
        "丹参片",
        "小金丸",
        "妇宁康",
        "糖脉康",
        "菲伯瑞",
        "乙肝解毒片",
        "脑血栓片"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        itemDecorationH = DividerItemDecoration(
            this,
            LinearLayoutManager.HORIZONTAL
        )
        itemDecorationV = DividerItemDecoration(
            this,
            LinearLayoutManager.VERTICAL
        )
        title = "RecyclerView"
        initRecyclerView5()
    }

    fun initRecyclerView() {
        sideBar.visibility = GONE
        recyclerViewHeadRefresh.visibility = GONE
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val simpleViewAdapter = SimpleAdapter(datas)
        recyclerView.adapter = simpleViewAdapter
        recyclerView.addHeaderView(createHeaderView(recyclerView))
        recyclerView.addFooterView(createFooterView(recyclerView))

        recyclerView.addItemDecoration(itemDecorationH)
        recyclerView.removeItemDecoration(itemDecorationH)
        recyclerView.addItemDecoration(itemDecorationV)
    }

    fun initRecyclerView2() {
        recyclerViewHeadRefresh.visibility = GONE
        sideBar.visibility = GONE
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        val simpleViewAdapter = SimpleAdapter(datas)
        recyclerView.adapter = simpleViewAdapter
        recyclerView.addHeaderView(createHeaderView(recyclerView))
    }

    fun initRecyclerView3() {
        recyclerViewHeadRefresh.visibility = GONE
        sideBar.visibility = GONE
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val simpleViewAdapter = SimpleAdapter(datas)
        recyclerView.adapter = simpleViewAdapter
        recyclerView.addHeaderView(createHeaderView(recyclerView))
        recyclerView.addItemDecoration(itemDecorationV)
        recyclerView.removeItemDecoration(itemDecorationV)
        recyclerView.addItemDecoration(itemDecorationH)
    }

    private var shouldScroll = true
    private var toPosition = -1
    fun initRecyclerView4() {
        recyclerViewHeadRefresh.visibility = GONE
        sideBar.visibility = VISIBLE
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val simpleViewAdapter =
            SimpleStickyAdatper(
                stickyDatas.sortedBy { PinYinUtil.getPinYinFirstString(it) }.toMutableList()
            )
        recyclerView.adapter = simpleViewAdapter
        recyclerView.addItemDecoration(StickyHeaderDecoration(simpleViewAdapter))
        recyclerView.addItemDecoration(object :
            DividerItemDecoration(this@RecyclerViewActivity, LinearLayoutManager.VERTICAL) {
            override fun drawDecoration(position: Int): Boolean {
                return !simpleViewAdapter.isGroupLast(position)
            }
        })

        sideBar.onTouchLetterChangedListener = {
            val position = simpleViewAdapter.getPositionForSection(it)
            if (position != -1) {
                recyclerView.smoothMoveToPosition(position) { shouldScroll, position ->
                    this.shouldScroll = shouldScroll
                    this.toPosition = position
                }
            }
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (shouldScroll && RecyclerView.SCROLL_STATE_IDLE == newState && toPosition != -1) {
                    shouldScroll = false
                    recyclerView.smoothMoveToPosition(toPosition)
                }
            }
        })
    }

    fun initRecyclerView5(){
        recyclerViewHeadRefresh.visibility = VISIBLE
        sideBar.visibility = GONE
        recyclerViewHeadRefresh.layoutManager = LinearLayoutManager(this)
        val simpleViewAdapter = SimpleAdapter(datas)
        recyclerViewHeadRefresh.adapter = simpleViewAdapter
        recyclerViewHeadRefresh.addRefreshViewCreator(DefaultRefreshCreator())
        recyclerViewHeadRefresh.addOnRefreshListener {
            recyclerViewHeadRefresh.postDelayed({
                recyclerViewHeadRefresh.onStopRefresh()
            },2000)
        }
        recyclerViewHeadRefresh.addHeaderView(createHeaderView(recyclerViewHeadRefresh))
        recyclerViewHeadRefresh.addFooterView(createFooterView(recyclerViewHeadRefresh))
        recyclerViewHeadRefresh.addItemDecoration(itemDecorationV)

        recyclerViewHeadRefresh.addLoadViewCreator(DefaultLoadCreator())
        recyclerViewHeadRefresh.addOnLoadListener {
            recyclerViewHeadRefresh.postDelayed({
                recyclerViewHeadRefresh.onStopLoad()
            },2000)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_recycler_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_linear_layout -> {
                initRecyclerView()
            }

            R.id.item_grid_layout -> {
                initRecyclerView2()
            }

            R.id.item_linear_horizontal_layout -> {
                initRecyclerView3()
            }
            R.id.item_sticky_layout -> {
                initRecyclerView4()
            }
            R.id.item_refresh_head_layout ->{
                initRecyclerView5()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createFooterView(parent:ViewGroup):View{
        val view = LayoutInflater.from(this).inflate(R.layout.item_footer,parent,false)
        return view
    }

    private fun createHeaderView(parent:ViewGroup): View {
        val view = LayoutInflater.from(this).inflate(R.layout.item_header, parent, false)
        val viewPagerAutoInfinite =
            view.findViewById<InfiniteViewPager<String>>(R.id.viewPagerAutoInfinite)
        viewPagerAutoInfinite.setPage(mutableListOf(
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
        viewPagerAutoInfinite.start()
        return view
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
