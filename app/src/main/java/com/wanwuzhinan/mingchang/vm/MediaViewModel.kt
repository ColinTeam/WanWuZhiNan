package com.wanwuzhinan.mingchang.vm

import android.util.SparseArray
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

    private val _mediaLessonInfo: MutableLiveData<LessonSubjectGroup> = MutableLiveData()
    val mediaLessonInfo: LiveData<LessonSubjectGroup> = _mediaLessonInfo

    private val _LessonInfo: MutableLiveData<LessonInfo> = MutableLiveData()
    val lessonInfo: LiveData<LessonInfo> = _LessonInfo

    private val _GroupPosition: MutableLiveData<Int> = MutableLiveData(0)
    val groupPosition: LiveData<Int> = _GroupPosition
    private val lessonInfoArray: SparseArray<LessonSubjectGroup> = SparseArray()
    //第一个请求的为第二个请求的值
//    fun getMediaLessonSubjectGroup(
//        groupID: Int = ConfigApp.TYPE_AUDIO, index: Int = 0, need: Int = 1
//    ) {
//        viewModelScope.launch(Dispatchers.IO) {
//            _showLoading.postValue(true)
//            val lessonID = viewModelScope.async(Dispatchers.IO) {
//                val group = service.newMediaLessonSubjectGroup(groupID)
//                val groupLesson = group.getData()?.list
//                if (!groupLesson.isNullOrEmpty<LessonSubject>() && groupLesson.size > index) {
//                    groupLesson[index].id
//                } else -1
//            }.await()
//            if (lessonID != -1) service.newAudioLessonQuarter(lessonID, need)
//            else _showToast.postValue(NetworkResult.failure(-1, "failed"))
//            _showLoading.postValue(true)
//        }
//    }

    fun getMediaLessonSubjectGroup(groupID: Int = ConfigApp.TYPE_AUDIO) {
        request({
            service.newMediaLessonSubjectGroup(groupID)
        }, success = {
            _mediaLessonSubjectGroup.postValue(it)
        }, failure = {
            _showToast.postValue(it)
        })
    }

    fun getMediaLessonInfo(groupID: Int, need: Int = 1) {
        request({
            service.newLessonSubject(groupID, need)
        }, success = {
            lessonInfoArray.put(groupID, it)
            _mediaLessonInfo.postValue(it)
        }, failure = {
            _showToast.postValue(it)
        })
    }

    fun getMediaLessonQuarter(lessonID: Int, need: Int = 1) {
        request({
            service.newAudioLessonQuarter(lessonID, need)
        }, success = {
            _mediaLessonInfo.postValue(it)
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
    fun getMediaLessonInfoValue() = mediaLessonInfo.value
    fun getMediaLessonInfoValue(id: Int) = lessonInfoArray.get(id, null)
    private fun updateGroup(position: Int) {

    }


}