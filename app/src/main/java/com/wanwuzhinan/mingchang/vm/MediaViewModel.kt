package com.wanwuzhinan.mingchang.vm

import android.util.SparseArray
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wanwuzhinan.mingchang.app.AppViewModel
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.entity.Lesson
import com.wanwuzhinan.mingchang.entity.LessonInfo
import com.wanwuzhinan.mingchang.entity.LessonStudyLog
import com.wanwuzhinan.mingchang.entity.LessonSubjectChild
import com.wanwuzhinan.mingchang.entity.LessonSubjectGroup
import com.wanwuzhinan.mingchang.entity.MediaInfo

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025/5/8 09:25
 *
 * Des   :AudioViewModel
 */
class MediaViewModel : AppViewModel() {

    private val _mediaLessonTab: MutableLiveData<LessonSubjectGroup> = MutableLiveData()
    val mediaLessonTab: LiveData<LessonSubjectGroup> = _mediaLessonTab

    private val _mediaLessonTabChild: MutableLiveData<LessonSubjectChild> = MutableLiveData()
    val mediaLessonTabChild: LiveData<LessonSubjectChild> = _mediaLessonTabChild

    private val _lessonInfo: MutableLiveData<LessonInfo> = MutableLiveData()
    val lessonInfo: LiveData<LessonInfo> = _lessonInfo
    private val _mediaInfoData: MutableLiveData<MediaInfo> = MutableLiveData()
    val mediaInfoData: LiveData<MediaInfo> = _mediaInfoData
    private val _studyLog: MutableLiveData<LessonStudyLog> = MutableLiveData()
    val studyLog: LiveData<LessonStudyLog> = _studyLog
    private val _positionData: MutableLiveData<Int> = MutableLiveData(0)
    val positionData: LiveData<Int> = _positionData

    private val _lessonsData: MutableLiveData<List<Lesson>?> = MutableLiveData(null)
    val lessonsData: LiveData<List<Lesson>?> = _lessonsData
    private val mediaLessonTabChildArray: SparseArray<LessonSubjectChild> = SparseArray()


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

    fun getMediaLessonTab(type: Int = ConfigApp.TYPE_AUDIO) {
        request(request = { service.newMediaLessonTab(type) }, success = { it ->
            it?.let { _mediaLessonTab.postValue(it) }
        })
    }

    fun getMediaLessonTabChild(id: Int, need: Int = 1) {
        request(request = { service.newMediaLessonTabChild(id, need) }, success = { it ->
            it?.let {
                mediaLessonTabChildArray.put(id, it)
                _mediaLessonTabChild.postValue(it)
            }
        })
    }

    fun getMediaLessonQuarter(id: Int, need: Int = 1) {
        request(request = { service.newMediaLessonQuarter(id, need) }, success = { it ->
            it?.let { _mediaLessonTab.postValue(it) }
        })
    }

    fun getLessonInfo(lessonID: Int) {
        request(request = { service.newLessonInfo(lessonID) }, success = { it ->
            it?.info?.let {
                if (mediaInfoData.value != it) _mediaInfoData.postValue(it)
            }
        })
    }

    fun study(id: Int, duration: Int) {
        request(request = { service.newLessonStudyLog(id, duration, duration) }, success = { it ->
            it?.let {
                _studyLog.postValue(it)
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
    fun getLessonsValue() = lessonsData.value
    fun getLessonInfoValue() = lessonInfo.value
    fun getMediaInfoValue() = mediaInfoData.value
    fun getMediaLessonTabValue() = mediaLessonTab.value
    fun getMediaLessonTabChildValue(id: Int) = mediaLessonTabChildArray.get(id, null)

    fun hasPlayMore(): Boolean {
        val position = positionData.value ?: 0
        val size = lessonsData.value?.size ?: 0
        return size > position
    }

}