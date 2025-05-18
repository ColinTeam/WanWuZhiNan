package com.wanwuzhinan.mingchang.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.colin.library.android.utils.ext.onClick
import com.colin.library.android.widget.motion.MotionTouchLister
import com.ssm.comm.config.Constant
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppActivity
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.ActivityProtocolBinding
import com.wanwuzhinan.mingchang.ui.pop.ImageTipsDialog
import com.wanwuzhinan.mingchang.utils.MMKVUtils
import com.wanwuzhinan.mingchang.utils.clearAllData
import com.wanwuzhinan.mingchang.vm.HomeViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-12 20:35
 *
 * Des   :ProtocolActivity
 */
class ProtocolActivity : AppActivity<ActivityProtocolBinding, HomeViewModel>() {
    @SuppressLint("ClickableViewAccessibility")
    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewBinding.apply {
            ivBack.setOnTouchListener(MotionTouchLister())
            viewUserBg.setOnTouchListener(MotionTouchLister())
            viewPrivacyBg.setOnTouchListener(MotionTouchLister())
            viewPwdBg.setOnTouchListener(MotionTouchLister())
            viewChildBg.setOnTouchListener(MotionTouchLister())
            btLogout.setOnTouchListener(MotionTouchLister())
            onClick(viewUserBg, viewPrivacyBg, viewChildBg, viewPwdBg, btLogout) {
                when (it) {
                    viewUserBg -> {
                        WebViewActivity.start(
                            this@ProtocolActivity,
                            url = ConfigApp.USER_AGREEMENT,
                            title = getString(R.string.login_protocol_link_1)
                        )
                    }

                    viewPrivacyBg -> {
                        WebViewActivity.start(
                            this@ProtocolActivity,
                            url = ConfigApp.PRIVACY_POLICY,
                            title = getString(R.string.login_protocol_link_2)
                        )
                    }

                    viewChildBg -> {
                        WebViewActivity.start(
                            this@ProtocolActivity,
                            url = ConfigApp.PRIVACY_CHILD,
                            title = getString(R.string.login_protocol_link_3)
                        )
                    }

                    viewPwdBg -> {
                        val phone = MMKVUtils.getString(Constant.USER_MOBILE)
                        if (!phone.isNullOrEmpty()) {
                            PasswordActivity.start(this@ProtocolActivity, phone)
                        }
                    }

                    btLogout -> {
                        ImageTipsDialog.newInstance(ImageTipsDialog.TYPE_LOGOUT).apply {
                            sure = { logout() }
                        }.show(this@ProtocolActivity)
                    }
                }
            }
        }
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewModel.getConfig()
    }

    fun logout() {
        clearAllData()
        LoginActivity.start(this)
        finish()
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, ProtocolActivity::class.java)
            context.startActivity(starter)
        }
    }
}