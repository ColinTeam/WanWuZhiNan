package com.wanwuzhinan.mingchang.data

import com.ssm.comm.ui.base.BaseModel

data class CourseInfoData(
    val info: Info
) : BaseModel()
data class Info(
    val create_time: Int,
    val del: Int,
    val delColor: String,
    val delName: String,
    val has_power: Int,
    val has_power_msg: String,
    val id: Int,
    val image: String,
    val is_open: Int,
    val lesson_group_id: Int,
    val lesson_quarter_id: Int,
    val lesson_quarter_name: String,
    val lesson_subject_id: Int,
    val lesson_subject_name: String,
    val name: String,
    val powerGroupInfo: PowerGroupInfo,
    val powerLessonInfo: PowerLessonInfo,
    val powerSubjectInfo: PowerSubjectInfo,
    val sort: Int,
    val study_end_second: Int,
    val study_is_success: Int,
    val study_success_percentage: Int,
    val update_time: Int,
    val videoAes: String,
    val content: String,
    val video_duration: Int,
    var questionsList: MutableList<QuestionListData.questionBean>,//
    var photos_tipsArr: MutableList<PhotosInfo>,//
) : BaseModel()

data class PowerSubjectInfo(
    val activity_name: String,
    val code: String,
    val id: String,
    val user_end: Int,
    val user_start: Int
) : BaseModel()

data class PowerLessonInfo(
    val activity_name: String,
    val code: String,
    val id: String,
    val user_end: Int,
    val user_start: Int
) : BaseModel()

data class PowerGroupInfo(
    val activity_name: String,
    val code: String,
    val id: String,
    val user_end: Int,
    val user_start: Int
) : BaseModel()

data class PhotosInfo(
    val path: String = "",
    val sort: String = "",
    var isShow: Boolean = false
) : BaseModel()