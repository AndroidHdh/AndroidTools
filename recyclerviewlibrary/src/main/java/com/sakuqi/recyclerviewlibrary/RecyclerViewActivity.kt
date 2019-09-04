package com.sakuqi.recyclerviewlibrary

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sakuqi.corelibrary.BaseActvity
import com.sakuqi.recyclerviewlibrary.adapter.SimpleViewAdapter
import kotlinx.android.synthetic.main.activity_recycler_view.*

class RecyclerViewActivity : BaseActvity() {
    lateinit var itemDecorationH:RecyclerView.ItemDecoration
    lateinit var itemDecorationV:RecyclerView.ItemDecoration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        itemDecorationH = DividerItemDecoration(this,LinearLayoutManager.HORIZONTAL)
        itemDecorationV = DividerItemDecoration(this,LinearLayoutManager.VERTICAL)
        title = "RecyclerView"
        initRecyclerView()
    }

    fun initRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        val simpleViewAdapter = SimpleViewAdapter(mutableListOf("1","2","3","4","5","6","7","8","9","10","11","12"))
        simpleViewAdapter.mUseHeader = true
        simpleViewAdapter.mUseFooter = true
        recyclerView.adapter =simpleViewAdapter

        recyclerView.addItemDecoration(itemDecorationH)
        recyclerView.removeItemDecoration(itemDecorationH)
        recyclerView.addItemDecoration(itemDecorationV)
    }

    fun initRecyclerView3(){
        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        val simpleViewAdapter = SimpleViewAdapter(mutableListOf("1","2","3","4","5","6","7","8","9","10","11","12"))
        simpleViewAdapter.mUseHeader = true
        simpleViewAdapter.mUseFooter = true
        recyclerView.adapter =simpleViewAdapter
        recyclerView.addItemDecoration(itemDecorationV)
        recyclerView.removeItemDecoration(itemDecorationV)
        recyclerView.addItemDecoration(itemDecorationH)
    }

    fun initRecyclerView2(){
        recyclerView.layoutManager = GridLayoutManager(this,3)
        val simpleViewAdapter = SimpleViewAdapter(mutableListOf("1","2","3","4","5","6","7","8","9","10","11","12"))
        simpleViewAdapter.mUseHeader = true
        simpleViewAdapter.mUseFooter = true
        simpleViewAdapter.adjustSpanSize(recyclerView)
        recyclerView.adapter =simpleViewAdapter
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_recycler_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_linear_layout -> {
                initRecyclerView()
            }

            R.id.item_grid_layout ->{
                initRecyclerView2()
            }

            R.id.item_linear_horizontal_layout ->{
                initRecyclerView3()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
