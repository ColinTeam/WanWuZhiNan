package com.wanwuzhinan.mingchang.ui.phone

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.colin.library.android.utils.ResourcesUtil
import com.colin.library.android.utils.ext.onClick
import com.ssm.comm.ext.getScreenHeight2
import com.ssm.comm.ext.getScreenWidth2
import com.ssm.comm.ui.base.BaseActivity
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.adapter.Viewpager2Adapter
import com.wanwuzhinan.mingchang.databinding.ActivityRankBinding
import com.wanwuzhinan.mingchang.ui.pad.RankIPadFragment
import com.wanwuzhinan.mingchang.ui.phone.fra.RankFragment
import com.wanwuzhinan.mingchang.vm.UserViewModel

class RankActivity : BaseActivity<ActivityRankBinding, UserViewModel>(UserViewModel()) {
    companion object {
        @JvmStatic
        fun start(activity: Activity) {
            val starter = Intent(activity, RankActivity::class.java)
            activity.startActivity(starter)
        }
    }
    var mFragmentList: MutableList<Fragment> = mutableListOf()

    override fun initView() {

        initChildView()

    }

    fun initChildView() {


        val dd = getScreenWidth2() / (getScreenHeight2() * 1.0f)
        if (dd >= (16 / 9.0)) {
            mFragmentList.add(RankFragment.instance)
        } else {
            mFragmentList.add(RankIPadFragment.instance)
        }
        mFragmentList.add(HonorListFragment.instance(0))
        mFragmentList.add(HonorListFragment.instance(1))

        mDataBinding.viewPager.adapter =
            Viewpager2Adapter(mActivity, mFragmentList as ArrayList<Fragment>)
        mDataBinding.viewPager.isUserInputEnabled = false  // 禁用用户滑动

        setSelect(2)

    }

    override fun initClick() {
        onClick(mDataBinding.tvRank, mDataBinding.tvCard, mDataBinding.tvHonor) {
            when (it) {
                mDataBinding.tvRank -> {
                    setSelect(1)

                }

                mDataBinding.tvCard -> {
                    setSelect(3)
                }

                mDataBinding.tvHonor -> {
                    setSelect(2)
                }
            }
        }
    }

    private fun setSelect(type: Int) {
        mDataBinding.viewPager.setCurrentItem(type - 1, true)

        mDataBinding.tvRank.setBackgroundResource(if (type == 1) R.drawable.shape_ffffff_18 else R.drawable.shape_ffffff03_18)
        mDataBinding.tvHonor.setBackgroundResource(if (type == 2) R.drawable.shape_ffffff_18 else R.drawable.shape_ffffff03_18)
        mDataBinding.tvCard.setBackgroundResource(if (type == 3) R.drawable.shape_ffffff_18 else R.drawable.shape_ffffff03_18)
        val selectColor =  ResourcesUtil.getColor(this, R.color.color_ff9424)
        val normalColor = ResourcesUtil.getColor(this, R.color.white)
        mDataBinding.tvRank.setTextColor(if (type == 1) selectColor else normalColor)
        mDataBinding.tvHonor.setTextColor(if (type == 2) selectColor else normalColor)
        mDataBinding.tvCard.setTextColor(if (type == 3) selectColor else normalColor)

    }


    override fun getLayoutId(): Int {
        return R.layout.activity_rank
    }

}