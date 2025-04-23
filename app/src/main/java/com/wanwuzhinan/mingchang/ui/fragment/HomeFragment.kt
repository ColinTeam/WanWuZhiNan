package com.wanwuzhinan.mingchang.ui.fragment

import android.os.Bundle
import com.wanwuzhinan.mingchang.app.AppFragment
import com.wanwuzhinan.mingchang.databinding.FragmentHomeBinding

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 22:34
 *
 * Des   :HomeFragment
 */
class HomeFragment : AppFragment<FragmentHomeBinding, HomeViewModel>() {

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
//        viewBinding.apply {
//            val array = resources.getStringArray(R.array.flow_data)
//            val list: MutableList<Fragment> = mutableListOf()
//            array.forEachIndexed { index, test ->
//                list.add(ListFragment.newInstance(index, test))
//            }
//            page.apply {
//                adapter = FragmentAdapter(thiscom.colin.android.demo.kotlin.ui.home.HomeFragment, list)
//            }
//            TabLayoutMediator(tabLayout, page, true, true) { tab, position ->
//                tab.text = array[position]
//            }.attach()
//        }
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {


    }

}