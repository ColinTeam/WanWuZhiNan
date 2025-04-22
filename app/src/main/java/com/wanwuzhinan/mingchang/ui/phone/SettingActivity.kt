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
import com.ssm.comm.ext.getCurrentVersionCode
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
import com.wanwuzhinan.mingchang.ui.phone.fra.ExchangeCourseFragment
import com.wanwuzhinan.mingchang.ui.phone.fra.ReportFragment
import com.wanwuzhinan.mingchang.ui.phone.fra.SettingFragment
import com.wanwuzhinan.mingchang.ui.pop.ExchangeContactPop
import com.wanwuzhinan.mingchang.ui.pop.ExchangeCoursePop
import com.wanwuzhinan.mingchang.ui.pop.NetErrorPop
import com.wanwuzhinan.mingchang.utils.ChooseCityUtils
import com.wanwuzhinan.mingchang.utils.SkeletonUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SettingActivity: BaseActivity<ActivitySettingBinding, UserViewModel>(UserViewModel()) {

    var currentPost = 0
    lateinit var mAdapter:SettingAdapter

    var mFragmentList: MutableList<Fragment> = mutableListOf()

    var mType = 0

    override fun initView() {

        mType=intent.getIntExtra(ConfigApp.INTENT_TYPE,mType)

        initList()
        initChildView()

    }

    fun initChildView(){

        mFragmentList.add(SettingFragment.instance)
        mFragmentList.add(ExchangeCourseFragment.instance)
        mFragmentList.add(ReportFragment.instance)

        mDataBinding.viewPager.adapter= Viewpager2Adapter(mActivity, mFragmentList as ArrayList<Fragment>)
        mDataBinding.viewPager.isUserInputEnabled = false  // 禁用用户滑动

        if (mType == 1) {
            mDataBinding.viewPager.setCurrentItem(1,true)
            mAdapter.getItem(1)!!.select = true
            mAdapter.getItem(0)!!.select = false
            currentPost = 1
        }
        if (mType == 2) {
            mDataBinding.viewPager.setCurrentItem(2,true)
            mAdapter.getItem(3)!!.select = true
            mAdapter.getItem(0)!!.select = false
            currentPost = 3
        }

    }

    private fun initList(){
        mAdapter= SettingAdapter()

        mAdapter.add(SettingData(R.mipmap.ic_setting_file,"个人信息","",true))
        mAdapter.add(SettingData(R.mipmap.ic_setting_exchange,"兑换记录"))
        mAdapter.add(SettingData(R.mipmap.ic_setting_video,"企业介绍"))
        mAdapter.add(SettingData(R.mipmap.ic_user_report,"用户反馈"))
        mAdapter.add(SettingData(R.mipmap.ic_setting_logout,"设置"))
//        if(getCurrentVersionCode() > getConfigData().android_code.toInt()){
//            mAdapter.add(SettingData(R.mipmap.ic_setting_logout,"注销账户"))
//        }
//        mAdapter.add(SettingData(R.mipmap.ic_setting_logout,"退出登录"))


        mDataBinding.reList.adapter = mAdapter

        mAdapter.setOnDebouncedItemClick{adapter, view, position ->


            when(adapter.getItem(position)!!.title){
                "个人信息"->{
                    mAdapter.getItem(currentPost)!!.select = false
                    mAdapter.getItem(position)!!.select = true
                    currentPost = position
                    mAdapter.notifyDataSetChanged()
                    mDataBinding.viewPager.setCurrentItem(0,true)

                }
                "兑换记录"->{
                    mAdapter.getItem(currentPost)!!.select = false
                    mAdapter.getItem(position)!!.select = true
                    currentPost = position
                    mAdapter.notifyDataSetChanged()

                    mDataBinding.viewPager.setCurrentItem(1,true)
                }
                "注销账户" ->{
                    mAdapter.notifyDataSetChanged()
                    showCancelPop(this,"确定要注销账号吗？注销后您将无法使用该账号，并且个人信息将无法找回。",true,
                        onSure = {
                            lifecycleScope.launch {
                                showBaseLoading()
                                delay(1000)
                                toastSuccess("注销成功")
                                dismissBaseLoading()
                                clearAllData()
                                launchLoginActivity()
                                AppActivityManager.getInstance().finishAllActivities()
                            }
                        },
                        onCancel = {
                            mAdapter.getItem(currentPost)!!.select=true
                            mAdapter.getItem(position)!!.select=false
                            mAdapter.notifyDataSetChanged()
                        })
                }
                "企业介绍"->{
                    performLaunchH5Agreements(ConfigApp.VIDEO_INTRODUCTION,"企业介绍")
                }
                "用户反馈"->{
                    mAdapter.getItem(currentPost)!!.select = false
                    mAdapter.getItem(position)!!.select = true
                    currentPost = position
                    mAdapter.notifyDataSetChanged()

                    mDataBinding.viewPager.setCurrentItem(2,true)
                }
                "设置" ->{
                    launchActivity(PrivacySetActivity::class.java)
                }
                "隐私协议" ->{
                   performLaunchH5Agreement(ConfigApp.PRIVACY_POLICY,getString(R.string.privacy_policy))
                }
                "用户协议" ->{
                    performLaunchH5Agreement(ConfigApp.USER_AGREEMENT,getString(R.string.user_privacy))
                }
                "退出登录"->{
                    mAdapter.getItem(currentPost)!!.select=false
                    mAdapter.getItem(position)!!.select=true
                    mAdapter.notifyDataSetChanged()

                    NetErrorPop(mActivity).showLogout(onSure = {
                        clearAllData()
                        launchLoginActivity()
                        AppActivityManager.getInstance().finishAllActivities()
                    })
//
//                    showCancelPop(this,"确定要退出登录吗？您的数据随账号保存",true,
//                        onSure = {
//
//                        },
//                        onCancel = {
//                            mAdapter.getItem(currentPost)!!.select=true
//                            mAdapter.getItem(position)!!.select=false
//                            mAdapter.notifyDataSetChanged()
//                        })
                }
            }
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_setting
    }

}