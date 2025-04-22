package com.wanwuzhinan.mingchang.entity

import com.ssm.comm.ui.base.BaseModel

data class UserInfoData(
    var info: infoBean
): BaseModel(){

    data class infoBean(
        var id: String="",//
        var headimg: String="",//
        var account: String="",//
        var truename: String="",//
        var nickname: String="",//
        var sex: String="",//
        var province_name: String="",//
        var city_name: String="",//
        var area_name: String="",//
        var school_name: String="",//
        var grade_name: String="",//
        var question_count_error: String="",
        var question_compass: String=""
    ):BaseModel()
}
