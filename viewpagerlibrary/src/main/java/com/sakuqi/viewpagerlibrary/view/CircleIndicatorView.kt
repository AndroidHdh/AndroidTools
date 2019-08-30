package com.sakuqi.viewpagerlibrary.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.sakuqi.viewpagerlibrary.R
import com.sakuqi.viewpagerlibrary.utils.DisplayUtils

/**
 * 指示器
 */
class CircleIndicatorView(val mContext: Context, val attrs: AttributeSet?, defStyleAttr: Int) :
    View(mContext, attrs, defStyleAttr),
    ViewPager.OnPageChangeListener {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    companion object {
        val LETTER = arrayOf(
            "A",
            "B",
            "C",
            "D",
            "E",
            "F",
            "G",
            "H",
            "I",
            "G",
            "K",
            "L",
            "M",
            "N",
            "O",
            "P",
            "Q",
            "R",
            "S",
            "T",
            "U",
            "V",
            "W",
            "X",
            "Y",
            "Z"
        )
    }

    private var mCirclePaint: Paint
    private var mTextPaint: Paint
    private var mViewPager: ViewPager? = null
    private var mIndicators: MutableList<Indicator> = mutableListOf()
    private var mRadius: Float = 0f
        set(value) {
            field = value
            initValue()
        }
    private var mCount: Int = 0
        set(value) {
            field = value
            invalidate()
        }
    var mTextColor = Color.parseColor("#FF0000")
        set(value) {
            field = value
            initValue()
        }
    var mStrokeWidth: Float = 0f
        set(value) {
            field = value
            initValue()
        }
    var mSelectColor = Color.parseColor("#FFFFFF")
    var mDotNormalColor = Color.parseColor("#000000")
    var mOnIndicatorClickListener: OnIndicatorClickListener? = null
    var mSpace = 0
    var mSelectPosition = 0
    var mFillMode = FillMode.NONE
    var mIsEnableClickSwitch = false

    init {
        mCirclePaint = Paint()
        mCirclePaint.isDither = true
        mCirclePaint.isAntiAlias = true
        mCirclePaint.style = Paint.Style.FILL_AND_STROKE

        mTextPaint = Paint()
        mTextPaint.isDither = true
        mTextPaint.isAntiAlias = true

        initValue()
        getAttr()
    }

    private fun initValue() {
        mCirclePaint.color = mDotNormalColor
        mCirclePaint.strokeWidth = mStrokeWidth
        mTextPaint.color = mTextColor
        mTextPaint.textSize = mRadius
    }

    private fun getAttr() {
        val typeArray = mContext.obtainStyledAttributes(attrs,
            R.styleable.CircleIndicatorView
        )
        mRadius =
            typeArray.getDimensionPixelSize(R.styleable.CircleIndicatorView_indicatorRadius, DisplayUtils.dpToPx(6f))
                .toFloat()
        mStrokeWidth = typeArray.getDimensionPixelSize(
            R.styleable.CircleIndicatorView_indicatorBorderWidth,
            DisplayUtils.dpToPx(2f)
        ).toFloat()
        mSpace =
            typeArray.getDimensionPixelSize(R.styleable.CircleIndicatorView_indicatorSpace, DisplayUtils.dpToPx(5f))

        mTextColor = typeArray.getColor(R.styleable.CircleIndicatorView_indicatorTextColor, Color.BLACK)
        mSelectColor = typeArray.getColor(R.styleable.CircleIndicatorView_indicatorSelectColor, Color.WHITE)
        mDotNormalColor = typeArray.getColor(R.styleable.CircleIndicatorView_indicatorColor, Color.GRAY)

        mIsEnableClickSwitch = typeArray.getBoolean(R.styleable.CircleIndicatorView_enableIndicatorSwitch, false)
        val fillMode = typeArray.getInt(R.styleable.CircleIndicatorView_fill_mode, 2)
        if (fillMode == 0) {
            mFillMode = FillMode.LETTER
        } else if (fillMode == 1) {
            mFillMode = FillMode.NUMBER
        } else {
            mFillMode = FillMode.NONE
        }
        typeArray.recycle()
    }

    /**
     * 测量每个原点的位置
     */
    private fun measureIndicator() {
        mIndicators.clear()
        var cx = 0f
        for (i in 0 until mCount) {
            val indicator = Indicator()
            if (i == 0) {
                cx = mRadius + mStrokeWidth
            } else {
                cx += (mRadius + mStrokeWidth) * 2 + mSpace
            }

            indicator.cx = cx
            indicator.cy = measuredHeight / 2f
            indicator.position = i
            mIndicators.add(indicator)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = (mRadius + mStrokeWidth) * 2 * mCount + mSpace * (mCount - 1)
        val height = mRadius * 2 + mSpace * 2

        setMeasuredDimension(width.toInt(), height.toInt())
        measureIndicator()
    }

    override fun onDraw(canvas: Canvas?) {
        for (indicator in mIndicators) {
            val x = indicator.cx
            val y = indicator.cy
            if (mSelectPosition == indicator.position) {
                mCirclePaint.style = Paint.Style.FILL
                mCirclePaint.color = mSelectColor
            } else {
                mCirclePaint.color = mDotNormalColor
                if (mFillMode != FillMode.NONE) {
                    mCirclePaint.style = Paint.Style.STROKE
                } else {
                    mCirclePaint.style = Paint.Style.FILL
                }
            }
            canvas?.drawCircle(x, y, mRadius, mCirclePaint)

            if (mFillMode != FillMode.NONE) {
                var text = ""
                if (mFillMode == FillMode.LETTER) {
                    if (indicator.position >= 0 && indicator.position < LETTER.size) {
                        text = LETTER[indicator.position]
                    }
                } else {
                    text = indicator.position.toString()
                }
                val bound = Rect()
                mTextPaint.getTextBounds(text, 0, text.length, bound)
                val textWidth = bound.width()
                val textHeight = bound.height()
                val textStartX = x - textWidth / 2
                val textStartY = y + textHeight / 2
                canvas?.drawText(text, textStartX, textStartY, mTextPaint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var xPoint:Float
        var yPoint:Float
        when(event?.action){
            MotionEvent.ACTION_DOWN ->{
                xPoint = event.x
                yPoint = event.y
                handleActionDown(xPoint,yPoint)
            }
        }
        return super.onTouchEvent(event)
    }

    private fun handleActionDown(xPoint: Float, yPoint: Float) {
        for (indicator in mIndicators){
            if(xPoint < (indicator.cx + mRadius+mStrokeWidth) &&
                    xPoint >= (indicator.cx - (mRadius + mStrokeWidth)) &&
                    yPoint >= (yPoint - (indicator.cy + mStrokeWidth)) &&
                    yPoint < (indicator.cy + mRadius+mStrokeWidth)){
                if(mIsEnableClickSwitch){
                    mViewPager?.setCurrentItem(indicator.position,false)
                }
                mOnIndicatorClickListener?.onSelected(indicator.position)
            }
            break
        }
    }

    fun setUpWithViewPager(viewPager:ViewPager?){
        releaseViewPager()
        viewPager?.let {
            mViewPager = it
            mViewPager?.addOnPageChangeListener(this)
            mCount = mViewPager?.adapter?.count?:0
        }
    }

    private fun releaseViewPager() {
        mViewPager?.let {
            it.removeOnPageChangeListener(this)
            mViewPager = null
        }
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        mSelectPosition = position
        invalidate()
    }

    interface OnIndicatorClickListener {
        fun onSelected(position: Int)
    }

    class Indicator {
        var cx: Float = 0f
        var cy: Float = 0f
        var position = 0
    }

    enum class FillMode {
        LETTER, NUMBER, NONE
    }
}