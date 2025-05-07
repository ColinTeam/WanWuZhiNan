package com.wanwuzhinan.mingchang.entity

import com.ssm.comm.ui.base.BaseModel
import com.wanwuzhinan.mingchang.data.AppResponse
import com.wanwuzhinan.mingchang.data.QuestionListData

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-06 21:07
 *
 * Des   :ResponseData
 */

data class CourseInfoData(
    val info: MediaInfo
) : BaseModel()


data class MediaInfo(
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
    var questionsList: MutableList<QuestionListData.questionBean>,
    var photos_tipsArr: MutableList<PhotosInfo>,
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
    val path: String = "", val sort: String = "", var isShow: Boolean = false
) : BaseModel()


data class UserInfoData(
    var info: InfoBean
) : BaseModel() {

    data class InfoBean(
        var id: String = "",//
        var headimg: String = "",//
        var account: String = "",//
        var truename: String = "",//
        var nickname: String = "",//
        var sex: String = "",//
        var province_name: String = "",//
        var city_name: String = "",//
        var area_name: String = "",//
        var school_name: String = "",//
        var grade_name: String = "",//
        var question_count_error: String = "", var question_compass: String = ""
    ) : BaseModel()
}

class UserInfoResponse() : AppResponse<UserInfo>()

data class UserInfo(
    val id: String = "",//
    val headimg: String = "",//
    val account: String = "",//
    val truename: String = "",//
    val nickname: String = "",//
    val sex: String = "",//
    val province_name: String = "",//
    val city_name: String = "",//
    val area_name: String = "",//
    val school_name: String = "",//
    val grade_name: String = "",//
    val question_count_error: String = "", val question_compass: String = ""
)


class ConfigDataResponse() : AppResponse<Config>()

data class Config(
    val info: ConfigData? = null, val user_count: Int = 0, val user_countShow: Int = 0
)

data class ConfigData(
    var name: String = "",
    var title: String = "",
    var keywords: String = "",
    var description: String = "",
    var logo: String = "",
    var url: String = "",
    var phone_shouqian: String = "",
    var phone_shouhou: String = "",
    var phone_tousu: String = "",
    var copyright: String = "",//
    var qrcode_image: String = "",//企业微信二维码
    var qrcode_desc: String = "",//二维码上方的描述
    var qrcode_image_download: String = "",//保存的图片地址
    var enterprise_btn_name: String = "",//客服二维码页面按钮
    var qrcode_type: String = "",//1 下载图片 2 打开微信
    var home_ad: String = "",
    var home_ad_link: String = "",
    var enterprise_WeChat_id: String = "",
    var enterprise_WeChat_url: String = "",
    var home_title1: String = "",
    var home_title2: String = "",
    var home_title3: String = "",
    var home_title4: String = "",
    var home_title5: String = "",
    var code_verification: String = "",
    var apple_is_audit: Int = 0,//1审核模式
    var home_all_number: String = "",//总人数
    var android_code: String = "",//1审核模式
    var android_update: String = "",//1强制更新
)