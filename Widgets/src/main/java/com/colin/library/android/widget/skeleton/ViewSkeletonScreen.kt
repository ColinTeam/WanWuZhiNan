package com.colin.library.android.widget.skeleton

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.ResourcesUtil
import com.colin.library.android.widget.Constants
import com.colin.library.android.widget.R
import java.lang.ref.WeakReference

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-01 12:44
 *
 * Des   :ViewSkeletonScreen
 */
class ViewSkeletonScreen private constructor(builder: Builder) : ISkeletonScreen {
    private val actualViewRef: WeakReference<View?> = WeakReference<View?>(builder.view)
    private val viewReplacer: ViewReplacer = ViewReplacer(builder.view)
    private val skeletonResID: Int = builder.layoutResID
    private val shimmerColor: Int = builder.shimmerColor
    private val shimmer: Boolean = builder.shimmer
    private val shimmerDuration: Int = builder.shimmerDuration
    private val shimmerAngle: Int = builder.shimmerAngle

    private fun generateShimmerContainerLayout(parentView: ViewGroup): ShimmerLayout? {
        val actualView = actualViewRef.get() ?: return null
        val shimmerLayout: ShimmerLayout = LayoutInflater.from(actualView.context).inflate(
            R.layout.layout_shimmer, parentView, false
        ) as ShimmerLayout
        shimmerLayout.setShimmerColor(shimmerColor)
        shimmerLayout.setShimmerAngle(shimmerAngle)
        shimmerLayout.setShimmerAnimationDuration(shimmerDuration)
        val innerView =
            LayoutInflater.from(actualView.context).inflate(skeletonResID, shimmerLayout, false)
        innerView.layoutParams?.let { shimmerLayout.setLayoutParams(it) }
        shimmerLayout.addView(innerView)
        shimmerLayout.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                shimmerLayout.startShimmerAnimation()
            }

            override fun onViewDetachedFromWindow(v: View) {
                shimmerLayout.stopShimmerAnimation()
            }
        })
        shimmerLayout.startShimmerAnimation()
        return shimmerLayout
    }

    private fun generateSkeletonLoadingView(): View? {
        val viewParent = actualViewRef.get()?.parent
        if (viewParent == null) {
            Log.d("the source view have not attach to any view")
            return null
        }
        val parentView = viewParent as ViewGroup
        if (shimmer) {
            return generateShimmerContainerLayout(parentView)
        }
        return LayoutInflater.from(parentView.context).inflate(skeletonResID, parentView, false)
    }

    override fun show() {
        val skeletonLoadingView = generateSkeletonLoadingView()
        if (skeletonLoadingView != null) {
            viewReplacer.replace(skeletonLoadingView)
        }
    }

    override fun hide() {
        if (viewReplacer.getTargetView() is ShimmerLayout) {
            (viewReplacer.getTargetView() as ShimmerLayout).stopShimmerAnimation()
        }
        viewReplacer.restore()
    }

    class Builder(val view: View) {
        var layoutResID = com.colin.library.android.utils.Constants.INVALID
        var shimmer = true
        var shimmerDuration = com.colin.library.android.utils.Constants.ONE_SECOND
        var shimmerAngle = Constants.SHIMMER_ANGLE
        var shimmerColor: Int

        init {
            shimmerColor = ResourcesUtil.getColor(view.context, R.color.skeleton_shimmer_light)
        }

        fun load(@LayoutRes layoutRes: Int): Builder {
            this.layoutResID = layoutRes
            return this
        }

        fun color(@ColorRes colorRes: Int): Builder {
            this.shimmerColor = ResourcesUtil.getColor(view.context, res = colorRes)
            return this
        }


        fun shimmer(shimmer: Boolean): Builder {
            this.shimmer = shimmer
            return this
        }

        fun duration(duration: Int): Builder {
            this.shimmerDuration = duration
            return this
        }

        fun angle(@IntRange(from = -45, to = 45) angle: Int): Builder {
            this.shimmerAngle = angle
            return this
        }

        fun build(): ViewSkeletonScreen {
            return ViewSkeletonScreen(this)
        }
    }

}
