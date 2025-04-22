package com.wanwuzhinan.mingchang.ui.pop

import android.app.Activity
import com.ad.img_load.setOnClickNoRepeat
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.databinding.PopLightBinding


class LightPop(var context: Activity) :BasePop<PopLightBinding>(context){

    override fun initClick() {
        setOnClickNoRepeat(mDataBinding.ivCancel,mDataBinding.tvNow){
            dismiss()
        }
    }


    override fun getLayoutID(): Int {
        return R.layout.pop_light
    }
}