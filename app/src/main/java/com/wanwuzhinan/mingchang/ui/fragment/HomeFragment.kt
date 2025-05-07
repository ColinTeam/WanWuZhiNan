package com.wanwuzhinan.mingchang.ui.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelStore
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.colin.library.android.image.glide.GlideImgManager
import com.colin.library.android.network.NetworkConfig
import com.colin.library.android.utils.Log
import com.ssm.comm.config.Constant
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppFragment
import com.wanwuzhinan.mingchang.databinding.FragmentHomeBinding
import com.wanwuzhinan.mingchang.entity.ConfigData
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
        super.onResume()
    }

    companion object{
        fun navigate(
            fragment: Fragment,
            type: Int = Constant.OTHER_TYPE,
            url: String? = null,
            title: String? = null,
            content: String? = null
        ) {
            fragment.findNavController().apply {
                navigate(
                    R.id.action_toWeb, Bundle().apply {
                        putInt(Constant.URL_TYPE, type)
                        url?.let { putString(Constant.H5_URL, it) }
                        title?.let { putString(Constant.WEB_TITLE, it) }
                        content?.let { putString(Constant.WEB_CONTENT, it) }
                    }, NavOptions.Builder().setLaunchSingleTop(true).setRestoreState(true).build()
                )
            }
        }
    }
}