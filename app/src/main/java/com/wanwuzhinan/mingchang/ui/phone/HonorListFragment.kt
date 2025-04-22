package com.wanwuzhinan.mingchang.ui.phone

import android.os.Bundle
import com.chad.library.adapter.base.layoutmanager.QuickGridLayoutManager
import com.chad.library.adapter.base.util.setOnDebouncedItemClick
import com.google.gson.Gson
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.adapter.HonorListAdapter
import com.wanwuzhinan.mingchang.databinding.FragmentHonorListBinding
import com.wanwuzhinan.mingchang.vm.UserViewModel
import com.ssm.comm.ext.observeState
import com.ssm.comm.ui.base.BaseFragment
import com.wanwuzhinan.mingchang.adapter.HonorCardAdapter
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.ext.launchAudioPlayInfoActivity

class HonorListFragment  : BaseFragment<FragmentHonorListBinding, UserViewModel>(UserViewModel()) {

    var mType=0
    lateinit var mAdapter: HonorListAdapter
    lateinit var mCardAdapter: HonorCardAdapter

    companion object {
        @JvmStatic
        fun instance(index:Int) = HonorListFragment().apply {
            arguments = Bundle().apply { putInt("index", index) }
        }

        val instance: HonorListFragment by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            HonorListFragment()
        }
    }

    override fun initView() {
        mType= requireArguments().getInt("index",0)
        initList()

        showBaseLoading()
        getData()
    }

    private fun getData(){
        if(mType==0){
            mViewModel.medalList(0)
        }else{
            mViewModel.cardList(0)
        }
    }

    override fun initClick() {
        mDataBinding.srl.setOnRefreshListener {
            getData()
        }
    }

    private fun initList(){
        mAdapter=HonorListAdapter()

        mAdapter.setEmptyViewLayout(mActivity,R.layout.view_empty_list)

        mAdapter.setOnDebouncedItemClick{adapter, view, position ->
            if (mAdapter.getItem(position)!!.is_has == 0) {
                launchAudioPlayInfoActivity(
                    mAdapter.getItem(position)!!.desc,
                    mAdapter.getItem(position)!!.name
                )
            }else{
                launchActivity(
                    HonorDetailActivity::class.java,
                    Pair(ConfigApp.INTENT_DATA, Gson().toJson(mAdapter.getItem(position)))
                )
            }
        }

        mCardAdapter = HonorCardAdapter()
        mCardAdapter.setEmptyViewLayout(mActivity,R.layout.view_empty_list)
        mCardAdapter.setOnDebouncedItemClick{adapter, view, position ->
            launchAudioPlayInfoActivity(
                mCardAdapter.getItem(position)!!.desc,
                mCardAdapter.getItem(position)!!.name
            )
        }

        if (mType == 0){
            var manager = QuickGridLayoutManager(mActivity,5)
            mDataBinding.reList.layoutManager = manager
            mDataBinding.reList.adapter=mAdapter
        }else{
            var manager = QuickGridLayoutManager(mActivity,4)
            mDataBinding.reList.layoutManager = manager
            mDataBinding.reList.adapter=mCardAdapter
        }

    }

    override fun initRequest() {
        mViewModel.medalListLiveData.observeState(this){
            mDataBinding.srl.finishRefresh()
            onSuccess={data, msg ->
                if (mType == 0) {
                    mAdapter.isEmptyViewEnable = true
                    mAdapter.submitList(data!!.list)
                }else{
                    mCardAdapter.isEmptyViewEnable = true
                    mCardAdapter.submitList(data!!.list)
                }
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_honor_list
    }
}