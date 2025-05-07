package com.wanwuzhinan.mingchang.ui.fragment

import android.os.Bundle
import com.colin.library.android.image.glide.GlideImgManager
import com.colin.library.android.utils.Log
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppFragment
import com.wanwuzhinan.mingchang.databinding.FragmentHomeBinding
import com.wanwuzhinan.mingchang.entity.ConfigData
import com.wanwuzhinan.mingchang.vm.HomeViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 22:34
 *
 * Des   :HomeFragment
 */
class HomeFragment : AppFragment<FragmentHomeBinding, HomeViewModel>() {

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        GlideImgManager.loadGif(viewBinding.ivAnim,R.raw.diqu)
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewModel.apply {
            configData.observe {
                Log.d("configData:$it")
                updateConfigData(it.info)
            }
        }

    }

    fun updateConfigData(data: ConfigData) {
        viewBinding.apply {
            ivOneTitle.text = data.home_title1
            ivTwoTitle.text = data.home_title2
            ivThreeTitle.text = data.home_title3
            ivFourTitle.text = data.home_title4
        }
    }
}