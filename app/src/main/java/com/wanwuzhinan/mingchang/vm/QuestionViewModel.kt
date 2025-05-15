package com.wanwuzhinan.mingchang.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.colin.library.android.network.request
import com.wanwuzhinan.mingchang.app.AppViewModel
import com.wanwuzhinan.mingchang.entity.Config
import com.wanwuzhinan.mingchang.entity.Question
import com.wanwuzhinan.mingchang.entity.UserInfo

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 22:10
 *
 * Des   :HomeViewModel
 */
class QuestionViewModel : AppViewModel() {

    private val _userInfo: MutableLiveData<UserInfo?> = MutableLiveData(null)
    private val _configData: MutableLiveData<Config?> = MutableLiveData(null)
    private val _questionList: MutableLiveData<List<Question>?> = MutableLiveData(null)

    val userInfo: LiveData<UserInfo?> = _userInfo
    val configData: LiveData<Config?> = _configData
    val questionList: LiveData<List<Question>?> = _questionList
    fun getConfig() {
        request({
            service.newConfig()
        }, success = {
            if (it != configData.value) {
                _configData.postValue(it)
            }
        }, failure = {
            postError(type = it.code, it.msg)
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
            postError(type = it.code, it.msg)
            _showToast.postValue(it)
        })
    }

    fun getQuestionList(type: Int) {
        request({
            service.newQuestionList(type)
        }, success = {
            _questionList.postValue(it.list)
        }, failure = {
            postError(type = it.code, it.msg)
            _showToast.postValue(it)
        })
    }


    fun getConfigValue() = configData.value
    fun getUserInfoValue() = userInfo.value
    fun getQuestionListValue() = questionList.value

}