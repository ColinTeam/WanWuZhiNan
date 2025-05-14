package com.wanwuzhinan.mingchang.ui.fragment

import android.os.Bundle
import com.wanwuzhinan.mingchang.app.AppFragment
import com.wanwuzhinan.mingchang.databinding.FragmentEditFileBinding
import com.wanwuzhinan.mingchang.vm.UserInfoViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-14 20:15
 *
 * Des   :RecordFragment
 */
class RecordFragment : AppFragment<FragmentEditFileBinding, UserInfoViewModel>() {
    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        TODO("Not yet implemented")
    }

    companion object {
        @JvmStatic
        fun newInstance(): RecordFragment {
            val fragment = RecordFragment()
            return fragment
        }
    }
}