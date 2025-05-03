package com.wanwuzhinan.mingchang.adapter

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.colin.library.android.image.glide.GlideImgManager
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.MedalListData


class HonorCardAdapter : BaseQuickAdapter<MedalListData,QuickViewHolder>() {

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: MedalListData?) {
        GlideImgManager.get().loadImg(if( item!!.is_has == 1) item.image_selected else item.image ,holder.getView(R.id.image),R.drawable.img_default_icon)
        holder.setText(R.id.tv_name,item.name)
            .setGone(R.id.tv_no_have,item.is_has.toInt() == 1)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_honor_card, parent)
    }

}