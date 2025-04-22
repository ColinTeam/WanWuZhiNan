package com.wanwuzhinan.mingchang.ui.phone

import com.ad.img_load.setOnClickNoRepeat
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.adapter.ExchangeCourseAdapter
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.ActivityExchangeCourseBinding
import com.wanwuzhinan.mingchang.utils.SkeletonUtils
import com.wanwuzhinan.mingchang.vm.UserViewModel
import com.ssm.comm.ext.observeState
import com.ssm.comm.ui.base.BaseActivity

//兑换记录
class ExchangeCourseActivity  : BaseActivity<ActivityExchangeCourseBinding, UserViewModel>(UserViewModel()) {

    private var mSelectType=ConfigApp.COURSE_VIDEO
    lateinit var mAdapter:ExchangeCourseAdapter

    override fun initView() {
        initList()

        setSelect(ConfigApp.COURSE_VIDEO)
        SkeletonUtils.showList(mDataBinding.reList,mAdapter,R.layout.item_exchange_course_skeleton)
        mViewModel.exchangeList(mSelectType)
    }

    private fun initList(){
        mAdapter= ExchangeCourseAdapter()

        mAdapter.setEmptyViewLayout(this,R.layout.view_empty_list)
        mDataBinding.reList.adapter=mAdapter
    }

    override fun initClick() {
        setOnClickNoRepeat(mDataBinding.ivBack,mDataBinding.tvVideo,mDataBinding.tvAudio) {
            when(it){
                mDataBinding.ivBack->{//
                    finish()
                }
                mDataBinding.tvVideo->{//
                   setSelect(ConfigApp.COURSE_VIDEO)
                }
                mDataBinding.tvAudio->{//
                    setSelect(ConfigApp.COURSE_AUDIO)
                }
            }
        }
    }

    private fun setSelect(type:Int){
        if(mSelectType==type) return
        mSelectType=type

        var select=type==ConfigApp.COURSE_VIDEO

        mDataBinding.tvVideo.setBackgroundResource(if(select) R.drawable.shape_51a8f8_6 else R.color.transparents)
        mDataBinding.tvAudio.setBackgroundResource(if(!select) R.drawable.shape_51a8f8_6 else R.color.transparents)

        mDataBinding.tvVideo.setTextColor(if(select) resources!!.getColor(R.color.white) else resources!!.getColor(R.color.color_333333))
        mDataBinding.tvAudio.setTextColor(if(!select) resources!!.getColor(R.color.white) else resources!!.getColor(R.color.color_333333))

        SkeletonUtils.showList(mDataBinding.reList,mAdapter,R.layout.item_exchange_course_skeleton)
        mViewModel.exchangeList(mSelectType)
    }

    override fun initRequest() {
//        mViewModel.exchangeListLiveData.observeState(this){
//            SkeletonUtils.hideList()
//            onSuccess={data, msg ->
//                mAdapter.isEmptyViewEnable=true
//                mAdapter.submitList(data!!.list)
//            }
//        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_exchange_course
    }

}