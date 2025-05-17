package com.wanwuzhinan.mingchang.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wanwuzhinan.mingchang.app.AppViewModel
import com.wanwuzhinan.mingchang.entity.CityInfo
import com.wanwuzhinan.mingchang.entity.GradeInfo
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

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
    private val _updateFile: MutableLiveData<String?> = MutableLiveData(null)

    val tabPosition: LiveData<Int> = _tabPosition
    val cityInfo: LiveData<CityInfo?> = _cityInfo
    val gradeInfo: LiveData<GradeInfo?> = _gradeInfo
    val updateFile: LiveData<String?> = _updateFile

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
        request(
            loading = true,
            toast = true,
            request = { service.newEditUserInfo(map) },
            success = { it ->

            },
            delay = 1000L
        )
    }

    //上传图片
    fun uploadImage(file: File) {
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("file", file.name, requestBody)
        request(
            loading = true,
            toast = true,
            request = { service.newUploadImage(part) },
            success = { it ->
                _updateFile.postValue(it?.file ?: "")
            },
            delay = 3000L
        )
    }

    fun tabPosition(position: Int) {
        if (tabPosition.value != position) {
            _tabPosition.value = position
        }
    }

    fun getCityInfoValue() = cityInfo.value
    fun getGradeInfoValue() = gradeInfo.value
}