package com.wanwuzhinan.mingchang.ui.fragment

import android.os.Bundle
import android.view.View
import com.ad.img_load.setOnClickNoRepeat
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppFragment
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.FragmentHomeBinding
import com.wanwuzhinan.mingchang.ext.getConfigData
import com.wanwuzhinan.mingchang.ext.navigate

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 22:34
 *
 * Des   :HomeFragment
 */
class HomeFragment : AppFragment<FragmentHomeBinding, HomeViewModel>() {

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        setOnClickNoRepeat(
            viewBinding.bgVideo,
            viewBinding.bgAudio,
            viewBinding.bgQuestion,
            viewBinding.ivQuestion,
            viewBinding.bgHonor,
            viewBinding.ivHonor,
            viewBinding.linSetting,
            viewBinding.rivHead,
            viewBinding.tvExchange,
            viewBinding.tvList,
            viewBinding.tvCasting,
            viewBinding.ivAd,
            viewBinding.ivAdClose
        ) {
            when (it) {
                viewBinding.bgVideo -> {//视频

                }

                viewBinding.bgAudio -> {//音频
                }

                viewBinding.bgQuestion, viewBinding.ivQuestion -> {//物理万问
                }

                viewBinding.bgHonor, viewBinding.ivHonor -> {//龙门科举
                }

                viewBinding.tvList -> {//荣誉墙
                }

                viewBinding.linSetting, viewBinding.rivHead -> {//设置
                    navigate(R.id.action_toLogin)
                }

                viewBinding.tvExchange, viewBinding.tvExchange -> {//课程兑换

                }

                viewBinding.tvCasting, viewBinding.tvCasting -> {//投屏
                    WebFragment.navigate(
                        this@HomeFragment,
                        url = ConfigApp.SCREEN_CASTING,
                        title = getConfigData().home_title5
                    )
                }

                viewBinding.ivAd -> {
                    WebFragment.navigate(
                        this@HomeFragment, url = getConfigData().home_ad_link, title = "详情"
                    )
                }

                viewBinding.ivAdClose -> {
                    viewBinding.llAd.visibility = View.GONE
                }
            }
        }
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {


    }

}