package com.wanwuzhinan.mingchang.ui.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.colin.library.android.utils.Log
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.adapter.SettingTabAdapter
import com.wanwuzhinan.mingchang.adapter.TabFragmentAdapter
import com.wanwuzhinan.mingchang.app.AppFragment
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.data.TabBean
import com.wanwuzhinan.mingchang.databinding.FragmentSettingTabBinding
import com.wanwuzhinan.mingchang.ui.HomeActivity.Companion.EXTRAS_POSITION
import com.wanwuzhinan.mingchang.ui.phone.fra.ExchangeCourseFragment
import com.wanwuzhinan.mingchang.ui.phone.fra.ReportFragment
import com.wanwuzhinan.mingchang.vm.UserInfoViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 22:34
 *
 * Des   :AudioFragment
 */
class SettingTabFragment : AppFragment<FragmentSettingTabBinding, UserInfoViewModel>() {
    private var tabAdapter: SettingTabAdapter? = null
    private var fragmentAdapter: TabFragmentAdapter? = null

    companion object {
        @JvmStatic
        fun navigate(fragment: Fragment, position: Int = 0) {
            navigate(fragment.findNavController(), position)
        }

        @JvmStatic
        fun navigate(activity: AppCompatActivity, position: Int = 0) {
            navigate(activity.findNavController(R.id.nav_host), position)
        }

        @JvmStatic
        fun navigate(controller: NavController, position: Int) {
            controller.navigate(
                R.id.action_toSettingTab, Bundle().apply { putInt(EXTRAS_POSITION, position) }, null
            )

        }
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

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        val position =
            savedInstanceState?.getInt(EXTRAS_POSITION, 0) ?: bundle?.getInt(EXTRAS_POSITION, 0)
            ?: 0
        viewModel.tabPosition(position)
        val tabs = getTabList()
        tabAdapter = SettingTabAdapter()
        tabAdapter!!.submitList(tabs)
        tabAdapter!!.onItemClickListener = { v, it ->
            val position = v.tag as Int
            viewModel.tabPosition(position)
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

    override fun onDestroyView() {
        tabAdapter = null
        fragmentAdapter = null
        super.onDestroyView()
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewModel.apply {
            tabPosition.observe {
                selected(it)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val position = tabAdapter?.selected ?: 0
        outState.putInt(EXTRAS_POSITION, position)
    }

    private fun selected(position: Int) {
        Log.e("selected $position")
        tabAdapter!!.selected = position
        if (position == 3) {
            viewBinding.pager.setCurrentItem(2, false)
        } else if (position == 2) {
            WebFragment.navigate(
                this@SettingTabFragment,
                url = ConfigApp.VIDEO_INTRODUCTION,
                title = tabAdapter!!.items[position].title.toString()
            )
        } else if (position == 4) {
            findNavController().navigate(R.id.action_toSettingOther)
        } else {
            viewBinding.pager.setCurrentItem(position, false)
        }
    }
}