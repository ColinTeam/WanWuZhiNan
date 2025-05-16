package com.wanwuzhinan.mingchang.ui.pop

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.wanwuzhinan.mingchang.app.AppDialogFragment
import com.wanwuzhinan.mingchang.databinding.DialogUserInfoBinding
import com.wanwuzhinan.mingchang.entity.UserInfo
import com.wanwuzhinan.mingchang.vm.UserInfoViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-16 16:39
 *
 * Des   :UserInfoDialog
 */
class UserInfoDialog private constructor(
    var user: UserInfo
) : AppDialogFragment<DialogUserInfoBinding>() {

    private val viewMode by lazy {
        ViewModelProvider.create(this)[UserInfoViewModel::class.java]
    }

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {

    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {

    }
}