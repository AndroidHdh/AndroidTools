package com.sakuqi.recyclerviewlibrary.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.sakuqi.recyclerviewlibrary.R

class SideBar(context:Context,attrs:AttributeSet?,defStyle:Int) :View(context,attrs,defStyle){
    constructor(context: Context,attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context):this(context,null,0)

    companion object{
        val DEFAULT_CHAR = arrayOf("A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#")
    }

    var choose = -1
    val paint = Paint()
    var onTouchLetterChangedListener :((s:String)->Unit)?=null

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var height = height
        var width = width
        var singleHeight = height / DEFAULT_CHAR.size
        repeat(DEFAULT_CHAR.count()) {
            paint.color = ContextCompat.getColor(context, R.color.side_bar_default_color)
            paint.typeface = Typeface.DEFAULT_BOLD
            paint.isAntiAlias = true
            paint.textSize = resources.getDimensionPixelSize(R.dimen.list_item_head_size).toFloat()
            if(it == choose){
                paint.color= ContextCompat.getColor(context, R.color.side_bar_select_color)
                paint.isFakeBoldText = true
            }
            var xPos = width / 2 - paint.measureText(DEFAULT_CHAR[it])/2
            var yPos = singleHeight * (it + 1).toFloat()
            canvas?.drawText(DEFAULT_CHAR[it],xPos,yPos,paint)
            paint.reset()
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        val action = event?.action
        val y = event?.y?:0f
        var oldChoose = choose

        var c = y.div( height/ DEFAULT_CHAR.size).toInt()
        when(action){
            MotionEvent.ACTION_UP ->{
                setBackgroundColor(Color.TRANSPARENT)
                choose = -1
                invalidate()
            }
            else ->{
                setBackgroundColor(Color.LTGRAY)
                if(oldChoose != c){
                    if(c >= 0 && c < DEFAULT_CHAR.size){
                        onTouchLetterChangedListener?.invoke(DEFAULT_CHAR[c])
                        choose = c
                        invalidate()
                    }
                }
            }
        }
        return true
    }

}