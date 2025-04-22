package com.ssm.comm.adapter.vp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.adapter.vp2
 * ClassName: DefPagerAdapter
 * Author:ShiMing Shi
 * CreateDate: 2022/9/6 19:44
 * Email:shiming024@163.com
 * Description:
 */
class BaseFragmentAdapter : FragmentStateAdapter {

    private var fragments: List<Fragment>

    constructor(fragmentActivity: FragmentActivity,fragments: List<Fragment>) :super(fragmentActivity){
        this.fragments = fragments
    }

    constructor(fragment: Fragment,fragments: List<Fragment>) :super(fragment){
        this.fragments = fragments
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}