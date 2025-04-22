package com.wanwuzhinan.mingchang.ui.phone

import android.text.TextUtils
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.ad.img_load.setOnClickNoRepeat
import com.chad.library.adapter.base.util.setOnDebouncedItemClick
import com.ssm.comm.config.Constant
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.adapter.SettingAdapter
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.data.SettingData
import com.wanwuzhinan.mingchang.databinding.ActivitySettingBinding
import com.wanwuzhinan.mingchang.entity.UserInfoData
import com.wanwuzhinan.mingchang.ext.*
import com.wanwuzhinan.mingchang.ui.pop.ChooseGradeDialog
import com.wanwuzhinan.mingchang.ui.pop.ChooseSexDialog
import com.wanwuzhinan.mingchang.vm.UserViewModel
import com.ssm.comm.ext.clearAllData
import com.ssm.comm.ext.getScreenHeight2
import com.ssm.comm.ext.getScreenWidth2
import com.ssm.comm.ext.observeState
import com.ssm.comm.ext.setData
import com.ssm.comm.ext.toastSuccess
import com.ssm.comm.global.AppActivityManager
import com.ssm.comm.ui.base.BaseActivity
import com.wanwuzhinan.mingchang.adapter.ExchangeCourseAdapter
import com.wanwuzhinan.mingchang.adapter.ExchangeCourseSetAdapter
import com.wanwuzhinan.mingchang.adapter.TabViewpagerAdapter
import com.wanwuzhinan.mingchang.adapter.Viewpager2Adapter
import com.wanwuzhinan.mingchang.data.ProvinceListData
import com.wanwuzhinan.mingchang.databinding.ActivityRankBinding
import com.wanwuzhinan.mingchang.ui.pad.RankIPadFragment
import com.wanwuzhinan.mingchang.ui.phone.fra.ExchangeCourseFragment
import com.wanwuzhinan.mingchang.ui.phone.fra.RankFragment
import com.wanwuzhinan.mingchang.ui.phone.fra.ReportFragment
import com.wanwuzhinan.mingchang.ui.phone.fra.SettingFragment
import com.wanwuzhinan.mingchang.ui.pop.ExchangeContactPop
import com.wanwuzhinan.mingchang.ui.pop.ExchangeCoursePop
import com.wanwuzhinan.mingchang.utils.ChooseCityUtils
import com.wanwuzhinan.mingchang.utils.SkeletonUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RankActivity: BaseActivity<ActivityRankBinding, UserViewModel>(UserViewModel()) {

    var mFragmentList: MutableList<Fragment> = mutableListOf()

    override fun initView() {

        initChildView()

    }

    fun initChildView(){


        val dd = getScreenWidth2() / (getScreenHeight2() *1.0f)
        if (dd >= (16/9.0)) {
            mFragmentList.add(RankFragment.instance)
        }else{
            mFragmentList.add(RankIPadFragment.instance)
        }
        mFragmentList.add(HonorListFragment.instance(0))
        mFragmentList.add(HonorListFragment.instance(1))

        mDataBinding.viewPager.adapter= Viewpager2Adapter(mActivity, mFragmentList as ArrayList<Fragment>)
        mDataBinding.viewPager.isUserInputEnabled = false  // 禁用用户滑动

        setSelect(2)

    }

    override fun initClick() {
        setOnClickNoRepeat(mDataBinding.tvRank,mDataBinding.tvCard,mDataBinding.tvHonor){
            when(it){
                mDataBinding.tvRank->{
                    setSelect(1)

                }
                mDataBinding.tvCard->{
                    setSelect(3)
                }
                mDataBinding.tvHonor->{
                    setSelect(2)
                }
            }
        }
    }

    private fun setSelect(type:Int){
        mDataBinding.viewPager.setCurrentItem(type-1,true)

        mDataBinding.tvRank.setBackgroundResource(if(type==1) R.drawable.shape_ffffff_18 else R.drawable.shape_ffffff03_18)
        mDataBinding.tvHonor.setBackgroundResource(if(type==2) R.drawable.shape_ffffff_18 else  R.drawable.shape_ffffff03_18)
        mDataBinding.tvCard.setBackgroundResource(if(type==3) R.drawable.shape_ffffff_18 else  R.drawable.shape_ffffff03_18)

        mDataBinding.tvRank.setTextColor(if(type==1) resources!!.getColor(R.color.color_ff9424) else resources!!.getColor(R.color.white))
        mDataBinding.tvHonor.setTextColor(if(type==2) resources!!.getColor(R.color.color_ff9424) else resources!!.getColor(R.color.white))
        mDataBinding.tvCard.setTextColor(if(type==3) resources!!.getColor(R.color.color_ff9424) else resources!!.getColor(R.color.white))

    }


    override fun getLayoutId(): Int {
        return R.layout.activity_rank
    }

}