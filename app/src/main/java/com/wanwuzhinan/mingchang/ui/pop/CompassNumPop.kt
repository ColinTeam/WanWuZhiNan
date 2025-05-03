package com.wanwuzhinan.mingchang.ui.pop

import android.app.Activity
import com.colin.library.android.utils.ext.onClick
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.databinding.PopCompassNumBinding
import com.wanwuzhinan.mingchang.ext.getConfigData


class CompassNumPop(var context: Activity, var onSure: () -> Unit, var onCancel: () -> Unit) :
    BasePop<PopCompassNumBinding>(context) {

    override fun initClick() {

        onClick(mDataBinding.tvCancel, mDataBinding.tvSure) {
            when (it) {
                mDataBinding.tvCancel -> {
                    onCancel()
                    dismiss()
                }

                mDataBinding.tvSure -> {
                    onSure()
                    dismiss()
                }
            }
        }
    }


    fun showPop(num: String) {
        mDataBinding.tvNum.text = "当前你拥有${num}个指南针"
        mDataBinding.tvContent.text = getConfigData().home_title3
        showHeightPop()
    }

    //完成阶段
    fun showPop(title: String, tips: String, num: String) {
        mDataBinding.tvContent.text = title
        mDataBinding.tvTips.text = tips
        mDataBinding.tvNum.text = "当前获得${num}个指南针"
        showHeightPop()
    }


    override fun getLayoutID(): Int {
        return R.layout.pop_compass_num
    }
}