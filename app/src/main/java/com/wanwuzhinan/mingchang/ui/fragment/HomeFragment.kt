package com.wanwuzhinan.mingchang.ui.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelStore
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.colin.library.android.image.glide.GlideImgManager
import com.colin.library.android.network.NetworkConfig
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.ext.onClick
import com.colin.library.android.widget.motion.MotionTouchLister
import com.ssm.comm.config.Constant
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppFragment
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.FragmentHomeBinding
import com.wanwuzhinan.mingchang.entity.ConfigData
import com.wanwuzhinan.mingchang.ui.HomeActivity
import com.wanwuzhinan.mingchang.ui.pad.AudioHomeIpadActivity
import com.wanwuzhinan.mingchang.ui.phone.AudioHomeActivity
import com.wanwuzhinan.mingchang.ui.phone.ExchangeActivity
import com.wanwuzhinan.mingchang.ui.phone.QuestionListPracticeActivity
import com.wanwuzhinan.mingchang.ui.phone.RankActivity
import com.wanwuzhinan.mingchang.ui.phone.VideoHomeActivity
import com.wanwuzhinan.mingchang.ui.pop.PrivacyPop
import com.wanwuzhinan.mingchang.utils.RATIO_16_9
import com.wanwuzhinan.mingchang.utils.getRatio
import com.wanwuzhinan.mingchang.utils.isShowPrivacy
import com.wanwuzhinan.mingchang.utils.setData
import com.wanwuzhinan.mingchang.vm.HomeViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 22:34
 *
 * Des   :HomeFragment
 */
class HomeFragment : AppFragment<FragmentHomeBinding, HomeViewModel>() {

    override fun bindViewModelStore(): ViewModelStore {
        return requireActivity().viewModelStore
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewBinding.apply {
            GlideImgManager.loadGif(ivAnim, R.raw.diqu)
            ivOneBg.setOnTouchListener(MotionTouchLister())
            ivTwoBg.setOnTouchListener(MotionTouchLister())
            ivThreeBg.setOnTouchListener(MotionTouchLister())
            ivFourBg.setOnTouchListener(MotionTouchLister())
            tab0.setOnTouchListener(MotionTouchLister())
            tab1.setOnTouchListener(MotionTouchLister())
            tab2.setOnTouchListener(MotionTouchLister())
            ivTabBg.setOnTouchListener(MotionTouchLister())
            onClick(ivOneBg, ivTwoBg, ivThreeBg, ivFourBg, ivTabBg, tab0, tab1, tab2, tvSetting) {
                viewModel.getConfigValue() ?: return@onClick
                when (it) {
                    tvSetting -> {
                        activity?.let {
                            SettingTabFragment.navigate(this@HomeFragment)
                        }
                    }

                    ivTabBg, tab0 -> {
                        activity?.let { ExchangeActivity.start(it) }
                    }

                    tab1 -> {
                        activity?.let { RankActivity.start(it) }
                    }

                    tab2 -> {
                        val title = viewModel.getConfigValue()?.info?.home_title5
                            ?: getString(R.string.home_tab_2)
                        WebFragment.navigate(
                            this@HomeFragment,
                            type = Constant.OTHER_TYPE,
                            url = ConfigApp.SCREEN_CASTING,
                            title = title
                        )
                    }

                    ivOneBg -> {//video
                        activity?.let { VideoHomeActivity.start(it) }
                    }

                    ivTwoBg -> {//audio
                        activity?.let {
                            val radio = it.getRatio()
                            if (radio > RATIO_16_9) AudioHomeActivity.start(it)
                            else AudioHomeIpadActivity.start(it)
                        }
                    }

                    ivThreeBg -> {
                        activity?.let {
//                            QuestionListAskActivity.start(it)
                            findNavController().navigate(R.id.action_toVideoHome)
                        }
                    }

                    ivFourBg -> {
                        activity?.let {
                            QuestionListPracticeActivity.start(it)
                            findNavController().navigate(R.id.action_toVideo)
                        }
                    }
                }
            }
        }

    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewModel.apply {
            configData.observe {
                Log.d("configData:$it")
                updateConfigData(it.info)
            }
            userInfo.observe {
                Log.d("userInfo:$it")
                ConfigApp.question_count_error = it.question_count_error
                ConfigApp.question_compass = it.question_compass
                setData(Constant.USER_INFO, NetworkConfig.gson.toJson(it))
                GlideImgManager.get().loadImg(it.headimg, viewBinding.ivAvatar, R.mipmap.logo)
                viewBinding.tvUserName.text = it.nickname
            }
        }

    }

    fun updateConfigData(data: ConfigData) {
        viewBinding.apply {
            tvOneTitle.text = data.home_title1
            tvTwoTitle.text = data.home_title2
            tvThreeTitle.text = data.home_title3
            tvFourTitle.text = data.home_title4
            tab2.text = data.home_title5
        }
        if (!("huawei".equals(Build.BRAND, true) || "huawei".equals(
                Build.MANUFACTURER, true
            ) || "honor".equals(Build.BRAND, true) || "honor".equals(
                Build.MANUFACTURER, true
            ))
        ) {
            data.apple_is_audit = 0
        }
        setData(Constant.CONFIG_DATA, NetworkConfig.gson.toJson(data))
    }

    override fun onResume() {
        (requireActivity() as HomeActivity).changeADState(viewModel.getAdStateValue())
        super.onResume()
        viewModel.getUserInfo()
        showPrivacyPop()
    }


    override fun onPause() {
        super.onPause()
        (requireActivity() as HomeActivity).changeADState(false)
    }

    private fun showPrivacyPop() {
        if (isShowPrivacy()) return
        PrivacyPop(requireActivity()).showPrivacyPop {
            when (it) {
                1 -> {
                    WebFragment.navigate(
                        this@HomeFragment,
                        url = ConfigApp.USER_AGREEMENT,
                        title = getString(R.string.login_protocol_link_1)
                    )
                }

                2 -> {
                    WebFragment.navigate(
                        this@HomeFragment,
                        url = ConfigApp.PRIVACY_POLICY,
                        title = getString(R.string.login_protocol_link_2)
                    )
                }
            }
        }
    }

    override fun onBackPressed(): Boolean {
        activity?.finish()
        return false
    }

    companion object {
        @JvmStatic
        fun navigate(fragment: Fragment) {
            val controller = fragment.findNavController()
            val option =
                NavOptions.Builder().setPopUpTo(controller.graph.startDestinationId, true, false)
                    .setLaunchSingleTop(true).build()
            controller.navigate(R.id.action_toHome, null, option)
        }
    }
}