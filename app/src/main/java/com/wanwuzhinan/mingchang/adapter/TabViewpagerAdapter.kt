package com.wanwuzhinan.mingchang.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter

@Suppress("DEPRECATION")
class TabViewpagerAdapter(activity: AppCompatActivity, val fs: MutableList<Fragment>) :
    FragmentStatePagerAdapter(
        activity.supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {


    override fun getItem(p0: Int) = fs[p0]

    override fun getCount() = fs.size
}