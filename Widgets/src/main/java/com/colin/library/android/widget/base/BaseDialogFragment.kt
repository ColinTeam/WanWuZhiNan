package com.colin.library.android.widget.base

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.Dimension
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.colin.library.android.utils.Constants
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.ResourcesUtil
import com.colin.library.android.widget.R
import kotlin.math.abs


/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Date  :2024-09-11
 *
 * Des   :Dialog基类:最简单的业务逻辑定义
 */
abstract class BaseDialogFragment(
    private val layoutRes: Int = Constants.ZERO,
    @IntRange(0, 3) private val style: Int = STYLE_NO_TITLE,
    private val theme: Int = R.style.Base_Dialog
) : DialogFragment(layoutRes), IBase {

    constructor() : this(Constants.ZERO)

    @StyleRes
    var animation: Int = android.R.style.Animation_Dialog

    var width: Float = DEFAULT_DIALOG_WINDOW_WIDTH

    var height: Float = DEFAULT_DIALOG_WINDOW_HEIGHT

    @Dimension
    var offsetX: Int = DEFAULT_DIALOG_WINDOW_OFFSET

    @Dimension
    var offsetY: Int = DEFAULT_DIALOG_WINDOW_OFFSET

    var gravity: Int = DEFAULT_DIALOG_WINDOW_GRAVITY

    @FloatRange(from = 0.0, to = 1.0)
    var windowAmount = DEFAULT_WINDOW_AMOUNT

    override fun onCreate(savedInstanceState: Bundle?) {
        parse(savedInstanceState ?: arguments)
        setStyle(style, theme)
        super.onCreate(savedInstanceState)
    }


    /**
     * dialog 布局两种实现方式
     * 1. builder构建的时候，layoutRes传布局id
     * 2.实现createView方法
     *     override fun createView(
     *         inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
     *     ): View {
     *         _viewBinding = reflectViewBinding(inflater, container)
     *         return viewBinding.root
     *     }
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Log.d("onCreateView layoutRes:${layoutRes()}")
        return if (layoutRes() != Constants.ZERO) {
            inflater.inflate(layoutRes(), container, false)
        } else createView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        initView(arguments, savedInstanceState)
        initData(arguments, savedInstanceState)
    }


    override fun onStart() {
        Log.d("onStart")
        dialog?.window?.let { initWindow(dialog!!, it) }
        super.onStart()
    }

    override fun onResume() {
        Log.d("onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d("onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d("onStop")
        super.onStop()
    }

    fun show(activity: FragmentActivity?) {
        Log.d("show activity.isFinishing:${activity?.isFinishing} isShowing:${isShowing()}")
        activity.takeIf { it?.isFinishing == false }?.let {
            show(it.supportFragmentManager, it::class.java.simpleName)
        }
    }

    fun show(fragment: Fragment?) {
        Log.d("show fragment.isAdded:${fragment?.isAdded} isShowing:${isShowing()}")
        fragment.takeIf { it?.isAdded == true }?.let {
            show(it.childFragmentManager, it::class.java.simpleName)
        }
    }

    fun isShowing(): Boolean = dialog?.isShowing == true


    override fun show(manager: FragmentManager, tag: String?) {
        val show = isRepeatedShow(tag)
        Log.d("show manager:${manager.isDestroyed} isRepeatedShow:$show isShowing:${isShowing()}")
        manager.takeIf { it.isDestroyed.not() && show.not() }?.let {
            super.show(manager, tag)
        }
    }

    override fun show(transaction: FragmentTransaction, tag: String?): Int {
        val show = isRepeatedShow(tag)
        Log.d("show isRepeatedShow:$show isShowing:${isShowing()}")
        return if (show.not()) super.show(transaction, tag) else -1
    }

    override fun showNow(manager: FragmentManager, tag: String?) {
        val show = isRepeatedShow(tag)
        Log.d("showNow manager:${manager.isDestroyed} isRepeatedShow:$show isShowing:${isShowing()}")
        if (manager.isDestroyed.not() && show.not()) super.showNow(manager, tag)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        parse(savedInstanceState ?: arguments)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putFloat(EXTRAS_BASE_WIDTH, width)
            putFloat(EXTRAS_BASE_HEIGHT, height)
            putInt(EXTRAS_BASE_OFFSET_X, offsetX)
            putInt(EXTRAS_BASE_OFFSET_Y, offsetY)
            putInt(EXTRAS_BASE_GRAVITY, gravity)
            putInt(EXTRAS_BASE_ANIMATION, animation)
            putFloat(EXTRAS_WINDOW_AMOUNT, windowAmount)
        }
    }

    override fun layoutRes() = layoutRes


    /*设置dialog window 属性*/
    open fun initWindow(dialog: Dialog, window: Window) {
        window.apply {
            // 透明背景
            setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            decorView.setPadding(0, 0, 0, 0)
            attributes.apply {
                //window 宽度
                val metrics = ResourcesUtil.getResources(requireContext()).displayMetrics
                width = getDisplaySize(metrics.widthPixels, this@BaseDialogFragment.width)
                height = getDisplaySize(metrics.heightPixels, this@BaseDialogFragment.height)
                //设置偏移量
                x = this@BaseDialogFragment.offsetX
                y = this@BaseDialogFragment.offsetY
                //亮度
                dimAmount = this@BaseDialogFragment.windowAmount
                //设置对齐方式
                gravity = this@BaseDialogFragment.gravity
                //设置动画
                windowAnimations = this@BaseDialogFragment.animation
            }
        }
    }

    /*重写布局View*/
    abstract fun createView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View

    private fun parse(bundle: Bundle?) {
        bundle?.let {
            if (it.containsKey(EXTRAS_BASE_ANIMATION)) {
                animation = it.getInt(EXTRAS_BASE_ANIMATION, animation)
            }
            if (it.containsKey(EXTRAS_BASE_WIDTH)) {
                width = it.getFloat(EXTRAS_BASE_WIDTH, width)
            }
            if (it.containsKey(EXTRAS_BASE_HEIGHT)) {
                height = it.getFloat(EXTRAS_BASE_HEIGHT, height)
            }
            if (it.containsKey(EXTRAS_BASE_OFFSET_X)) {
                offsetX = it.getInt(EXTRAS_BASE_OFFSET_X, offsetX)
            }
            if (it.containsKey(EXTRAS_BASE_OFFSET_Y)) {
                offsetY = it.getInt(EXTRAS_BASE_OFFSET_Y, offsetY)
            }
            if (it.containsKey(EXTRAS_BASE_GRAVITY)) {
                gravity = it.getInt(EXTRAS_BASE_GRAVITY, DEFAULT_DIALOG_WINDOW_GRAVITY)
            }
            if (it.containsKey(EXTRAS_WINDOW_AMOUNT)) {
                windowAmount = it.getFloat(EXTRAS_WINDOW_AMOUNT, DEFAULT_WINDOW_AMOUNT)
            }
        }
    }

    /**
     * 根据 tag 判断这个 Dialog 是否重复显示了
     *
     * @param tag Tag标记
     */
    private fun isRepeatedShow(tag: String?): Boolean {
        val result = tag == mShowTag && SystemClock.uptimeMillis() - mLastTime < 500
        mShowTag = tag
        mLastTime = SystemClock.uptimeMillis()
        return result
    }

    private fun getDisplaySize(total: Int, size: Float): Int {
        return if (abs(size) == 1.0f) {
            WindowManager.LayoutParams.MATCH_PARENT
        } else if (abs(size) == 2.0f) {
            WindowManager.LayoutParams.WRAP_CONTENT
        } else (total * size).toInt()
    }

    companion object {
        const val DEFAULT_DIALOG_WINDOW_WIDTH = 1.0F
        const val DEFAULT_DIALOG_WINDOW_HEIGHT = 1.0F
        const val DEFAULT_DIALOG_WINDOW_OFFSET = 0
        const val DEFAULT_DIALOG_WINDOW_GRAVITY = Gravity.CENTER
        const val DEFAULT_WINDOW_AMOUNT = 0.6F

        const val EXTRAS_BASE_WIDTH = "EXTRAS_BASE_WIDTH"
        const val EXTRAS_BASE_HEIGHT = "EXTRAS_BASE_HEIGHT"
        const val EXTRAS_BASE_OFFSET_X = "EXTRAS_BASE_OFFSET_X"
        const val EXTRAS_BASE_OFFSET_Y = "EXTRAS_BASE_OFFSET_Y"
        const val EXTRAS_BASE_GRAVITY = "EXTRAS_BASE_GRAVITY"
        const val EXTRAS_BASE_ANIMATION = "EXTRAS_BASE_ANIMATION"
        const val EXTRAS_WINDOW_AMOUNT = "EXTRAS_WINDOW_AMOUNT"

        private var mShowTag: String? = null
        private var mLastTime: Long = 0
    }
}




