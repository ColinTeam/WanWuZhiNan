package com.colin.library.android.widget.banner.base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.colin.library.android.widget.banner.def.IndicatorSlideMode
import com.colin.library.android.widget.banner.def.IndicatorSlideMode.Companion.NORMAL
import com.colin.library.android.widget.banner.def.IndicatorStyle
import com.colin.library.android.widget.banner.indicator.IIndicator
import com.colin.library.android.widget.banner.options.IndicatorOptions

/**
 * IndicatorView基类，处理了页面滑动。
 */
@Suppress("UNUSED")
open class BaseIndicatorView constructor(
    context: Context, attrs: AttributeSet?, defStyleAttr: Int
) : View(context, attrs, defStyleAttr), IIndicator {

    private var indicatorOptions: IndicatorOptions? = null
    private var mViewPager: ViewPager? = null
    private var mViewPager2: ViewPager2? = null


    override fun onPageSelected(position: Int) {
        if (getSlideMode() == NORMAL) {
            setCurrentPosition(position)
            setSlideProgress(0f)
            invalidate()
        }
    }

    override fun onPageScrolled(
        position: Int, positionOffset: Float, positionOffsetPixels: Int
    ) {
        if (getSlideMode() != NORMAL && getPageSize() > 1) {
            scrollSlider(position, positionOffset)
            invalidate()
        }
    }

    private fun scrollSlider(
        position: Int, positionOffset: Float
    ) {
        if (getIndicatorOptions().slideMode == IndicatorSlideMode.SCALE || getIndicatorOptions().slideMode == IndicatorSlideMode.COLOR) {
            setCurrentPosition(position)
            setSlideProgress(positionOffset)
        } else {
            if (position % getPageSize() == getPageSize() - 1) { //   最后一个页面与第一个页面
                if (positionOffset < 0.5) {
                    setCurrentPosition(position)
                    setSlideProgress(0f)
                } else {
                    setCurrentPosition(0)
                    setSlideProgress(0f)
                }
            } else {    //  中间页面
                setCurrentPosition(position)
                setSlideProgress(positionOffset)
            }
        }
    }

    override fun notifyDataChanged() {
        setupViewPager()
        requestLayout()
        invalidate()
    }

    private fun setupViewPager() {
        mViewPager?.let {
            mViewPager?.removeOnPageChangeListener(this)
            mViewPager?.addOnPageChangeListener(this)
            mViewPager?.adapter?.let {
                setPageSize(it.count)
            }
        }

        mViewPager2?.let {
            mViewPager2?.unregisterOnPageChangeCallback(mOnPageChangeCallback)
            mViewPager2?.registerOnPageChangeCallback(mOnPageChangeCallback)
            mViewPager2?.adapter?.let {
                setPageSize(it.itemCount)
            }
        }
    }

    fun getNormalSlideWidth(): Float {
        return getIndicatorOptions().normalSliderWidth
    }

    fun setNormalSlideWidth(normalSliderWidth: Float) {
        getIndicatorOptions().normalSliderWidth = normalSliderWidth
    }

    fun getCheckedSlideWidth(): Float {
        return getIndicatorOptions().checkedSliderWidth
    }

    fun setCheckedSlideWidth(checkedSliderWidth: Float) {
        getIndicatorOptions().checkedSliderWidth = checkedSliderWidth
    }

    val checkedSliderWidth: Float get() = getIndicatorOptions().checkedSliderWidth

    fun setCurrentPosition(currentPosition: Int) {
        getIndicatorOptions().currentPosition = currentPosition
    }

    fun getCurrentPosition(): Int {
        return getIndicatorOptions().currentPosition
    }

    fun getIndicatorGap(indicatorGap: Float) {
        getIndicatorOptions().sliderGap = indicatorGap
    }

    fun setIndicatorGap(indicatorGap: Float) {
        getIndicatorOptions().sliderGap = indicatorGap
    }

    fun setCheckedColor(@ColorInt normalColor: Int) {
        getIndicatorOptions().checkedSliderColor = normalColor
    }

    fun getCheckedColor(): Int {
        return getIndicatorOptions().checkedSliderColor
    }

    fun setNormalColor(@ColorInt normalColor: Int) {
        getIndicatorOptions().normalSliderColor = normalColor
    }

    fun getSlideProgress(): Float {
        return getIndicatorOptions().slideProgress
    }

    fun setSlideProgress(slideProgress: Float) {
        getIndicatorOptions().slideProgress = slideProgress
    }

    fun getPageSize(): Int {
        return getIndicatorOptions().pageSize
    }

    fun setPageSize(pageSize: Int): BaseIndicatorView {
        getIndicatorOptions().pageSize = pageSize
        return this
    }

    fun setSliderColor(
        @ColorInt normalColor: Int, @ColorInt selectedColor: Int
    ): BaseIndicatorView {
        getIndicatorOptions().setSliderColor(normalColor, selectedColor)
        return this
    }

    fun setSliderWidth(sliderWidth: Float): BaseIndicatorView {
        getIndicatorOptions().setSliderWidth(sliderWidth)
        return this
    }

    fun setSliderWidth(
        normalSliderWidth: Float, selectedSliderWidth: Float
    ): BaseIndicatorView {
        getIndicatorOptions().setSliderWidth(normalSliderWidth, selectedSliderWidth)
        return this
    }

    fun setSliderGap(sliderGap: Float): BaseIndicatorView {
        getIndicatorOptions().sliderGap = sliderGap
        return this
    }

    fun getSlideMode(): Int {
        return getIndicatorOptions().slideMode
    }

    fun setSlideMode(@IndicatorSlideMode slideMode: Int): BaseIndicatorView {
        getIndicatorOptions().slideMode = slideMode
        return this
    }

    fun setIndicatorStyle(@IndicatorStyle indicatorStyle: Int): BaseIndicatorView {
        getIndicatorOptions().indicatorStyle = indicatorStyle
        return this
    }

    fun setSliderHeight(sliderHeight: Float): BaseIndicatorView {
        getIndicatorOptions().sliderHeight = sliderHeight
        return this
    }

    fun setupWithViewPager(viewPager: ViewPager) {
        mViewPager = viewPager
        notifyDataChanged()
    }

    fun setupWithViewPager(viewPager2: ViewPager2) {
        mViewPager2 = viewPager2
        notifyDataChanged()
    }

    fun showIndicatorWhenOneItem(showIndicatorWhenOneItem: Boolean) {
        getIndicatorOptions().showIndicatorOneItem = showIndicatorWhenOneItem
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun setIndicatorOptions(options: IndicatorOptions) {
        indicatorOptions = options
    }


    fun getIndicatorOptions(): IndicatorOptions {
        if (indicatorOptions == null) {
            indicatorOptions = IndicatorOptions()
        }
        return indicatorOptions!!
    }

    private val mOnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int, positionOffset: Float, positionOffsetPixels: Int
        ) {
            this@BaseIndicatorView.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            this@BaseIndicatorView.onPageSelected(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            this@BaseIndicatorView.onPageScrollStateChanged(state)
        }
    }
}
