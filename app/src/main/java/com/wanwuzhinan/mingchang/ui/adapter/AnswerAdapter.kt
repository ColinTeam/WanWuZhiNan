package com.wanwuzhinan.mingchang.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.colin.library.android.utils.ext.onClick
import com.colin.library.android.widget.base.BaseAdapter
import com.colin.library.android.widget.base.BaseViewHolder
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.entity.AnswerBean

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-16 13:11
 *
 * Des   :AnswerAdapter
 */
class AnswerAdapter(
    val item: ArrayList<AnswerBean>, layoutRes: Int = R.layout.item_answer
) : BaseAdapter<AnswerBean>(item, layoutRes) {

    var selected: Int = 0
        @SuppressLint("NotifyDataSetChanged") set(value) {
            if (field == value) return
            field = value
            notifyDataSetChanged()
        }

    override fun bindListViewHolder(
        holder: BaseViewHolder, item: AnswerBean, position: Int, payloads: MutableList<Any>
    ) {
        holder.setText(R.id.tvAnswer, item.answer)
            .setImageResource(R.id.ivAnswerBg, getAnswerBg(item.answerType))
            .setImageResource(R.id.ivOptionBg, getOptionBg(item.answerType))
            .setImageResource(R.id.ivOption, getOption(position, item.answerType))
            .setTextColor(R.id.tvAnswer, getTextColor(context, item.answerType))
        if (item.answerType == 1) {
            holder.setImageResource(R.id.ivResult, R.mipmap.ic_answer_yes)
                .setVisibility(R.id.ivResult, View.VISIBLE)
        } else if (item.answerType == 2) {
            holder.setImageResource(R.id.ivResult, R.mipmap.ic_answer_no)
                .setVisibility(R.id.ivResult, View.VISIBLE)
        } else {
            holder.setVisibility(R.id.ivResult, View.GONE)
        }
        holder.getView<View>(R.id.layout).onClick {
            onItemClickListener?.invoke(it, item, position)
        }
    }

    private fun getTextColor(context: Context, type: Int): Int {
        return if (type == 1) context.getColor(R.color.answer_yes)
        else if (type == 2) context.getColor(R.color.answer_no)
        else context.getColor(R.color.answer_def)
    }


    // 0 正常 1 对 2错
    private fun getAnswerBg(type: Int): Int {
        return if (type == 1) R.drawable.bg_answer_bg_right
        else if (type == 2) R.drawable.bg_answer_bg_error
        else R.drawable.bg_answer_bg_default
    }

    // 0 正常 1 对 2错
    private fun getOptionBg(type: Int): Int {
        return if (type == 1) R.drawable.bg_answer_bg_right
        else if (type == 2) R.drawable.bg_answer_bg_error
        else R.drawable.bg_answer_bg_default
    }

    private fun getOption(position: Int, type: Int): Int {
        return if (position == 0) {
            if (type > 0) R.mipmap.ic_question_answer_a_s else R.mipmap.ic_question_answer_a
        } else if (position == 1) {
            if (type > 0) R.mipmap.ic_question_answer_b_s else R.mipmap.ic_question_answer_b
        } else {
            if (type > 0) R.mipmap.ic_question_answer_c_s else R.mipmap.ic_question_answer_c
        }
    }
}