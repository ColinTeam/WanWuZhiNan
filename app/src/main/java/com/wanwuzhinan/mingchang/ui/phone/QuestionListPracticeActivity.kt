package com.wanwuzhinan.mingchang.ui.phone

import com.colin.library.android.utils.ext.onClick
import com.ssm.comm.ext.observeState
import com.ssm.comm.ui.base.BaseActivity
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.data.QuestionListData
import com.wanwuzhinan.mingchang.databinding.ActivityQuestionListPracticeBinding
import com.wanwuzhinan.mingchang.ext.getConfigData
import com.wanwuzhinan.mingchang.ext.launchAnswerPracticeActivity
import com.wanwuzhinan.mingchang.ext.launchExchangeActivity
import com.wanwuzhinan.mingchang.ui.pop.ExchangeContactPop
import com.wanwuzhinan.mingchang.ui.pop.ExchangeCoursePop
import com.wanwuzhinan.mingchang.ui.pop.NetErrorPop
import com.wanwuzhinan.mingchang.utils.SkeletonUtils
import com.wanwuzhinan.mingchang.vm.UserViewModel
import java.text.SimpleDateFormat

//龙门题库
class QuestionListPracticeActivity   : BaseActivity<ActivityQuestionListPracticeBinding, UserViewModel>(UserViewModel()) {

    var mType=0
    var list:MutableList<QuestionListData> = mutableListOf()

    override fun initView() {
        mType=intent.getIntExtra(ConfigApp.INTENT_TYPE,mType)
        initList()

        mViewModel.questionList(mType)
    }

    private fun initList(){
        onClick(mDataBinding.tvNumber1,
            mDataBinding.tvNumber2,
            mDataBinding.tvNumber3,
            mDataBinding.tvNumber4,
            mDataBinding.tvNumber5,
            mDataBinding.tvNumber6
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
            }
        }
    }

    fun clickIndex(position: Int){
        if (position < list.size) {
            if (list.get(position).has_power.toInt() == 1) {
                if (list.get(position).questionsCount == 0) {
                    NetErrorPop(mActivity).showPop(
                        getConfigData().home_title4,
                        "哎呀，来早啦",
                        "暂时还不能探索"
                    )
                } else {
                    launchAnswerPracticeActivity(list.get(position)!!.id)
                }
            }else {
                ExchangeCoursePop(mActivity).showPop(onSure = {
                    launchExchangeActivity()
                }, onContact = {
                    ExchangeContactPop(mActivity).showHeightPop()
                })
            }
        }else{
            NetErrorPop(mActivity).showPop(getConfigData().home_title4,"哎呀，来早啦","暂时还不能探索")
        }
    }

    override fun initRequest() {
       mViewModel.questionListLiveData.observeState(this){
           SkeletonUtils.hideList()
           onSuccess={data, msg ->
               list = data!!.list!!;
           }
       }
    }

    fun format(position: Long): String {
        val sdf = SimpleDateFormat("mm:ss")
        return sdf.format(position)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_question_list_practice
    }

}