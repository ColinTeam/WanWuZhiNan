package com.wanwuzhinan.mingchang.ui.pop

import android.app.Activity
import com.colin.library.android.utils.ext.onClick
import com.ssm.comm.app.appContext
import com.ssm.comm.config.Constant
import com.ssm.comm.ext.setData
import com.ssm.comm.ext.setOnClickNoRepeat
import com.ssm.comm.global.AppActivityManager
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.databinding.PopPrivacyBinding
import kotlin.system.exitProcess

//隐私政策弹窗
class PrivacyPop(var context: Activity):BasePop<PopPrivacyBinding>(context){

    override fun initClick() {
        isFocusable=false
        onClick(mDataBinding.tvRefuse){
            dismiss()
            AppActivityManager.getInstance().finishAllActivities()
            exitProcess(0)
        }
    }

    fun showPrivacyPop(onSure: (type:Int) -> Unit) {
        showHeightPop()
        setOnClickNoRepeat(mDataBinding.tvUserAgreement,mDataBinding.tvPrivacyAgreement,mDataBinding.tvAgree){
            when (it) {
                mDataBinding.tvUserAgreement -> {
                    onSure(1)
                }

                mDataBinding.tvPrivacyAgreement -> {
                    onSure(2)
                }

                mDataBinding.tvAgree -> {
                    setData(Constant.IS_SHOW_PRIVACY_DIALOG, true)
                    appContext.registerToWX()
                    appContext.initBugly()
                    onSure(3)
                    dismiss()
                }
            }
        }
    }

    override fun getLayoutID(): Int {
        return R.layout.pop_privacy
    }
}