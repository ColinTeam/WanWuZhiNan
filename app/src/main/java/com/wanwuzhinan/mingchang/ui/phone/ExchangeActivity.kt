package com.wanwuzhinan.mingchang.ui.phone

import android.graphics.Color
import com.ad.img_load.setOnClickNoRepeat
import com.ssm.comm.event.MessageEvent
import com.ssm.comm.ext.editChange
import com.ssm.comm.ext.initEditChange
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.databinding.ActivityExchangeBinding
import com.wanwuzhinan.mingchang.ext.editTips
import com.wanwuzhinan.mingchang.ext.launchExchangeCourseActivity
import com.wanwuzhinan.mingchang.ui.pop.ExchangeContactPop
import com.wanwuzhinan.mingchang.vm.UserViewModel
import com.ssm.comm.ext.observeState
import com.ssm.comm.ext.post
import com.ssm.comm.ui.base.BaseActivity
import com.wanwuzhinan.mingchang.ext.launchSettingActivity
import com.wanwuzhinan.mingchang.ui.pop.NetErrorPop

//课程兑换
class ExchangeActivity : BaseActivity<ActivityExchangeBinding, UserViewModel>(UserViewModel()) {

    override fun initView() {

        initEditChange(mDataBinding.edCode){
            var editTips= editChange(mDataBinding.edCode)
            mDataBinding.tvNow.setBackgroundResource(if(editTips) R.drawable.bg_btn_sure else R.drawable.shape_c9c6c333_24)
            mDataBinding.tvNow.setTextColor(if(editTips) Color.parseColor("#FFFFFF") else Color.parseColor("#AAAAAA"))
        }
    }

    override fun initClick() {
        setOnClickNoRepeat(mDataBinding.tvRecord,mDataBinding.tvContact,mDataBinding.tvNow) {
            when(it){
                mDataBinding.tvRecord->{//兑换记录
                    launchSettingActivity(1)
                }
                mDataBinding.tvContact->{//联系老师
                    ExchangeContactPop(this).showHeightPop()
                }
                mDataBinding.tvNow->{//立即兑换
                    var tips= editTips(mDataBinding.edCode)
                    if(!tips) return@setOnClickNoRepeat

                    showBaseLoading()
                    mViewModel.courseExchange(mDataBinding.edCode.text.toString())
                }
            }
        }
    }

    override fun initRequest() {
        mViewModel.courseExchangeLiveData.observeState(this){
            onSuccess={data, msg ->
                post(MessageEvent.EXCHANGE_COURSE)
                NetErrorPop(mActivity).showPop(1,"兑换课程",msg)
//                finish()
            }
            onDataEmpty2 = {msg ->
                post(MessageEvent.EXCHANGE_COURSE)
                NetErrorPop(mActivity).showPop(1,"兑换课程",msg)
//                finish()
            }
            onFailed ={ code, msg ->
                if (msg != null) {
                    NetErrorPop(mActivity).showPop(0,"兑换课程",msg)
                }
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_exchange
    }
}