package com.wanwuzhinan.mingchang.ui.pop

import android.app.Activity
import com.ad.img_load.setOnClickNoRepeat
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.databinding.PopSureBinding
import com.wanwuzhinan.mingchang.ext.visible

//确认框
class SurePop(var context: Activity) :BasePop<PopSureBinding>(context){

    //只有确定有点击回调
    fun showPop(content:String,title:String,cancel:String,sure:String,is_show:Boolean,onSure: () -> Unit) {
        mDataBinding.tvCancel.visible(is_show)
        mDataBinding.tvTitle.text=title
        mDataBinding.tvContent.text=content
        mDataBinding.tvCancel.text=cancel
        mDataBinding.tvSure.text=sure

        showPop()
        setOnClickNoRepeat(mDataBinding.tvSure,mDataBinding.tvCancel){
            when(it){
                mDataBinding.tvSure ->{
                    onSure()
                }
                mDataBinding.tvCancel ->{
                    dismiss()
                }
            }
        }
    }

    //取消和确定都有点击回调
    fun showCancelPop(content:String,title:String,cancel:String,sure:String,is_show:Boolean,onSure: () -> Unit,onCancel: () -> Unit) {
        mDataBinding.tvCancel.visible(is_show)
        mDataBinding.tvTitle.text=title
        mDataBinding.tvContent.text=content
        mDataBinding.tvCancel.text=cancel
        mDataBinding.tvSure.text=sure

        showPop()
        setOnClickNoRepeat(mDataBinding.tvSure,mDataBinding.tvCancel){
            dismiss()
            when(it){
                mDataBinding.tvSure ->{
                    onSure()
                }
                mDataBinding.tvCancel ->{
                    onCancel()
                }
            }
        }
    }

    override fun getLayoutID(): Int {
        return R.layout.pop_sure
    }
}