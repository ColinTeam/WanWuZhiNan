package com.wanwuzhinan.mingchang.ui.fragment

import android.os.Bundle
import com.ssm.comm.ext.editChange
import com.ssm.comm.ext.getClearAccount
import com.ssm.comm.ext.toastError
import com.ssm.comm.ext.toastSuccess
import com.wanwuzhinan.mingchang.app.AppFragment
import com.wanwuzhinan.mingchang.databinding.FragmentLoginBinding
import com.wanwuzhinan.mingchang.ext.editTips
import com.wanwuzhinan.mingchang.ext.getConfigData
import com.wanwuzhinan.mingchang.vm.LoginViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 22:34
 *
 * Des   :HomeFragment
 */
class LoginFragment : AppFragment<FragmentLoginBinding, LoginViewModel>() {

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        com.ad.img_load.setOnClickNoRepeat(
            viewBinding.linArea, viewBinding.tvGet, viewBinding.tvLogin, viewBinding.tvCodeVer
        ) {
            when (it) {
                viewBinding.linArea -> {//选区号

                }

                viewBinding.tvGet -> {//获取验证码
                    val editTips = editTips(viewBinding.edPhone)
                    if (!editTips) return@setOnClickNoRepeat
                }

                viewBinding.tvLogin -> {//登录
                    var editTips = editChange(viewBinding.edPhone, viewBinding.edCode)
                    if (!editTips) return@setOnClickNoRepeat

                    if (!viewBinding.defaultProtocol.isSelect()) {
                        toastSuccess("请阅读并同意《用户协议》和《隐私协议》")
                        return@setOnClickNoRepeat
                    }
                    if (viewBinding.edPhone.text.toString() == getClearAccount()) {
                        toastError("账号不存在")
                        return@setOnClickNoRepeat
                    }
                }

                viewBinding.tvCodeVer -> {
                    WebFragment.navigate(
                        this@LoginFragment, url = getConfigData().code_verification
                    )
                }
            }
        }
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {


    }

}