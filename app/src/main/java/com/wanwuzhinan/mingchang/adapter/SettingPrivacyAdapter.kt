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


class SettingPrivacyAdapter : BaseQuickAdapter<SettingData,QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: SettingData?) {
        holder.setText(R.id.tv_title,item?.title)
            .setText(R.id.tv_sub_title,item?.subTitle)
    }

    private fun getColor(color: Int): ColorStateList {
        val colorStateList = ColorStateList.valueOf(context.resources.getColor(color))
        return colorStateList
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_setting_privacy, parent)
    }

}