package com.wanwuzhinan.mingchang.vm

import android.util.SparseArray
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wanwuzhinan.mingchang.app.AppViewModel
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.entity.Lesson
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

    private val _lessonInfo: MutableLiveData<LessonInfo> = MutableLiveData()
    val lessonInfo: LiveData<LessonInfo> = _lessonInfo

    private val _positionData: MutableLiveData<Int> = MutableLiveData(0)
    val positionData: LiveData<Int> = _positionData

    private val _lessonsData: MutableLiveData<List<Lesson?>?> = MutableLiveData(null)
    val lessonsData: LiveData<List<Lesson?>?> = _lessonsData
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
        request(request = { service.newMediaLessonSubjectGroup(groupID) }, success = { it ->
            it?.let { _mediaLessonSubjectGroup.postValue(it) }
        })
    }

    fun getMediaLessonInfo(groupID: Int, need: Int = 1) {
        request(request = { service.newLessonSubject(groupID, need) }, success = { it ->
            it?.let {
                lessonInfoArray.put(groupID, it)
                _mediaLessonSubjectGroup.postValue(it)
            }
        })
    }

    fun getMediaLessonQuarter(lessonID: Int, need: Int = 1) {
        request(request = { service.newAudioLessonQuarter(lessonID, need) }, success = { it ->
            it?.let {
                _mediaLessonInfo.postValue(it)
            }
        })
    }

    fun getLessonInfo(lessonID: Int) {
        request(request = { service.newLessonInfo(lessonID) }, success = { it ->
            it?.let {
                _lessonInfo.postValue(it)
            }
        })
    }

    fun updatePosition(position: Int) {
        if (position != getPositionValue()) {
            _positionData.value = position
        }
    }

    fun updateLessons(list: List<Lesson>?) {
        if (list != null && list.isNotEmpty()) {
            _lessonsData.value = list
        }
    }

    fun getPositionValue() = positionData.value ?: 0
    fun getLessons() = lessonsData.value
    fun getLessonInfoValue() = lessonInfo.value
    fun getMediaLessonSubjectGroupValue() = mediaLessonSubjectGroup.value
    fun getMediaLessonInfoValue() = mediaLessonInfo.value
    fun getMediaLessonInfoValue(id: Int) = lessonInfoArray.get(id, null)
    private fun updateGroup(position: Int) {

    }


}