package com.wanwuzhinan.mingchang.adapter

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.entity.LessonSubject


class CatePadAdapter : BaseQuickAdapter<LessonSubject,QuickViewHolder>() {

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: LessonSubject?) {
        holder.setBackgroundResource(R.id.ll_root,if(curPage == position) R.drawable.shape_ffffff_12_line_1 else R.drawable.shape_ffc285_12_line_1)
            .setText(R.id.tv_title,item?.name)
            .setTextColor(R.id.tv_title,if(curPage == position) Color.parseColor("#ffffff") else Color.parseColor("#FF9424"))
            .setGone(R.id.iv_state,curPage != position)
            .setImageResource(R.id.iv_star_state,if(curPage == position) R.mipmap.ic_mj_star_n else R.mipmap.ic_mj_star_s)
    }

    var curPage = 0

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_cate_list_pad, parent)
    }

}