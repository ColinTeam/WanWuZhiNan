package com.wanwuzhinan.mingchang.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
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

    constructor(activity: FragmentActivity, list: List<Fragment>) : super(
        activity.supportFragmentManager, activity.lifecycle
    ) {
        this.list.addAll(list)
    }

    constructor(fragment: Fragment, list: List<Fragment>) : super(
        fragment.childFragmentManager, fragment.lifecycle
    ) {
        this.list.addAll(list)
    }

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return list[position].hashCode().toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        return list.any { it.hashCode().toLong() == itemId }
    }
}