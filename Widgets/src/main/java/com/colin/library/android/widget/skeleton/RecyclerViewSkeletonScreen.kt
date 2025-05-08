package com.colin.library.android.widget.skeleton

import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.colin.library.android.utils.ResourcesUtil
import com.colin.library.android.widget.R
import java.lang.ref.WeakReference

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-01 13:28
 *
 * Des   :RecyclerViewSkeletonScreen
 */
class RecyclerViewSkeletonScreen private constructor(builder: Builder) : ISkeletonScreen {
    private val actualViewRef: WeakReference<RecyclerView?> =
        WeakReference<RecyclerView?>(builder.view)
    private val actualAdapter: RecyclerView.Adapter<*>?
    private val skeletonAdapter: SkeletonAdapter
    private val frozen: Boolean

    init {
        frozen = builder.frozen
        actualAdapter = builder.itemAdapter
        skeletonAdapter = SkeletonAdapter().apply {
            itemCount = builder.itemCount
            setLayoutId(builder.itemResId)
            setLayoutIds(builder.itemsResIds)
            shimmer(builder.shimmer)
            setShimmerColor(builder.shimmerColor)
            setShimmerAngle(builder.shimmerAngle)
            setShimmerDuration(builder.shimmerDuration)
        }

    }

    override fun show() {
        val recyclerView = actualViewRef.get() ?: return
        recyclerView.adapter = skeletonAdapter
        if (!recyclerView.isComputingLayout && frozen) {
            recyclerView.suppressLayout(true)
        }
    }

    override fun hide() {
        val recyclerView = actualViewRef.get() ?: return
        recyclerView.adapter = actualAdapter
    }

    class Builder(val view: RecyclerView) {
        var itemAdapter: RecyclerView.Adapter<*>? = null
        var shimmer = true
        var itemCount = 10
        var itemResId: Int = R.layout.item_skeleton
        var itemsResIds: IntArray? = null
        var shimmerColor: Int
        var shimmerDuration = 1000
        var shimmerAngle = 20
        var frozen = true

        init {
            this.shimmerColor = ResourcesUtil.getColor(view.context, R.color.skeleton_shimmer_light)
        }

        /**
         * @param adapter the target recyclerView actual adapter
         */
        fun adapter(adapter: RecyclerView.Adapter<*>?): Builder {
            this.itemAdapter = adapter
            return this
        }

        /**
         * @param itemCount the child item count in recyclerView
         */
        fun count(count: Int): Builder {
            this.itemCount = count
            return this
        }

        /**
         * @param shimmer whether show shimmer animation
         */
        fun shimmer(shimmer: Boolean): Builder {
            this.shimmer = shimmer
            return this
        }

        /**
         * the duration of the animation , the time it will take for the highlight to move from one end of the layout
         * to the other.
         *
         * @param shimmerDuration Duration of the shimmer animation, in milliseconds
         */
        fun duration(duration: Int): Builder {
            this.shimmerDuration = duration
            return this
        }

        /**
         * @param shimmerColor the shimmer color
         */
        fun color(@ColorRes colorRes: Int): Builder {
            this.shimmerColor = ResourcesUtil.getColor(view.context, colorRes)
            return this
        }

        /**
         * @param shimmerAngle the angle of the shimmer effect in clockwise direction in degrees.
         */
        fun angle(@IntRange(from = -45, to = 45) angle: Int): Builder {
            this.shimmerAngle = angle
            return this
        }

        /**
         * @param skeletonLayoutResID the loading skeleton layoutResID
         */
        fun itemResId(@LayoutRes layoutRes: Int): Builder {
            this.itemResId = layoutRes
            return this
        }

        /**
         * @param arrayRes the loading array of skeleton layoutResID
         */
        fun itemResIds(@ArrayRes arrayRes: Int): Builder {
            this.itemsResIds = ResourcesUtil.getIntArray(view.context, arrayRes)
            return this
        }

        /**
         * @param frozen whether frozen recyclerView during skeleton showing
         * @return
         */
        fun frozen(frozen: Boolean): Builder {
            this.frozen = frozen
            return this
        }

        fun build(): RecyclerViewSkeletonScreen {
            return RecyclerViewSkeletonScreen(this)
        }
    }
}
