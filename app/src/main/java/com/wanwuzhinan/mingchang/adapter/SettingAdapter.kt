package com.wanwuzhinan.mingchang.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.colin.library.android.utils.ResourcesUtil
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.SettingData


class SettingAdapter : BaseQuickAdapter<SettingData, QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: SettingData?) {
        item?.let {
            holder.getView<ImageView>(R.id.image).apply {
                this.imageTintList = ResourcesUtil.getColorList(
                    context,
                    if (it.select) R.color.white else R.color.color_ff9424
                )
                this.setImageResource(it.image)
            }
            holder.setText(R.id.tv_title, it.title)
            val color = if (it.select) R.color.white else R.color.black
            holder.setTextColor(R.id.tv_title, ResourcesUtil.getColor(context, color))
            holder.setBackgroundResource(
                R.id.lin, if (it.select) R.drawable.shape_ff6e29_top_10 else R.drawable.shape_ffddb9_top_10
            )
        }
    }


    override fun onCreateViewHolder(
        context: Context, parent: ViewGroup, viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_setting, parent)
    }

}