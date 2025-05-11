package com.wanwuzhinan.mingchang.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.colin.library.android.network.data.NetworkResult
import com.colin.library.android.network.request
import com.wanwuzhinan.mingchang.app.AppViewModel
import com.wanwuzhinan.mingchang.entity.CityInfo
import com.wanwuzhinan.mingchang.entity.Config
import com.wanwuzhinan.mingchang.entity.GradeInfo
import com.wanwuzhinan.mingchang.entity.UserInfo

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 22:10
 *
 * Des   :HomeViewModel
 */
class HomeViewModel : AppViewModel() {

    private val _closeAD: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _userInfo: MutableLiveData<UserInfo?> = MutableLiveData(null)
    private val _configData: MutableLiveData<Config?> = MutableLiveData(null)
    private val _cityInfo: MutableLiveData<CityInfo?> = MutableLiveData(null)
    private val _gradeInfo: MutableLiveData<GradeInfo?> = MutableLiveData(null)

    val closeAD: LiveData<Boolean> = _closeAD
    val userInfo: LiveData<UserInfo?> = _userInfo
    val configData: LiveData<Config?> = _configData
    val cityInfo: LiveData<CityInfo?> = _cityInfo
    val gradeInfo: LiveData<GradeInfo?> = _gradeInfo
    fun getConfig() {
        request({
            service.newConfig()
        }, success = {
            if (it != configData.value) {
                _configData.postValue(it)
            }
        }, failure = {
            _showToast.postValue(it)
        })
    }

    //获取用户信息
    fun getUserInfo() {
        request({
            service.newUserInfo()
        }, success = {
            _userInfo.postValue(it.info)
        }, failure = {
            _showToast.postValue(it)
        })
    }


    fun getCityInfo() {
        request({
            service.newCityInfo()
        }, success = {
            _cityInfo.postValue(it)
        }, failure = {
            _showToast.postValue(it)
        })
    }

    fun getGradeInfo(id: Int = 16) {
        request({
            service.newGrade(id)
        }, success = {
            _gradeInfo.postValue(it)
        }, failure = {
            _showToast.postValue(it)
        })
    }

    fun editUserInfo(map: HashMap<String, String>) {
        request({
            service.newEditUserInfo(map)
        }, success = {
            _showToast.postValue(NetworkResult.failure(0, "保存成功"))
        }, failure = {
            _showToast.postValue(it)
        })
    }

    fun updateAD(close: Boolean) {
        if (_closeAD.value != close) {
            _closeAD.value = close
        }
    }

    fun getConfigValue() = configData.value
    fun getUserInfoValue() = userInfo.value
    fun getAdStateValue() = closeAD.value == true
    fun getCityInfoValue() = cityInfo.value
    fun getGradeInfoValue() = gradeInfo.value
}