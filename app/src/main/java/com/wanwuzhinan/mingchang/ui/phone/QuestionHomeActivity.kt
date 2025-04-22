package com.wanwuzhinan.mingchang.ui.phone

import com.ad.img_load.setOnClickNoRepeat
import com.chad.library.adapter.base.util.setOnDebouncedItemClick
import com.ssm.comm.ext.observeState
import com.ssm.comm.ext.setData
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.ActivityQuestionHomeBinding
import com.wanwuzhinan.mingchang.ext.launchQuestionListActivity
import com.wanwuzhinan.mingchang.vm.UserViewModel
import com.ssm.comm.ui.base.BaseActivity
import com.wanwuzhinan.mingchang.ext.launchQuestionVideoActivity
import com.wanwuzhinan.mingchang.utils.SkeletonUtils

class QuestionHomeActivity  : BaseActivity<ActivityQuestionHomeBinding, UserViewModel>(UserViewModel()) {


    var mType = 1

    override fun initView() {

    }

    override fun initClick() {
        setOnClickNoRepeat(mDataBinding.llAsk,mDataBinding.llPractice) {
            when(it){
                mDataBinding.llAsk->{//问
                    mType = ConfigApp.TYPE_ASK;
                    launchQuestionListActivity(mType)
                }
                mDataBinding.llPractice->{//练
                    mType = ConfigApp.TYPE_PRACTICE;
                    launchQuestionListActivity(mType)
                }
            }
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_question_home
    }

}