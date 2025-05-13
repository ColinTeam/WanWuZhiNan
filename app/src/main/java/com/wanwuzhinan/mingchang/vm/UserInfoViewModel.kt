package com.wanwuzhinan.mingchang.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.colin.library.android.network.data.NetworkResult
import com.colin.library.android.network.request
import com.wanwuzhinan.mingchang.app.AppViewModel
import com.wanwuzhinan.mingchang.entity.CityInfo
import com.wanwuzhinan.mingchang.entity.GradeInfo
import com.wanwuzhinan.mingchang.entity.UserInfo

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 22:10
 *
 * Des   :HomeViewModel
 */
class UserInfoViewModel : AppViewModel() {

    private val _tabPosition: MutableLiveData<Int> = MutableLiveData(0)
    private val _userInfo: MutableLiveData<UserInfo?> = MutableLiveData(null)
    private val _cityInfo: MutableLiveData<CityInfo?> = MutableLiveData(null)
    private val _gradeInfo: MutableLiveData<GradeInfo?> = MutableLiveData(null)

    val tabPosition: LiveData<Int> = _tabPosition
    val userInfo: LiveData<UserInfo?> = _userInfo
    val cityInfo: LiveData<CityInfo?> = _cityInfo
    val gradeInfo: LiveData<GradeInfo?> = _gradeInfo

    //获取用户信息
    fun getUserInfo() {
        request({
            service.newUserInfo()
        }, success = {
            _userInfo.postValue(it.info)
        }, failure = {
            postError(type = it.code, it.msg)
            _showToast.postValue(it)
        })
    }


    fun getCityInfo() {
        request({
            service.newCityInfo()
        }, success = {
            _cityInfo.postValue(it)
        }, failure = {
            postError(type = it.code, it.msg)
            _showToast.postValue(it)
        })
    }

    fun getGradeInfo(id: Int = 16) {
        request({
            service.newGrade(id)
        }, success = {
            _gradeInfo.postValue(it)
        }, failure = {
            postError(type = it.code, it.msg)
            _showToast.postValue(it)
        })
    }

    fun editUserInfo(map: HashMap<String, String>) {
        request({
            service.newEditUserInfo(map)
        }, success = {
            _showToast.postValue(NetworkResult.failure(0, "保存成功"))
        }, failure = {
            postError(type = it.code, it.msg)
            _showToast.postValue(it)
        })
    }

    fun tabPosition(position: Int) {
        if (tabPosition.value != position) {
            _tabPosition.value = position
        }
    }

    fun getUserInfoValue() = userInfo.value
    fun getCityInfoValue() = cityInfo.value
    fun getGradeInfoValue() = gradeInfo.value
}