package com.colin.library.android.widget.skeleton

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.colin.library.android.utils.Constants

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-01 13:17
 *
 * Des   :SkeletonAdapter
 */
class SkeletonAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private var count = Constants.ZERO
    private var layoutId = Constants.ZERO
    private var layoutIds: IntArray? = null
    private var shimmer = false
    private var shimmerColor = 0
    private var shimmerDuration = 0
    private var shimmerAngle = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if (doesArrayOfLayoutsExist()) layoutId = viewType

        if (shimmer) return ShimmerViewHolder(inflater, parent, layoutId)

        return object : RecyclerView.ViewHolder(inflater.inflate(layoutId, parent, false)) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (shimmer) {
            val layout: ShimmerLayout = holder.itemView as ShimmerLayout
            layout.setAnimationDuration(shimmerDuration.toLong())
            layout.setShimmerAngle(shimmerAngle)
            layout.setShimmerColor(shimmerColor)
            layout.startShimmerAnimation()
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (doesArrayOfLayoutsExist()) {
            return getCorrectLayoutItem(position)
        }
        return super.getItemViewType(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return count
    }

    fun setLayoutId(@LayoutRes layoutId: Int) {
        this.layoutId = layoutId
    }

    fun setLayoutIds(arrays: IntArray?) {
        this.layoutIds = arrays
    }

    fun setItemCount(count: Int) {
        this.count = count
    }

    fun setShimmerColor(@ColorInt color: Int) {
        this.shimmerColor = color
    }

    fun shimmer(shimmer: Boolean) {
        this.shimmer = shimmer
    }

    fun setShimmerDuration(duration: Int) {
        this.shimmerDuration = duration
    }

    fun setShimmerAngle(@IntRange(from = -45, to = 45) shimmerAngle: Int) {
        this.shimmerAngle = shimmerAngle
    }

    fun getCorrectLayoutItem(position: Int): Int {
        if (doesArrayOfLayoutsExist()) {
            if (layoutIds != null) {
                return layoutIds!![position % layoutIds!!.size]
            }
        }
        return layoutId
    }

    private fun doesArrayOfLayoutsExist(): Boolean {
        if (layoutIds != null && layoutIds != null) {
            if (layoutIds!!.isNotEmpty()) return true
        }
        return false
    }
}
