package com.wanwuzhinan.mingchang.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.colin.library.android.widget.skeleton.ISkeletonScreen
import com.colin.library.android.widget.skeleton.RecyclerViewSkeletonScreen
import com.colin.library.android.widget.skeleton.ViewSkeletonScreen
import com.wanwuzhinan.mingchang.R

object SkeletonUtils {

    var mSkeletonList: RecyclerViewSkeletonScreen? = null
    var mSkeletonView: ISkeletonScreen? = null

    fun showList(view: RecyclerView, adapter: Adapter<RecyclerView.ViewHolder>, layout: Int) {
        mSkeletonList =
            bind(view).adapter(adapter).frozen(false).color(R.color.shimmer_color).itemResId(layout)
                .build()
        mSkeletonList?.show()
    }

    fun showView(view: View, layout: Int) {
        mSkeletonView = bind(view).color(R.color.shimmer_color).load(layout).build()
        mSkeletonView?.show()
    }

    fun hideList() {
        mSkeletonList?.hide()
    }

    fun hideView() {
        mSkeletonView?.hide()
    }


    private fun bind(recyclerView: RecyclerView): RecyclerViewSkeletonScreen.Builder {
        return RecyclerViewSkeletonScreen.Builder(recyclerView)
    }

    private fun bind(view: View): ViewSkeletonScreen.Builder {
        return ViewSkeletonScreen.Builder(view)
    }
}