package com.wanwuzhinan.mingchang.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
class TabViewpagerAdapter(fm: FragmentManager, var fs: MutableList<Fragment>) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return fs!!.size
    }

    override fun getItem(position: Int): Fragment {
        return fs!!.get(position)
    }
}