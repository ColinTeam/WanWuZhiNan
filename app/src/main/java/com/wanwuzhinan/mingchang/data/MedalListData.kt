package com.wanwuzhinan.mingchang.data

data class MedalListData(
    var id: String,//
    var name: String,//
    var image: String,
    var image_show: String,
    var image_selected: String,
    var desc: String,
    var lesson_study_number_min: String,
    var lesson_study_hours_min: String,
    var question_number_min: String,
    var sort: String,
    var is_open: String,
    var update_time: String,
    var create_time: String,
    var del: String,
    var delName: String,
    var delColor: String,
    var is_has: Int,
    var is_has_new: String,
    var has_time: String,
    var needMsg: String,
    var needMin: Double,//任务数量
    var needHas: Double,//完成数量
)