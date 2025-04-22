package com.wanwuzhinan.mingchang.ui.pop

import android.app.Activity
import android.graphics.Color
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.View.OnLongClickListener
import com.ad.img_load.glide.manager.GlideImgManager
import com.ad.img_load.setOnClickNoRepeat
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.MedalListData
import com.wanwuzhinan.mingchang.databinding.PopAudioCardBinding
import com.wanwuzhinan.mingchang.databinding.PopExchangeContactBinding
import com.wanwuzhinan.mingchang.databinding.PopNetErrorBinding
import com.wanwuzhinan.mingchang.ext.createQRCodeBitmap
import com.wanwuzhinan.mingchang.ext.getConfigData
import com.wanwuzhinan.mingchang.ext.getUserInfo
import com.wanwuzhinan.mingchang.ext.launchWechatService


class AudioCardPop(var context: Activity,var onSure: () -> Unit) :BasePop<PopAudioCardBinding>(context){

    override fun initClick() {
        setOnClickNoRepeat(mDataBinding.tvSure){
            when(it){
                mDataBinding.tvSure->{
                    timer.cancel()
                    dismiss()
                    onSure()
                }
            }
        }
    }

    val timer = object : CountDownTimer(6000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            mDataBinding.tvDown.text = "${millisUntilFinished / 1000+1}S"
        }

        override fun onFinish() {
            dismiss()
            onSure()
        }
    }

    fun showPop(title:String,image:String,type:Int){

        mDataBinding.tvName.text = title
        GlideImgManager.get().loadImg(image,mDataBinding.ivCover,R.drawable.img_default_icon)
        if (type == 1){
            mDataBinding.tvType.text = "科学家集卡"
        }else{
            mDataBinding.tvType.text = "勋章"
        }
        timer.start()
        showHeightPop()
    }


    override fun getLayoutID(): Int {
        return R.layout.pop_audio_card
    }
}