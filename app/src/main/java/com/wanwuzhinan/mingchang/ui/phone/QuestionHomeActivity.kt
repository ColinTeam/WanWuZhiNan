package com.wanwuzhinan.mingchang.ui.phone

import com.colin.library.android.utils.ext.onClick
import com.ssm.comm.ui.base.BaseActivity
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.ActivityQuestionHomeBinding
import com.wanwuzhinan.mingchang.ext.launchQuestionListActivity
import com.wanwuzhinan.mingchang.vm.UserViewModel

class QuestionHomeActivity :
    BaseActivity<ActivityQuestionHomeBinding, UserViewModel>(UserViewModel()) {


    var mType = 1

    override fun initView() {

    }

    override fun initClick() {
        onClick(mDataBinding.llAsk, mDataBinding.llPractice) {
            when (it) {
                mDataBinding.llAsk -> {//问
                    mType = ConfigApp.TYPE_ASK;
                    launchQuestionListActivity(mType)
                }

                mDataBinding.llPractice -> {//练
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