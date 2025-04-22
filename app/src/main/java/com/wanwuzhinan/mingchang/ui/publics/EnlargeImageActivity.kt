package com.wanwuzhinan.mingchang.ui.publics

import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.adapter.EnlargeImageAdapter
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.ActivityEnlargeImageBinding
import com.wanwuzhinan.mingchang.view.PagingImageHelper
import com.wanwuzhinan.mingchang.vm.SplashViewModel
import com.ssm.comm.ui.base.BaseActivity

class EnlargeImageActivity : BaseActivity<ActivityEnlargeImageBinding, SplashViewModel>(SplashViewModel()) {

    lateinit var mAdapter: EnlargeImageAdapter
    lateinit var mList:MutableList<String>
    var mPosition=0

    override fun initView() {
        mList= intent.getStringArrayListExtra(ConfigApp.INTENT_DATA)!!
        mPosition=intent.getIntExtra(ConfigApp.INTENT_TYPE,0)

        initList()
    }

    private fun initList(){
        mAdapter= EnlargeImageAdapter()
        mAdapter.submitList(mList)

        var scrollHelper= PagingImageHelper()
        scrollHelper.setUpRecycleView(mDataBinding.reList)

        mDataBinding.reList.adapter=mAdapter
        mDataBinding.reList.isHorizontalScrollBarEnabled=true
        mDataBinding.reList.scrollToPosition(mPosition)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_enlarge_image
    }
}