package com.wanwuzhinan.mingchang.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import com.ad.img_load.glide.manager.GlideImgManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.SubjectListData


class VideoHomeAdapter : BaseQuickAdapter<SubjectListData.dataBean,QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: SubjectListData.dataBean?) {
        GlideImgManager.get().loadImg(item!!.image,holder.getView(R.id.riv_image),R.drawable.img_default_icon)
        holder.setText(R.id.tv_number,"共${item.lessonCount}节")
        holder.setText(R.id.tv_first,item.name)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_video_home, parent)
    }

    fun getViewByPosition(position: Int, @IdRes viewId: Int): View? {
        val viewHolder = recyclerView.findViewHolderForLayoutPosition(position) as QuickViewHolder? ?: return null
        return viewHolder.getViewOrNull(viewId)
    }
}