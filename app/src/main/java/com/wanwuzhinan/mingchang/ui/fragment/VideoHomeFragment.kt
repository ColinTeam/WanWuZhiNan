package com.wanwuzhinan.mingchang.ui.fragment

import android.os.Bundle
import com.wanwuzhinan.mingchang.app.AppFragment
import com.wanwuzhinan.mingchang.databinding.FragmentVideoHomeBinding
import com.wanwuzhinan.mingchang.vm.MediaViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-11 18:01
 *
 * Des   :VideoHomeFragment
 */
class VideoHomeFragment : AppFragment<FragmentVideoHomeBinding, MediaViewModel>() {
    var tab = 0
    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        tab = getExtrasPosition(bundle, savedInstanceState)
        viewBinding.apply {

        }
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {

    }


}