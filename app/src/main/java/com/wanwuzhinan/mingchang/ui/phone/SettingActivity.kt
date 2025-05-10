package com.wanwuzhinan.mingchang.ui.phone

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.chad.library.adapter.base.util.setOnDebouncedItemClick
import com.ssm.comm.ui.base.BaseActivity
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.adapter.SettingAdapter
import com.wanwuzhinan.mingchang.adapter.Viewpager2Adapter
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.data.SettingData
import com.wanwuzhinan.mingchang.databinding.ActivitySettingBinding
import com.wanwuzhinan.mingchang.ext.performLaunchH5Agreements
import com.wanwuzhinan.mingchang.ui.phone.fra.ExchangeCourseFragment
import com.wanwuzhinan.mingchang.ui.phone.fra.ReportFragment
import com.wanwuzhinan.mingchang.ui.phone.fra.SettingFragment
import com.wanwuzhinan.mingchang.vm.UserViewModel

class SettingActivity : BaseActivity<ActivitySettingBinding, UserViewModel>(UserViewModel()) {
    companion object {
        @JvmStatic
        fun start(activity: Activity, index: Int = 0) {
            val starter =
                Intent(activity, SettingActivity::class.java).putExtra(ConfigApp.INTENT_TYPE, index)
            activity.startActivity(starter)
        }
    }

    var currentPost = 0
    lateinit var mAdapter: SettingAdapter

    var mFragmentList: MutableList<Fragment> = mutableListOf()

    var mType = 0

    override fun initView() {

        mType = intent.getIntExtra(ConfigApp.INTENT_TYPE, mType)

        initList()
        initChildView()

    }

    fun initChildView() {

        mFragmentList.add(SettingFragment.instance)
        mFragmentList.add(ExchangeCourseFragment.instance)
        mFragmentList.add(ReportFragment.instance)

        mDataBinding.viewPager.adapter =
            Viewpager2Adapter(mActivity, mFragmentList as ArrayList<Fragment>)
        mDataBinding.viewPager.isUserInputEnabled = false  // 禁用用户滑动

        if (mType == 1) {
            mDataBinding.viewPager.setCurrentItem(1, true)
            mAdapter.getItem(1)!!.select = true
            mAdapter.getItem(0)!!.select = false
            currentPost = 1
        }
        if (mType == 2) {
            mDataBinding.viewPager.setCurrentItem(2, true)
            mAdapter.getItem(3)!!.select = true
            mAdapter.getItem(0)!!.select = false
            currentPost = 3
        }

    }

    private fun initList() {
        mAdapter = SettingAdapter()

        mAdapter.add(SettingData(R.mipmap.ic_setting_file, "个人信息", "", true))
        mAdapter.add(SettingData(R.mipmap.ic_setting_exchange, "兑换记录"))
        mAdapter.add(SettingData(R.mipmap.ic_setting_video, "企业介绍"))
        mAdapter.add(SettingData(R.mipmap.ic_user_report, "用户反馈"))
        mAdapter.add(SettingData(R.mipmap.ic_setting_logout, "设置"))
        mDataBinding.reList.adapter = mAdapter

        mAdapter.setOnDebouncedItemClick { adapter, view, position ->

            when (adapter.getItem(position)!!.title) {
                "个人信息" -> {
                    mAdapter.getItem(currentPost)!!.select = false
                    mAdapter.getItem(position)!!.select = true
                    currentPost = position
                    mAdapter.notifyDataSetChanged()
                    mDataBinding.viewPager.setCurrentItem(0, true)

                }

                "兑换记录" -> {
                    mAdapter.getItem(currentPost)!!.select = false
                    mAdapter.getItem(position)!!.select = true
                    currentPost = position
                    mAdapter.notifyDataSetChanged()

                    mDataBinding.viewPager.setCurrentItem(1, true)
                }


                "企业介绍" -> {
                    performLaunchH5Agreements(ConfigApp.VIDEO_INTRODUCTION, "企业介绍")
                }

                "用户反馈" -> {
                    mAdapter.getItem(currentPost)!!.select = false
                    mAdapter.getItem(position)!!.select = true
                    currentPost = position
                    mAdapter.notifyDataSetChanged()

                    mDataBinding.viewPager.setCurrentItem(2, true)
                }

                "设置" -> {
                    launchActivity(PrivacySetActivity::class.java)
                }

            }
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_setting
    }

}