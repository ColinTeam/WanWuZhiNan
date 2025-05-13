package com.wanwuzhinan.mingchang.ui.pop

import android.app.Activity
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.ext.onClick
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.adapter.QuestionScreenAdapter
import com.wanwuzhinan.mingchang.data.QuestionListData.QuestionBean
import com.wanwuzhinan.mingchang.data.QuestionScreenData
import com.wanwuzhinan.mingchang.databinding.PopQuestionScreenBinding


class QuestionScreenPop(var context: Activity , var onSure: (finishS:Boolean,noFinishS:Boolean,trueS:Boolean,falseS:Boolean,selectList:MutableList<Int> ) -> Unit) :BasePop<PopQuestionScreenBinding>(context){


    lateinit var mAdapter:QuestionScreenAdapter
    var mList:MutableList<QuestionScreenData> = mutableListOf()


    override fun initClick() {
        mAdapter= QuestionScreenAdapter()

        mDataBinding.reList.adapter=mAdapter

        mAdapter.setOnItemClickListener{adapter, view, position ->
            mList.get(position).isSelect = !mList.get(position).isSelect
            mAdapter.notifyDataSetChanged()
        }

        onClick(
            mDataBinding.tvSure,
            mDataBinding.tvFinish,
            mDataBinding.tvNofinish,
            mDataBinding.tvTrue,
            mDataBinding.tvFalse
        ){
            when(it){
                mDataBinding.tvSure->{
                    dismiss()

                    var selectList:MutableList<Int> = mutableListOf()

                    for (obj in mList){
                        if (obj.isSelect){
                            selectList.add(obj.page)
                        }
                    }

                    onSure(
                        mDataBinding.tvFinish.isSelected,
                        mDataBinding.tvNofinish.isSelected,
                        mDataBinding.tvTrue.isSelected,
                        mDataBinding.tvFalse.isSelected,
                        selectList
                        )
                }
                mDataBinding.tvFinish->{
                    if (mDataBinding.tvFinish.isSelected){
                        mDataBinding.tvFinish.isSelected = false
                    }else{
                        mDataBinding.tvFinish.isSelected = true
//                        mDataBinding.tvNofinish.isSelected = false
                    }
                }
                mDataBinding.tvNofinish->{
                    if (mDataBinding.tvNofinish.isSelected){
                        mDataBinding.tvNofinish.isSelected = false
                    }else{
                        mDataBinding.tvNofinish.isSelected = true
//                        mDataBinding.tvFinish.isSelected = false
                    }
                }
                mDataBinding.tvTrue->{
                    if (mDataBinding.tvTrue.isSelected){
                        mDataBinding.tvTrue.isSelected = false
                    }else{
                        mDataBinding.tvTrue.isSelected = true
//                        mDataBinding.tvFalse.isSelected = false
                    }
                }
                mDataBinding.tvFalse->{
                    if (mDataBinding.tvFalse.isSelected){
                        mDataBinding.tvFalse.isSelected = false
                    }else{
                        mDataBinding.tvFalse.isSelected = true
//                        mDataBinding.tvTrue.isSelected = false
                    }
                }
            }
        }
    }

    fun setList(list:MutableList<QuestionBean>){
        for (i in 0 until Math.floor(list.size/20.0).toInt()){
            mList.add(QuestionScreenData(i,false))
        }
        Log.e("QuestionScreenPop", "setList: "+list.size )
        mAdapter.submitList(mList)
    }


    fun showPop(){

        showHeightPop()
    }


    override fun getLayoutID(): Int {
        return R.layout.pop_question_screen
    }
}