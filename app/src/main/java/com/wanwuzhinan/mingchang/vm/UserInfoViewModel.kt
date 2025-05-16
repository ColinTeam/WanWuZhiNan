package com.wanwuzhinan.mingchang.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.colin.library.android.network.data.HttpResult
import com.wanwuzhinan.mingchang.app.AppViewModel
import com.wanwuzhinan.mingchang.entity.CityInfo
import com.wanwuzhinan.mingchang.entity.GradeInfo
import com.wanwuzhinan.mingchang.entity.HTTP_APP_TOAST

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 22:10
 *
 * Des   :HomeViewModel
 */
class UserInfoViewModel : AppViewModel() {

    private val _tabPosition: MutableLiveData<Int> = MutableLiveData(0)
    private val _cityInfo: MutableLiveData<CityInfo?> = MutableLiveData(null)
    private val _gradeInfo: MutableLiveData<GradeInfo?> = MutableLiveData(null)

    val tabPosition: LiveData<Int> = _tabPosition
    val cityInfo: LiveData<CityInfo?> = _cityInfo
    val gradeInfo: LiveData<GradeInfo?> = _gradeInfo

    fun getCityInfo() {
        request(request = { service.newCityInfo() }, success = { it ->
            _cityInfo.postValue(it)
        })
    }

    fun getGradeInfo(id: Int = 16) {
        request(request = { service.newGrade(id) }, success = { it ->
            _gradeInfo.postValue(it)
        })
    }

    fun editUserInfo(map: HashMap<String, String>) {
        request(request = { service.newEditUserInfo(map) }, success = { it ->
            _showToast.postValue(HttpResult.Toast(HTTP_APP_TOAST, "修改成功"))
        })
    }

    fun tabPosition(position: Int) {
        if (tabPosition.value != position) {
            _tabPosition.value = position
        }
    }

    fun getCityInfoValue() = cityInfo.value
    fun getGradeInfoValue() = gradeInfo.value
}