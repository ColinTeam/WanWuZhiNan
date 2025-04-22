package com.wanwuzhinan.mingchang.ui.phone

import com.ad.img_load.setOnClickNoRepeat
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.adapter.HonorHomeAdapter
import com.wanwuzhinan.mingchang.databinding.ActivityHonorHomeBinding
import com.wanwuzhinan.mingchang.ext.*
import com.wanwuzhinan.mingchang.ui.pop.SharePop
import com.wanwuzhinan.mingchang.utils.SaveImageUtils
import com.wanwuzhinan.mingchang.utils.SaveListener
import com.wanwuzhinan.mingchang.view.CardScaleHelper
import com.wanwuzhinan.mingchang.vm.UserViewModel
import com.ssm.comm.ext.observeState
import com.ssm.comm.ui.base.BaseActivity
import com.wanwuzhinan.mingchang.data.MedalListData

class HonorHomeActivity  : BaseActivity<ActivityHonorHomeBinding, UserViewModel>(UserViewModel()) {

    lateinit var mAdapter:HonorHomeAdapter
    lateinit var mCardScaleHelper: CardScaleHelper

    override fun initView() {
//        initList()

        showBaseLoading()
        mViewModel.medalList(0)
    }

    private fun initList(list : List<MedalListData>?){
        mAdapter= HonorHomeAdapter()
        mAdapter.submitList(list)
        // mRecyclerView绑定scale效果
        mCardScaleHelper = CardScaleHelper()
        mCardScaleHelper.setCurrentItemPos(2)
        mCardScaleHelper.attachToRecyclerView(mDataBinding.reList)

        mCardScaleHelper.currentItemPos=0

        mDataBinding.reList.adapter=mAdapter

        mCardScaleHelper.setOnScrollListener {

        }
    }

    override fun initClick() {
        setOnClickNoRepeat(mDataBinding.linMy,mDataBinding.tvShare) {
            when(it){
                mDataBinding.linMy->{
                    launchHonorListActivity()
                }
                mDataBinding.tvShare->{
                    SharePop(this).showPop(mAdapter.getItem(mCardScaleHelper.currentItemPos)!!){view,type ->
                        SaveImageUtils.saveBitmap(it,object :SaveListener{
                            override fun show() {
                                showBaseLoading()
                            }

                            override fun dismiss() {
                                dismissBaseLoading()
                            }
                        })
                    }
                }
            }
        }
    }

    override fun initRequest() {
        mViewModel.medalListLiveData.observeState(this){
            onSuccess={data, msg ->
                mDataBinding.tvShare.visible(true)
                initList(data!!.list)
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_honor_home
    }
}