package com.wanwuzhinan.mingchang.adapter

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.SubjectListData
import com.wanwuzhinan.mingchang.view.DefaultEvaluate

class VideoListLeftPadAdapter : BaseQuickAdapter<SubjectListData,QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: SubjectListData?) {
        holder.setBackgroundResource(R.id.cl_bg,if(item!!.select) R.drawable.shape_dae3fc_select_30 else R.drawable.shape_dae3fc_30)
        holder.setText(R.id.tv_name,item.name)
        holder.setText(R.id.tv_number,"共${item.lessonCount}节")

        var evaluate=holder.getView<DefaultEvaluate>(R.id.evaluate)
        evaluate.setEvaluate(item.lessonCountStar)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_video_list_left_pad, parent)
    }

}