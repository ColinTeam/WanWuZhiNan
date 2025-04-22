package com.wanwuzhinan.mingchang.ui.phone.fra

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.ad.img_load.glide.manager.GlideImgManager
import com.ad.img_load.setOnClickNoRepeat
import com.chad.library.adapter.base.layoutmanager.QuickGridLayoutManager
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.ssm.comm.ext.dismissLoadingExt
import com.ssm.comm.ext.editChange
import com.ssm.comm.ext.initEditChange
import com.ssm.comm.ext.observeState
import com.ssm.comm.ext.showLoadingExt
import com.ssm.comm.ext.toastSuccess
import com.ssm.comm.media.MediaManager
import com.ssm.comm.ui.base.BaseFragment
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.adapter.ExchangeCourseAdapter
import com.wanwuzhinan.mingchang.adapter.ExchangeGiveAdapter
import com.wanwuzhinan.mingchang.adapter.SettingAdapter
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.data.ProvinceListData
import com.wanwuzhinan.mingchang.databinding.FragmentEditFileBinding
import com.wanwuzhinan.mingchang.databinding.FragmentExchangeCourseBinding
import com.wanwuzhinan.mingchang.entity.UserInfoData
import com.wanwuzhinan.mingchang.ext.performLaunchGoodsDetail
import com.wanwuzhinan.mingchang.ext.performLaunchH5Agreements
import com.wanwuzhinan.mingchang.ui.pop.ChooseCityDialog
import com.wanwuzhinan.mingchang.ui.pop.ChooseGradeDialog
import com.wanwuzhinan.mingchang.ui.pop.EditAddressDialog
import com.wanwuzhinan.mingchang.ui.pop.EditFileDialog
import com.wanwuzhinan.mingchang.ui.pop.ExchangeContactPop
import com.wanwuzhinan.mingchang.utils.SkeletonUtils
import com.wanwuzhinan.mingchang.view.GlideEngine
import com.wanwuzhinan.mingchang.vm.UserViewModel
import java.io.File

//
class ExchangeCourseFragment :
    BaseFragment<FragmentExchangeCourseBinding, UserViewModel>(UserViewModel()) {
    private var TAG = "ExchangeCourseFragment"

    private var mSelectType= -1
    lateinit var mAdapter: ExchangeCourseAdapter
    lateinit var mGiveAdapter: ExchangeGiveAdapter
    lateinit var layoutManager:QuickGridLayoutManager

    companion object {
        val instance: ExchangeCourseFragment by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ExchangeCourseFragment()
        }
    }

    override fun initView() {
        super.initView()
        initList()
        Log.e(TAG, "initView:COURSE_VIDEO",)

//        SkeletonUtils.showList(mDataBinding.reList,mAdapter,R.layout.item_exchange_course_skeleton)
//        mViewModel.exchangeList(mSelectType)
        mDataBinding.tvVideo.setBackgroundResource(R.drawable.shape_fc982e_16)
        mDataBinding.tvVideo.setTextColor(resources!!.getColor(R.color.white))
        SkeletonUtils.showList(mDataBinding.reList,mAdapter,R.layout.item_exchange_course_skeleton)
        layoutManager.spanCount = 1
        mViewModel.exchangeCodeList(1)
        mDataBinding.reList.adapter=mAdapter
    }

    private fun initList(){
        mAdapter= ExchangeCourseAdapter()

        layoutManager = mDataBinding.reList.layoutManager as QuickGridLayoutManager
        mAdapter.setEmptyViewLayout(mActivity,R.layout.view_empty_list)
        mDataBinding.reList.adapter=mAdapter

        mGiveAdapter = ExchangeGiveAdapter()
        mGiveAdapter.setEmptyViewLayout(mActivity,R.layout.view_empty_list)

        mAdapter.setOnItemClickListener { adapter, view, position ->
            if (mAdapter.getItem(position)!!.is_goods == 1) {
                performLaunchGoodsDetail(mAdapter.getItem(position)!!.goods_idArr.get(0))
            }
        }
        mGiveAdapter.setOnItemClickListener { adapter, view, position ->
            performLaunchGoodsDetail(mGiveAdapter.getItem(position)!!.id)
        }
    }

    private fun setSelect(type:Int){
        if(mSelectType==type) return
        mSelectType=type
        Log.e(TAG, "setSelect: 1111${type}" )
        mDataBinding.tvVideo.setBackgroundResource(if(type==ConfigApp.COURSE_VIDEO) R.drawable.shape_fc982e_16 else R.drawable.shape_ffeacc_16)
        mDataBinding.tvAudio.setBackgroundResource(if(type==ConfigApp.COURSE_AUDIO) R.drawable.shape_fc982e_16 else  R.drawable.shape_ffeacc_16)
        mDataBinding.tvGive.setBackgroundResource(if(type==ConfigApp.COURSE_GIVE) R.drawable.shape_fc982e_16 else  R.drawable.shape_ffeacc_16)

        mDataBinding.tvVideo.setTextColor(if(type==ConfigApp.COURSE_VIDEO) resources!!.getColor(R.color.white) else resources!!.getColor(R.color.color_ff9424))
        mDataBinding.tvAudio.setTextColor(if(type==ConfigApp.COURSE_AUDIO) resources!!.getColor(R.color.white) else resources!!.getColor(R.color.color_ff9424))
        mDataBinding.tvGive.setTextColor(if(type==ConfigApp.COURSE_GIVE) resources!!.getColor(R.color.white) else resources!!.getColor(R.color.color_ff9424))

        if (mSelectType == ConfigApp.COURSE_GIVE){
            SkeletonUtils.showList(mDataBinding.reList,mGiveAdapter,R.layout.item_exchange_course_skeleton)
            mViewModel.giveList()
            layoutManager.spanCount = 2
            mDataBinding.reList.adapter=mGiveAdapter
        }else{
            SkeletonUtils.showList(mDataBinding.reList,mAdapter,R.layout.item_exchange_course_skeleton)
            layoutManager.spanCount = 1
            mViewModel.exchangeCodeList(mSelectType)
            mDataBinding.reList.adapter=mAdapter
        }
    }

    override fun initClick() {

        setOnClickNoRepeat(
            mDataBinding.tvAudio,
            mDataBinding.tvVideo,
            mDataBinding.tvGive,
            mDataBinding.tvAddress,
            mDataBinding.tvKefu,
            ){
            when(it){
                mDataBinding.tvAudio ->{
                    setSelect(ConfigApp.COURSE_AUDIO)
                }
                mDataBinding.tvVideo ->{
                    setSelect(ConfigApp.COURSE_VIDEO)
                }
                mDataBinding.tvGive ->{
                    setSelect(ConfigApp.COURSE_GIVE)
                }
                mDataBinding.tvKefu ->{
                    ExchangeContactPop(mActivity).showHeightPop()
                }
                mDataBinding.tvAddress ->{
                    EditAddressDialog().show(
                        getCurrentActivity().supportFragmentManager,
                        "EditAddressDialog"
                    )
                }
            }
        }
    }

    override fun initRequest() {
//        mViewModel.exchangeListLiveData.observeState(this){
//            SkeletonUtils.hideList()
//            onSuccess={data, msg ->
//                mAdapter.isEmptyViewEnable=true
//                mAdapter.submitList(data!!.list)
//            }
//        }
        mViewModel.exchangeCodeLiveData.observeState(this){
            SkeletonUtils.hideList()
            onSuccess={data, msg ->
                mAdapter.isEmptyViewEnable=true
                mAdapter.submitList(data!!.list)
            }
        }
        mViewModel.giveListLiveData.observeState(this){
            SkeletonUtils.hideList()
            onSuccess={data, msg ->
                mGiveAdapter.isEmptyViewEnable=true
                mGiveAdapter.submitList(data!!.list)
            }
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_exchange_course
    }



}