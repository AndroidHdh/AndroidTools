package com.sakuqi.corelibrary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_base.*

open class BaseActvity :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_base)
        setSupportActionBar(toolBar)
    }

    override fun setContentView(layoutResID: Int) {
        findViewById<FrameLayout>(R.id.frameLayout).removeAllViews()
        LayoutInflater.from(this).inflate(layoutResID,findViewById(R.id.frameLayout))
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        findViewById<FrameLayout>(R.id.frameLayout).removeAllViews()
        findViewById<FrameLayout>(R.id.frameLayout).addView(view,params)
    }

    override fun setContentView(view: View?) {
        findViewById<FrameLayout>(R.id.frameLayout).removeAllViews()
        findViewById<FrameLayout>(R.id.frameLayout).addView(view)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}