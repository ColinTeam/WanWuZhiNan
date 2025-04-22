package com.wanwuzhinan.mingchang.adapter

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.SubjectListData
import com.wanwuzhinan.mingchang.view.DefaultEvaluate

class VideoListLeftAdapter : BaseQuickAdapter<SubjectListData,QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: SubjectListData?) {
        holder.setBackgroundResource(R.id.cl_bg,if(item!!.select) R.drawable.shape_ffcb48_line_2 else R.drawable.shape_fff1e3_line_2)
        holder.setText(R.id.tv_name,item.name)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_video_list_left, parent)
    }

}