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

    private val _AudioLessonSubjectGroup: MutableLiveData<LessonSubjectGroup> = MutableLiveData()
    val audioLessonSubjectGroup: LiveData<LessonSubjectGroup> = _AudioLessonSubjectGroup

    private val _AudioLessonQuarter: MutableLiveData<LessonSubjectGroup> = MutableLiveData()
    val audioLessonQuarter: LiveData<LessonSubjectGroup> = _AudioLessonQuarter

    private val _LessonInfo: MutableLiveData<LessonInfo> = MutableLiveData()
    val lessonInfo: LiveData<LessonInfo> = _LessonInfo

    private val _GroupPosition: MutableLiveData<Int> = MutableLiveData(0)
    val groupPosition: LiveData<Int> = _GroupPosition

    fun getAudioLessonSubjectGroup(groupID: Int = ConfigApp.TYPE_AUDIO) {
        request({
            service.newMediaLessonSubjectGroup(groupID)
        }, success = {
            _AudioLessonSubjectGroup.postValue(it)
        }, failure = {
            _showToast.postValue(it)
        })
    }

    fun getAudioLessonQuarter(subjectID: Int, need: Int = 1) {
        request({
            service.newAudioLessonQuarter(subjectID, need)
        }, success = {
            _AudioLessonQuarter.postValue(it)
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

    fun setGroupPosition(position: Int) {
        if (position != getGroupPositionValue()) {
            _GroupPosition.value = position
        }
    }

    fun getGroupPositionValue() = _GroupPosition.value ?: 0
}