package com.sakuqi.recyclerviewlibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.sakuqi.recyclerviewlibrary.adapter.SimpleViewAdapter
import kotlinx.android.synthetic.main.activity_recycler_view.*

class RecyclerViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val simpleViewAdapter = SimpleViewAdapter(mutableListOf("1","2","3","4","5","6","7","8","9","10","11","12"))
        simpleViewAdapter.mUseHeader = true
        simpleViewAdapter.mUseFooter = true
        recyclerView.adapter =simpleViewAdapter
    }
}
