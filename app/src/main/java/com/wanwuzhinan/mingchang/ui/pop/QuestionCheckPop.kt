package com.wanwuzhinan.mingchang.ui.pop

import android.app.Activity
import android.graphics.Color
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.View.OnLongClickListener
import com.ad.img_load.glide.manager.GlideImgManager
import com.ad.img_load.setOnClickNoRepeat
import com.ad.img_load.startVibrate
import com.hpplay.common.asyncmanager.AsyncFileParameter.In
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.adapter.AnswerPracticeSelectAdapter
import com.wanwuzhinan.mingchang.adapter.QuestionCheckAdapter
import com.wanwuzhinan.mingchang.adapter.QuestionScreenAdapter
import com.wanwuzhinan.mingchang.data.MedalListData
import com.wanwuzhinan.mingchang.data.QuestionListData
import com.wanwuzhinan.mingchang.data.QuestionListData.questionBean
import com.wanwuzhinan.mingchang.data.QuestionScreenData
import com.wanwuzhinan.mingchang.databinding.PopAudioCardBinding
import com.wanwuzhinan.mingchang.databinding.PopExchangeContactBinding
import com.wanwuzhinan.mingchang.databinding.PopNetErrorBinding
import com.wanwuzhinan.mingchang.databinding.PopQuestionCheckBinding
import com.wanwuzhinan.mingchang.databinding.PopQuestionScreenBinding
import com.wanwuzhinan.mingchang.ext.createQRCodeBitmap
import com.wanwuzhinan.mingchang.ext.getConfigData
import com.wanwuzhinan.mingchang.ext.getUserInfo
import com.wanwuzhinan.mingchang.ext.launchWechatService


class QuestionCheckPop(var context: Activity) :BasePop<PopQuestionCheckBinding>(context){


    lateinit var mAdapter:QuestionScreenAdapter
    lateinit var mOptionAdapter:QuestionCheckAdapter
    var mList:MutableList<QuestionScreenData> = mutableListOf()
    var mOptionList:MutableList<MutableList<questionBean>> = mutableListOf()
    var cur = -1

    override fun initClick() {
        mAdapter= QuestionScreenAdapter()
        mDataBinding.reList.adapter=mAdapter

        mOptionAdapter = QuestionCheckAdapter()
        mDataBinding.reListOption.adapter=mOptionAdapter

        mAdapter.setOnItemClickListener{adapter, view, position ->
            view.startVibrate()
            for (obj in mList){
                obj.isSelect = false
            }
            cur = position
            mList.get(position).isSelect = true
            if (position < mOptionList.size) {
                mOptionAdapter.submitList(mOptionList.get(position))
            }
            mAdapter.notifyDataSetChanged()
        }

        mOptionAdapter.setOnItemClickListener{adapter, view, position ->
            view.startVibrate()
            if (mListener != null){
                mListener!!.click(cur,position)
                dismiss()
            }
        }

        setOnClickNoRepeat(
            mDataBinding.ivClose
        ){
            when(it){
                mDataBinding.ivClose->{
                    dismiss()
                }
            }
        }
    }

    fun setList(list:MutableList<questionBean>){
        for (i in 0 until Math.floor(list.size/20.0).toInt()){
            if (i == 0){
                mList.add(QuestionScreenData(i,true))
            }else {
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

        if (mList.size > 0){
            cur = 0
            mOptionAdapter.submitList(mOptionList.get(cur))
        }

        Log.e("QuestionCheckPop", "setList: "+mOptionList.size )
        mAdapter.submitList(mList)
    }

    var mListener:OnCheckListener? = null

    fun setOnCheckListener(listener: OnCheckListener) {
        mListener = listener
    }

    interface OnCheckListener {
        fun click(cur:Int,postion:Int)
    }

    fun showPop(){
        mAdapter.notifyDataSetChanged()
        mOptionAdapter.notifyDataSetChanged()
        showHeightPop()
    }


    override fun getLayoutID(): Int {
        return R.layout.pop_question_check
    }
}