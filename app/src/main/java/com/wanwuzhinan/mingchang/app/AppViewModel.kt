package com.wanwuzhinan.mingchang.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.colin.library.android.network.NetworkHelper
import com.colin.library.android.network.data.NetworkResult
import com.wanwuzhinan.mingchang.net.ApiService

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Date  :2024-09-13
 *
 * Des   :AppViewModel
 */
open class AppViewModel : ViewModel() {
    val service: ApiService by lazy {
        NetworkHelper.create(ApiService::class.java)
    }
    protected val _showError: MutableLiveData<NetworkResult.Failure?> = MutableLiveData(null)
    val showError: LiveData<NetworkResult.Failure?> = _showError


}