package com.colin.library.android.widget.banner

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.colin.library.android.widget.R

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-13 15:52
 *
 * Des   :Banner
 */

class BannerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), LifecycleEventObserver {
    override fun onStateChanged(
        source: LifecycleOwner,
        event: Lifecycle.Event
    ) {

    }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_banner, null, false)
    }

//
//    private var mViewPager2: ViewPager2? = null
//    private var mLoopTask: com.comm.banner.Banner.AutoLoopTask? = null
//    private var mOnPageChangeListener: OnPageChangeListener? = null
//    private var mAdapter: BA? = null
//    private var mIndicator: Indicator? = null
//    private var mCompositePageTransformer: CompositePageTransformer? = null
//    private var mPageChangeCallback: com.comm.banner.Banner.BannerOnPageChangeCallback? = null
//
//    // 是否允许无限轮播（即首尾直接切换）
//    var isInfiniteLoop: Boolean = BannerConfig.IS_INFINITE_LOOP
//        private set
//
//    // 是否自动轮播
//    private var mIsAutoLoop: Boolean = BannerConfig.IS_AUTO_LOOP
//
//    // 轮播切换间隔时间
//    private var mLoopTime: Long = BannerConfig.LOOP_TIME
//
//    // 轮播切换时间
//    var scrollTime: Int = BannerConfig.SCROLL_TIME
//        private set
//
//    // 轮播开始位置
//    private var mStartPosition = 1
//
//    // banner圆角半径
//    private var mBannerRadius = 0f
//
//    // 指示器相关配置
//    private var normalWidth: Int = BannerConfig.INSTANCE.getINDICATOR_NORMAL_WIDTH()
//    private var selectedWidth: Int = BannerConfig.INSTANCE.getINDICATOR_SELECTED_WIDTH()
//    private var normalColor: Int = BannerConfig.INDICATOR_NORMAL_COLOR
//    private var selectedColor: Int = BannerConfig.INDICATOR_SELECTED_COLOR
//    private var indicatorGravity: Int = Direction.CENTER
//    private var indicatorSpace = 0
//    private var indicatorMargin = 0
//    private var indicatorMarginLeft = 0
//    private var indicatorMarginTop = 0
//    private var indicatorMarginRight = 0
//    private var indicatorMarginBottom = 0
//    private var indicatorHeight: Int = BannerConfig.INSTANCE.getINDICATOR_HEIGHT()
//    private var indicatorRadius: Int = BannerConfig.INSTANCE.getINDICATOR_RADIUS()
//
//    // 滑动距离范围
//    private var mTouchSlop = 0
//
//    // 记录触摸的位置（主要用于解决事件冲突问题）
//    private var mStartX = 0f
//    private var mStartY = 0f
//
//    // 记录viewpager2是否被拖动
//    private var mIsViewPager2Drag = false
//
//    // 是否要拦截事件
//    private var isIntercept = true
//
//    //绘制圆角视图
//    private var mRoundPaint: Paint? = null
//    private var mImagePaint: Paint? = null
//
//    private fun init(context: Context) {
//        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop() / 2
//        mCompositePageTransformer = CompositePageTransformer()
//        mPageChangeCallback = com.comm.banner.Banner.BannerOnPageChangeCallback()
//        mLoopTask = com.comm.banner.Banner.AutoLoopTask(this)
//        mViewPager2 = ViewPager2(context)
//        mViewPager2!!.setLayoutParams(
//            LayoutParams(
//                LayoutParams.MATCH_PARENT,
//                LayoutParams.MATCH_PARENT
//            )
//        )
//        mViewPager2!!.setOffscreenPageLimit(1)
//        mViewPager2!!.registerOnPageChangeCallback(mPageChangeCallback)
//        mViewPager2!!.setPageTransformer(mCompositePageTransformer)
//        ScrollSpeedManger.Companion.reflectLayoutManager(this)
//        addView(mViewPager2)
//
//        mRoundPaint = Paint()
//        mRoundPaint!!.setColor(Color.WHITE)
//        mRoundPaint!!.setAntiAlias(true)
//        mRoundPaint!!.setStyle(Paint.Style.FILL)
//        mRoundPaint!!.setXfermode(PorterDuffXfermode(PorterDuff.Mode.DST_OUT))
//        mImagePaint = Paint()
//        mImagePaint!!.setXfermode(null)
//    }
//
//    private fun initTypedArray(context: Context, attrs: AttributeSet?) {
//        if (attrs == null) {
//            return
//        }
//        val a = context.obtainStyledAttributes(attrs, R.styleable.Banner)
//        mBannerRadius = a.getDimensionPixelSize(R.styleable.Banner_banner_radius, 0).toFloat()
//        mLoopTime = a.getInt(R.styleable.Banner_banner_loop_time, BannerConfig.LOOP_TIME).toLong()
//        mIsAutoLoop = a.getBoolean(R.styleable.Banner_banner_auto_loop, BannerConfig.IS_AUTO_LOOP)
//        this.isInfiniteLoop =
//            a.getBoolean(R.styleable.Banner_banner_infinite_loop, BannerConfig.IS_INFINITE_LOOP)
//        normalWidth = a.getDimensionPixelSize(
//            R.styleable.Banner_banner_indicator_normal_width,
//            BannerConfig.INSTANCE.getINDICATOR_NORMAL_WIDTH()
//        )
//        selectedWidth = a.getDimensionPixelSize(
//            R.styleable.Banner_banner_indicator_selected_width,
//            BannerConfig.INSTANCE.getINDICATOR_SELECTED_WIDTH()
//        )
//        normalColor = a.getColor(
//            R.styleable.Banner_banner_indicator_normal_color,
//            BannerConfig.INDICATOR_NORMAL_COLOR
//        )
//        selectedColor = a.getColor(
//            R.styleable.Banner_banner_indicator_selected_color,
//            BannerConfig.INDICATOR_SELECTED_COLOR
//        )
//        indicatorGravity = a.getInt(R.styleable.Banner_banner_indicator_gravity, Direction.CENTER)
//        indicatorSpace = a.getDimensionPixelSize(R.styleable.Banner_banner_indicator_space, 0)
//        indicatorMargin = a.getDimensionPixelSize(R.styleable.Banner_banner_indicator_margin, 0)
//        indicatorMarginLeft =
//            a.getDimensionPixelSize(R.styleable.Banner_banner_indicator_marginLeft, 0)
//        indicatorMarginTop =
//            a.getDimensionPixelSize(R.styleable.Banner_banner_indicator_marginTop, 0)
//        indicatorMarginRight =
//            a.getDimensionPixelSize(R.styleable.Banner_banner_indicator_marginRight, 0)
//        indicatorMarginBottom =
//            a.getDimensionPixelSize(R.styleable.Banner_banner_indicator_marginBottom, 0)
//        indicatorHeight = a.getDimensionPixelSize(
//            R.styleable.Banner_banner_indicator_height,
//            BannerConfig.INSTANCE.getINDICATOR_HEIGHT()
//        )
//        indicatorRadius = a.getDimensionPixelSize(
//            R.styleable.Banner_banner_indicator_radius,
//            BannerConfig.INSTANCE.getINDICATOR_RADIUS()
//        )
//        val orientation = a.getInt(R.styleable.Banner_banner_orientation, Orientation.HORIZONTAL)
//        setOrientation(orientation)
//        setInfiniteLoop()
//        a.recycle()
//    }
//
//    private fun initIndicatorAttr() {
//        if (indicatorMargin != 0) {
//            setIndicatorMargins(Margins(indicatorMargin))
//        } else if (indicatorMarginLeft != 0 || indicatorMarginTop != 0 || indicatorMarginRight != 0 || indicatorMarginBottom != 0) {
//            val margins: IndicatorConfig.Margins = Margins(
//                indicatorMarginLeft, indicatorMarginTop,
//                indicatorMarginRight, indicatorMarginBottom
//            )
//            setIndicatorMargins(margins)
//        }
//        if (indicatorSpace > 0) {
//            setIndicatorSpace(indicatorSpace)
//        }
//        if (indicatorGravity != Direction.CENTER) {
//            setIndicatorGravity(indicatorGravity)
//        }
//        if (normalWidth > 0) {
//            setIndicatorNormalWidth(normalWidth)
//        }
//        if (selectedWidth > 0) {
//            setIndicatorSelectedWidth(selectedWidth)
//        }
//
//        if (indicatorHeight > 0) {
//            setIndicatorHeight(indicatorHeight)
//        }
//        if (indicatorRadius > 0) {
//            setIndicatorRadius(indicatorRadius)
//        }
//        setIndicatorNormalColor(normalColor)
//        setIndicatorSelectedColor(selectedColor)
//    }
//
//    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//        if (!this.viewPager2.isUserInputEnabled()) {
//            return super.dispatchTouchEvent(ev)
//        }
//
//        val action = ev.getActionMasked()
//        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_OUTSIDE) {
//            start()
//        } else if (action == MotionEvent.ACTION_DOWN) {
//            stop()
//        }
//        return super.dispatchTouchEvent(ev)
//    }
//
//    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
//        if (!this.viewPager2.isUserInputEnabled() || !isIntercept) {
//            return super.onInterceptTouchEvent(event)
//        }
//        when (event.getAction()) {
//            MotionEvent.ACTION_DOWN -> {
//                mStartX = event.getX()
//                mStartY = event.getY()
//                getParent().requestDisallowInterceptTouchEvent(true)
//            }
//
//            MotionEvent.ACTION_MOVE -> {
//                val endX = event.getX()
//                val endY = event.getY()
//                val distanceX = abs((endX - mStartX).toDouble()).toFloat()
//                val distanceY = abs((endY - mStartY).toDouble()).toFloat()
//                if (this.viewPager2.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
//                    mIsViewPager2Drag = distanceX > mTouchSlop && distanceX > distanceY
//                } else {
//                    mIsViewPager2Drag = distanceY > mTouchSlop && distanceY > distanceX
//                }
//                getParent().requestDisallowInterceptTouchEvent(mIsViewPager2Drag)
//            }
//
//            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> getParent().requestDisallowInterceptTouchEvent(
//                false
//            )
//        }
//        return super.onInterceptTouchEvent(event)
//    }
//
//    override fun dispatchDraw(canvas: Canvas) {
//        if (mBannerRadius > 0) {
//            canvas.saveLayer(
//                RectF(
//                    0f,
//                    0f,
//                    canvas.getWidth().toFloat(),
//                    canvas.getHeight().toFloat()
//                ), mImagePaint, Canvas.ALL_SAVE_FLAG
//            )
//            super.dispatchDraw(canvas)
//            //绘制外圆环边框圆环
//            drawTopLeft(canvas)
//            drawTopRight(canvas)
//            drawBottomLeft(canvas)
//            drawBottomRight(canvas)
//            canvas.restore()
//        } else {
//            super.dispatchDraw(canvas)
//        }
//    }
//
//    private fun drawTopLeft(canvas: Canvas) {
//        val path = Path()
//        path.moveTo(0f, mBannerRadius)
//        path.lineTo(0f, 0f)
//        path.lineTo(mBannerRadius, 0f)
//        path.arcTo(
//            RectF(0f, 0f, mBannerRadius * 2, mBannerRadius * 2),
//            -90f, -90f
//        )
//        path.close()
//        canvas.drawPath(path, mRoundPaint!!)
//    }
//
//    private fun drawTopRight(canvas: Canvas) {
//        val width = getWidth()
//        val path = Path()
//        path.moveTo(width - mBannerRadius, 0f)
//        path.lineTo(width.toFloat(), 0f)
//        path.lineTo(width.toFloat(), mBannerRadius)
//        path.arcTo(
//            RectF(
//                width - 2 * mBannerRadius, 0f, width.toFloat(),
//                mBannerRadius * 2
//            ), 0f, -90f
//        )
//        path.close()
//        canvas.drawPath(path, mRoundPaint!!)
//    }
//
//    private fun drawBottomLeft(canvas: Canvas) {
//        val height = getHeight()
//        val path = Path()
//        path.moveTo(0f, height - mBannerRadius)
//        path.lineTo(0f, height.toFloat())
//        path.lineTo(mBannerRadius, height.toFloat())
//        path.arcTo(
//            RectF(
//                0f, height - 2 * mBannerRadius,
//                mBannerRadius * 2, height.toFloat()
//            ), 90f, 90f
//        )
//        path.close()
//        canvas.drawPath(path, mRoundPaint!!)
//    }
//
//    private fun drawBottomRight(canvas: Canvas) {
//        val height = getHeight()
//        val width = getWidth()
//        val path = Path()
//        path.moveTo(width - mBannerRadius, height.toFloat())
//        path.lineTo(width.toFloat(), height.toFloat())
//        path.lineTo(width.toFloat(), height - mBannerRadius)
//        path.arcTo(
//            RectF(
//                width - 2 * mBannerRadius, height - 2
//                        * mBannerRadius, width.toFloat(), height.toFloat()
//            ), 0f, 90f
//        )
//        path.close()
//        canvas.drawPath(path, mRoundPaint!!)
//    }
//
//    override fun onAttachedToWindow() {
//        super.onAttachedToWindow()
//        start()
//    }
//
//    override fun onDetachedFromWindow() {
//        super.onDetachedFromWindow()
//        stop()
//    }
//
//    internal inner class BannerOnPageChangeCallback : ViewPager2.OnPageChangeCallback() {
//        private var mTempPosition = INVALID_VALUE
//        private var isScrolled = false
//
//        override fun onPageScrolled(
//            position: Int,
//            positionOffset: Float,
//            positionOffsetPixels: Int
//        ) {
//            val realPosition: Int = BannerUtils.INSTANCE.getRealPosition(
//                this.isInfiniteLoop, position,
//                this.realCount
//            )
//            if (mOnPageChangeListener != null) {
//                mOnPageChangeListener.onPageScrolled(
//                    realPosition,
//                    positionOffset,
//                    positionOffsetPixels
//                )
//            }
//            if (mIndicator != null) {
//                mIndicator.onPageScrolled(realPosition, positionOffset, positionOffsetPixels)
//            }
//        }
//
//        override fun onPageSelected(position: Int) {
//            if (isScrolled) {
//                mTempPosition = position
//                val realPosition: Int = BannerUtils.INSTANCE.getRealPosition(
//                    this.isInfiniteLoop, position,
//                    this.realCount
//                )
//                if (mOnPageChangeListener != null) {
//                    mOnPageChangeListener.onPageSelected(realPosition)
//                }
//                if (mIndicator != null) {
//                    mIndicator.onPageSelected(realPosition)
//                }
//            }
//        }
//
//        override fun onPageScrollStateChanged(state: Int) {
//            //手势滑动中,代码执行滑动中
//            if (state == ViewPager2.SCROLL_STATE_DRAGGING || state == ViewPager2.SCROLL_STATE_SETTLING) {
//                isScrolled = true
//            } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
//                //滑动闲置或滑动结束
//                isScrolled = false
//                if (mTempPosition != INVALID_VALUE && this.isInfiniteLoop) {
//                    if (mTempPosition == 0) {
//                        setCurrentItem(this.realCount, false)
//                    } else if (mTempPosition == this.itemCount - 1) {
//                        setCurrentItem(1, false)
//                    }
//                }
//            }
//            if (mOnPageChangeListener != null) {
//                mOnPageChangeListener.onPageScrollStateChanged(state)
//            }
//            if (mIndicator != null) {
//                mIndicator.onPageScrollStateChanged(state)
//            }
//        }
//    }
//
//    internal class AutoLoopTask(banner: Banner<*, *>?) : Runnable {
//        private val reference: WeakReference<Banner<*, *>?>
//
//        init {
//            this.reference = WeakReference<Banner<*, *>?>(banner)
//        }
//
//        override fun run() {
//            val banner = reference.get()
//            if (banner != null && banner.mIsAutoLoop) {
//                val count = banner.itemCount
//                if (count == 0) {
//                    return
//                }
//                val next = (banner.currentItem + 1) % count
//                banner.setCurrentItem(next)
//                banner.postDelayed(banner.mLoopTask, banner.mLoopTime)
//            }
//        }
//    }
//
//    private val mAdapterDataObserver: RecyclerView.AdapterDataObserver =
//        object : RecyclerView.AdapterDataObserver() {
//            override fun onChanged() {
//                if (this.itemCount <= 1) {
//                    stop()
//                } else {
//                    start()
//                }
//                setIndicatorPageChange()
//            }
//        }
//
//    init {
//        init(context)
//        initTypedArray(context, attrs)
//    }
//
//    private fun initIndicator() {
//        if (mIndicator == null || this.adapter == null) {
//            return
//        }
//        if (mIndicator.getIndicatorConfig().isAttachToBanner()) {
//            removeIndicator()
//            addView(mIndicator.getIndicatorView())
//        }
//        initIndicatorAttr()
//        setIndicatorPageChange()
//    }
//
//    private fun setInfiniteLoop() {
//        // 当不支持无限循环时，要关闭自动轮播
//        if (!this.isInfiniteLoop) {
//            isAutoLoop(false)
//        }
//        setStartPosition(if (this.isInfiniteLoop) 1 else 0)
//    }
//
//    private fun setRecyclerViewPadding(itemPadding: Int) {
//        setRecyclerViewPadding(itemPadding, itemPadding)
//    }
//
//    private fun setRecyclerViewPadding(leftItemPadding: Int, rightItemPadding: Int) {
//        val recyclerView = this.viewPager2.getChildAt(0) as RecyclerView
//        if (this.viewPager2.getOrientation() == ViewPager2.ORIENTATION_VERTICAL) {
//            recyclerView.setPadding(0, leftItemPadding, 0, rightItemPadding)
//        } else {
//            recyclerView.setPadding(leftItemPadding, 0, rightItemPadding, 0)
//        }
//        recyclerView.setClipToPadding(false)
//    }
//
//    val currentItem: Int
//        /**
//         * **********************************************************************
//         * ------------------------ 对外公开API ---------------------------------*
//         * **********************************************************************
//         */
//        get() = this.viewPager2.getCurrentItem()
//
//    val itemCount: Int
//        get() {
//            if (this.adapter == null) {
//                return 0
//            }
//            return this.adapter.getItemCount()
//        }
//
//    val adapter: BA?
//        get() {
//            if (mAdapter == null) {
//                LogUtils.INSTANCE.e(getContext().getString(R.string.banner_adapter_use_error))
//            }
//            return mAdapter
//        }
//
//    val viewPager2: ViewPager2
//        get() = mViewPager2
//
//    val indicator: Indicator?
//        get() {
//            if (mIndicator == null) {
//                LogUtils.INSTANCE.e(getContext().getString(R.string.indicator_null_error))
//            }
//            return mIndicator
//        }
//
//    val indicatorConfig: IndicatorConfig?
//        get() {
//            if (this.indicator != null) {
//                return this.indicator.getIndicatorConfig()
//            }
//            return null
//        }
//
//    val realCount: Int
//        /**
//         * 返回banner真实总数
//         */
//        get() = this.adapter.getRealCount()
//
//    //-----------------------------------------------------------------------------------------
//    /**
//     * 是否要拦截事件
//     *
//     * @param intercept
//     * @return
//     */
//    fun setIntercept(intercept: Boolean): Banner<*, *> {
//        isIntercept = intercept
//        return this
//    }
//
//    /**
//     * 跳转到指定位置（最好在设置了数据后在调用，不然没有意义）
//     *
//     * @param position
//     * @return
//     */
//    fun setCurrentItem(position: Int): Banner<*, *> {
//        return setCurrentItem(position, true)
//    }
//
//    /**
//     * 跳转到指定位置（最好在设置了数据后在调用，不然没有意义）
//     *
//     * @param position
//     * @param smoothScroll
//     * @return
//     */
//    fun setCurrentItem(position: Int, smoothScroll: Boolean): Banner<*, *> {
//        this.viewPager2.setCurrentItem(position, smoothScroll)
//        return this
//    }
//
//    fun setIndicatorPageChange(): Banner<*, *> {
//        if (mIndicator != null) {
//            val realPosition: Int = BannerUtils.INSTANCE.getRealPosition(
//                this.isInfiniteLoop,
//                this.currentItem,
//                this.realCount
//            )
//            mIndicator.onPageChanged(this.realCount, realPosition)
//        }
//        return this
//    }
//
//    fun removeIndicator(): Banner<*, *> {
//        if (mIndicator != null) {
//            removeView(mIndicator.getIndicatorView())
//        }
//        return this
//    }
//
//
//    /**
//     * 设置开始的位置 (需要在setAdapter或者setDatas之前调用才有效哦)
//     */
//    fun setStartPosition(mStartPosition: Int): Banner<*, *> {
//        this.mStartPosition = mStartPosition
//        return this
//    }
//
//    /**
//     * 禁止手动滑动
//     *
//     * @param enabled true 允许，false 禁止
//     */
//    fun setUserInputEnabled(enabled: Boolean): Banner<*, *> {
//        this.viewPager2.setUserInputEnabled(enabled)
//        return this
//    }
//
//    /**
//     * 添加PageTransformer，可以组合效果
//     * [ViewPager2.PageTransformer]
//     * 如果找不到请导入implementation "androidx.viewpager2:viewpager2:1.0.0"
//     */
//    fun addPageTransformer(transformer: ViewPager2.PageTransformer?): Banner<*, *> {
//        mCompositePageTransformer!!.addTransformer(transformer!!)
//        return this
//    }
//
//    /**
//     * 设置PageTransformer，和addPageTransformer不同，这个只支持一种transformer
//     */
//    fun setPageTransformer(transformer: ViewPager2.PageTransformer?): Banner<*, *> {
//        this.viewPager2.setPageTransformer(transformer)
//        return this
//    }
//
//    fun removeTransformer(transformer: ViewPager2.PageTransformer): Banner<*, *> {
//        mCompositePageTransformer!!.removeTransformer(transformer)
//        return this
//    }
//
//    /**
//     * 添加 ItemDecoration
//     */
//    fun addItemDecoration(decor: RecyclerView.ItemDecoration): Banner<*, *> {
//        this.viewPager2.addItemDecoration(decor)
//        return this
//    }
//
//    fun addItemDecoration(decor: RecyclerView.ItemDecoration, index: Int): Banner<*, *> {
//        this.viewPager2.addItemDecoration(decor, index)
//        return this
//    }
//
//    /**
//     * 是否允许自动轮播
//     *
//     * @param isAutoLoop ture 允许，false 不允许
//     */
//    fun isAutoLoop(isAutoLoop: Boolean): Banner<*, *> {
//        this.mIsAutoLoop = isAutoLoop
//        return this
//    }
//
//
//    /**
//     * 设置轮播间隔时间
//     *
//     * @param loopTime 时间（毫秒）
//     */
//    fun setLoopTime(loopTime: Long): Banner<*, *> {
//        this.mLoopTime = loopTime
//        return this
//    }
//
//    /**
//     * 设置轮播滑动过程的时间
//     */
//    fun setScrollTime(scrollTime: Int): Banner<*, *> {
//        this.scrollTime = scrollTime
//        return this
//    }
//
//    /**
//     * 开始轮播
//     */
//    fun start(): Banner<*, *> {
//        if (mIsAutoLoop) {
//            stop()
//            postDelayed(mLoopTask, mLoopTime)
//        }
//        return this
//    }
//
//    /**
//     * 停止轮播
//     */
//    fun stop(): Banner<*, *> {
//        if (mIsAutoLoop) {
//            removeCallbacks(mLoopTask)
//        }
//        return this
//    }
//
//    /**
//     * 移除一些引用
//     */
//    fun destroy() {
//        if (this.viewPager2 != null && mPageChangeCallback != null) {
//            this.viewPager2.unregisterOnPageChangeCallback(mPageChangeCallback)
//            mPageChangeCallback = null
//        }
//        stop()
//    }
//
//    /**
//     * 设置banner的适配器
//     */
//    fun setAdapter(adapter: BA?): Banner<*, *> {
//        if (adapter == null) {
//            throw NullPointerException(getContext().getString(R.string.banner_adapter_null_error))
//        }
//        this.mAdapter = adapter
//        if (!this.isInfiniteLoop) {
//            mAdapter.setIncreaseCount(0)
//        }
//        mAdapter.registerAdapterDataObserver(mAdapterDataObserver)
//        mViewPager2!!.setAdapter(adapter)
//        setCurrentItem(mStartPosition, false)
//        initIndicator()
//        return this
//    }
//
//    /**
//     * 设置banner的适配器
//     *
//     * @param adapter
//     * @param isInfiniteLoop 是否支持无限循环
//     * @return
//     */
//    fun setAdapter(adapter: BA?, isInfiniteLoop: Boolean): Banner<*, *> {
//        this.isInfiniteLoop = isInfiniteLoop
//        setInfiniteLoop()
//        setAdapter(adapter)
//        return this
//    }
//
//
//    /**
//     * 重新设置banner数据，当然你也可以在你adapter中自己操作数据,不要过于局限在这个方法，举一反三哈
//     *
//     * @param datas 数据集合，当传null或者datas没有数据时，banner会变成空白的，请做好占位UI处理
//     */
//    fun setDatas(datas: MutableList<T?>?): Banner<*, *> {
//        if (this.adapter != null) {
//            this.adapter.setDatas(datas)
//            this.adapter.notifyDataSetChanged()
//            setCurrentItem(mStartPosition, false)
//            setIndicatorPageChange()
//            start()
//        }
//        return this
//    }
//
//    /**
//     * 设置banner轮播方向
//     *
//     * @param orientation [Orientation]
//     */
//    fun setOrientation(@Orientation orientation: Int): Banner<*, *> {
//        this.viewPager2.setOrientation(orientation)
//        return this
//    }
//
//    /**
//     * 改变最小滑动距离
//     */
//    fun setTouchSlop(mTouchSlop: Int): Banner<*, *> {
//        this.mTouchSlop = mTouchSlop
//        return this
//    }
//
//    /**
//     * 设置点击事件
//     */
//    fun setOnBannerListener(listener: OnBannerListener<T?>?): Banner<*, *> {
//        if (this.adapter != null) {
//            this.adapter.setOnBannerListener(listener)
//        }
//        return this
//    }
//
//    /**
//     * 添加viewpager切换事件
//     *
//     *
//     * 在viewpager2中切换事件[ViewPager2.OnPageChangeCallback]是一个抽象类，
//     * 为了方便使用习惯这里用的是和viewpager一样的[ViewPager.OnPageChangeListener]接口
//     *
//     */
//    fun addOnPageChangeListener(pageListener: OnPageChangeListener?): Banner<*, *> {
//        this.mOnPageChangeListener = pageListener
//        return this
//    }
//
//    /**
//     * 设置banner圆角
//     *
//     *
//     * 默认没有圆角，需要取消圆角把半径设置为0即可
//     *
//     * @param radius 圆角半径
//     */
//    fun setBannerRound(radius: Float): Banner<*, *> {
//        mBannerRadius = radius
//        return this
//    }
//
//    /**
//     * 设置banner圆角(第二种方式，和上面的方法不要同时使用)，只支持5.0以上
//     */
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    fun setBannerRound2(radius: Float): Banner<*, *> {
//        BannerUtils.INSTANCE.setBannerRound(this, radius)
//        return this
//    }
//
//    /**
//     * 为banner添加画廊效果
//     *
//     * @param itemWidth  item左右展示的宽度,单位dp
//     * @param pageMargin 页面间距,单位dp
//     */
//    fun setBannerGalleryEffect(itemWidth: Int, pageMargin: Int): Banner<*, *> {
//        return setBannerGalleryEffect(itemWidth, pageMargin, .85f)
//    }
//
//    /**
//     * 为banner添加画廊效果
//     *
//     * @param leftItemWidth  item左展示的宽度,单位dp
//     * @param rightItemWidth item右展示的宽度,单位dp
//     * @param pageMargin     页面间距,单位dp
//     */
//    fun setBannerGalleryEffect(
//        leftItemWidth: Int,
//        rightItemWidth: Int,
//        pageMargin: Int
//    ): Banner<*, *> {
//        return setBannerGalleryEffect(leftItemWidth, rightItemWidth, pageMargin, .85f)
//    }
//
//    /**
//     * 为banner添加画廊效果
//     *
//     * @param itemWidth  item左右展示的宽度,单位dp
//     * @param pageMargin 页面间距,单位dp
//     * @param scale      缩放[0-1],1代表不缩放
//     */
//    fun setBannerGalleryEffect(itemWidth: Int, pageMargin: Int, scale: Float): Banner<*, *> {
//        return setBannerGalleryEffect(itemWidth, itemWidth, pageMargin, scale)
//    }
//
//    /**
//     * 为banner添加画廊效果
//     *
//     * @param leftItemWidth  item左展示的宽度,单位dp
//     * @param rightItemWidth item右展示的宽度,单位dp
//     * @param pageMargin     页面间距,单位dp
//     * @param scale          缩放[0-1],1代表不缩放
//     */
//    fun setBannerGalleryEffect(
//        leftItemWidth: Int,
//        rightItemWidth: Int,
//        pageMargin: Int,
//        scale: Float
//    ): Banner<*, *> {
//        if (pageMargin > 0) {
//            addPageTransformer(MarginPageTransformer(BannerUtils.INSTANCE.dp2px(pageMargin) as Int))
//        }
//        if (scale < 1 && scale > 0) {
//            addPageTransformer(ScaleInTransformer(scale))
//        }
//        setRecyclerViewPadding(
//            if (leftItemWidth > 0) BannerUtils.INSTANCE.dp2px(leftItemWidth + pageMargin) as Int else 0,
//            if (rightItemWidth > 0) BannerUtils.INSTANCE.dp2px(rightItemWidth + pageMargin) as Int else 0
//        )
//        return this
//    }
//
//    /**
//     * 为banner添加魅族效果
//     *
//     * @param itemWidth item左右展示的宽度,单位dp
//     */
//    fun setBannerGalleryMZ(itemWidth: Int): Banner<*, *> {
//        return setBannerGalleryMZ(itemWidth, .88f)
//    }
//
//    /**
//     * 为banner添加魅族效果
//     *
//     * @param itemWidth item左右展示的宽度,单位dp
//     * @param scale     缩放[0-1],1代表不缩放
//     */
//    fun setBannerGalleryMZ(itemWidth: Int, scale: Float): Banner<*, *> {
//        if (scale < 1 && scale > 0) {
//            addPageTransformer(MZScaleInTransformer(scale))
//        }
//        setRecyclerViewPadding(BannerUtils.INSTANCE.dp2px(itemWidth) as Int)
//        return this
//    }
//
//    /**
//     * **********************************************************************
//     * ------------------------ 指示器相关设置 --------------------------------*
//     * **********************************************************************
//     */
//    /**
//     * 设置轮播指示器(显示在banner上)
//     */
//    fun setIndicator(indicator: Indicator): Banner<*, *> {
//        return setIndicator(indicator, true)
//    }
//
//    /**
//     * 设置轮播指示器(如果你的指示器写在布局文件中，attachToBanner传false)
//     *
//     * @param attachToBanner 是否将指示器添加到banner中，false 代表你可以将指示器通过布局放在任何位置
//     * 注意：设置为false后，内置的 setIndicatorGravity()和setIndicatorMargins() 方法将失效。
//     * 想改变可以自己调用系统提供的属性在布局文件中进行设置。具体可以参照demo
//     */
//    fun setIndicator(indicator: Indicator, attachToBanner: Boolean): Banner<*, *> {
//        removeIndicator()
//        indicator.getIndicatorConfig().setAttachToBanner(attachToBanner)
//        this.mIndicator = indicator
//        initIndicator()
//        return this
//    }
//
//
//    fun setIndicatorSelectedColor(@ColorInt color: Int): Banner<*, *> {
//        if (mIndicator != null) {
//            mIndicator.getIndicatorConfig().setSelectedColor(color)
//        }
//        return this
//    }
//
//    fun setIndicatorSelectedColorRes(@ColorRes color: Int): Banner<*, *> {
//        setIndicatorSelectedColor(ContextCompat.getColor(getContext(), color))
//        return this
//    }
//
//    fun setIndicatorNormalColor(@ColorInt color: Int): Banner<*, *> {
//        if (mIndicator != null) {
//            mIndicator.getIndicatorConfig().setNormalColor(color)
//        }
//        return this
//    }
//
//    fun setIndicatorNormalColorRes(@ColorRes color: Int): Banner<*, *> {
//        setIndicatorNormalColor(ContextCompat.getColor(getContext(), color))
//        return this
//    }
//
//    fun setIndicatorGravity(@Direction gravity: Int): Banner<*, *> {
//        if (mIndicator != null && mIndicator.getIndicatorConfig().isAttachToBanner()) {
//            mIndicator.getIndicatorConfig().setGravity(gravity)
//            mIndicator.getIndicatorView().postInvalidate()
//        }
//        return this
//    }
//
//    fun setIndicatorSpace(indicatorSpace: Int): Banner<*, *> {
//        if (mIndicator != null) {
//            mIndicator.getIndicatorConfig().setIndicatorSpace(indicatorSpace)
//        }
//        return this
//    }
//
//    fun setIndicatorMargins(margins: IndicatorConfig.Margins?): Banner<*, *> {
//        if (mIndicator != null && mIndicator.getIndicatorConfig().isAttachToBanner()) {
//            mIndicator.getIndicatorConfig().setMargins(margins)
//            mIndicator.getIndicatorView().requestLayout()
//        }
//        return this
//    }
//
//    fun setIndicatorWidth(normalWidth: Int, selectedWidth: Int): Banner<*, *> {
//        if (mIndicator != null) {
//            mIndicator.getIndicatorConfig().setNormalWidth(normalWidth)
//            mIndicator.getIndicatorConfig().setSelectedWidth(selectedWidth)
//        }
//        return this
//    }
//
//    fun setIndicatorNormalWidth(normalWidth: Int): Banner<*, *> {
//        if (mIndicator != null) {
//            mIndicator.getIndicatorConfig().setNormalWidth(normalWidth)
//        }
//        return this
//    }
//
//    fun setIndicatorSelectedWidth(selectedWidth: Int): Banner<*, *> {
//        if (mIndicator != null) {
//            mIndicator.getIndicatorConfig().setSelectedWidth(selectedWidth)
//        }
//        return this
//    }
//
//    fun setIndicatorRadius(indicatorRadius: Int): Banner<T?, BA?> {
//        if (mIndicator != null) {
//            mIndicator.getIndicatorConfig().setRadius(indicatorRadius)
//        }
//        return this
//    }
//
//    fun setIndicatorHeight(indicatorHeight: Int): Banner<T?, BA?> {
//        if (mIndicator != null) {
//            mIndicator.getIndicatorConfig().setHeight(indicatorHeight)
//        }
//        return this
//    }
//
//    /**
//     * **********************************************************************
//     * ------------------------ 生命周期控制 --------------------------------*
//     * **********************************************************************
//     */
//    fun addBannerLifecycleObserver(owner: LifecycleOwner?): Banner<*, *> {
//        if (owner != null) {
//            owner.lifecycle.addObserver(BannerLifecycleObserverAdapter(owner, this))
//        }
//        return this
//    }
//
//    public override fun onStart(owner: LifecycleOwner?) {
//        start()
//    }
//
//    public override fun onStop(owner: LifecycleOwner?) {
//        stop()
//    }
//
//    public override fun onDestroy(owner: LifecycleOwner?) {
//        destroy()
//    }
//
//    companion object {
//        val INVALID_VALUE: Int = -1
//    }
}