package com.wanwuzhinan.mingchang.ui.phone

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.chad.library.adapter.base.util.setOnDebouncedItemClick
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.data.SettingData
import com.wanwuzhinan.mingchang.ext.*
import com.wanwuzhinan.mingchang.vm.UserViewModel
import com.ssm.comm.ext.clearAllData
import com.ssm.comm.ext.getCurrentVersionCode
import com.ssm.comm.ext.getCurrentVersionName
import com.ssm.comm.ext.toastSuccess
import com.ssm.comm.global.AppActivityManager
import com.ssm.comm.ui.base.BaseActivity
import com.wanwuzhinan.mingchang.adapter.SettingPrivacyAdapter
import com.wanwuzhinan.mingchang.databinding.ActivitySettingPrivacyBinding
import com.wanwuzhinan.mingchang.ui.phone.fra.ExchangeCourseFragment
import com.wanwuzhinan.mingchang.ui.phone.fra.ReportFragment
import com.wanwuzhinan.mingchang.ui.phone.fra.SettingFragment
import com.wanwuzhinan.mingchang.ui.pop.NetErrorPop
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PrivacySetActivity: BaseActivity<ActivitySettingPrivacyBinding, UserViewModel>(UserViewModel()) {

    lateinit var mAdapter:SettingPrivacyAdapter

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


    }

    private fun initList(){
        mAdapter= SettingPrivacyAdapter()

        mAdapter.add(SettingData(R.drawable.icon_privacy_policy,"用户协议"))
        mAdapter.add(SettingData(R.drawable.icon_privacy_policy,"隐私协议"))
        mAdapter.add(SettingData(R.drawable.icon_privacy_policy,"儿童隐私政策"))
        mAdapter.add(SettingData(R.drawable.icon_privacy_policy,"注销账户"))
        mAdapter.add(SettingData(R.drawable.icon_privacy_policy,"当前版本",
            getCurrentVersionName()
        ))
        mAdapter.add(SettingData(R.mipmap.ic_setting_logout,"退出登录"))

        mDataBinding.reList.adapter = mAdapter

        mAdapter.setOnDebouncedItemClick{adapter, view, position ->


            when(adapter.getItem(position)!!.title){

                "隐私协议" ->{
                   performLaunchH5Agreement(ConfigApp.PRIVACY_POLICY,getString(R.string.privacy_policy))
                }
                "用户协议" ->{
                    performLaunchH5Agreement(ConfigApp.USER_AGREEMENT,getString(R.string.user_privacy))
                }
                "儿童隐私政策" ->{
                    performLaunchH5Agreement(ConfigApp.PRIVACY_CHILD,getString(R.string.privacy_child))
                }
                "注销账户" ->{
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

                        })
                }
                "退出登录"->{
                    NetErrorPop(mActivity).showLogout(onSure = {
                        clearAllData()
                        launchLoginActivity()
                        AppActivityManager.getInstance().finishAllActivities()
                    })
                }

                "当前版本"->{
                    if (getConfigData().android_code.toInt() > getCurrentVersionCode()) {
                        NetErrorPop(mActivity).showUpdate(getConfigData().android_update,onSure = {

                        }, onCancel = {

                        })
                    }
                }
            }
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_setting_privacy
    }

}