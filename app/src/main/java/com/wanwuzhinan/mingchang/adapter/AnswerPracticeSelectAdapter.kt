package com.wanwuzhinan.mingchang.adapter

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.wanwuzhinan.mingchang.R


class AnswerPracticeSelectAdapter : BaseQuickAdapter<Boolean,QuickViewHolder>() {

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: Boolean?) {
        holder.setBackgroundResource(R.id.iv_select,if(item!!) R.drawable.shape_ff7e47_18 else R.drawable.shape_eeeeee_22)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_answer_practice, parent)
    }

}