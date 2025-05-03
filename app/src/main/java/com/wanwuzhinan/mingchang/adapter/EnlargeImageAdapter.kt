package com.wanwuzhinan.mingchang.adapter

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.colin.library.android.image.glide.GlideImgManager
import com.wanwuzhinan.mingchang.R

//放大图片适配器
class EnlargeImageAdapter : BaseQuickAdapter<String, QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: String?) {
        GlideImgManager.get().loadDefaultImg(item,holder.getView(R.id.image),R.drawable.img_default_icon)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_enlarge_image, parent)
    }

}