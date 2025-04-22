package com.wanwuzhinan.mingchang.ui.phone

import android.text.Html
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.vm.UserViewModel
import com.ssm.comm.ui.base.BaseActivity
import com.wanwuzhinan.mingchang.databinding.ActivityAudioInfoBinding

//音频播放
class AudioPlayInfoActivity : BaseActivity<ActivityAudioInfoBinding, UserViewModel>(UserViewModel()) {



    override fun initView() {
        val data = intent.getStringExtra(ConfigApp.INTENT_DATA)
        val title = intent.getStringExtra(ConfigApp.INTENT_ID)


        mDataBinding.tvTitle.text = title
        mDataBinding.tvContent.text = Html.fromHtml(data)

    }


    override fun getLayoutId(): Int {
        return R.layout.activity_audio_info
    }

}