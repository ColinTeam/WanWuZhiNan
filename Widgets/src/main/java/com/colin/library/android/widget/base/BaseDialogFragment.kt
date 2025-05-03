package com.colin.library.android.widget.base

import android.app.Dialog
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.SystemClock
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.Dimension
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.colin.library.android.utils.Constants
import com.colin.library.android.utils.Log
import com.colin.library.android.widget.R


/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Date  :2024-09-11
 *
 * Des   :Dialog基类:最简单的业务逻辑定义
 */
abstract class BaseDialogFragment(
    private val layoutRes: Int, private val builder: Builder<*, *>? = null
) : DialogFragment(layoutRes), IBase {

    constructor() : this(Constants.ZERO, null)

    constructor(@LayoutRes layoutRes: Int) : this(layoutRes, null)


    val TAG = this::class.simpleName!!

    @StyleRes
    internal var animation: Int = android.R.style.Animation_Dialog

    @Dimension
    internal var width: Int = DEFAULT_DIALOG_WINDOW_WIDTH

    @Dimension
    internal var height: Int = DEFAULT_DIALOG_WINDOW_HEIGHT

    @Dimension
    internal var offsetX: Int = DEFAULT_DIALOG_WINDOW_OFFSET

    @Dimension
    internal var offsetY: Int = DEFAULT_DIALOG_WINDOW_OFFSET

    internal var gravity: Int = DEFAULT_DIALOG_WINDOW_GRAVITY

    init {
        arguments = Bundle().apply {
            putInt(EXTRAS_BASE_WIDTH, builder?.width ?: DEFAULT_DIALOG_WINDOW_WIDTH)
            putInt(EXTRAS_BASE_HEIGHT, builder?.height ?: DEFAULT_DIALOG_WINDOW_HEIGHT)
            putInt(EXTRAS_BASE_OFFSET_X, builder?.offsetX ?: DEFAULT_DIALOG_WINDOW_OFFSET)
            putInt(EXTRAS_BASE_OFFSET_Y, builder?.offsetY ?: DEFAULT_DIALOG_WINDOW_OFFSET)
            putInt(EXTRAS_BASE_GRAVITY, builder?.gravity ?: DEFAULT_DIALOG_WINDOW_GRAVITY)
            putInt(EXTRAS_BASE_ANIMATION, builder?.animation ?: android.R.style.Animation_Dialog)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        builder?.let {
            Log.d(TAG, "onCreate style:${it.style} theme:${it.theme} cancelable:${it.cancelable}")
        }
        setStyle(builder?.style ?: DEFAULT_DIALOG_STYLE, builder?.theme ?: R.style.Base_Dialog)
        isCancelable = builder?.cancelable ?: DEFAULT_DIALOG_CANCELABLE
        parse(savedInstanceState ?: arguments)
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
        Log.d(TAG, "onCreateView layoutRes:${layoutRes()}")
        return if (layoutRes() != Resources.ID_NULL) {
            inflater.inflate(layoutRes(), container, false)
        } else createView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        initView(arguments, savedInstanceState)
        initData(arguments, savedInstanceState)
    }

    override fun onStart() {
        Log.d(TAG, "onStart")
        dialog?.window?.let { initWindow(dialog!!, it) }
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop")
        super.onStop()
    }

    fun show(activity: FragmentActivity?) {
        Log.d(TAG, "show activity.isFinishing:${activity?.isFinishing}")
        activity.takeIf { it?.isFinishing == false }?.let {
            show(it.supportFragmentManager, it::class.java.simpleName)
        }
    }

    fun show(fragment: Fragment?) {
        Log.d(TAG, "show fragment.isAdded:${fragment?.isAdded}")
        fragment.takeIf { it?.isAdded == true }?.let {
            show(it.childFragmentManager, it::class.java.simpleName)
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        val show = isRepeatedShow(tag)
        Log.d(TAG, "show manager:${manager.isDestroyed} isRepeatedShow:$show")
        manager.takeIf { it.isDestroyed.not() && show.not() }?.let {
            super.show(manager, tag)
        }
    }

    override fun show(transaction: FragmentTransaction, tag: String?): Int {
        val show = isRepeatedShow(tag)
        Log.d(TAG, "show isRepeatedShow:$show")
        return if (show.not()) super.show(transaction, tag) else -1
    }

    override fun showNow(manager: FragmentManager, tag: String?) {
        val show = isRepeatedShow(tag)
        Log.d(TAG, "showNow manager:${manager.isDestroyed} isRepeatedShow:$show")
        if (manager.isDestroyed.not() && show.not()) super.showNow(manager, tag)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        parse(savedInstanceState ?: arguments)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putInt(EXTRAS_BASE_WIDTH, width)
            putInt(EXTRAS_BASE_HEIGHT, height)
            putInt(EXTRAS_BASE_OFFSET_X, offsetX)
            putInt(EXTRAS_BASE_OFFSET_Y, offsetY)
            putInt(EXTRAS_BASE_GRAVITY, gravity)
            putInt(EXTRAS_BASE_ANIMATION, animation)
        }
    }

    override fun layoutRes() = layoutRes


    /*设置dialog window 属性*/
    open fun initWindow(dialog: Dialog, window: Window) {
        window.apply {
            // 透明背景
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            decorView.setPadding(0, 0, 0, 0)
            attributes.apply {
                //window 宽度
                width = this@BaseDialogFragment.width
                height = this@BaseDialogFragment.height
                //设置偏移量
                x = this@BaseDialogFragment.offsetX
                y = this@BaseDialogFragment.offsetY
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
                width = it.getInt(EXTRAS_BASE_WIDTH, width)
            }
            if (it.containsKey(EXTRAS_BASE_HEIGHT)) {
                height = it.getInt(EXTRAS_BASE_HEIGHT, height)
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

    companion object {
        const val DEFAULT_DIALOG_STYLE = STYLE_NO_TITLE
        const val DEFAULT_DIALOG_WINDOW_WIDTH = WindowManager.LayoutParams.MATCH_PARENT
        const val DEFAULT_DIALOG_WINDOW_HEIGHT = WindowManager.LayoutParams.MATCH_PARENT
        const val DEFAULT_DIALOG_WINDOW_OFFSET = 0
        const val DEFAULT_DIALOG_WINDOW_GRAVITY = Gravity.CENTER
        const val DEFAULT_DIALOG_CANCELABLE = true

        const val EXTRAS_BASE_WIDTH = "EXTRAS_BASE_WIDTH"
        const val EXTRAS_BASE_HEIGHT = "EXTRAS_BASE_HEIGHT"
        const val EXTRAS_BASE_OFFSET_X = "EXTRAS_BASE_OFFSET_X"
        const val EXTRAS_BASE_OFFSET_Y = "EXTRAS_BASE_OFFSET_Y"
        const val EXTRAS_BASE_GRAVITY = "EXTRAS_BASE_GRAVITY"
        const val EXTRAS_BASE_ANIMATION = "EXTRAS_BASE_ANIMATION"

        private var mShowTag: String? = null
        private var mLastTime: Long = 0
    }


    @Suppress("UNCHECKED_CAST")
    abstract class Builder<Returner, Dialog>(
        val layoutRes: Int = Constants.ZERO,
        val style: Int = DEFAULT_DIALOG_STYLE,
        val theme: Int = R.style.Base_Dialog,
        val cancelable: Boolean = DEFAULT_DIALOG_CANCELABLE
    ) {
        @StyleRes
        internal var animation = android.R.style.Animation_Dialog

        @Dimension
        internal var width = DEFAULT_DIALOG_WINDOW_WIDTH

        @Dimension
        internal var height = DEFAULT_DIALOG_WINDOW_HEIGHT

        @Dimension
        internal var offsetX = DEFAULT_DIALOG_WINDOW_OFFSET

        @Dimension
        internal var offsetY = DEFAULT_DIALOG_WINDOW_OFFSET

        internal var gravity = DEFAULT_DIALOG_WINDOW_GRAVITY


        fun animation(@StyleRes animation: Int): Returner {
            this.animation = animation
            return this as Returner
        }

        fun size(@Dimension width: Int, @Dimension height: Int): Returner {
            this.width = width
            this.height = height
            return this as Returner
        }

        fun offset(@Dimension offsetX: Int, @Dimension offsetY: Int): Returner {
            this.offsetX = offsetX
            this.offsetY = offsetY
            return this as Returner
        }

        fun gravity(gravity: Int): Returner {
            this.gravity = gravity
            return this as Returner
        }

        abstract fun build(): Dialog
    }


}




