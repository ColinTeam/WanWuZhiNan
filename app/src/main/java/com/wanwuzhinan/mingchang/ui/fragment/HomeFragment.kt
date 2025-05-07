package com.wanwuzhinan.mingchang.ui.fragment

import android.os.Build
import android.os.Bundle
import androidx.lifecycle.ViewModelStore
import com.colin.library.android.image.glide.GlideImgManager
import com.colin.library.android.network.NetworkConfig
import com.colin.library.android.utils.Log
import com.ssm.comm.config.Constant
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppFragment
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.FragmentHomeBinding
import com.wanwuzhinan.mingchang.entity.ConfigData
import com.wanwuzhinan.mingchang.ui.HomeActivity
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

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        GlideImgManager.loadGif(viewBinding.ivAnim, R.raw.diqu)
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
    }

    override fun onPause() {
        super.onPause()
        (requireActivity() as HomeActivity).changeADState(false)
    }

}