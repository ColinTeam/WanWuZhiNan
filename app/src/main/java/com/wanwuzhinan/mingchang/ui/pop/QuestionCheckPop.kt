package com.wanwuzhinan.mingchang.ui.pop

import android.app.Activity
import android.content.Context
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.VibratorUtil
import com.colin.library.android.utils.ext.onClick
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.adapter.QuestionCheckAdapter
import com.wanwuzhinan.mingchang.adapter.QuestionScreenAdapter
import com.wanwuzhinan.mingchang.data.QuestionListData.questionBean
import com.wanwuzhinan.mingchang.data.QuestionScreenData
import com.wanwuzhinan.mingchang.databinding.PopQuestionCheckBinding
import java.lang.ref.WeakReference


class QuestionCheckPop(context: Activity) : BasePop<PopQuestionCheckBinding>(context) {
    private val contextRef: WeakReference<Context?> = WeakReference<Context?>(context)

    lateinit var mAdapter: QuestionScreenAdapter
    lateinit var mOptionAdapter: QuestionCheckAdapter
    var mList: MutableList<QuestionScreenData> = mutableListOf()
    var mOptionList: MutableList<MutableList<questionBean>> = mutableListOf()
    var cur = -1

    override fun initClick() {
        mAdapter = QuestionScreenAdapter()
        mDataBinding.reList.adapter = mAdapter

        mOptionAdapter = QuestionCheckAdapter()
        mDataBinding.reListOption.adapter = mOptionAdapter

        mAdapter.setOnItemClickListener { adapter, view, position ->
            val context = contextRef.get()?:return@setOnItemClickListener
            VibratorUtil.startVibrator(context)
            for (obj in mList) {
                obj.isSelect = false
            }
            cur = position
            mList.get(position).isSelect = true
            if (position < mOptionList.size) {
                mOptionAdapter.submitList(mOptionList.get(position))
            }
            mAdapter.notifyDataSetChanged()
        }

        mOptionAdapter.setOnItemClickListener { adapter, view, position ->
            val context = contextRef.get()?:return@setOnItemClickListener
            if (mListener != null) {
                mListener!!.click(cur, position)
                dismiss()
            }
        }

        onClick(
            mDataBinding.ivClose
        ) {
            when (it) {
                mDataBinding.ivClose -> {
                    dismiss()
                }
            }
        }
    }

    fun setList(list: MutableList<questionBean>) {
        for (i in 0 until Math.floor(list.size / 20.0).toInt()) {
            if (i == 0) {
                mList.add(QuestionScreenData(i, true))
            } else {
                mList.add(QuestionScreenData(i, false))
            }
        }

        val chunkSize = 20
        var tmpList = mutableListOf<questionBean>()
        mOptionList.clear()

        list.forEach { obj ->
            tmpList.add(obj)
            if (tmpList.size == chunkSize) {
                mOptionList.add(tmpList)
                tmpList = mutableListOf() // 重新初始化
            }
        }

        if (tmpList.isNotEmpty()) {
            mOptionList.add(tmpList)
        }

        if (mList.size > 0) {
            cur = 0
            mOptionAdapter.submitList(mOptionList.get(cur))
        }

        Log.e("QuestionCheckPop", "setList: " + mOptionList.size)
        mAdapter.submitList(mList)
    }

    var mListener: OnCheckListener? = null

    fun setOnCheckListener(listener: OnCheckListener) {
        mListener = listener
    }

    interface OnCheckListener {
        fun click(cur: Int, postion: Int)
    }

    fun showPop() {
        mAdapter.notifyDataSetChanged()
        mOptionAdapter.notifyDataSetChanged()
        showHeightPop()
    }


    override fun getLayoutID(): Int {
        return R.layout.pop_question_check
    }
}