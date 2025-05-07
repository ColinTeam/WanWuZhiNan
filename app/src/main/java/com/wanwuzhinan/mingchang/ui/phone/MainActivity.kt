package com.wanwuzhinan.mingchang.ui.phone

import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.colin.library.android.image.glide.GlideImgManager
import com.google.gson.Gson
import com.ssm.comm.config.Constant
import com.ssm.comm.event.MessageEvent
import com.ssm.comm.ext.getCurrentVersionCode
import com.ssm.comm.ext.observeState
import com.ssm.comm.ext.registerBus
import com.ssm.comm.ext.setOnClickNoRepeat
import com.ssm.comm.ext.unregisterBus
import com.ssm.comm.ui.base.BaseActivity
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.ActivityMainBinding
import com.wanwuzhinan.mingchang.ext.getConfigData
import com.wanwuzhinan.mingchang.ext.jumpLoginActivity
import com.wanwuzhinan.mingchang.ext.launchAudioHomeActivity
import com.wanwuzhinan.mingchang.ext.launchExchangeActivity
import com.wanwuzhinan.mingchang.ext.launchQuestionListActivity
import com.wanwuzhinan.mingchang.ext.launchRankHomeActivity
import com.wanwuzhinan.mingchang.ext.launchSettingActivity
import com.wanwuzhinan.mingchang.ext.launchVideoHomeActivity
import com.wanwuzhinan.mingchang.ext.performLaunchH5Agreements
import com.wanwuzhinan.mingchang.ui.pop.NetErrorPop
import com.wanwuzhinan.mingchang.utils.getToken
import com.wanwuzhinan.mingchang.utils.setData
import com.wanwuzhinan.mingchang.vm.UserViewModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : BaseActivity<ActivityMainBinding, UserViewModel>(UserViewModel()) {

    var mLogin=0

    override fun initView() {
        registerBus(this)
        mViewModel.getUserInfo()
        Log.e("TAG", "initView: "+ getToken() )

        mDataBinding.tvVideoTitle.text = getConfigData().home_title1
        mDataBinding.tvAudioTitle.text = getConfigData().home_title2
        mDataBinding.tvQuestionTitle.text = getConfigData().home_title3
        mDataBinding.tvHonorTitle.text = getConfigData().home_title4
        mDataBinding.tvCasting.text = getConfigData().home_title5

        Glide.with(this).asGif().load(R.raw.diqu).into(mDataBinding.ivDiqu)

        mDataBinding.llAd.visibility = if (getConfigData().home_ad.isEmpty()) View.GONE else View.VISIBLE
    }

    override fun initClick() {
        setOnClickNoRepeat(mDataBinding.bgVideo,
            mDataBinding.bgAudio,
            mDataBinding.bgQuestion,
            mDataBinding.ivQuestion,
            mDataBinding.bgHonor,
            mDataBinding.ivHonor,
            mDataBinding.linSetting,
            mDataBinding.rivHead,
            mDataBinding.tvExchange,
            mDataBinding.tvList,
            mDataBinding.tvCasting,
            mDataBinding.ivAd,
            mDataBinding.ivAdClose) {
            when(it){
                mDataBinding.bgVideo->{//视频
                    launchVideoHomeActivity()
                }
                mDataBinding.bgAudio->{//音频
                    launchAudioHomeActivity()
                }
                mDataBinding.bgQuestion,mDataBinding.ivQuestion->{//物理万问
                    launchQuestionListActivity(ConfigApp.TYPE_ASK)
                }
                mDataBinding.bgHonor, mDataBinding.ivHonor->{//龙门科举
                    launchQuestionListActivity(ConfigApp.TYPE_PRACTICE)
                }
                mDataBinding.tvList ->{//荣誉墙
                    launchRankHomeActivity()
                }
                mDataBinding.linSetting,mDataBinding.rivHead ->{//设置
                    launchSettingActivity(0)
                }
                mDataBinding.tvExchange,mDataBinding.tvExchange->{//课程兑换
                    launchExchangeActivity()
                }
                mDataBinding.tvCasting,mDataBinding.tvCasting->{//投屏
                    performLaunchH5Agreements(ConfigApp.SCREEN_CASTING,getConfigData().home_title5)
                }
                mDataBinding.ivAd ->{
                    performLaunchH5Agreements(getConfigData().home_ad_link,"详情")
                }
                mDataBinding.ivAdClose ->{
                    mDataBinding.llAd.visibility = View.GONE
                }
            }
        }
    }

    override fun initRequest() {
        mViewModel.getUserInfoLiveData.observeState(this){
            onSuccess={data, msg ->
                if (data!=null){
                    setData(Constant.USER_INFO, Gson().toJson(data))
                    ConfigApp.question_count_error = data.question_count_error
                    ConfigApp.question_compass = data.question_compass
                    GlideImgManager.get().loadImg(data.headimg, mDataBinding.rivHead, R.mipmap.logo)
                    mDataBinding.tvName.text = data.nickname
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        Log.e("TAG", "onMessageEvent: "+event.type )
        if (event.type == MessageEvent.LOGIN_EXPIRED) {
            if(mLogin==0){
                mLogin=1
                jumpLoginActivity(true)
            }
        }
        if (event.type == MessageEvent.UPDATE_USERINFO){
            mViewModel.getUserInfo()
        }
    }

    override fun onDestroy() {
        unregisterBus(this)
        super.onDestroy()
    }

    var  mIsUpdata = false
    override fun onResume() {
        super.onResume()
        if (getConfigData().android_code.toInt() > getCurrentVersionCode() && !mIsUpdata) {
            window.decorView.post {
                mIsUpdata = true
                NetErrorPop(mActivity).showUpdate(getConfigData().android_update,onSure = {

                }, onCancel = {

                })
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

}