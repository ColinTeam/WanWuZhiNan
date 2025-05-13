package com.wanwuzhinan.mingchang.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.util.setOnDebouncedItemClick
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.colin.library.android.image.glide.GlideImgManager
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.QuestionListData


class AnswerAskAdapter : BaseQuickAdapter<QuestionListData.QuestionBean?,QuickViewHolder>() {


    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: QuestionListData.QuestionBean?) {

        holder.setText(R.id.tv_title,"${item?.title}")
            .setGone(R.id.iv_topic,item?.title_img?.length == 0)
            .setGone(R.id.ll_result,item?.answer_res_answer?.length == 0)
            .setText(R.id.tv_sure_answer,"正确答案：${item?.answer_true}")
            .setText(R.id.tv_answer_desc,item?.answer_desc)
            .setText(R.id.tv_num,"${item!!.index+1}")
            .setGone(R.id.tv_topic, item.index%20 != 0)
            .setText(R.id.tv_topic,"第${Math.floor(item.index/20.0).toInt()+1}节")

        GlideImgManager.get().loadImgFitCenter(item.title_img,holder.getView(R.id.iv_topic),R.drawable.img_default_icon)

        var mAdapter:AnswerAskOptionAdapter = AnswerAskOptionAdapter()
        holder.getView<RecyclerView>(R.id.re_option).adapter = mAdapter
        mAdapter.submitList(item.answersArr)

        mAdapter.setOnDebouncedItemClick(){adapter, view, cposition ->
            if (mListener != null){
                mListener?.click(position,cposition)
            }
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_answer_ask, parent)
    }


    var mListener:OnAnswerListener? = null

    fun setOnAnswerListener(listener: OnAnswerListener) {
        mListener = listener
    }


    interface OnAnswerListener {
        fun click(questionIndex:Int,answerIndex:Int)
    }
}