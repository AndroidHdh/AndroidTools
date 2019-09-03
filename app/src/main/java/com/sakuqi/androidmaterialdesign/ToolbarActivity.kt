package com.sakuqi.androidmaterialdesign

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sakuqi.androidmaterialdesign.behavior.CustomBehaviorActivity
import com.sakuqi.recyclerviewlibrary.RecyclerViewActivity
import com.sakuqi.viewpagerlibrary.demo.CommonViewPagerActivity
import com.sakuqi.webviewlibrary.WebViewActivity
import kotlinx.android.synthetic.main.activity_main.*

class ToolbarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolBar.inflateMenu(R.menu.setting_menu)
        toolBar.setNavigationOnClickListener {
            finish()
        }
        toolBar.setOnMenuItemClickListener { item ->
            if(item?.itemId == R.id.item_viewpager){
                startActivity(Intent(this@ToolbarActivity, CommonViewPagerActivity::class.java))
            }else if(item?.itemId == R.id.item_snap_recyclerview){
                startActivity(Intent(this@ToolbarActivity, SnapRecyclerViewActivity::class.java))
            }else if(item?.itemId == R.id.item_webview){
                startActivity(Intent(this@ToolbarActivity, WebViewActivity::class.java))
            }else if(item?.itemId == R.id.item_recyclerview){
                startActivity(Intent(this@ToolbarActivity, RecyclerViewActivity::class.java))
            }
            true
        }

        val sheetBehavior = BottomSheetBehavior.from(share_view)
        sheetBehavior.setBottomSheetCallback(object:BottomSheetBehavior.BottomSheetCallback(){
            override fun onSlide(p0: View, p1: Float) {

            }

            override fun onStateChanged(p0: View, p1: Int) {

            }

        })
        sheetBehavior.isHideable = true
        showOrHidden.setOnClickListener {
            if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }else{
                sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }

        showOrHiddenDialog.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog,null);
            dialog.setContentView(view)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.show()
        }

        customBehavior.setOnClickListener {
            startActivity(Intent(this@ToolbarActivity,CustomBehaviorActivity::class.java))
        }
    }
}
