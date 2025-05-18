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

    private val _mediaLessonTab: MutableLiveData<LessonSubjectGroup> = MutableLiveData()
    val mediaLessonTab: LiveData<LessonSubjectGroup> = _mediaLessonTab

    private val _mediaLessonTabChild: MutableLiveData<LessonSubjectGroup> = MutableLiveData()
    val mediaLessonTabChild: LiveData<LessonSubjectGroup> = _mediaLessonTabChild

    private val _lessonInfo: MutableLiveData<LessonInfo> = MutableLiveData()
    val lessonInfo: LiveData<LessonInfo> = _lessonInfo

    private val _positionData: MutableLiveData<Int> = MutableLiveData(0)
    val positionData: LiveData<Int> = _positionData

    private val _lessonsData: MutableLiveData<List<Lesson?>?> = MutableLiveData(null)
    val lessonsData: LiveData<List<Lesson?>?> = _lessonsData
    private val mediaLessonTabChildArray: SparseArray<LessonSubjectGroup> = SparseArray()


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

    fun getMediaLessonTab(groupID: Int = ConfigApp.TYPE_AUDIO) {
        request(request = { service.newMediaLessonTab(groupID) }, success = { it ->
            it?.let { _mediaLessonTab.postValue(it) }
        })
    }

    fun getMediaLessonTabChild(groupID: Int, need: Int = 1) {
        request(request = { service.newMediaLessonTabChild(groupID, need) }, success = { it ->
            it?.let {
                mediaLessonTabChildArray.put(groupID, it)
                _mediaLessonTabChild.postValue(it)
            }
        })
    }

    fun getMediaQuarterChild(groupID: Int, need: Int = 1) {
        request(request = { service.newAudioLessonQuarter(groupID, need) }, success = { it ->
            it?.let {
                mediaLessonTabChildArray.put(groupID, it)
                _mediaLessonTabChild.postValue(it)
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
    fun getMediaLessonTabValue() = mediaLessonTab.value
    fun getMediaLessonTabChildValue(id: Int) = mediaLessonTabChildArray.get(id, null)


}