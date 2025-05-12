package com.wanwuzhinan.mingchang.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.colin.library.android.utils.ext.onClick
import com.colin.library.android.widget.motion.MotionTouchLister
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppFragment
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.FragmentSettingOtherBinding
import com.wanwuzhinan.mingchang.ui.pop.ImageTipsDialog
import com.wanwuzhinan.mingchang.utils.clearAllData
import com.wanwuzhinan.mingchang.vm.HomeViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 22:34
 *
 * Des   :SettingOtherFragment
 */
class SettingOtherFragment : AppFragment<FragmentSettingOtherBinding, HomeViewModel>() {
    override fun onBackPressed(): Boolean {
        findNavController().navigate(R.id.action_setting_other_back)
    }

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
                        WebFragment.navigate(
                            this@SettingOtherFragment,
                            url = ConfigApp.USER_AGREEMENT,
                            title = getString(R.string.login_protocol_link_1)
                        )
                    }

                    viewPrivacyBg -> {
                        WebFragment.navigate(
                            this@SettingOtherFragment,
                            url = ConfigApp.PRIVACY_POLICY,
                            title = getString(R.string.login_protocol_link_2)
                        )
                    }

                    viewChildBg -> {
                        WebFragment.navigate(
                            this@SettingOtherFragment,
                            url = ConfigApp.PRIVACY_CHILD,
                            title = getString(R.string.login_protocol_link_3)
                        )
                    }

                    viewPwdBg -> {
                        findNavController().navigate(R.id.action_toPassword)
                    }

                    btLogout -> {
                        ImageTipsDialog.newInstance(ImageTipsDialog.TYPE_LOGOUT).apply {
                            sure = { logout() }
                        }.show(this@SettingOtherFragment)
                    }
                }
            }
        }
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {

    }

    fun logout() {
        clearAllData()
        LoginFragment.navigate(this)
    }
}