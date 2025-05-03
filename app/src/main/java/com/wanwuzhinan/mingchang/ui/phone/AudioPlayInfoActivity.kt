package com.wanwuzhinan.mingchang.ui.phone

import android.text.Html
import com.ssm.comm.ui.base.BaseActivity
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.ActivityAudioInfoBinding
import com.wanwuzhinan.mingchang.vm.UserViewModel

//音频播放
class AudioPlayInfoActivity :
    BaseActivity<ActivityAudioInfoBinding, UserViewModel>(UserViewModel()) {


    override fun initView() {
        val data = intent.getStringExtra(ConfigApp.INTENT_DATA)
        val title = intent.getStringExtra(ConfigApp.INTENT_ID)


        mDataBinding.tvTitle.text = title
        mDataBinding.tvContent.text = Html.fromHtml(data, 0)

    }


    override fun getLayoutId(): Int {
        return R.layout.activity_audio_info
    }

}