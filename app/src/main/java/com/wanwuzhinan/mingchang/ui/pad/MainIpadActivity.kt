package com.wanwuzhinan.mingchang.ui.pad

import android.util.Log
import android.view.View
import com.ad.img_load.glide.manager.GlideImgManager
import com.ad.img_load.setOnClickNoRepeat
import com.bumptech.glide.Glide
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.ext.*
import com.wanwuzhinan.mingchang.ui.pop.EditFileDialog
import com.wanwuzhinan.mingchang.vm.UserViewModel
import com.google.gson.Gson
import com.ssm.comm.config.Constant
import com.ssm.comm.event.MessageEvent
import com.ssm.comm.ext.*
import com.ssm.comm.ui.base.BaseActivity
import com.wanwuzhinan.mingchang.databinding.ActivityMainIpadHeightBinding
import com.wanwuzhinan.mingchang.ui.pop.NetErrorPop
import me.jessyan.autosize.internal.CustomAdapt
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainIpadActivity : BaseActivity<ActivityMainIpadHeightBinding, UserViewModel>(UserViewModel()),CustomAdapt{

    var mLogin=0

    override fun initView() {
        registerBus(this)

//        AnimationUtils.rotationAnimation(mDataBinding.ivDiqu)
        mViewModel.getUserInfo()

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
                setData(Constant.USER_INFO, Gson().toJson(data!!.info))
                ConfigApp.question_count_error = data.info.question_count_error.toInt()
                ConfigApp.question_compass = data.info.question_compass.toInt()
                GlideImgManager.get().loadImg(data.info.headimg,mDataBinding!!.rivHead,R.mipmap.logo)
                mDataBinding.tvName.text = data.info.nickname
                if(data.info.truename.isEmpty() && getConfigData().android_code.toInt() >= getCurrentVersionCode()) {
                    EditFileDialog(data.info).show(
                        getCurrentActivity().supportFragmentManager,
                        "EditFileDialog"
                    )
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
        super.onDestroy()
//        unregisterBus(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main_ipad_height
    }

    override fun isBaseOnWidth(): Boolean {
        return false
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

    override fun getSizeInDp(): Float {
        return 1200f;
    }


}