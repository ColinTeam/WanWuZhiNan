package com.wanwuzhinan.mingchang.adapter

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.colin.library.android.image.glide.GlideImgManager
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.SubjectListData


class AudioHomeAdapter : BaseQuickAdapter<SubjectListData.dataBean,QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: SubjectListData.dataBean?) {
        GlideImgManager.get().loadImg(item!!.image,holder.getView(R.id.riv_image),R.drawable.img_default_icon)
        holder.setText(R.id.tv_first,item.name)
        holder.setText(R.id.tv_number,"共${item.lessonCount}节")
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_audio_home, parent)
    }

}