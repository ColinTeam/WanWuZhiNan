package com.colin.library.android.widget.skeleton

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.colin.library.android.widget.R

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-01 13:13
 *
 * Des   :ShimmerViewHolder
 */
class ShimmerViewHolder(inflater: LayoutInflater, parent: ViewGroup?, @LayoutRes layoutRes: Int) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.layout_shimmer, parent, false)) {
    init {
        val layout = itemView as ViewGroup
        val view = inflater.inflate(layoutRes, layout, false)
        view.layoutParams?.let { layout.setLayoutParams(it) }
        layout.addView(view)
    }
}