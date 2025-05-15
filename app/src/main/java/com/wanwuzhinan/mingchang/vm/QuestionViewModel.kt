package com.wanwuzhinan.mingchang.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wanwuzhinan.mingchang.app.AppViewModel
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
    private val _questionList: MutableLiveData<List<Question>?> = MutableLiveData(null)

    val userInfo: LiveData<UserInfo?> = _userInfo
    val questionList: LiveData<List<Question>?> = _questionList


    //获取用户信息
    fun getUserInfo() {
        request(request = { service.newUserInfo() }, success = { it ->
            it?.info?.let { _userInfo.postValue(it) }
        })
    }

    fun getQuestionList(type: Int) {
        request(request = { service.newQuestionList(type) }, success = { it ->
            it?.list?.let { _questionList.postValue(it) }
        })
    }


    fun getConfigValue() = configData.value
    fun getUserInfoValue() = userInfo.value
    fun getQuestionListValue() = questionList.value

}