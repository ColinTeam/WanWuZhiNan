package com.colin.library.android.widget.banner

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.os.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import androidx.core.view.get
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.colin.library.android.utils.ext.dp
import com.colin.library.android.widget.banner.base.BaseBannerAdapter
import com.colin.library.android.widget.banner.base.BaseBannerAdapter.Companion.MAX_VALUE
import com.colin.library.android.widget.banner.def.IndicatorGravity
import com.colin.library.android.widget.banner.def.IndicatorSlideMode
import com.colin.library.android.widget.banner.def.IndicatorStyle
import com.colin.library.android.widget.banner.def.PageStyle
import com.colin.library.android.widget.banner.indicator.IIndicator
import com.colin.library.android.widget.banner.indicator.IndicatorView
import com.colin.library.android.widget.banner.manager.BannerManager
import com.colin.library.android.widget.banner.manager.ReflectLayoutManager
import com.colin.library.android.widget.banner.options.BannerOptions
import com.colin.library.android.widget.banner.options.BannerOptions.Companion.DEFAULT_REVEAL_WIDTH
import com.colin.library.android.widget.banner.options.IndicatorOptions
import com.colin.library.android.widget.banner.transform.ScaleInTransformer.Companion.DEFAULT_MIN_SCALE
import com.colin.library.android.widget.banner.utils.BannerUtils
import com.colin.library.android.widget.banner.utils.BannerUtils.getOriginalPosition
import com.colin.library.android.widget.setClipViewCornerRadius
import kotlin.math.abs

/**
 * BannerViewPager
 */
open class BannerViewPager<ITEM> @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RelativeLayout(context, attrs), DefaultLifecycleObserver {
    private val bannerManager: BannerManager = BannerManager()

    private var currentPosition = 0

    private var isCustomIndicator = false

    private var isLooping = false

    private var mOnPageClickListener: OnPageClickListener? = null

    private var mIndicatorView: IIndicator? = null

    private var mIndicatorLayout: RelativeLayout? = null

    private var mViewPager: ViewPager2? = null


    private val mHandler = Handler(Looper.getMainLooper())

    private var mBannerPagerAdapter: BaseBannerAdapter<ITEM>? = null

    private var onPageChangeCallback: OnPageChangeCallback? = null

    private val mRunnable = Runnable { handlePosition() }

    private var mRadiusRectF: RectF? = null
    private var mRadiusPath: Path? = null

    private var startX = 0
    private var startY = 0

    private val mOnPageChangeCallback: OnPageChangeCallback = object : OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int, positionOffset: Float, positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            pageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            //轮播时判断当前控件是否已被隐藏,并且是在当前界面部分可见
            if (getGlobalVisibleRect(Rect()) && windowVisibility == VISIBLE) {
                pageSelected(position)
            }
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            pageScrollStateChanged(state)
        }
    }


    init {
        bannerManager.initAttrs(context, attrs)
        initView()
    }

    private fun initView() {
        mViewPager = ViewPager2(context).apply {
            getChildAt(0).apply {
                clipToPadding = parentClipToPadding()
                clipChildren = parentClipToPadding()
            }
        }
        addView(
            mViewPager, ViewGroup.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
            )
        )
        mIndicatorLayout = RelativeLayout(context)
        addView(
            mIndicatorLayout, LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT
            ).apply {
                addRule(ALIGN_PARENT_BOTTOM)
            })
        mViewPager?.setPageTransformer(bannerManager.getCompositePageTransformer())
        clipChildren = parentClipToPadding()
        clipToPadding = parentClipToPadding()
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                isLooping = true
                stopPlay()
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_OUTSIDE -> {
                isLooping = false
                startPlay()
            }

            else -> {
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val doNotNeedIntercept =
            ((mViewPager?.isUserInputEnabled != true || (mBannerPagerAdapter != null && (mBannerPagerAdapter?.getData()?.size
                ?: 0) <= 1)))
        if (doNotNeedIntercept) {
            return super.onInterceptTouchEvent(ev)
        }
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = ev.x.toInt()
                startY = ev.y.toInt()
                parent.requestDisallowInterceptTouchEvent(
                    !bannerManager.getBannerOptions().isDisallowParentInterceptDownEvent()
                )
            }

            MotionEvent.ACTION_MOVE -> {
                val endX = ev.x.toInt()
                val endY = ev.y.toInt()
                val disX = abs(endX - startX)
                val disY: Int = abs(endY - startY)
                val orientation: Int = bannerManager.getBannerOptions().getOrientation()
                if (orientation == ViewPager2.ORIENTATION_VERTICAL) {
                    onVerticalActionMove(endY, disX, disY)
                } else if (orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                    onHorizontalActionMove(endX, disX, disY)
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> parent.requestDisallowInterceptTouchEvent(
                false
            )

            MotionEvent.ACTION_OUTSIDE -> {
            }

            else -> {
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    private fun onVerticalActionMove(endY: Int, disX: Int, disY: Int) {
        if (disY > disX) {
            val canLoop: Boolean = bannerManager.getBannerOptions().isCyclic()
            if (!canLoop) {
                if (currentPosition == 0 && endY - startY > 0) {
                    parent.requestDisallowInterceptTouchEvent(false)
                } else {
                    parent.requestDisallowInterceptTouchEvent(
                        currentPosition != getData().size - 1 || endY - startY >= 0
                    )
                }
            } else {
                parent.requestDisallowInterceptTouchEvent(true)
            }
        } else if (disX > disY) {
            parent.requestDisallowInterceptTouchEvent(false)
        }
    }

    private fun onHorizontalActionMove(endX: Int, disX: Int, disY: Int) {
        if (disX > disY) {
            val canLoop: Boolean = bannerManager.getBannerOptions().isCyclic()
            if (!canLoop) {
                if (currentPosition == 0 && endX - startX > 0) {
                    parent.requestDisallowInterceptTouchEvent(false)
                } else {
                    parent.requestDisallowInterceptTouchEvent(
                        (currentPosition != getData().size - 1 || endX - startX >= 0)
                    )
                }
            } else {
                parent.requestDisallowInterceptTouchEvent(true)
            }
        } else if (disY > disX) {
            parent.requestDisallowInterceptTouchEvent(false)
        }
    }

    private fun pageScrollStateChanged(state: Int) {
        mIndicatorView?.onPageScrollStateChanged(state)
        onPageChangeCallback?.onPageScrollStateChanged(state)
    }

    private fun pageSelected(position: Int) {
        val size: Int = mBannerPagerAdapter?.getListSize() ?: 0
        val canLoop: Boolean = bannerManager.getBannerOptions().isCyclic()
        currentPosition = BannerUtils.getRealPosition(position, size)
        val needResetCurrentItem =
            (size > 0) && canLoop && (position == 0 || position == MAX_VALUE - 1)
        if (needResetCurrentItem) {
            resetCurrentItem(currentPosition)
        }
        onPageChangeCallback?.onPageSelected(currentPosition)
        mIndicatorView?.onPageSelected(currentPosition)
    }

    private fun pageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        val listSize: Int = mBannerPagerAdapter?.getListSize() ?: 0
        val realPosition: Int = BannerUtils.getRealPosition(position, listSize)
        if (listSize > 0) {
            onPageChangeCallback?.onPageScrolled(
                realPosition, positionOffset, positionOffsetPixels
            )
            mIndicatorView?.onPageScrolled(realPosition, positionOffset, positionOffsetPixels)
        }
    }

    private fun handlePosition() {
        if ((mBannerPagerAdapter != null) && ((mBannerPagerAdapter?.getListSize()
                ?: 0) > 1) && isAutoPlay()
        ) {
            var currentItem = (mViewPager?.currentItem ?: 0) + 1
            if (currentItem >= (mViewPager?.adapter?.itemCount ?: MAX_VALUE)) {
                currentItem = 0
            }
            mViewPager?.currentItem = currentItem
            mHandler.postDelayed(mRunnable, getInterval())
        }
    }

    private fun initBannerData(isInitCurrent: Boolean = true) {
        val list: List<ITEM>? = mBannerPagerAdapter?.getData()
        if (list != null) {
            setIndicatorValues(list)
            setupViewPager(list, isInitCurrent)
            initRoundCorner()
        }
    }

    private fun setIndicatorValues(list: List<ITEM>) {
        val bannerOptions: BannerOptions = bannerManager.getBannerOptions()
        mIndicatorLayout?.visibility = bannerOptions.getIndicatorVisibility()
        bannerOptions.resetIndicatorOptions()
        if (!isCustomIndicator || null == mIndicatorView) {
            mIndicatorView = IndicatorView(context)
        }
        initIndicator(bannerOptions.getIndicatorOptions(), list)
    }

    private fun initIndicator(indicatorOptions: IndicatorOptions, list: List<ITEM>) {
        if ((mIndicatorView is View) && (mIndicatorView as View?)?.parent == null) {
            mIndicatorLayout?.removeAllViews()
            mIndicatorLayout?.addView(mIndicatorView as View?)
            initIndicatorSliderMargin()
            initIndicatorGravity()
        }
        mIndicatorView?.setIndicatorOptions(indicatorOptions)
        indicatorOptions.pageSize = list.size
        mIndicatorView?.notifyDataChanged()
    }

    private fun initIndicatorGravity() {
        val layoutParams = (mIndicatorView as View?)?.layoutParams as LayoutParams
        when (bannerManager.getBannerOptions().getIndicatorGravity()) {
            IndicatorGravity.CENTER -> layoutParams.addRule(
                CENTER_HORIZONTAL
            )

            IndicatorGravity.START -> layoutParams.addRule(ALIGN_PARENT_LEFT)
            IndicatorGravity.END -> layoutParams.addRule(ALIGN_PARENT_RIGHT)
            else -> {
            }
        }
    }

    private fun initIndicatorSliderMargin() {
        val layoutParams = (mIndicatorView as View?)?.layoutParams as MarginLayoutParams
        val indicatorMargin: BannerOptions.IndicatorMargin? =
            bannerManager.getBannerOptions().getIndicatorMargin()
        if (indicatorMargin == null) {
            val dp10: Int = 10.dp()
            layoutParams.setMargins(dp10, dp10, dp10, dp10)
        } else {
            layoutParams.setMargins(
                indicatorMargin.left,
                indicatorMargin.top,
                indicatorMargin.right,
                indicatorMargin.bottom
            )
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        val roundRectRadiusArray: FloatArray? =
            bannerManager.getBannerOptions().getRoundRectRadiusArray()
        if ((mRadiusRectF != null) && (mRadiusPath != null) && (roundRectRadiusArray != null)) {
            mRadiusRectF?.right = this.width.toFloat()
            mRadiusRectF?.bottom = this.height.toFloat()
            mRadiusPath?.addRoundRect(mRadiusRectF!!, roundRectRadiusArray, Path.Direction.CW)
            canvas.clipPath(mRadiusPath!!)
        }
        super.dispatchDraw(canvas)
    }

    private fun initRoundCorner() {
        val roundCorner: Int = bannerManager.getBannerOptions().getRoundRectRadius()
        if (roundCorner > 0) {
            setClipViewCornerRadius(roundCorner)
        }
    }

    private fun setupViewPager(list: List<ITEM>, isInitCurrent: Boolean = true) {
        if (mBannerPagerAdapter == null) {
            throw NullPointerException("You must set adapter for BannerViewPager")
        }
        val bannerOptions: BannerOptions = bannerManager.getBannerOptions()
        if (bannerOptions.getScrollDuration() != 0) {
            mViewPager?.let {
                ReflectLayoutManager.reflectLayoutManager(
                    it, bannerOptions.getScrollDuration()
                )
            }
        }
        if (isInitCurrent) {
            currentPosition = 0
        }
        mBannerPagerAdapter?.setCyclic(bannerOptions.isCyclic())
        mBannerPagerAdapter?.setPageClickListener(mOnPageClickListener)
        mViewPager?.adapter = mBannerPagerAdapter
        if (isCyclicSafely()) {
            mViewPager?.setCurrentItem(getOriginalPosition(list.size) + currentPosition, false)
        }
        mViewPager?.unregisterOnPageChangeCallback(mOnPageChangeCallback)
        mViewPager?.registerOnPageChangeCallback(mOnPageChangeCallback)
        mViewPager?.orientation = bannerOptions.getOrientation()
        val limit = bannerOptions.getOffScreenPageLimit()
        mViewPager?.offscreenPageLimit = if (limit > 0) limit else OFFSCREEN_PAGE_LIMIT_DEFAULT
        initRevealWidth(bannerOptions)
        initPageStyle(bannerOptions.getPageStyle())
        stopPlay()
        startPlay()
    }

    private fun initRevealWidth(bannerOptions: BannerOptions) {
        val rightRevealWidth: Int = bannerOptions.getRightRevealWidth()
        val leftRevealWidth: Int = bannerOptions.getLeftRevealWidth()
        if (leftRevealWidth != DEFAULT_REVEAL_WIDTH || rightRevealWidth != DEFAULT_REVEAL_WIDTH) {
            val recyclerView = mViewPager?.getChildAt(0)
            if (recyclerView is RecyclerView) {
                val orientation: Int = bannerOptions.getOrientation()
                val padding2: Int = bannerOptions.getPageMargin() + rightRevealWidth
                val padding1: Int = bannerOptions.getPageMargin() + leftRevealWidth
                if (orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                    recyclerView.setPadding(padding1, 0, padding2, 0)
                } else if (orientation == ViewPager2.ORIENTATION_VERTICAL) {
                    recyclerView.setPadding(0, padding1, 0, padding2)
                }
                recyclerView.clipToPadding = false
            }
        }
        bannerManager.createMarginTransformer()
    }

    fun refreshRevealWidth() {
        initRevealWidth(bannerManager.getBannerOptions())
    }

    private fun initPageStyle(@PageStyle pageStyle: Int) {
        val pageScale: Float = bannerManager.getBannerOptions().getPageScale()
        if (pageStyle == PageStyle.MULTI_PAGE_OVERLAP) {
            bannerManager.setMultiPageStyle(true, pageScale)
        } else if (pageStyle == PageStyle.MULTI_PAGE_SCALE) {
            bannerManager.setMultiPageStyle(false, pageScale)
        }
    }

    private fun resetCurrentItem(item: Int) {
        if (isCyclicSafely()) {
            mViewPager?.setCurrentItem(
                getOriginalPosition(mBannerPagerAdapter?.getListSize() ?: 0) + item, false
            )
        } else {
            mViewPager?.setCurrentItem(item, false)
        }
    }

    private fun refreshIndicator(data: List<ITEM>) {
        setIndicatorValues(data)
        bannerManager.getBannerOptions().getIndicatorOptions().currentPosition =
            BannerUtils.getRealPosition(mViewPager?.currentItem ?: 0, data.size)
        mIndicatorView?.notifyDataChanged()
    }

    private val KEY_SUPER_STATE = "SUPER_STATE"
    private val KEY_CURRENT_POSITION = "CURRENT_POSITION"
    private val KEY_IS_CUSTOM_INDICATOR = "IS_CUSTOM_INDICATOR"

    private fun getInterval(): Long {
        return bannerManager.getBannerOptions().getInterval()
    }

    private fun isAutoPlay(): Boolean {
        return bannerManager.getBannerOptions().isAutoPlay()
    }

    private fun isCyclicSafely(): Boolean {
        return ((bannerManager.getBannerOptions().isCyclic() && ((mBannerPagerAdapter?.getListSize()
            ?: 0) > 1)))
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val bundle = Bundle()
        bundle.putParcelable(KEY_SUPER_STATE, superState)
        bundle.putInt(KEY_CURRENT_POSITION, currentPosition)
        bundle.putBoolean(KEY_IS_CUSTOM_INDICATOR, isCustomIndicator)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val bundle = state as Bundle
        val superState = bundle.getParcelable<Parcelable>(KEY_SUPER_STATE)
        super.onRestoreInstanceState(superState)
        currentPosition = bundle.getInt(KEY_CURRENT_POSITION)
        isCustomIndicator = bundle.getBoolean(KEY_IS_CUSTOM_INDICATOR)
        setCurrentItem(currentPosition, false)
    }

    open fun parentClipToPadding(): Boolean = true

    /**
     * @return BannerViewPager data set
     */
    fun getData(): List<ITEM> {
        return mBannerPagerAdapter?.getData() ?: emptyList()
    }


    fun startPlay(interval: Long = getInterval()) {
        if (!isLooping && isAutoPlay() && (mBannerPagerAdapter != null) && ((mBannerPagerAdapter?.getListSize()
                ?: 0) > 1)
        ) {
            mHandler.postDelayed(mRunnable, interval)
            isLooping = true
        }
    }

    /*Stop play*/
    fun stopPlay() {
        if (isLooping) {
            mHandler.removeCallbacks(mRunnable)
            isLooping = false
        }
    }


    fun setAdapter(adapter: BaseBannerAdapter<ITEM>): BannerViewPager<ITEM> {
        mBannerPagerAdapter = adapter
        return this
    }

    fun getAdapter(): BaseBannerAdapter<ITEM>? {
        return mBannerPagerAdapter
    }

    /**
     * Set round rectangle effect for BannerViewPager.
     *
     * @param radius round radius
     */
    fun setRoundCorner(radius: Int): BannerViewPager<ITEM> {
        bannerManager.getBannerOptions().setRoundRectRadius(radius)
        return this
    }

    /**
     * Set round rectangle effect for BannerViewPager.
     *
     * @param topLeftRadius top left round radius
     * @param topRightRadius top right round radius
     * @param bottomLeftRadius bottom left round radius
     * @param bottomRightRadius bottom right round radius
     */
    fun setRoundCorner(
        topLeftRadius: Int, topRightRadius: Int, bottomLeftRadius: Int, bottomRightRadius: Int
    ): BannerViewPager<ITEM> {
        mRadiusRectF = RectF()
        mRadiusPath = Path()
        bannerManager.getBannerOptions()
            .setRoundRectRadius(topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius)
        return this
    }

    /**
     * Enable/disable auto play
     *
     * @param autoPlay is enable auto play
     */
    fun setAutoPlay(autoPlay: Boolean): BannerViewPager<ITEM> {
        bannerManager.getBannerOptions().setAutoPlay(autoPlay)
        if (isAutoPlay()) {
            bannerManager.getBannerOptions().setCyclic(true)
        }
        return this
    }

    /**
     * Enable/disable loop
     *
     * @param cyclic is can loop
     */
    fun setCyclic(cyclic: Boolean): BannerViewPager<ITEM> {
        bannerManager.getBannerOptions().setCyclic(cyclic)
        if (!cyclic) {
            bannerManager.getBannerOptions().setAutoPlay(false)
        }
        return this
    }

    /**
     * Set loop interval
     *
     * @param interval loop interval,unit is millisecond.
     */
    fun setInterval(interval: Long): BannerViewPager<ITEM> {
        bannerManager.getBannerOptions().setInterval(interval)
        return this
    }

    /**
     * @param transformer PageTransformer that will modify each page's animation properties
     */
    fun setPageTransformer(transformer: ViewPager2.PageTransformer?): BannerViewPager<ITEM> {
        if (transformer != null) {
            mViewPager?.setPageTransformer(transformer)
        }
        return this
    }

    /**
     * @param transformer PageTransformer that will modify each page's animation properties
     */
    fun addPageTransformer(transformer: ViewPager2.PageTransformer?): BannerViewPager<ITEM> {
        if (transformer != null) {
            bannerManager.addTransformer(transformer)
        }
        return this
    }

    fun removeTransformer(transformer: ViewPager2.PageTransformer?) {
        if (transformer != null) {
            bannerManager.removeTransformer(transformer)
        }
    }

    fun removeDefaultPageTransformer() {
        bannerManager.removeDefaultPageTransformer()
    }

    fun removeMarginPageTransformer() {
        bannerManager.removeMarginPageTransformer()
    }

    /**
     * set page margin
     *
     * @param pageMargin page margin
     */
    fun setPageMargin(pageMargin: Int): BannerViewPager<ITEM> {
        bannerManager.setPageMargin(pageMargin)
        return this
    }

    /**
     * set item click listener
     *
     * @param onPageClickListener item click listener
     */
    fun setOnPageClickListener(onPageClickListener: OnPageClickListener?): BannerViewPager<ITEM> {
        mOnPageClickListener = onPageClickListener
        return this
    }

    /**
     * Set page scroll duration
     *
     * @param scrollDuration page scroll duration
     */
    fun setScrollDuration(scrollDuration: Int): BannerViewPager<ITEM> {
        bannerManager.getBannerOptions().setScrollDuration(scrollDuration)
        return this
    }

    /**
     * set indicator color
     *
     * @param checkedColor checked color of indicator
     * @param normalColor unchecked color of indicator
     */
    fun setIndicatorSliderColor(
        @ColorInt normalColor: Int, @ColorInt checkedColor: Int
    ): BannerViewPager<ITEM> {
        bannerManager.getBannerOptions().setIndicatorSliderColor(normalColor, checkedColor)
        return this
    }

    /**
     * set indicator circle radius
     *
     *
     * if the indicator style is [com.colin.library.android.widget.banner.def.IndicatorStyle.DASH]
     * or [com.colin.library.android.widget.banner.def.IndicatorStyle.ROUND_RECT]
     * the indicator dash width=2*radius
     *
     * @param radius 指示器圆点半径
     */
    fun setIndicatorSliderRadius(radius: Int): BannerViewPager<ITEM> {
        setIndicatorSliderRadius(radius, radius)
        return this
    }

    /**
     * set indicator circle radius
     *
     * @param normalRadius unchecked circle radius
     * @param checkedRadius checked circle radius
     */
    fun setIndicatorSliderRadius(normalRadius: Int, checkedRadius: Int): BannerViewPager<ITEM> {
        bannerManager.getBannerOptions()
            .setIndicatorSliderWidth(normalRadius * 2, checkedRadius * 2)
        return this
    }

    fun setIndicatorSliderWidth(indicatorWidth: Int): BannerViewPager<ITEM> {
        setIndicatorSliderWidth(indicatorWidth, indicatorWidth)
        return this
    }

    /**
     * Set indicator dash width，if indicator style is [com.colin.library.android.widget.banner.def.IndicatorStyle.CIRCLE],
     * the indicator circle radius is indicatorWidth/2.
     *
     * @param normalWidth if the indicator style is [com.colin.library.android.widget.banner.def.IndicatorStyle.DASH]
     * the params means unchecked dash width
     * if the indicator style is [com.colin.library.android.widget.banner.def.IndicatorStyle.ROUND_RECT]  means
     * unchecked round rectangle width
     * if the indicator style is [com.colin.library.android.widget.banner.def.IndicatorStyle.CIRCLE] means
     * unchecked circle diameter
     * @param checkWidth if the indicator style is [com.colin.library.android.widget.banner.def.IndicatorStyle.DASH]
     * the params means checked dash width
     * if the indicator style is [com.colin.library.android.widget.banner.def.IndicatorStyle.ROUND_RECT] the
     * params means checked round rectangle width
     * if the indicator style is [com.colin.library.android.widget.banner.def.IndicatorStyle.CIRCLE] means
     * checked circle diameter
     */
    fun setIndicatorSliderWidth(normalWidth: Int, checkWidth: Int): BannerViewPager<ITEM> {
        bannerManager.getBannerOptions().setIndicatorSliderWidth(normalWidth, checkWidth)
        return this
    }

    fun setIndicatorHeight(indicatorHeight: Int): BannerViewPager<ITEM> {
        bannerManager.getBannerOptions().setIndicatorHeight(indicatorHeight)
        return this
    }

    /**
     * Set Indicator gap of dash/circle
     *
     * @param indicatorGap indicator gap
     */
    fun setIndicatorSliderGap(indicatorGap: Int): BannerViewPager<ITEM> {
        bannerManager.getBannerOptions().setIndicatorGap(indicatorGap.toFloat())
        return this
    }

    /**
     * Set the visibility state of indicator view.
     *
     * @param visibility One of [View.VISIBLE], [View.INVISIBLE], or [View.GONE].
     */
    fun setIndicatorVisibility(visibility: Int): BannerViewPager<ITEM> {
        bannerManager.getBannerOptions().setIndicatorVisibility(visibility)
        return this
    }

    /**
     * set indicator gravity in BannerViewPager
     *
     * @param gravity indicator gravity
     * [IndicatorGravity.CENTER]
     * [IndicatorGravity.START]
     * [IndicatorGravity.END]
     */
    fun setIndicatorGravity(@IndicatorGravity gravity: Int): BannerViewPager<ITEM> {
        bannerManager.getBannerOptions().setIndicatorGravity(gravity)
        return this
    }

    /**
     * Set Indicator slide mode，default value is [com.colin.library.android.widget.banner.def.IndicatorSlideMode.NORMAL]
     *
     * @param slideMode Indicator slide mode
     * @see com.colin.library.android.widget.banner.def.IndicatorSlideMode.NORMAL
     *
     * @see com.colin.library.android.widget.banner.def.IndicatorSlideMode.SMOOTH
     */
    fun setIndicatorSlideMode(@IndicatorSlideMode slideMode: Int): BannerViewPager<ITEM> {
        bannerManager.getBannerOptions().setIndicatorSlideMode(slideMode)
        return this
    }

    /**
     * Set custom indicator.
     * the custom indicator view must extends BaseIndicator or implements IIndicator
     *
     * @param customIndicator custom indicator view
     */
    fun setIndicatorView(customIndicator: IIndicator?): BannerViewPager<ITEM> {
        if (customIndicator is View) {
            isCustomIndicator = true
            mIndicatorView = customIndicator
        }
        return this
    }

    /**
     * Set indicator style
     *
     * @param indicatorStyle indicator style
     * @see com.colin.library.android.widget.banner.def.IndicatorStyle.CIRCLE
     *
     * @see com.colin.library.android.widget.banner.def.IndicatorStyle.DASH
     *
     * @see com.colin.library.android.widget.banner.def.IndicatorStyle.ROUND_RECT
     */
    fun setIndicatorStyle(@IndicatorStyle indicatorStyle: Int): BannerViewPager<ITEM> {
        bannerManager.getBannerOptions().setIndicatorStyle(indicatorStyle)
        return this
    }

    /**
     * Create BannerViewPager with data.
     * If data has fetched when create BannerViewPager,you can call this method.
     */
    fun create(data: List<ITEM>?, isInitCurrent: Boolean = true) {
        if (mBannerPagerAdapter == null) {
            throw NullPointerException("You must set adapter for BannerViewPager")
        }
        mBannerPagerAdapter?.setData(data)
        initBannerData(isInitCurrent)
    }

    /**
     * Create BannerViewPager with no data
     * If there is no data while you create BannerViewPager(for example,The data is from remote
     * server)，you can call this method.
     * Then,while you fetch data successfully,just need call [.refreshData] method to
     * refresh.
     */
    fun create() {
        create(ArrayList())
    }

    /**
     * Sets the orientation of the ViewPager2.
     *
     * @param orientation [ViewPager2.ORIENTATION_HORIZONTAL] or
     * [ViewPager2.ORIENTATION_VERTICAL]
     */
    fun setOrientation(@ViewPager2.Orientation orientation: Int): BannerViewPager<ITEM> {
        bannerManager.getBannerOptions().setOrientation(orientation)
        return this
    }

    fun addItemDecoration(decor: RecyclerView.ItemDecoration, index: Int) {
        if (isCyclicSafely()) {
            val pageSize: Int = mBannerPagerAdapter?.getListSize() ?: 0
            val currentItem = mViewPager?.currentItem ?: 0
            val realPosition: Int = BannerUtils.getRealPosition(currentItem, pageSize)
            if (currentItem != index) {
                if (index == 0 && realPosition == pageSize - 1) {
                    mViewPager?.addItemDecoration(decor, currentItem + 1)
                } else if (realPosition == 0 && index == pageSize - 1) {
                    mViewPager?.addItemDecoration(decor, currentItem - 1)
                } else {
                    mViewPager?.addItemDecoration(decor, currentItem + (index - realPosition))
                }
            }
        } else {
            mViewPager?.addItemDecoration(decor, index)
        }
    }

    fun addItemDecoration(decor: RecyclerView.ItemDecoration) {
        mViewPager?.addItemDecoration(decor)
    }

    /**
     * Refresh data.
     * Confirm the [.create] or [.create] method has been called,
     * else the data won't be shown.
     *
     * Fix #209 如果BVP没有 attach 到 Window 上的时候刷新 ViewPager2 就会导致
     * ViewPager2 的 currentItem 被 reset 为 0，故出现 BVP 的 item 快速滚动问题
     * 为了避免这一问题，只能在已经attach 到 Window 上的时候去刷新数据。
     */
    fun refreshData(list: List<ITEM>?) {
        post {
            if (isAttachedToWindow && (list != null) && (mBannerPagerAdapter != null)) {
                stopPlay()
                mBannerPagerAdapter?.setData(list)
                val limitSize = bannerManager.getBannerOptions().getOffScreenPageLimit()
                mViewPager?.offscreenPageLimit =
                    if (limitSize > 0) limitSize else OFFSCREEN_PAGE_LIMIT_DEFAULT
                resetCurrentItem(getCurrentItem())
                refreshIndicator(list)
                startPlay()
            }
        }
    }

    fun addData(list: List<ITEM>?) {
        if (isAttachedToWindow && (list != null) && (mBannerPagerAdapter != null)) {
            mBannerPagerAdapter?.addData(list)
            resetCurrentItem(getCurrentItem())
            val data: MutableList<ITEM>? = mBannerPagerAdapter?.getData()
            data?.let { refreshIndicator(it) }
        }
    }

//    /**
//     * Removes the item at the specified position in this list.
//     *
//     * @param index the index of the item to be removed
//     */
//    fun removeItem(index: Int) {
//        val data: List<T>? = mBannerPagerAdapter.getData()
//        if (isAttachedToWindow && (index >= 0) && (index < data?.size)) {
//            data.removeAt(index)
//            mBannerPagerAdapter.notifyDataSetChanged()
//            resetCurrentItem(getCurrentItem())
//            refreshIndicator(data)
//        }
//    }
//
//    /**
//     * Inserts the specified element at the specified position in this list
//     *
//     * @param index index at which the specified element is to be inserted
//     * @param item item element to be inserted
//     */
//    fun insertItem(index: Int, item: T) {
//        val data: List<T> = mBannerPagerAdapter.getData()
//        if (isAttachedToWindow && (index >= 0) && (index <= data.size)) {
//            data.add(index, item)
//            mBannerPagerAdapter.notifyDataSetChanged()
//            resetCurrentItem(getCurrentItem())
//            refreshIndicator(data)
//        }
//    }

    /**
     * @return the currently selected page position.
     */
    fun getCurrentItem(): Int {
        return currentPosition
    }

    /**
     * Set the currently selected page. If the ViewPager has already been through its first
     * layout with its current adapter there will be a smooth animated transition between
     * the current item and the specified item.
     *
     * @param item Item index to select
     */
    fun setCurrentItem(item: Int) {
        setCurrentItem(item, true)
    }

    /**
     * Set the currently selected page.
     *
     * @param item Item index to select
     * @param smoothScroll True to smoothly scroll to the new item, false to transition immediately
     */
    fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        var item = item
        if (isCyclicSafely()) {
            val pageSize: Int = mBannerPagerAdapter?.getListSize() ?: 0
            item = if (item >= pageSize) pageSize - 1 else item
            val currentItem = mViewPager?.currentItem ?: 0
            val realPosition: Int = BannerUtils.getRealPosition(currentItem, pageSize)
            if (currentItem != item) {
                stopPlay()
                if (item == 0 && realPosition == pageSize - 1) {
                    mViewPager?.setCurrentItem(currentItem + 1, smoothScroll)
                } else if (realPosition == 0 && item == pageSize - 1) {
                    mViewPager?.setCurrentItem(currentItem - 1, smoothScroll)
                } else {
                    mViewPager?.setCurrentItem(currentItem + (item - realPosition), smoothScroll)
                }
                startPlay()
            }
        } else {
            mViewPager?.setCurrentItem(item, smoothScroll)
        }
    }

    /**
     * Set the default PageTrans former for [ViewPager2]
     * Option params:
     * [PageStyle.MULTI_PAGE_OVERLAP]
     * [PageStyle.MULTI_PAGE_SCALE]
     * [PageStyle.NORMAL]
     */
    fun setPageStyle(@PageStyle pageStyle: Int): BannerViewPager<ITEM> {
        return setPageStyle(pageStyle, DEFAULT_MIN_SCALE)
    }

    fun setPageStyle(@PageStyle pageStyle: Int, pageScale: Float): BannerViewPager<ITEM> {
        bannerManager.getBannerOptions().setPageStyle(pageStyle)
        bannerManager.getBannerOptions().setPageScale(pageScale)
        return this
    }

    /**
     * @param revealWidth In the multi-page mode, The exposed width of the items on the left and right
     * sides
     */
    fun setRevealWidth(revealWidth: Int): BannerViewPager<ITEM> {
        setRevealWidth(revealWidth, revealWidth)
        return this
    }

    /**
     * This method is apply to multi-page mode [.setPageStyle]
     *
     * @param leftRevealWidth The exposed width of left side
     * @param rightRevealWidth The exposed width of right side
     */
    fun setRevealWidth(leftRevealWidth: Int, rightRevealWidth: Int): BannerViewPager<ITEM> {
        bannerManager.getBannerOptions().setRightRevealWidth(rightRevealWidth)
        bannerManager.getBannerOptions().setLeftRevealWidth(leftRevealWidth)
        return this
    }

    /**
     * Suggest to use default offScreenPageLimit.
     */
    fun setOffScreenPageLimit(offScreenPageLimit: Int): BannerViewPager<ITEM> {
        bannerManager.getBannerOptions().setOffScreenPageLimit(offScreenPageLimit)
        return this
    }

    fun setIndicatorMargin(left: Int, top: Int, right: Int, bottom: Int): BannerViewPager<ITEM> {
        bannerManager.getBannerOptions().setIndicatorMargin(left, top, right, bottom)
        return this
    }

    /**
     * Enable or disable user initiated scrolling
     */
    fun setUserInputEnabled(userInputEnabled: Boolean): BannerViewPager<ITEM> {
        bannerManager.getBannerOptions().setUserInputEnabled(userInputEnabled)
        mViewPager?.isUserInputEnabled = userInputEnabled
        return this
    }

    interface OnPageClickListener {
        fun onPageClick(clickedView: View?, position: Int)
    }

    fun registerOnPageChangeCallback(
        onPageChangeCallback: OnPageChangeCallback?
    ): BannerViewPager<ITEM> {
        this.onPageChangeCallback = onPageChangeCallback
        return this
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        stopPlay()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        startPlay()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        stopPlay()
    }

    /**
     * 设置是否允许在BVP的[MotionEvent.ACTION_DOWN]事件中禁止父View对事件的拦截，该方法
     * 用于解决CoordinatorLayout+CollapsingToolbarLayout在嵌套BVP时引起的滑动冲突问题。
     *
     *
     * BVP在处理ViewPager2嵌套滑动冲突时，在[.onInterceptTouchEvent]
     * 方法的[MotionEvent.ACTION_DOWN]事件中禁止了BVP的父View对触摸事件的拦截，
     * 导致CollapsingToolbarLayout的布局无法获取[MotionEvent.ACTION_DOWN]事件，
     * 致使CollapsingToolbarLayout无法处理down事件后的一系列事件而无法滑动。
     * 对于这种情况可以调用该方法不允许在BVP在[MotionEvent.ACTION_DOWN]事件中禁止父View的事件拦截。
     *
     * 调用该方法将disallowIntercept设置为true后虽然解决了滑动冲突，但也会造成一定的不良影响，即如果BVP设置
     * 水平滑动，同时BVP外部也是可以水平滑动的ViewPager，则存在较小概率的滑动冲突，即滑动BVP的同时可能会触发
     * 外部ViewPager的滑动。但这一问题到目前为止似乎没有好的解决方案。
     *
     * @param disallowParentInterceptDownEvent 是否允许BVP在[MotionEvent.ACTION_DOWN]事件中禁止父View拦截事件，默认值为false
     * true 不允许BVP在[MotionEvent.ACTION_DOWN]时间中禁止父View的时间拦截，
     * 设置disallowIntercept为true可以解决CoordinatorLayout+CollapsingToolbarLayout的滑动冲突
     * false 允许BVP在[MotionEvent.ACTION_DOWN]时间中禁止父View的时间拦截，
     */
    fun disallowParentInterceptDownEvent(
        disallowParentInterceptDownEvent: Boolean
    ): BannerViewPager<ITEM> {
        bannerManager.getBannerOptions()
            .setDisallowParentInterceptDownEvent(disallowParentInterceptDownEvent)
        return this
    }

    /**
     * Set right to left mode.
     *
     * @param rtlMode true:right to left mode,
     * false:right to left mode.
     */
    fun setRTLMode(rtlMode: Boolean): BannerViewPager<ITEM> {
        mViewPager?.layoutDirection = if (rtlMode) LAYOUT_DIRECTION_RTL else LAYOUT_DIRECTION_LTR
        bannerManager.getBannerOptions().setRtl(rtlMode)
        return this
    }

    /**
     * @param stopLoopWhenDetachedFromWindow 当BVP滑动出屏幕的时候是否要停止轮播，
     *
     * true:滑动出屏幕停止自动轮播，false:滑动出屏幕继续自动轮播。默认值为true
     */
    fun stopLoopWhenDetachedFromWindow(stopLoopWhenDetachedFromWindow: Boolean): BannerViewPager<ITEM> {
        bannerManager.getBannerOptions()
            .setStopLoopWhenDetachedFromWindow(stopLoopWhenDetachedFromWindow)
        return this
    }

    /**
     * @param showIndicatorWhenOneItem 只有一个item时是否显示指示器，
     * true：显示，false：不显示，默认值false
     */
    fun showIndicatorWhenOneItem(showIndicatorWhenOneItem: Boolean): BannerViewPager<ITEM> {
        bannerManager.getBannerOptions().showIndicatorWhenOneItem(showIndicatorWhenOneItem)
        return this
    }


    @Deprecated("Use {@link BannerViewPager#disallowParentInterceptDownEvent(boolean)} instead.")
    fun disallowInterceptTouchEvent(disallowIntercept: Boolean): BannerViewPager<ITEM> {
        bannerManager.getBannerOptions().setDisallowParentInterceptDownEvent(disallowIntercept)
        return this
    }

    /**
     * Set round rectangle effect for BannerViewPager.
     *
     * @param radius round radius
     */
    @Deprecated("Use {@link #setRoundCorner(int)} instead.")
    fun setRoundRect(radius: Int): BannerViewPager<ITEM> {
        return setRoundCorner(radius)
    }

    /**
     * Set round rectangle effect for BannerViewPager.
     *
     * @param topLeftRadius top left round radius
     * @param topRightRadius top right round radius
     * @param bottomLeftRadius bottom left round radius
     * @param bottomRightRadius bottom right round radius
     */
    @Deprecated("Use {@link #setRoundCorner(int, int, int, int)} instead.")
    fun setRoundRect(
        topLeftRadius: Int, topRightRadius: Int, bottomLeftRadius: Int, bottomRightRadius: Int
    ): BannerViewPager<ITEM> {
        return setRoundCorner(topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius)
    }

    /**
     * 获取展示的ViewHolder
     */
    @SuppressLint("UseKtx")
    fun getCurrentViewHolder(): ViewHolder? {
        mViewPager?.let {
            if (it.childCount > 0) {
                val view = it[0]
                if (view is RecyclerView) {
                    return view.findViewHolderForAdapterPosition(it.currentItem)
                }
            }
        }
        return null
    }

    /**
     * 设置当前Position
     */
    fun setCurrentPosition(position: Int): BannerViewPager<ITEM> {
        currentPosition = position
        return this
    }
}