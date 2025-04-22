package com.wanwuzhinan.mingchang.ui.phone

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.adapter.TabViewpagerAdapter
import com.wanwuzhinan.mingchang.databinding.ActivityHonorListBinding
import com.wanwuzhinan.mingchang.vm.SplashViewModel
import com.ssm.comm.ui.base.BaseActivity

//我的荣誉墙
class HonorListActivity : BaseActivity<ActivityHonorListBinding, SplashViewModel>(SplashViewModel()){

    var mFragmentList = mutableListOf<Fragment>(
        HonorListFragment.instance(0),
        HonorListFragment.instance(1)
    )
    var mStringList = mutableListOf("知识勋章","卡牌")

    override fun initView() {
        initTab()
    }

    private fun initTab() {
        mDataBinding.viewPager.adapter= TabViewpagerAdapter(supportFragmentManager, mFragmentList)

        mDataBinding.tabLayout.setupWithViewPager(mDataBinding.viewPager)

        for(i in mStringList.indices){
            mDataBinding.tabLayout.getTabAt(i)?.setCustomView(getTabFocus(mStringList.get(i),this,R.layout.tab_honor,R.id.text))
        }
    }

    fun getTabFocus(name: String, context: Context, layout:Int, id:Int): View? {
        val inflate = LayoutInflater.from(context).inflate(layout, null)
        val centerText = inflate.findViewById<TextView>(id)
        centerText.text = name

        return inflate
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_honor_list
    }

}