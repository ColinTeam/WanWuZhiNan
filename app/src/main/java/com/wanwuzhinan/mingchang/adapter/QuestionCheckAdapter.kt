package com.wanwuzhinan.mingchang.adapter

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ad.img_load.glide.manager.GlideImgManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.util.setOnDebouncedItemClick
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.comm.banner.listener.OnBannerListener
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.QuestionListData
import com.wanwuzhinan.mingchang.data.QuestionListData.questionBean
import com.wanwuzhinan.mingchang.data.QuestionScreenData


class QuestionCheckAdapter : BaseQuickAdapter<questionBean?,QuickViewHolder>() {


    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: questionBean?) {

        holder.setText(R.id.tv_title,"${item!!.index+1}")

        if (item.answer_res_answer.isNotEmpty()){
            if (item.answer_res_true.toInt() == 1){
                holder.setBackgroundResource(R.id.tv_title,R.drawable.shape_option_check_true)
                    .setTextColor(R.id.tv_title,Color.parseColor("#009E6C"))
            }else{
                holder.setBackgroundResource(R.id.tv_title,R.drawable.shape_option_check_false)
                    .setTextColor(R.id.tv_title,Color.parseColor("#F76348"))
            }
        }else{
            holder.setBackgroundResource(R.id.tv_title,R.drawable.shape_option_check_defalut)
                .setTextColor(R.id.tv_title,Color.parseColor("#ffffff"))
        }

    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_question_check, parent)
    }


    var mListener:OnScreenListener? = null

    fun setOnAnswerListener(listener: OnScreenListener) {
        mListener = listener
    }


    interface OnScreenListener {
        fun click(answerIndex:Int)
    }
}