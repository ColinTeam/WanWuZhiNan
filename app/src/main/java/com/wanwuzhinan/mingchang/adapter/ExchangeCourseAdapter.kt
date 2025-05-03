package com.wanwuzhinan.mingchang.adapter

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.ExchangeCodeData
import com.wanwuzhinan.mingchang.ext.convertTimestampToDateTime


class ExchangeCourseAdapter : BaseQuickAdapter<ExchangeCodeData,QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: ExchangeCodeData?) {
        holder.setText(R.id.tv_name,item!!.activity_name)
        holder.setText(R.id.tv_exchange_time,"兑换时间：${convertTimestampToDateTime(item!!.user_start)}")
        holder.setText(R.id.tv_effective_time,"有效期至：${convertTimestampToDateTime(item!!.user_end)}")
            .setText(R.id.tv_give,if(item.is_goods==1)"有赠品" else "无赠品")
            .setGone(R.id.tv_check,if(item.is_goods==1) false else true)
            .setGone(R.id.tv_give,if(item.is_goods==1) false else true)
            .setGone(R.id.tv_class_give,if(item.is_goods==1) false else true)
            .setGone(R.id.tv_code_state,if(item.statusName.isEmpty()) true else false)
            .setText(R.id.tv_code_state,item.statusName)

    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_exchange_course, parent)
    }

}