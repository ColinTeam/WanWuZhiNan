package com.wanwuzhinan.mingchang.ui.phone

import com.ad.img_load.setOnClickNoRepeat
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.ext.launchAnswerAskActivity
import com.wanwuzhinan.mingchang.ext.launchAnswerPracticeActivity
import com.wanwuzhinan.mingchang.utils.SkeletonUtils
import com.wanwuzhinan.mingchang.vm.UserViewModel
import com.ssm.comm.ext.observeState
import com.ssm.comm.ui.base.BaseActivity
import com.wanwuzhinan.mingchang.data.QuestionListData
import com.wanwuzhinan.mingchang.databinding.ActivityQuestionListAskBinding
import com.wanwuzhinan.mingchang.ext.getConfigData
import com.wanwuzhinan.mingchang.ext.launchAnswerErrorAskActivity
import com.wanwuzhinan.mingchang.ext.launchExchangeActivity
import com.wanwuzhinan.mingchang.ui.pop.CompassNumPop
import com.wanwuzhinan.mingchang.ui.pop.ExchangeContactPop
import com.wanwuzhinan.mingchang.ui.pop.ExchangeCoursePop
import com.wanwuzhinan.mingchang.ui.pop.NetErrorPop
import java.text.SimpleDateFormat

//物理万问
class QuestionListAskActivity   : BaseActivity<ActivityQuestionListAskBinding, UserViewModel>(UserViewModel()) {

    var mType=0
    var list:MutableList<QuestionListData> = mutableListOf()

    override fun initView() {
        mType=intent.getIntExtra(ConfigApp.INTENT_TYPE,mType)
        initList()

        mViewModel.questionList(mType)

        mDataBinding.tvErrorNum.text = "${ConfigApp.question_count_error}"
    }

    private fun initList(){
        setOnClickNoRepeat(mDataBinding.tvNumber1,
            mDataBinding.tvNumber2,
            mDataBinding.tvNumber3,
            mDataBinding.tvNumber4,
            mDataBinding.tvNumber5,
            mDataBinding.tvNumber6,
            mDataBinding.llError
            ) {
            when(it){
                mDataBinding.tvNumber1->{
                    clickIndex(0);
                }
                mDataBinding.tvNumber2->{
                    clickIndex(1);
                }
                mDataBinding.tvNumber3->{
                    clickIndex(2);
                }
                mDataBinding.tvNumber4->{
//                    clickIndex(3);
                }
                mDataBinding.tvNumber5->{
                    clickIndex(3);
                }
                mDataBinding.tvNumber6->{
                    clickIndex(4);
                }
                mDataBinding.llError->{
                    launchAnswerErrorAskActivity()
//                    CompassNumPop(mActivity, onSure = {
//
//                    },onCancel = {
//
//                    }).showPop("1")
                }
            }
        }
    }

    fun clickIndex(position: Int){
        if (position < list.size) {
            if (list.get(position).has_power.toInt() == 1) {
                if (list.get(position).questionsCount == 0) {
                    NetErrorPop(mActivity).showPop(
                        getConfigData().home_title3,
                        "哎呀，来早啦",
                        "暂时还不能探索"
                    )
                } else {
                    launchAnswerAskActivity(list.get(position)!!.id)
                }
            }else{
                ExchangeCoursePop(mActivity).showPop(onSure = {
                    launchExchangeActivity()
                }, onContact = {
                    ExchangeContactPop(mActivity).showHeightPop()
                })
            }
        }else{
            NetErrorPop(mActivity).showPop(getConfigData().home_title3,"哎呀，来早啦","暂时还不能探索")
        }
    }

    override fun initRequest() {
       mViewModel.questionListLiveData.observeState(this){
           SkeletonUtils.hideList()
           onSuccess={data, msg ->
               list = data!!.list!!;
           }
       }

        mViewModel.getUserInfoLiveData.observeState(this){
            onSuccess={data, msg ->
                ConfigApp.question_count_error = data!!.info.question_count_error.toInt()
                mDataBinding.tvErrorNum.text = "${data!!.info.question_count_error}"
            }
        }
    }

    fun format(position: Long): String {
        val sdf = SimpleDateFormat("mm:ss")
        return sdf.format(position)
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getUserInfo()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_question_list_ask
    }

}