package com.wanwuzhinan.mingchang.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-11 08:50
 *
 * Des   :TabFragmentAdapter
 */
class TabFragmentAdapter : FragmentStateAdapter {
    private val list: ArrayList<Fragment> = arrayListOf()

    constructor(fragment: Fragment, list: List<Fragment>) : super(
        fragment.childFragmentManager, fragment.lifecycle
    ) {
        this.list.addAll(list)
    }

    override fun createFragment(position: Int): Fragment {
        // 示例实现，需根据实际业务逻辑替换
        return list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }
}