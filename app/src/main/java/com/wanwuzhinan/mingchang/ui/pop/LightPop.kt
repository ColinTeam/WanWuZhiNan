package com.wanwuzhinan.mingchang.ui.pop

import android.app.Activity
import com.colin.library.android.utils.ext.onClick
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.databinding.PopLightBinding


class LightPop(var context: Activity) :BasePop<PopLightBinding>(context){

    override fun initClick() {
        onClick(mDataBinding.ivCancel,mDataBinding.tvNow){
            dismiss()
        }
    }


    override fun getLayoutID(): Int {
        return R.layout.pop_light
    }
}