package com.wanwuzhinan.mingchang.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import com.colin.library.android.image.glide.GlideImgManager
import com.colin.library.android.network.NetworkConfig
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.ext.onClick
import com.colin.library.android.widget.motion.MotionTouchLister
import com.ssm.comm.config.Constant
import com.ssm.comm.ext.getCurrentVersionCode
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppActivity
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.FragmentHomeBinding
import com.wanwuzhinan.mingchang.entity.Config
import com.wanwuzhinan.mingchang.entity.ConfigData
import com.wanwuzhinan.mingchang.entity.UserInfo
import com.wanwuzhinan.mingchang.ui.pad.AudioHomeIpadActivity
import com.wanwuzhinan.mingchang.ui.phone.AudioHomeActivity
import com.wanwuzhinan.mingchang.ui.phone.ExchangeActivity
import com.wanwuzhinan.mingchang.ui.phone.QuestionListAskActivity
import com.wanwuzhinan.mingchang.ui.phone.QuestionListPracticeActivity
import com.wanwuzhinan.mingchang.ui.phone.RankActivity
import com.wanwuzhinan.mingchang.ui.phone.VideoHomeActivity
import com.wanwuzhinan.mingchang.ui.pop.ImageTipsDialog
import com.wanwuzhinan.mingchang.ui.pop.UserInfoDialog
import com.wanwuzhinan.mingchang.utils.RATIO_16_9
import com.wanwuzhinan.mingchang.utils.getRatio
import com.wanwuzhinan.mingchang.utils.load
import com.wanwuzhinan.mingchang.utils.setData
import com.wanwuzhinan.mingchang.vm.HomeViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 21:57
 *
 * Des   :HomeActivity
 */
class HomeActivity : AppActivity<FragmentHomeBinding, HomeViewModel>() {
    var checkUpdate = false
    var checkUserInfo = false

    override fun onDestroy() {
        checkUpdate = false
        checkUserInfo = false
        super.onDestroy()
    }

    /**
     * 重新启动 一般需要跳转到制定界面如Login
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("onNewIntent:$intent")
        loadData(true)
    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.R)
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
            onClick(
                ivOneBg,
                ivTwoBg,
                ivThreeBg,
                ivFourBg,
                ivTabBg,
                tab0,
                tab1,
                tab2,
                tvSetting,
                ivAd,
                ivAdclose
            ) {
                viewModel.getConfigValue() ?: return@onClick
                when (it) {
                    tvSetting -> {
                        SettingTabActivity.start(this@HomeActivity)
                    }

                    ivTabBg, tab0 -> {
                        ExchangeActivity.start(this@HomeActivity)
                    }

                    tab1 -> {
                        RankActivity.start(this@HomeActivity)
                    }

                    tab2 -> {
                        WebViewActivity.start(
                            this@HomeActivity,
                            url = ConfigApp.SCREEN_CASTING,
                            title = viewModel.getConfigValue()?.info?.home_title5
                                ?: getString(R.string.home_tab_2)
                        )
                    }

                    ivOneBg -> {//video
                        VideoHomeActivity.start(this@HomeActivity)
                    }

                    ivTwoBg -> {//audio
                        val radio = this@HomeActivity.getRatio()
                        if (radio > RATIO_16_9) AudioHomeActivity.start(this@HomeActivity)
                        else AudioHomeIpadActivity.start(this@HomeActivity)
                    }

                    ivThreeBg -> {
                        QuestionListAskActivity.start(this@HomeActivity)
                    }

                    ivFourBg -> {
                        QuestionListPracticeActivity.start(this@HomeActivity)
                    }

                    ivAd -> {
                        val url = viewModel.getConfigValue()?.info?.home_ad_link ?: return@onClick
                        WebViewActivity.start(
                            this@HomeActivity, url = url, title = getString(R.string.home_detail)
                        )
                    }

                    ivAdclose -> {
                        viewModel.updateAD(true)
                    }
                }
            }
        }

    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewModel.apply {
            configData.observe {
                Log.i("configData:$it")
                updateHomeContent(it.info)
                doNextAction()
            }
            userInfo.observe {
                Log.i("userInfo:$it")
                viewBinding.ivAvatar.load(it.headimg, R.mipmap.logo)
                viewBinding.tvUserName.text = it.nickname
                ConfigApp.question_count_error = it.question_count_error
                ConfigApp.question_compass = it.question_compass
                setData(Constant.USER_INFO, NetworkConfig.gson.toJson(it))
                doNextAction()
            }
            closeAD.observe {
                Log.i("closeAD:$it")
                changeADState(!it, getConfigValue())
            }
        }

    }

    override fun loadData(refresh: Boolean) {
        viewModel.getConfig()
        viewModel.getUserInfo()
    }

    fun changeADState(visible: Boolean, config: Config? = viewModel.getConfigValue()) {
        val isEmpty =
            config?.info?.home_ad.isNullOrEmpty() or config?.info?.home_ad_link.isNullOrEmpty()
        val isVisible = visible && !isEmpty
        viewBinding.ivAd.isVisible = isVisible
        viewBinding.ivAdclose.isVisible = isVisible
    }


    fun doNextAction(
        data: Config? = viewModel.getConfigValue(), user: UserInfo? = viewModel.getUserInfoValue()
    ) {
        user ?: return
        val info = data?.info ?: return
        if (isFinishing || isDestroyed || !lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            return
        }
        if (info.android_code.toInt() > getCurrentVersionCode() && !checkUpdate) {
            checkUpdate = true
            ImageTipsDialog.newInstance(ImageTipsDialog.TYPE_UPGRADE, extra = info.android_update)
                .apply {
                    isCancelable = false
                    sure = {
                        editUserInfo(user)
                    }
                    cancel = {
                        editUserInfo(user)
                    }

                }.show(this)
        } else {
            editUserInfo(user)
        }
    }

    fun editUserInfo(user: UserInfo) {
        if (isFinishing || isDestroyed || !lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            return
        }
        if (user.nickname.isNotEmpty()) {
            return
        }
        if (checkUserInfo) return
        checkUserInfo = true
        UserInfoDialog.newInstance(user).apply {
            success = {
                if (it) {
                    viewModel.getUserInfo()
                    SettingTabActivity.start(this@HomeActivity)
                }
            }
        }.show(this)
    }

    fun updateHomeContent(data: ConfigData) {
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
        //全局实用
        setData(Constant.CONFIG_DATA, NetworkConfig.gson.toJson(data))
    }


    companion object {

        @JvmStatic
        fun start(context: Context, id: Int = R.id.action_toHome) {
            val starter = Intent(
                context, HomeActivity::class.java
            ).putExtra(ConfigApp.EXTRAS_POSITION, id)
            context.startActivity(starter)
        }
    }
}