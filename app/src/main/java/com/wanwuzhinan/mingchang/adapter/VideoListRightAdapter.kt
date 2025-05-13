package com.wanwuzhinan.mingchang.adapter

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.colin.library.android.image.glide.GlideImgManager
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.entity.Lesson


class VideoListRightAdapter : BaseQuickAdapter<Lesson,QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: Lesson?) {
        GlideImgManager.get().loadImg(item!!.image,holder.getView(R.id.riv_head),R.drawable.img_default_icon)
        holder.setText(R.id.tv_name,item.name)

        var size=position+1
        holder.setGone(R.id.iv_empty,size%3==0||position==items.size-1)
        holder.setImageResource(R.id.iv_ball,if(item.study_is_success==1) R.mipmap.ic_ball_night else R.mipmap.ic_ball_unnight)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_video_list_right, parent)
    }

}