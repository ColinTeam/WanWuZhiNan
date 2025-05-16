package com.wanwuzhinan.mingchang.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wanwuzhinan.mingchang.app.AppViewModel
import com.wanwuzhinan.mingchang.entity.Question

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 22:10
 *
 * Des   :HomeViewModel
 */
class QuestionViewModel : AppViewModel() {
    private val _questionList: MutableLiveData<List<Question>?> = MutableLiveData(null)
    private val _questionErrorList: MutableLiveData<List<Question>?> = MutableLiveData(null)
    private val _questions: MutableLiveData<Question?> = MutableLiveData(null)
    val questionList: LiveData<List<Question>?> = _questionList
    val questionErrorList: LiveData<List<Question>?> = _questionErrorList
    val questions: LiveData<Question?> = _questions


    fun getQuestionList(type: Int) {
        request(request = { service.newQuestionList(type) }, success = { it ->
            it?.list?.let { _questionList.postValue(it) }
        })
    }

    fun getQuestions(id: Int, lessonID: Int) {
        request(request = { service.newQuestionDetail(id, lessonID) }, success = { it ->
            it?.let { _questions.postValue(it) }
        })
    }

    fun getQuestionErrorList() {
        request(request = { service.newQuestionErrorList() }, success = { it ->
            it?.list?.let { _questionErrorList.postValue(it) }
        })
    }

    fun getQuestionListValue() = questionList.value

}