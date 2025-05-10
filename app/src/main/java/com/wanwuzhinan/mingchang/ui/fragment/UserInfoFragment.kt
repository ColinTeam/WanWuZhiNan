package com.wanwuzhinan.mingchang.ui.fragment

import android.os.Bundle
import androidx.lifecycle.ViewModelStore
import com.wanwuzhinan.mingchang.app.AppFragment
import com.wanwuzhinan.mingchang.databinding.FragmentEditFileBinding
import com.wanwuzhinan.mingchang.vm.HomeViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-11 10:09
 *
 * Des   :UserInfoFragment
 */
class UserInfoFragment : AppFragment<FragmentEditFileBinding, HomeViewModel>() {
    override fun bindViewModelStore(): ViewModelStore {
        return requireActivity().viewModelStore
    }

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {

    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {

    }

    companion object {
        @JvmStatic
        fun newInstance(): UserInfoFragment {
            val fragment = UserInfoFragment()
            return fragment
        }
    }
}