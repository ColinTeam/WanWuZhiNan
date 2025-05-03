package com.colin.library.android.widget.picker.wheel

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Handler
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import androidx.annotation.Px
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.withClip
import com.colin.library.android.utils.Constants
import com.colin.library.android.widget.R
import com.colin.library.android.widget.picker.IPickerText
import com.colin.library.android.widget.picker.PickerGestureListener
import com.colin.library.android.widget.picker.adapter.WheelAdapter
import kotlinx.coroutines.Runnable
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-02 08:14
 *
 * Des   :WheelView
 */
class WheelView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    enum class ACTION {
        // 点击，滑翔(滑到尽头)，拖拽事件
        CLICK, FLING, DRAG
    }

    enum class DividerType {
        // 分隔线类型
        FILL, WRAP, CIRCLE
    }

    private var dividerType: DividerType? = null //分隔线类型

    private var handler: Handler? = null
    private var gestureDetector: GestureDetector? = null

    private var isOptions = false
    private var isCenterLabel = true

    // Timer mTimer;
    private val mExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    private var mFuture: ScheduledFuture<*>? = null

    private var paintOuterText: Paint
    private var paintCenterText: Paint
    private var paintIndicator: Paint

    private var adapter: WheelAdapter? = null

    private var label: String? = null //附加单位

    private var selectedTextSize: Float = 28f //选项的文字大小
    private var maxTextWidth = 0
    private var maxTextHeight = 0
    private var textXOffset = 0
    private var itemHeight = 0f //每行高度


    private var textTypeface: Typeface? = Typeface.MONOSPACE //字体样式，默认是等宽字体
    private var textColorOut = 0
    private var textColorCenter = 0
    private var dividerColor = 0
    private var dividerWidth = 0

    // 条目间距倍数
    private var lineSpacingMultiplier = 1.6f
    private var isLoop = false

    // 第一条线Y坐标值
    private var firstLineY = 0f

    //第二条线Y坐标
    private var secondLineY = 0f

    //中间label绘制的Y坐标
    private var centerY = 0f

    //当前滚动总高度y值
    private var totalScrollY = 0f

    //初始化默认选中项
    private var initPosition = 0

    //选中的Item是第几个
    private var selectedItem = 0
    private var preCurrentIndex = 0

    // 绘制几个条目，实际上第一项和最后一项Y轴压缩成0%了，所以可见的数目实际为9
    private var itemsVisible = 11

    private var measuredHeight = 0 // WheelView 控件高度
    private var measuredWidth = 0 // WheelView 控件宽度

    // 半径
    private var radius = 0

    private var mOffset = 0
    private var previousY = 0f
    private var startTime: Long = 0

    private var widthMeasureSpec = 0

    private var mGravity = Gravity.CENTER
    private var drawCenterContentStart = 0 //中间选中文字开始绘制位置
    private var drawOutContentStart = 0 //非中间文字开始绘制位置
    private var CENTER_CONTENT_OFFSET = 0f //偏移量

    private var isAlphaGradient = false //透明度渐变

    private var onItemSelectedListener: ((Int) -> Unit)? = null

    init {
        if (attrs != null) {
            context.withStyledAttributes(attrs, R.styleable.WheelView, 0, 0) {
                isLoop = getBoolean(R.styleable.WheelView_wheel_loop, true)
                mGravity = getInt(R.styleable.WheelView_wheel_gravity, Gravity.CENTER)
                textColorOut = getColor(R.styleable.WheelView_wheel_textColorOut, -0x575758)
                textColorCenter = getColor(R.styleable.WheelView_wheel_textColorCenter, -0xd5d5d6)
                dividerColor = getColor(R.styleable.WheelView_wheel_dividerColor, -0x2a2a2b)
                dividerWidth = getDimensionPixelSize(R.styleable.WheelView_wheel_dividerWidth, 2)
                selectedTextSize =
                    getDimensionPixelSize(R.styleable.WheelView_wheel_textSize, 16).toFloat()
                lineSpacingMultiplier = getFloat(
                    R.styleable.WheelView_wheel_lineSpacingMultiplier, lineSpacingMultiplier
                )
            }
        }
        // 屏幕密度比（0.75/1.0/1.5/2.0/3.0）
        val density = resources.displayMetrics.density
        if (density < 1) { //根据密度不同进行适配
            CENTER_CONTENT_OFFSET = 2.4f
        } else if (1 <= density && density < 2) {
            CENTER_CONTENT_OFFSET = 4.0f
        } else if (2 <= density && density < 3) {
            CENTER_CONTENT_OFFSET = 6.0f
        } else if (density >= 3) {
            CENTER_CONTENT_OFFSET = density * 2.5f
        }
        updateLineSpace()
        handler = MessageHandler(this)
        gestureDetector = GestureDetector(context, PickerGestureListener(this))
        gestureDetector?.setIsLongpressEnabled(false)
        totalScrollY = 0f
        initPosition = -1
        paintOuterText = Paint().apply {
            isAntiAlias = true
            color = textColorOut
            typeface = textTypeface
            textSize = selectedTextSize
        }
        paintCenterText = Paint().apply {
            isAntiAlias = true
            color = textColorCenter
            typeface = textTypeface
            textSize = selectedTextSize
            textScaleX = 1.1f
        }
        paintIndicator = Paint().apply {
            isAntiAlias = true
            color = dividerColor
        }
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    /**
     * 判断间距是否在1.0-4.0之间
     */
    private fun updateLineSpace() {
        if (lineSpacingMultiplier < 1.0f) {
            lineSpacingMultiplier = 1.0f
        } else if (lineSpacingMultiplier > 4.0f) {
            lineSpacingMultiplier = 4.0f
        }
    }


    private fun reMeasure() { //重新测量
        adapter ?: return
        measureTextWidthHeight()
        //半圆的周长 = item高度乘以item数目-1
        val halfCircumference = (itemHeight * (itemsVisible - 1)).toInt()
        //整个圆的周长除以PI得到直径，这个直径用作控件的总高度
        measuredHeight = ((halfCircumference * 2) / Math.PI).toInt()
        //求出半径
        radius = (halfCircumference / Math.PI).toInt()
        //控件宽度，这里支持weight
        measuredWidth = MeasureSpec.getSize(widthMeasureSpec)
        //计算两条横线 和 选中项画笔的基线Y位置
        firstLineY = (measuredHeight - itemHeight) / 2.0f
        secondLineY = (measuredHeight + itemHeight) / 2.0f
        centerY = secondLineY - (itemHeight - maxTextHeight) / 2.0f - CENTER_CONTENT_OFFSET

        //初始化显示的item的position
        if (initPosition == -1) {
            initPosition = if (isLoop) (adapter!!.getItemsCount() + 1) / 2 else 0
        }
        preCurrentIndex = initPosition
    }

    /**
     * 计算最大length的Text的宽高度
     */
    private fun measureTextWidthHeight() {
        adapter ?: return
        val rect = Rect()
        for (i in 0..<adapter!!.getItemsCount()) {
            val s1 = getContentText(adapter!!.getItem(i))
            paintCenterText.getTextBounds(s1, 0, s1.length, rect)

            val textWidth = rect.width()
            if (textWidth > maxTextWidth) {
                maxTextWidth = textWidth
            }
        }
        paintCenterText.getTextBounds("\u661F\u671F", 0, 2, rect) // 星期的字符编码（以它为标准高度）
        maxTextHeight = rect.height() + 2
        itemHeight = lineSpacingMultiplier * maxTextHeight
    }

    fun smoothScroll(action: ACTION?) { //平滑滚动的实现
        cancelFuture()
        if (action == ACTION.FLING || action == ACTION.DRAG) {
            mOffset = ((totalScrollY % itemHeight + itemHeight) % itemHeight).toInt()
            if (mOffset.toFloat() > itemHeight / 2.0f) { //如果超过Item高度的一半，滚动到下一个Item去
                mOffset = (itemHeight - mOffset.toFloat()).toInt()
            } else {
                mOffset = -mOffset
            }
        }
        //停止的时候，位置有偏移，不是全部都能正确停止到中间位置的，这里把文字位置挪回中间去
        mFuture = mExecutor.scheduleWithFixedDelay(
            SmoothScrollTimerTask(this, mOffset), 0, 10, TimeUnit.MILLISECONDS
        )
    }

    override fun scrollBy(x: Int, y: Int) {
        super.scrollBy(x, y)
        cancelFuture()
        mFuture = mExecutor.scheduleWithFixedDelay(
            InertiaTimerTask(this, y), 0, VELOCITY_FLING.toLong(), TimeUnit.MILLISECONDS
        )
    }

    fun cancelFuture() {
        if (mFuture != null && !mFuture!!.isCancelled) {
            mFuture!!.cancel(true)
            mFuture = null
        }
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic 是否循环
     */
    fun setCyclic(cyclic: Boolean) {
        isLoop = cyclic
    }

    fun setTypeface(font: Typeface?) {
        textTypeface = font
        paintOuterText.setTypeface(textTypeface)
        paintCenterText.setTypeface(textTypeface)
    }

    fun setTextSize(@Px size: Float) {
        if (size > 0f && selectedTextSize != size) {
            paintOuterText.textSize = selectedTextSize
            paintCenterText.textSize = selectedTextSize
        }
    }

    fun setCurrentItem(currentItem: Int) {
        //不添加这句,当这个wheelView不可见时,默认都是0,会导致获取到的时间错误
        this.selectedItem = currentItem
        this.initPosition = currentItem
        totalScrollY = 0f //回归顶部，不然重设setCurrentItem的话位置会偏移的，就会显示出不对位置的数据
        invalidate()
    }

    fun setOnItemSelectedListener(listener: ((Int) -> Unit)?) {
        this.onItemSelectedListener = listener
    }

    fun setAdapter(adapter: WheelAdapter) {
        this.adapter = adapter
        reMeasure()
        invalidate()
    }

    fun setItemsVisibleCount(visibleCount: Int) {
        var visibleCount = visibleCount
        if (visibleCount % 2 == 0) {
            visibleCount += 1
        }
        this.itemsVisible = visibleCount + 2 //第一条和最后一条
    }

    fun setAlphaGradient(alphaGradient: Boolean) {
        isAlphaGradient = alphaGradient
    }

    fun getAdapter(): WheelAdapter? {
        return adapter
    }

    fun getCurrentItem(): Int {
        if (adapter == null) {
            return 0
        }
        if (isLoop && (selectedItem < 0 || selectedItem >= adapter!!.getItemsCount())) {
            return max(
                0.0, min(
                    abs((abs(selectedItem.toDouble()) - adapter!!.getItemsCount()).toDouble()),
                    (adapter!!.getItemsCount() - 1).toDouble()
                )
            ).toInt()
        }
        return max(
            0.0, min(selectedItem.toDouble(), (adapter!!.getItemsCount() - 1).toDouble())
        ).toInt()
    }

    fun onItemSelected() {
        postDelayed(Runnable {
            onItemSelectedListener?.invoke(getCurrentItem())
        }, Constants.TIMEOUT_CLICK)
    }

    @SuppressLint("UseKtx")
    override fun onDraw(canvas: Canvas) {
        if (adapter == null) {
            return
        }
        //initPosition越界会造成preCurrentIndex的值不正确
        initPosition = min(
            max(0.0, initPosition.toDouble()), (adapter!!.getItemsCount() - 1).toDouble()
        ).toInt()

        //滚动的Y值高度除去每行Item的高度，得到滚动了多少个item，即change数
        //滚动偏移值,用于记录滚动了多少个item
        val change = (totalScrollY / itemHeight).toInt()

        // Log.d("change", "" + change);
        try {
            //滚动中实际的预选中的item(即经过了中间位置的item) ＝ 滑动前的位置 ＋ 滑动相对位置
            preCurrentIndex = initPosition + change % adapter!!.getItemsCount()
        } catch (e: ArithmeticException) {
            Log.e("WheelView", "出错了！adapter.getItemsCount() == 0，联动数据不匹配")
        }
        if (!isLoop) { //不循环的情况
            if (preCurrentIndex < 0) {
                preCurrentIndex = 0
            }
            if (preCurrentIndex > adapter!!.getItemsCount() - 1) {
                preCurrentIndex = adapter!!.getItemsCount() - 1
            }
        } else { //循环
            if (preCurrentIndex < 0) { //举个例子：如果总数是5，preCurrentIndex ＝ －1，那么preCurrentIndex按循环来说，其实是0的上面，也就是4的位置
                preCurrentIndex = adapter!!.getItemsCount() + preCurrentIndex
            }
            if (preCurrentIndex > adapter!!.getItemsCount() - 1) { //同理上面,自己脑补一下
                preCurrentIndex = preCurrentIndex - adapter!!.getItemsCount()
            }
        }
        //跟滚动流畅度有关，总滑动距离与每个item高度取余，即并不是一格格的滚动，每个item不一定滚到对应Rect里的，这个item对应格子的偏移值
        val itemHeightOffset = (totalScrollY % itemHeight)


        //绘制中间两条横线
        if (dividerType == DividerType.WRAP) { //横线长度仅包裹内容
            var startX: Float

            startX = if (TextUtils.isEmpty(label)) { //隐藏Label的情况
                ((measuredWidth - maxTextWidth) / 2 - 12).toFloat()
            } else {
                ((measuredWidth - maxTextWidth) / 4 - 12).toFloat()
            }

            if (startX <= 0) { //如果超过了WheelView的边缘
                startX = 10f
            }
            val endX: Float = measuredWidth - startX
            canvas.drawLine(startX, firstLineY, endX, firstLineY, paintIndicator)
            canvas.drawLine(startX, secondLineY, endX, secondLineY, paintIndicator)
        } else if (dividerType == DividerType.CIRCLE) {
            //分割线为圆圈形状
            paintIndicator.style = Paint.Style.STROKE
            paintIndicator.strokeWidth = dividerWidth.toFloat()
            var startX: Float
            if (TextUtils.isEmpty(label)) { //隐藏Label的情况
                startX = (measuredWidth - maxTextWidth) / 2f - 12
            } else {
                startX = (measuredWidth - maxTextWidth) / 4f - 12
            }
            if (startX <= 0) { //如果超过了WheelView的边缘
                startX = 10f
            }
            val endX: Float = measuredWidth - startX
            //半径始终以宽高中最大的来算
            val radius: Double = max((endX - startX).toDouble(), itemHeight.toDouble()) / 1.8f
            canvas.drawCircle(
                measuredWidth / 2f, measuredHeight / 2f, radius.toFloat(), paintIndicator
            )
        } else {
            canvas.drawLine(0.0f, firstLineY, measuredWidth.toFloat(), firstLineY, paintIndicator)
            canvas.drawLine(
                0.0f, secondLineY, measuredWidth.toFloat(), secondLineY, paintIndicator
            )
        }

        //只显示选中项Label文字的模式，并且Label文字不为空，则进行绘制
        if (!TextUtils.isEmpty(label) && isCenterLabel) {
            //绘制文字，靠右并留出空隙
            val drawRightContentStart = measuredWidth - getTextWidth(paintCenterText, label)
            canvas.drawText(
                label!!, drawRightContentStart - CENTER_CONTENT_OFFSET, centerY, paintCenterText
            )
        }

        // 设置数组中每个元素的值
        var counter = 0
        while (counter < itemsVisible) {
            val showText: Any?
            var index =
                preCurrentIndex - (itemsVisible / 2 - counter) //索引值，即当前在控件中间的item看作数据源的中间，计算出相对源数据源的index值

            //判断是否循环，如果是循环数据源也使用相对循环的position获取对应的item值，如果不是循环则超出数据源范围使用""空白字符串填充，在界面上形成空白无数据的item项
            if (isLoop) {
                index = getLoopMappingIndex(index)
                showText = adapter!!.getItem(index)
            } else if (index < 0) {
                showText = ""
            } else if (index > adapter!!.getItemsCount() - 1) {
                showText = ""
            } else {
                showText = adapter!!.getItem(index)
            }

            canvas.save()
            // 弧长 L = itemHeight * counter - itemHeightOffset
            // 求弧度 α = L / r  (弧长/半径) [0,π]
            val radian = (((itemHeight * counter - itemHeightOffset)) / radius).toDouble()
            // 弧度转换成角度(把半圆以Y轴为轴心向右转90度，使其处于第一象限及第四象限
            // angle [-90°,90°]
            val angle = (90.0 - (radian / Math.PI) * 180.0).toFloat() //item第一项,从90度开始，逐渐递减到 -90度

            // 计算取值可能有细微偏差，保证负90°到90°以外的不绘制
            if (angle > 90f || angle < -90f) {
                canvas.restore()
            } else {
                //获取内容文字
                val contentText: String

                //如果是label每项都显示的模式，并且item内容不为空、label 也不为空
                if (!isCenterLabel && !TextUtils.isEmpty(label) && !TextUtils.isEmpty(
                        getContentText(
                            showText
                        )
                    )
                ) {
                    contentText = getContentText(showText) + label
                } else {
                    contentText = getContentText(showText)
                }
                // 根据当前角度计算出偏差系数，用以在绘制时控制文字的 水平移动 透明度 倾斜程度.
                val offsetCoefficient = (abs(angle.toDouble()) / 90f).toDouble().pow(2.2).toFloat()

                reMeasureTextSize(contentText)
                //计算开始绘制的位置
                measuredCenterContentStart(contentText)
                measuredOutContentStart(contentText)
                val translateY =
                    (radius - cos(radian) * radius - (sin(radian) * maxTextHeight) / 2.0).toFloat()
                //根据Math.sin(radian)来更改canvas坐标系原点，然后缩放画布，使得文字高度进行缩放，形成弧形3d视觉差
                canvas.translate(0.0f, translateY)
                if (translateY <= firstLineY && maxTextHeight + translateY >= firstLineY) {
                    // 条目经过第一条线
                    canvas.save()
                    canvas.clipRect(0f, 0f, measuredWidth.toFloat(), firstLineY - translateY)
                    canvas.scale(1.0f, sin(radian).toFloat() * SCALE_CONTENT)
                    setOutPaintStyle(offsetCoefficient, angle)
                    canvas.drawText(
                        contentText,
                        drawOutContentStart.toFloat(),
                        maxTextHeight.toFloat(),
                        paintOuterText
                    )
                    canvas.restore()
                    canvas.withClip(
                        0f,
                        firstLineY - translateY,
                        measuredWidth.toFloat(),
                        (itemHeight).toInt().toFloat()
                    ) {
                        scale(1.0f, sin(radian).toFloat() * 1.0f)
                        drawText(
                            contentText,
                            drawCenterContentStart.toFloat(),
                            maxTextHeight - CENTER_CONTENT_OFFSET,
                            paintCenterText
                        )
                    }
                } else if (translateY <= secondLineY && maxTextHeight + translateY >= secondLineY) {
                    // 条目经过第二条线
                    canvas.save()
                    canvas.clipRect(0f, 0f, measuredWidth.toFloat(), secondLineY - translateY)
                    canvas.scale(1.0f, sin(radian).toFloat() * 1.0f)
                    canvas.drawText(
                        contentText,
                        drawCenterContentStart.toFloat(),
                        maxTextHeight - CENTER_CONTENT_OFFSET,
                        paintCenterText
                    )
                    canvas.restore()
                    canvas.withClip(
                        0f,
                        secondLineY - translateY,
                        measuredWidth.toFloat(),
                        (itemHeight).toInt().toFloat()
                    ) {
                        scale(1.0f, sin(radian).toFloat() * SCALE_CONTENT)
                        setOutPaintStyle(offsetCoefficient, angle)
                        drawText(
                            contentText,
                            drawOutContentStart.toFloat(),
                            maxTextHeight.toFloat(),
                            paintOuterText
                        )
                    }
                } else if (translateY >= firstLineY && maxTextHeight + translateY <= secondLineY) {
                    // 中间条目
                    // canvas.clipRect(0, 0, measuredWidth, maxTextHeight);
                    //让文字居中
                    val Y =
                        maxTextHeight - CENTER_CONTENT_OFFSET //因为圆弧角换算的向下取值，导致角度稍微有点偏差，加上画笔的基线会偏上，因此需要偏移量修正一下
                    canvas.drawText(
                        contentText, drawCenterContentStart.toFloat(), Y, paintCenterText
                    )
                    //设置选中项
                    selectedItem = preCurrentIndex - (itemsVisible / 2 - counter)
                } else {
                    // 其他条目
                    canvas.save()
                    canvas.clipRect(0, 0, measuredWidth, (itemHeight).toInt())
                    canvas.scale(1.0f, sin(radian).toFloat() * SCALE_CONTENT)
                    setOutPaintStyle(offsetCoefficient, angle)
                    // 控制文字水平偏移距离
                    canvas.drawText(
                        contentText,
                        drawOutContentStart + textXOffset * offsetCoefficient,
                        maxTextHeight.toFloat(),
                        paintOuterText
                    )
                    canvas.restore()
                }
                canvas.restore()
                paintCenterText.textSize = selectedTextSize.toFloat()
            }
            counter++
        }
    }

    //设置文字倾斜角度，透明度
    private fun setOutPaintStyle(offsetCoefficient: Float, angle: Float) {
        // 控制文字倾斜角度
        val DEFAULT_TEXT_TARGET_SKEW_X = 0.5f
        var multiplier = 0
        if (textXOffset > 0) {
            multiplier = 1
        } else if (textXOffset < 0) {
            multiplier = -1
        }
        paintOuterText!!.textSkewX =
            multiplier * (if (angle > 0) -1 else 1) * DEFAULT_TEXT_TARGET_SKEW_X * offsetCoefficient

        // 控制透明度
        val alpha =
            if (isAlphaGradient) ((90f - abs(angle.toDouble())) / 90f * 255).toInt() else 255
        // Log.d("WheelView", "alpha:" + alpha);
        paintOuterText!!.setAlpha(alpha)
    }

    /**
     * reset the size of the text Let it can fully display
     *
     * @param contentText item text content.
     */
    private fun reMeasureTextSize(contentText: String) {
        val rect = Rect()
        paintCenterText.getTextBounds(contentText, 0, contentText.length, rect)
        var width = rect.width()
        var size = selectedTextSize
        while (width > measuredWidth) {
            size--
            //设置2条横线中间的文字大小
            paintCenterText.textSize = size
            paintCenterText.getTextBounds(contentText, 0, contentText.length, rect)
            width = rect.width()
        }
        //设置2条横线外面的文字大小
        paintOuterText.textSize = size.toFloat()
    }


    //递归计算出对应的index
    private fun getLoopMappingIndex(index: Int): Int {
        adapter ?: return index
        var index = index
        if (index < 0) {
            index = index + adapter!!.getItemsCount()
            index = getLoopMappingIndex(index)
        } else if (index > adapter!!.getItemsCount() - 1) {
            index = index - adapter!!.getItemsCount()
            index = getLoopMappingIndex(index)
        }
        return index
    }

    /**
     * 获取所显示的数据源
     *
     * @param item data resource
     * @return 对应显示的字符串
     */
    private fun getContentText(item: Any?): String {

        if (item == null) {
            return ""
        } else if (item is IPickerText) {
            return item.showText()
        } else if (item is Int) {
            //如果为整形则最少保留两位数.
            return getFixNum(item)
        }
        return "$item"
    }

    private fun getFixNum(timeNum: Int): String {
        return if (timeNum >= 0 && timeNum < 10) TIME_NUM[timeNum] else timeNum.toString()
    }

    private fun measuredCenterContentStart(content: String) {
        val rect = Rect()
        paintCenterText!!.getTextBounds(content, 0, content.length, rect)
        when (mGravity) {
            Gravity.CENTER -> if (isOptions || label == null || label == "" || !isCenterLabel) {
                drawCenterContentStart = ((measuredWidth - rect.width()) * 0.5).toInt()
            } else { //只显示中间label时，时间选择器内容偏左一点，留出空间绘制单位标签
                drawCenterContentStart = ((measuredWidth - rect.width()) * 0.25).toInt()
            }

            Gravity.LEFT -> drawCenterContentStart = 0
            Gravity.RIGHT -> drawCenterContentStart =
                measuredWidth - rect.width() - CENTER_CONTENT_OFFSET.toInt()
        }
    }

    private fun measuredOutContentStart(content: String) {
        val rect = Rect()
        paintOuterText!!.getTextBounds(content, 0, content.length, rect)
        when (mGravity) {
            Gravity.CENTER -> drawOutContentStart =
                if (isOptions || label == null || label == "" || !isCenterLabel) {
                    ((measuredWidth - rect.width()) * 0.5).toInt()
                } else { //只显示中间label时，时间选择器内容偏左一点，留出空间绘制单位标签
                    ((measuredWidth - rect.width()) * 0.25).toInt()
                }

            Gravity.LEFT -> drawOutContentStart = 0
            Gravity.RIGHT -> drawOutContentStart =
                measuredWidth - rect.width() - CENTER_CONTENT_OFFSET.toInt()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        this.widthMeasureSpec = widthMeasureSpec
        reMeasure()
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        adapter ?: return onTouchEvent(event)
        val eventConsumed = gestureDetector!!.onTouchEvent(event)
        var isIgnore = false //超过边界滑动时，不再绘制UI。

        val top = -initPosition * itemHeight
        val bottom: Float = (adapter!!.getItemsCount() - 1 - initPosition) * itemHeight
        val ratio = 0.25f

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startTime = System.currentTimeMillis()
                cancelFuture()
                previousY = event.rawY
            }

            MotionEvent.ACTION_MOVE -> {
                val dy = previousY - event.rawY
                previousY = event.rawY
                totalScrollY = totalScrollY + dy

                // normal mode。
                if (!isLoop) {
                    if ((totalScrollY - itemHeight * ratio < top && dy < 0) || (totalScrollY + itemHeight * ratio > bottom && dy > 0)) {
                        //快滑动到边界了，设置已滑动到边界的标志
                        totalScrollY -= dy
                        isIgnore = true
                    } else {
                        isIgnore = false
                    }
                }
            }

            MotionEvent.ACTION_UP -> if (!eventConsumed) { //未消费掉事件
                /**
                 * @describe <关于弧长的计算>
                 *
                 * 弧长公式： L = α*R
                 * 反余弦公式：arccos(cosα) = α
                 * 由于之前是有顺时针偏移90度，
                 * 所以实际弧度范围α2的值 ：α2 = π/2-α    （α=[0,π] α2 = [-π/2,π/2]）
                 * 根据正弦余弦转换公式 cosα = sin(π/2-α)
                 * 代入，得： cosα = sin(π/2-α) = sinα2 = (R - y) / R
                 * 所以弧长 L = arccos(cosα)*R = arccos((R - y) / R)*R
                </关于弧长的计算> */

                val y = event.getY()
                val L = acos(((radius - y) / radius).toDouble()) * radius
                //item0 有一半是在不可见区域，所以需要加上 itemHeight / 2
                val circlePosition = ((L + itemHeight / 2) / itemHeight).toInt()
                val extraOffset = (totalScrollY % itemHeight + itemHeight) % itemHeight
                //已滑动的弧长值
                mOffset = ((circlePosition - itemsVisible / 2) * itemHeight - extraOffset).toInt()

                if ((System.currentTimeMillis() - startTime) > 120) {
                    // 处理拖拽事件
                    smoothScroll(ACTION.DRAG)
                } else {
                    // 处理条目点击事件
                    smoothScroll(ACTION.CLICK)
                }
            }

            else -> if (!eventConsumed) {
                val y = event.getY()
                val L = acos(((radius - y) / radius).toDouble()) * radius
                val circlePosition = ((L + itemHeight / 2) / itemHeight).toInt()
                val extraOffset = (totalScrollY % itemHeight + itemHeight) % itemHeight
                mOffset = ((circlePosition - itemsVisible / 2) * itemHeight - extraOffset).toInt()

                if ((System.currentTimeMillis() - startTime) > 120) {
                    smoothScroll(ACTION.DRAG)
                } else {
                    smoothScroll(ACTION.CLICK)
                }
            }
        }
        if (!isIgnore && event.action != MotionEvent.ACTION_DOWN) {
            invalidate()
        }
        return true
    }

    fun getItemsCount(): Int {
        return adapter?.getItemsCount() ?: 0
    }

    fun setLabel(label: String?) {
        this.label = label
    }

    fun isCenterLabel(isCenterLabel: Boolean) {
        this.isCenterLabel = isCenterLabel
    }

    fun setGravity(gravity: Int) {
        this.mGravity = gravity
    }

    fun getTextWidth(paint: Paint, str: String?): Int { //calculate text width
        var iRet = 0
        if (str != null && str.isNotEmpty()) {
            val len = str.length
            val widths = FloatArray(len)
            paint.getTextWidths(str, widths)
            for (j in 0..<len) {
                iRet += ceil(widths[j].toDouble()).toInt()
            }
        }
        return iRet
    }

    fun setIsOptions(options: Boolean) {
        isOptions = options
    }

    fun setTextColorOut(textColorOut: Int) {
        this.textColorOut = textColorOut
        paintOuterText.setColor(this.textColorOut)
    }

    fun setTextColorCenter(textColorCenter: Int) {
        this.textColorCenter = textColorCenter
        paintCenterText.setColor(this.textColorCenter)
    }

    fun setTextXOffset(textXOffset: Int) {
        this.textXOffset = textXOffset
        if (textXOffset != 0) {
            paintCenterText.textScaleX = 1.0f
        }
    }

    fun setDividerWidth(dividerWidth: Int) {
        this.dividerWidth = dividerWidth
        paintIndicator.strokeWidth = dividerWidth.toFloat()
    }

    fun setDividerColor(dividerColor: Int) {
        this.dividerColor = dividerColor
        paintIndicator.setColor(dividerColor)
    }

    fun setDividerType(dividerType: DividerType?) {
        this.dividerType = dividerType
    }

    fun setLineSpacingMultiplier(lineSpacing: Float) {
        if (lineSpacing != Constants.ZERO.toFloat() && lineSpacing != lineSpacingMultiplier) {
            lineSpacingMultiplier = lineSpacing
            updateLineSpace()
        }
    }

    fun isLoop(): Boolean {
        return isLoop
    }

    fun getTotalScrollY(): Float {
        return totalScrollY
    }

    fun setTotalScrollY(totalScrollY: Float) {
        this.totalScrollY = totalScrollY
    }

    fun getItemHeight(): Float {
        return itemHeight
    }

    fun getInitPosition(): Int {
        return initPosition
    }

    override fun getHandler(): Handler? {
        return handler
    }

    companion object {
        private val TIME_NUM =
            arrayOf<String>("00", "01", "02", "03", "04", "05", "06", "07", "08", "09")

        // 修改这个值可以改变滑行速度
        private const val VELOCITY_FLING = 5
        private const val SCALE_CONTENT = 0.8f //非中间文字则用此控制高度，压扁形成3d错觉
    }
}