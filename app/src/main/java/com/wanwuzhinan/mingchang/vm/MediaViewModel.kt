package com.wanwuzhinan.mingchang.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.colin.library.android.network.request
import com.wanwuzhinan.mingchang.app.AppViewModel
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.entity.LessonInfo
import com.wanwuzhinan.mingchang.entity.LessonSubjectGroup

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025/5/8 09:25
 *
 * Des   :AudioViewModel
 */
class MediaViewModel : AppViewModel() {

    private val _mediaLessonSubjectGroup: MutableLiveData<LessonSubjectGroup> = MutableLiveData()
    val mediaLessonSubjectGroup: LiveData<LessonSubjectGroup> = _mediaLessonSubjectGroup

    private val _mediaLessonQuarter: MutableLiveData<LessonSubjectGroup> = MutableLiveData()
    val mediaLessonQuarter: LiveData<LessonSubjectGroup> = _mediaLessonQuarter

    private val _LessonInfo: MutableLiveData<LessonInfo> = MutableLiveData()
    val lessonInfo: LiveData<LessonInfo> = _LessonInfo

    private val _GroupPosition: MutableLiveData<Int> = MutableLiveData(0)
    val groupPosition: LiveData<Int> = _GroupPosition

    fun getMediaLessonSubjectGroup(groupID: Int = ConfigApp.TYPE_AUDIO) {
        request({
            service.newMediaLessonSubjectGroup(groupID)
        }, success = {
            _mediaLessonSubjectGroup.postValue(it)
        }, failure = {
            _showToast.postValue(it)
        })
    }

    fun getMediaLessonQuarter(groupID: Int, need: Int = 1) {
        request({
            service.newAudioLessonQuarter(groupID, need)
        }, success = {
            _mediaLessonQuarter.postValue(it)
        }, failure = {
            _showToast.postValue(it)
        })
    }

    fun getLessonInfo(lessonID: Int) {
        request({
            service.newLessonInfo(lessonID)
        }, success = {
            _LessonInfo.postValue(it)
        }, failure = {
            _showToast.postValue(it)
        })
    }

    fun updateGroupPosition(position: Int) {
        if (position != getGroupPositionValue()) {
            _GroupPosition.value = position
        }
    }

    fun getGroupPositionValue() = groupPosition.value ?: 0
    fun getMediaLessonSubjectGroupValue() = mediaLessonSubjectGroup.value
    private fun updateGroup(position: Int) {

    }



}