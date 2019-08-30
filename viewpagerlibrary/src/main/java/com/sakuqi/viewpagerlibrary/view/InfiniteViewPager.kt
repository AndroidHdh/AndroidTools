package com.sakuqi.viewpagerlibrary.view

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.DrawableRes
import androidx.viewpager.widget.ViewPager
import com.sakuqi.viewpagerlibrary.transformer.CustomTransformer
import com.sakuqi.viewpagerlibrary.R
import com.sakuqi.viewpagerlibrary.ViewPagerScroller
import com.sakuqi.viewpagerlibrary.adapter.InfiniteViewPagerAdapter
import com.sakuqi.viewpagerlibrary.holder.ViewPagerHolder
import com.sakuqi.viewpagerlibrary.holder.ViewPagerHolderCreator
import com.sakuqi.viewpagerlibrary.transformer.CoverModeTransformer
import com.sakuqi.viewpagerlibrary.utils.DisplayUtils
import java.lang.IllegalStateException
import java.lang.reflect.Field




/**
 * 无限轮播ViewPager
 */
class InfiniteViewPager<T>(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    RelativeLayout(context, attrs, defStyleAttr) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    var mOnPageChangeListener: ViewPager.OnPageChangeListener? = null
    var mPageClickListener: ((View, Int) -> Unit)? = null
    var mDelayedTime = 3000L
    var mViewPager: ViewPager? = null
        private set
    var mIndicatorContainer: LinearLayout? = null
        private set
    var mIsCanLoop = true
        set(value) {
            field = value
            if (!value) {
                pause()
            }
        }
    private var mAdapter: InfiniteViewPagerAdapter<T>? = null
    private var mDatas: MutableList<T> = mutableListOf()
    private var mIsAutoPlay = true
    var mIsNormal = true
    private var mCurrentItem = 0
    private var mHandler = Handler()
    private var mViewPagerScroller: ViewPagerScroller? = null

    private var mIndicators = arrayListOf<ImageView>()
    private var mIndicatorRes = intArrayOf(R.drawable.indicator_normal, R.drawable.indicator_selected)
    private var mIndicatorPaddingLeft = 0
    private var mIndicatorPaddingRight = 0
    private var mIndicatorPaddingTop = 0
    private var mIndicatorPaddingBottom = 0
    private var mChildPadding = 0
    private var mIndicatorAlign = IndicatorAlign.CENTER.ordinal
    private var mIsMiddlePageCover = true

    enum class IndicatorAlign {
        LEFT, CENTER, RIGHT
    }

    init {
        getAttes(context, attrs)
        val view = if (mIsNormal) {
            LayoutInflater.from(context).inflate(R.layout.viewpager_normal_layout, this, true)
        } else {
            LayoutInflater.from(context).inflate(R.layout.viewpager_effect_layout, this, true)
        }
        mIndicatorContainer = view.findViewById(R.id.banner_indicator_container)
        mViewPager = view.findViewById(R.id.infiniteViewPager)
        mViewPager?.offscreenPageLimit = 4
        mChildPadding = DisplayUtils.dpToPx(30f)
        initViewPagerScroll()
        sureIndicatorPosition()

    }

    private fun getAttes(context: Context, attrs: AttributeSet?) {
        val typedValue = context.obtainStyledAttributes(attrs, R.styleable.InfiniteViewPager)
        with(typedValue) {
            mIsMiddlePageCover = getBoolean(R.styleable.InfiniteViewPager_middle_page_cover, true)
            mIsCanLoop = getBoolean(R.styleable.InfiniteViewPager_canLoop, true)
            mIndicatorAlign = getInt(R.styleable.InfiniteViewPager_indicatorAlign, IndicatorAlign.CENTER.ordinal)
            mIndicatorPaddingLeft = getDimensionPixelSize(R.styleable.InfiniteViewPager_indicatorPaddingLeft, 0)
            mIndicatorPaddingRight = getDimensionPixelSize(R.styleable.InfiniteViewPager_indicatorPaddingRight, 0)
            mIndicatorPaddingTop = getDimensionPixelSize(R.styleable.InfiniteViewPager_indicatorPaddingTop, 0)
            mIndicatorPaddingBottom = getDimensionPixelSize(R.styleable.InfiniteViewPager_indicatorPaddingBottom, 0)
            mIsNormal = getBoolean(R.styleable.InfiniteViewPager_normal, true)
            recycle()
        }
    }

    private fun setOpenEffectMode() {
        if (!mIsNormal) {
            if(mIsMiddlePageCover){
                mViewPager?.setPageTransformer(true, CoverModeTransformer(mViewPager!!))
            }else {
                mViewPager?.setPageTransformer(false, CustomTransformer())
            }
        }
    }

    private fun sureIndicatorPosition() {
        when (mIndicatorAlign) {
            IndicatorAlign.LEFT.ordinal -> setIndicatorAlign(IndicatorAlign.LEFT)
            IndicatorAlign.CENTER.ordinal -> setIndicatorAlign(IndicatorAlign.CENTER)
            else -> setIndicatorAlign(IndicatorAlign.RIGHT)
        }
    }

    private fun initViewPagerScroll() {
        try {
            var mScroller: Field = ViewPager::class.java.getDeclaredField("mScroller")
            mScroller.isAccessible = true
            mViewPagerScroller = ViewPagerScroller(context)
            mScroller.set(mViewPager, mViewPagerScroller)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    private val mLoopRunnable = object : Runnable {
        override fun run() {
            if (mIsAutoPlay) {
                mCurrentItem = mViewPager?.currentItem ?: 0
                mCurrentItem++
                if (mCurrentItem == mAdapter?.count!! - 1) {
                    mCurrentItem = 0
                    mViewPager?.setCurrentItem(mCurrentItem, false)
                    mHandler.postDelayed(this, mDelayedTime)
                } else {
                    mViewPager?.currentItem = mCurrentItem
                    mHandler.postDelayed(this, mDelayedTime)
                }
            } else {
                mHandler.postDelayed(this, mDelayedTime)
            }
        }
    }

    private fun initIndicator() {
        mIndicatorContainer?.removeAllViews()
        mIndicators.clear()
        for (i in 0 until mDatas.size) {
            val imageView = ImageView(context)
            if (mIndicatorAlign == IndicatorAlign.LEFT.ordinal) {
                if (i == 0) {
                    val paddingLeft = if (mIsNormal) mIndicatorPaddingLeft else mIndicatorPaddingLeft + mChildPadding
                    imageView.setPadding(paddingLeft + 6, 0, 6, 0)
                } else {
                    imageView.setPadding(6, 0, 6, 0)
                }
            } else if (mIndicatorAlign == IndicatorAlign.RIGHT.ordinal) {
                if (i == mDatas.size - 1) {
                    val paddingRight = if (mIsNormal) mIndicatorPaddingRight else mChildPadding + mIndicatorPaddingRight
                    imageView.setPadding(6, 0, 6 + paddingRight, 0)
                } else {
                    imageView.setPadding(6, 0, 6, 0)
                }
            } else {
                imageView.setPadding(6, 0, 6, 0)
            }
            if (i == (mCurrentItem % mDatas.size)) {
                imageView.setImageResource(mIndicatorRes[1])
            } else {
                imageView.setImageResource(mIndicatorRes[0])
            }
            mIndicators.add(imageView)
            mIndicatorContainer?.addView(imageView)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (!mIsCanLoop) return super.dispatchTouchEvent(ev)
        when (ev?.action) {
            MotionEvent.ACTION_MOVE,
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_OUTSIDE,
            MotionEvent.ACTION_DOWN -> {
                val paddingLeft = mViewPager?.left!!
                val touchX = ev.rawX
                if (touchX >= paddingLeft && touchX < getScreenWidth(context) - paddingLeft) {
                    pause()
                }
            }
            MotionEvent.ACTION_UP -> {
                start()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun getScreenWidth(context: Context): Int {
        val resources = context.resources
        val dm = resources.displayMetrics
        return dm.widthPixels
    }

    fun start() {
        mAdapter?.let {
            if (mIsCanLoop) {
                pause()
                mIsAutoPlay = true
                mHandler.postDelayed(mLoopRunnable, mDelayedTime)
            }
        }
    }

    fun pause() {
        mIsAutoPlay = false
        mHandler.removeCallbacks(mLoopRunnable)
    }

    fun setIndicatorVisible(visible: Boolean) {
        if (visible) {
            mIndicatorContainer?.visibility = View.VISIBLE
        } else {
            mIndicatorContainer?.visibility = View.GONE
        }
    }

    fun setIndicatorPadding(paddingLeft: Int, paddingTop: Int, paddingRight: Int, paddingBottom: Int) {
        mIndicatorPaddingLeft = paddingLeft
        mIndicatorPaddingTop = paddingTop
        mIndicatorPaddingRight = paddingRight
        mIndicatorPaddingBottom = paddingBottom
    }

    fun setIndicatorRes(@DrawableRes unSelectRes: Int, @DrawableRes selectRes: Int) {
        mIndicatorRes[0] = unSelectRes
        mIndicatorRes[1] = selectRes
    }

    fun setPage(datas: MutableList<T>, viewPagerHolderCreator: ViewPagerHolderCreator<ViewPagerHolder<T>>) {
        mDatas = datas
        pause()
        if (datas.size < 3) {
            mIsNormal = true
            val layoutParams = mViewPager?.layoutParams as MarginLayoutParams
            layoutParams.setMargins(0, 0, 0, 0)
            mViewPager?.layoutParams = layoutParams
            clipChildren = true
            mViewPager?.clipChildren = true
        }
        setOpenEffectMode()
        initIndicator()
        mAdapter = InfiniteViewPagerAdapter(datas, viewPagerHolderCreator, mIsCanLoop)
        mAdapter?.setUpViewPager(mViewPager!!)
        mAdapter?.mPageClickListener = mPageClickListener

        mViewPager?.clearOnPageChangeListeners()
        mViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                when (state) {
                    ViewPager.SCROLL_STATE_DRAGGING -> {
                        mIsAutoPlay = false
                    }
                    ViewPager.SCROLL_STATE_SETTLING -> {
                        mIsAutoPlay = true
                    }
                }
                mOnPageChangeListener?.onPageScrollStateChanged(state)
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                val realPosition = position % mIndicators.size
                mOnPageChangeListener?.onPageScrolled(realPosition, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                mCurrentItem = position
                val realSelectPosition = mCurrentItem % mIndicators.size
                for (i in 0 until mDatas.size) {
                    if (i == realSelectPosition) {
                        mIndicators[i].setImageResource(mIndicatorRes[1])
                    } else {
                        mIndicators[i].setImageResource(mIndicatorRes[0])
                    }
                }

                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener?.onPageSelected(realSelectPosition)
                }
            }
        })
    }

    fun setIndicatorAlign(indicatorAlign: IndicatorAlign) {
        mIndicatorAlign = indicatorAlign.ordinal
        val layoutParams = mIndicatorContainer?.layoutParams as LayoutParams
        when (indicatorAlign) {
            IndicatorAlign.LEFT -> layoutParams.addRule(ALIGN_PARENT_LEFT)
            IndicatorAlign.RIGHT -> layoutParams.addRule(ALIGN_PARENT_RIGHT)
            else -> layoutParams.addRule(CENTER_HORIZONTAL)
        }
        layoutParams.setMargins(0,mIndicatorPaddingTop,0,mIndicatorPaddingBottom)
        mIndicatorContainer?.layoutParams = layoutParams
    }

    fun setDuration(duration:Int){
        mViewPagerScroller?.duration = duration
    }

    /**
     * 设置是否使用ViewPager默认是的切换速度
     * @param useDefaultDuration 切换动画时间
     */
    fun setUseDefaultDuration(useDefaultDuration: Boolean) {
        mViewPagerScroller?.isUseDefaultDuration = useDefaultDuration
    }

    /**
     * 获取Banner页面切换动画时间
     * @return
     */
    fun getDuration(): Int {
        return mViewPagerScroller?.scrollDuration?:0
    }

}