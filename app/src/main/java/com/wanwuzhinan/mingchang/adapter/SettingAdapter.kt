package com.wanwuzhinan.mingchang.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.ViewGroup
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.SettingData


class SettingAdapter : BaseQuickAdapter<SettingData,QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: SettingData?) {
        holder.setImageResource(R.id.image,item!!.image)
        holder.setText(R.id.tv_title,item.title)
        holder.setTextColor(R.id.tv_title,if(item.select) context.resources.getColor(R.color.white) else context.resources.getColor(R.color.black))

        var image=holder.getView<ImageView>(R.id.image)
        image.imageTintList=if(item.select)  getColor(R.color.white) else getColor(R.color.color_ff9424)

        holder.setBackgroundResource(R.id.lin,if(item.select) R.drawable.shape_ff6e29_top_10 else R.drawable.shape_ffddb9_top_10 )
    }

    private fun getColor(color: Int): ColorStateList {
        val colorStateList = ColorStateList.valueOf(context.resources.getColor(color))
        return colorStateList
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_setting, parent)
    }

}