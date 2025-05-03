package com.wanwuzhinan.mingchang.ui.phone

import android.view.View
import android.view.WindowManager
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.ssm.comm.ui.base.BaseActivity
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.ActivityQuestionVideoBinding
import com.wanwuzhinan.mingchang.vm.UserViewModel

class QuestionVideoActivity :
    BaseActivity<ActivityQuestionVideoBinding, UserViewModel>(UserViewModel()) {

    var mUrl = ""
    var mName = ""
    var orientationUtils: OrientationUtils? = null

    override fun initView() {
        mName = intent.getStringExtra(ConfigApp.INTENT_ID).toString()
        mUrl = intent.getStringExtra(ConfigApp.INTENT_DATA).toString()
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)

        initVideo()
    }

    private fun initVideo() {
        //设置返回键
        mDataBinding.detailPlayer.backButton.visibility = View.VISIBLE
        orientationUtils = OrientationUtils(this, mDataBinding.detailPlayer)
        mDataBinding.detailPlayer.fullscreenButton.setOnClickListener(View.OnClickListener {

            orientationUtils?.resolveByClick()
            //finish();
        })
        mDataBinding.detailPlayer.backButton.setOnClickListener {
            finish()
        }
        mDataBinding.detailPlayer.setIsTouchWiget(true)
        mDataBinding.detailPlayer.backButton.setOnClickListener(View.OnClickListener { this.onBackPressedDispatcher.onBackPressed() })
        mDataBinding.detailPlayer.isNeedOrientationUtils = false
        mDataBinding.detailPlayer.fullscreenButton.visibility = View.GONE

        mDataBinding.detailPlayer.setUp(mUrl, false, mName)
        mDataBinding.detailPlayer.startPlayLogic()
    }

    override fun onDestroy() {
        mDataBinding.detailPlayer.setVideoAllCallBack(null)
        super.onDestroy()

        GSYVideoManager.releaseAllVideos()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_question_video
    }

}