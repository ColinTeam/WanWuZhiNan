package com.wanwuzhinan.mingchang.vm

import androidx.lifecycle.viewModelScope
import com.wanwuzhinan.mingchang.entity.UserInfoData
import com.wanwuzhinan.mingchang.net.repository.HomeRepository
import com.ssm.comm.data.VersionData
import com.ssm.comm.ext.StateMutableLiveData
import com.ssm.comm.ui.base.BaseViewModel
import kotlinx.coroutines.launch


class HomeViewModel : BaseViewModel<UserInfoData, HomeRepository>(HomeRepository()) {

    val getVersionLiveData = StateMutableLiveData<VersionData>()


    //获取版本号
    fun getVersion() {
        viewModelScope.launch {
            //请求到的数据用livedata包裹
            getVersionLiveData.value = repository.getVersion()
        }
    }

}