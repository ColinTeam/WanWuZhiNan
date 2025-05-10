package com.wanwuzhinan.mingchang.ui.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelStore
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
import com.wanwuzhinan.mingchang.vm.HomeViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 22:34
 *
 * Des   :AudioFragment
 */
class SettingTabFragment : AppFragment<FragmentSettingTabBinding, HomeViewModel>() {
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

    override fun bindViewModelStore(): ViewModelStore {
        return requireActivity().viewModelStore
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
        val tabs = getTabList()
        tabAdapter = SettingTabAdapter()
        tabAdapter!!.submitList(tabs)
        tabAdapter!!.onItemClickListener = { v, it ->
            {
                Log.e("selected onItemClickListener $it")
                when (it.title) {
                    tabs[0] -> selected(0)
                    tabs[1] -> selected(1)
                    tabs[3] -> selected(3)
                    tabs[2] -> {
                        WebFragment.navigate(
                            this@SettingTabFragment,
                            url = ConfigApp.VIDEO_INTRODUCTION,
                            title = it.title.toString()
                        )
                    }

                    tabs[4] -> {
                        findNavController().navigate(R.id.action_toSettingOther)
                    }
                }
            }
        }
//        tabAdapter.setOnDebouncedItemClick { adapter, view, position ->
//            {
//                Log.e("selected setOnDebouncedItemClick $position")
//            }
//        }

//        tabAdapter!!.setOnDebouncedItemClick { _, view, position ->
//            {
//                startVibrator(requireActivity())
//                Log.e("selected setOnDebouncedItemClick $position")
//                when (position) {
//                    0, 1, 3 -> {
////                        selected(position)
//                        tabAdapter!!.selected = position
//                        viewBinding.pager.setCurrentItem(position, false)
//                    }
//
//                    2 -> {//企业介绍
//                        WebFragment.navigate(
//                            this@SettingTabFragment,
//                            url = ConfigApp.VIDEO_INTRODUCTION
//                        )
//                    }
//
//                    4 -> {//用户反馈
//                        findNavController().navigate(R.id.action_toSettingOther)
//                    }
//                }
//            }
//        }
        viewBinding.list.adapter = tabAdapter
        fragmentAdapter = TabFragmentAdapter(
            this, listOf(
                UserInfoFragment.newInstance(),
                UserInfoFragment.newInstance(),
                UserInfoFragment.newInstance(),
            )
        )
        viewBinding.pager.apply {
            isUserInputEnabled = false  // 禁用用户滑动
            adapter = fragmentAdapter
        }
    }

    override fun onDestroyView() {
        tabAdapter = null
        fragmentAdapter = null
        super.onDestroyView()
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            selected(savedInstanceState.getInt(EXTRAS_POSITION, 0))
        } else {
            selected(bundle?.getInt(EXTRAS_POSITION, 0) ?: 0)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(EXTRAS_POSITION, tabAdapter?.selected ?: 0)
    }

    private fun selected(position: Int) {
        Log.e("selected $position")
        tabAdapter!!.selected = position
        if (position == 3) {
            viewBinding.pager.setCurrentItem(2, true)
        } else {
            viewBinding.pager.setCurrentItem(position, true)
        }
    }
}