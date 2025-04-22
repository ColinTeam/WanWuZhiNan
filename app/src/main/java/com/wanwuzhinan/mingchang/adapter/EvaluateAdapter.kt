package com.wanwuzhinan.mingchang.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.IdRes
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.wanwuzhinan.mingchang.R

//评价
class EvaluateAdapter : BaseQuickAdapter<Boolean,QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: Boolean?) {
        var star=holder.getView<ImageView>(R.id.iv_evaluate)
        star.alpha=if(item!!) 1f else 0.4f
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_evaluate, parent)
    }

    fun getViewByPosition(position: Int, @IdRes viewId: Int): View? {
        val viewHolder = recyclerView.findViewHolderForLayoutPosition(position) as QuickViewHolder? ?: return null
        return viewHolder.getViewOrNull(viewId)
    }
}