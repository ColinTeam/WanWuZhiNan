package com.wanwuzhinan.mingchang.data

import android.os.Parcelable
import com.ssm.comm.ui.base.BaseModel
import kotlinx.parcelize.Parcelize

data class SubjectListData(
    var id: String,//
    var select: Boolean,//
    var group_id: String,
    var type_id: String,
    var name: String,
    var image: String,
    var photos: String,
    var content: String,
    var sort: String,
    var is_open: String,
    var update_time: String,
    var create_time: String,
    var del: String,
    var delName: String,
    var delColor: String,
    var photosArr: MutableList<photosBean>,
    var lessonList: MutableList<lessonBean>,
    var group_idName: String,
    var group_idColor: String,
    var type_idName: String,
    var lesson_quarterCount: String,
    var lesson_subject_name: String,
    var lessonCount: String,
    var lessonCountStar: Int,
    var lesson_quarterList: MutableList<dataBean>
) : BaseModel(){
    @Parcelize
    data class lessonBean(
        var id: String,//
        var lesson_quarter_id: String,//
        var name: String,//
        var image: String,
        var video: String,
        var video_duration: String,
        var is_video: String,
        var sort: String,
        var is_open: String,
        var update_time: String,
        var create_time: String,
        var del: String,
        var has_power : String,
        var has_power_msg : String,
        var study_is_success : Int,
        var answerAccuracy : Int,
        var questionsCount : Int
    ):BaseModel(), Parcelable

    data class photosBean(
        var path: String,//
        var title: String,//
        var desc: String,//
        var sort: String
    ):BaseModel()

    data class dataBean(
        var id: String,//
        var lesson_subject_id: String,//
        var name: String,//
        var image: String,//
        var photos: String,//
        var content: String,//
        var sort: String,//
        var is_open: Int,//
        var lessonCount: String,//
        var update_time: String,//
        var create_time: String,//
        var del: String,//
        var index : Int,//
    ):BaseModel()
}
