package com.wanwuzhinan.mingchang.adapter

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import com.ad.img_load.glide.manager.GlideImgManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.SubjectListData


class AudioHomeListAdapter : BaseQuickAdapter<SubjectListData.lessonBean,QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: SubjectListData.lessonBean?) {

        GlideImgManager.get().loadImg(item!!.image,holder.getView(R.id.riv_head),R.drawable.img_default_icon)
////        GlideImgManager.get().loadImg(item.image,holder.getView(R.id.riv_head),R.drawable.img_default_icon)
//        holder.setText(R.id.tv_title,"共${item.lessonCount}节")
        holder.setText(R.id.tv_title,item!!.name)
            .setGone(R.id.iv_state,playId != item.id.toInt())
            .setTextColor(R.id.tv_title,if (playId != item.id.toInt()) Color.parseColor("#333333") else Color.parseColor("#FE9324"))
            .setBackgroundResource(R.id.ll_root,if (playId != item.id.toInt()) R.drawable.shape_ffffff_8 else R.drawable.shape_ff8585_line_1_8)
    }

    var playId = 0

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_audio_home_list, parent)
    }

}