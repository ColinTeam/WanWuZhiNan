package com.wanwuzhinan.mingchang.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.adapter.SettingTabAdapter
import com.wanwuzhinan.mingchang.adapter.TabFragmentAdapter
import com.wanwuzhinan.mingchang.app.AppActivity
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.data.TabBean
import com.wanwuzhinan.mingchang.databinding.FragmentSettingTabBinding
import com.wanwuzhinan.mingchang.ui.fragment.UserInfoFragment
import com.wanwuzhinan.mingchang.ui.phone.fra.ExchangeCourseFragment
import com.wanwuzhinan.mingchang.ui.phone.fra.ReportFragment
import com.wanwuzhinan.mingchang.vm.UserInfoViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-12 17:30
 *
 * Des   :SettingTabActivity
 */
class SettingTabActivity : AppActivity<FragmentSettingTabBinding, UserInfoViewModel>() {

    private var tabAdapter: SettingTabAdapter? = null
    private var fragmentAdapter: TabFragmentAdapter? = null


    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        val tabs = getTabList()
        tabAdapter = SettingTabAdapter()
        tabAdapter!!.submitList(tabs)
        tabAdapter!!.onItemClickListener = { _, _, position ->
            selected(position)
        }
        viewBinding.list.adapter = tabAdapter
        fragmentAdapter = TabFragmentAdapter(
            this, listOf(
                UserInfoFragment.newInstance(),
                ExchangeCourseFragment.instance,
                ReportFragment.instance,
            )
        )
        viewBinding.pager.apply {
            isUserInputEnabled = false
            adapter = fragmentAdapter
            offscreenPageLimit = fragmentAdapter!!.itemCount
        }
    }

    override fun onResume() {
        super.onResume()
        //防止协议界面退出登录，回来不能跳转到登录界面
        viewModel.getUserInfo()
    }

    override fun onDestroy() {
        tabAdapter = null
        fragmentAdapter = null
        super.onDestroy()
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        selected(getExtrasPosition(bundle, savedInstanceState))
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(ConfigApp.EXTRAS_POSITION, tabAdapter?.selected ?: 0)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        selected(savedInstanceState.getInt(ConfigApp.EXTRAS_POSITION, 0))
    }

    private fun getTabList(): List<TabBean> {
        val list = arrayListOf<TabBean>()
        val title = resources.getStringArray(R.array.setting_tab_title)
        val array = resources.obtainTypedArray(R.array.setting_tab_icon)
        for (index in 0 until array.length()) {
            val iconResId = array.getResourceId(index, -1)
            if (iconResId != -1) {
                list.add(TabBean(iconResId, title[index]))
            }
        }
        array.recycle()
        return list
    }

    private fun selected(position: Int) {
        if (position == 3) {
            tabAdapter!!.selected = 3
            viewBinding.pager.setCurrentItem(2, false)
        } else if (position == 2) {
            WebViewActivity.start(
                this@SettingTabActivity,
                url = ConfigApp.VIDEO_INTRODUCTION,
                title = tabAdapter!!.items[position].title.toString()
            )
        } else if (position == 4) {
            ProtocolActivity.start(this)
        } else {
            tabAdapter!!.selected = position
            viewBinding.pager.setCurrentItem(position, false)
        }
    }

    companion object {
        @JvmStatic
        fun start(context: Context, position: Int = 0) {
            val starter = Intent(context, SettingTabActivity::class.java).putExtra(
                ConfigApp.EXTRAS_POSITION, position
            )
            context.startActivity(starter)
        }
    }
}