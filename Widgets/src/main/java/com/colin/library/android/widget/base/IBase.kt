package com.colin.library.android.widget.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import com.colin.library.android.utils.Constants

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Date  :2024-08-23
 *
 * Des   :Customize the interface initialization action
 */
interface IBase {
    @LayoutRes
    fun layoutRes() = Constants.INVALID

    /*init default view(findView、listener)*/
    fun initView(bundle: Bundle?, savedInstanceState: Bundle?)

    /*Previous interface data、bind liveData*/
    fun initData(bundle: Bundle?, savedInstanceState: Bundle?)

    /*load data by sqlite、http等耗时动作*/
    fun loadData(refresh: Boolean)

    fun goBack(): Boolean

}