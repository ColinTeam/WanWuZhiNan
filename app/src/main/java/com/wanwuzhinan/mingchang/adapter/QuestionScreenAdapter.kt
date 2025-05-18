package com.wanwuzhinan.mingchang.adapter

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.QuestionScreenData


class QuestionScreenAdapter : BaseQuickAdapter<QuestionScreenData?,QuickViewHolder>() {


    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: QuestionScreenData?) {

        holder.setText(R.id.tv_title,"第${item!!.page+1}节")
            .setSelected(R.id.tv_title, item.isSelect)

    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_question_screen, parent)
    }


    var mListener:OnScreenListener? = null

    fun setOnAnswerListener(listener: OnScreenListener) {
        mListener = listener
    }


    interface OnScreenListener {
        fun click(answerIndex:Int)
    }
}