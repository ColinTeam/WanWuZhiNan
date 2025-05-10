package com.wanwuzhinan.mingchang.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.SettingData


class SettingAdapter : BaseQuickAdapter<SettingData, QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: SettingData?) {
        item?.let {
            holder.getView<View>(R.id.lin).isSelected = it.select
            holder.getView<ImageView>(R.id.image).apply {
                this.isSelected = it.select
                this.setImageResource(it.image)
            }
            holder.getView<TextView>(R.id.tv_title).apply {
                this.isSelected = it.select
                this.text = it.title
            }

        }
    }


    override fun onCreateViewHolder(
        context: Context, parent: ViewGroup, viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_setting, parent)
    }

}