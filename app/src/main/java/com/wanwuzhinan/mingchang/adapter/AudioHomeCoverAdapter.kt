package com.wanwuzhinan.mingchang.adapter

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.colin.library.android.image.glide.GlideImgManager
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.SubjectListData


class AudioHomeCoverAdapter : BaseQuickAdapter<SubjectListData,QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: SubjectListData?) {
        GlideImgManager.get().loadImg(item!!.image,holder.getView(R.id.riv_image),R.drawable.img_default_icon)
        holder.setText(R.id.tv_num,"${position+1}")
            .setText(R.id.tv_subtitle,item.lesson_subject_name)
            .setText(R.id.tv_title,item.name)
            .setBackgroundResource(R.id.ll_cover,if (selectIndex == position) R.drawable.shape_ffffff_8 else R.drawable.shape_clear)
    }

    var selectIndex = 0

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_audio_home_cover, parent)
    }

}