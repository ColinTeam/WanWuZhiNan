package com.wanwuzhinan.mingchang.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.ssm.comm.ui.base.BaseModel
import com.wanwuzhinan.mingchang.data.AppResponse
import com.wanwuzhinan.mingchang.data.QuestionListData
import kotlinx.parcelize.Parcelize


/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-06 21:07
 *
 * Des   :ResponseData
 */
//1验证码登录，2密码登录，3找回密码 默认1
const val HTTP_ACTION_LOGIN_SMS = 1
const val HTTP_ACTION_LOGIN_PWD = 2
const val HTTP_ACTION_LOGIN_FIND_PWD = 3
const val HTTP_LOGIN_DEVICE_PHONE = 1
const val HTTP_LOGIN_DEVICE_TABLET = 2
const val HTTP_SUCCESS = 0
const val HTTP_CONFIRM = 9
const val HTTP_TOKEN_ERROR = 4
const val HTTP_TOKEN_EMPTY = 2

class SmsCodeResponse() : AppResponse<String>()
class RegisterResponse() : AppResponse<RegisterData>()
class UserInfoResponse() : AppResponse<UserData>()
class ConfigDataResponse() : AppResponse<Config>()
class LessonSubjectGroupResponse() : AppResponse<LessonSubjectGroup>()
class LessonInfoResponse() : AppResponse<LessonInfo>()
class CityInfoResponse() : AppResponse<CityInfo>()
class GradeResponse() : AppResponse<GradeInfo>()
class QuestionListResponse() : AppResponse<QuestionList>()

data class LessonInfo(
    val info: MediaInfo
)

/*后期替换成LessonInfo*/
data class CourseInfoData(
    val info: MediaInfo
) : BaseModel()

data class LessonStudyLog(
    var medalList: List<StudyLogInfo>, var medalCardList: List<StudyLogInfo>
) : BaseModel()

data class StudyLogInfo(
    val id: String,//
    val name: String,//
    val image: String,
    val image_show: String,
    val image_selected: String,
    val desc: String,
    val lesson_study_number_min: String,
    val lesson_study_hours_min: String,
    val question_number_min: String,
    val sort: String,
    val is_open: String,
    val update_time: String,
    val create_time: String,
    val del: String,
    val delName: String,
    val delColor: String,
    val is_has: Int,
    val is_has_new: String,
    val has_time: String,
    val needMsg: String,
    val needMin: Double,//任务数量
    val needHas: Double,//完成数量
)

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
    var questionsList: MutableList<QuestionListData.QuestionBean>,
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

data class UserData(val info: UserInfo)

data class UserInfo(
    val account: String = "",
    val area_name: String = "",
    val city_name: String = "",
    val contact_address: String = "",
    val contact_name: String = "",
    val contact_phone: String = "",
    val create_time: Int = 0,
    val grade_name: String = "",
    val headimg: String = "",
    val id: Int = 0,
    val nickname: String = "",
    val province_name: String = "",
    val question_compass: Int = 0,
    val question_count_error: Int = 0,
    val school_name: String = "",
    val sex: String = "",
    val truename: String = ""
) : BaseModel()

// {"code":0,"msg":"用户信息","data":{"info":{"id":2573,"account":"17721919249","headimg":"https:\/\/s.wanwuzhinan.top\/upload\/20250213\/07420263f3d58e477ba99e90cfee06fb.png","truename":"急急急","nickname":"急急急","sex":"女生","province_name":"北京","city_name":"北京市","area_name":"东城区","school_name":"","grade_name":"中班","question_compass":0,"contact_name":"","contact_phone":"","contact_address":"","create_time":1746852131,"question_count_error":0}}}


data class Config(
    val info: ConfigData = ConfigData(), val user_count: Int = 0, val user_countShow: Int = 0
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
    var android_code: Int = 0,//1审核模式
    var android_update: String = "",//1强制更新
)

data class LessonSubjectGroup(
    val list: List<LessonSubject> = emptyList<LessonSubject>()
)

//
//val id: String,//
//val select: Boolean,//
//val group_id: String,
//val type_id: String,
//val name: String,
//val image: String,
//val photos: String,
//val content: String,
//val sort: String,
//val is_open: String,
//val update_time: String,
//val create_time: String,
//val del: String,
//val delName: String,
//val delColor: String,
//val photosArr: List<PhotosBean>,
//val lessonList: List<Lesson>,
//val group_idName: String,
//val group_idColor: String,
//val type_idName: String,
//val lesson_quarterCount: String,
//val lesson_subject_name: String,
//val lessonCount: String,
//val lessonCountStar: Int,
//val lesson_quarterList: List<DataBean>

data class LessonSubject(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("content") val content: String = "",
    @SerializedName("create_time") val createTime: Int = 0,
    @SerializedName("del") val del: Int = 0,
    @SerializedName("delColor") val delColor: String = "",
    @SerializedName("delName") val delName: String = "",
    @SerializedName("group_id") val groupId: Int = 0,
    @SerializedName("group_idColor") val groupIdColor: String = "",
    @SerializedName("group_idName") val groupIdName: String = "",
    @SerializedName("image") val image: String = "",
    @SerializedName("is_open") val isOpen: Int = 0,
    @SerializedName("lessonCount") val lessonCount: Int = 0,
    @SerializedName("lesson_quarterCount") val lessonQuarterCount: Int = 0,
    @SerializedName("lesson_subject_name") val lesson_subject_name: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("photos") val photos: String = "",
    @SerializedName("photosArr") val photo: List<Photo> = listOf(),
    @SerializedName("lessonList") val lessons: List<Lesson> = listOf(),
    @SerializedName("lesson_quarterList") val lessonQuarter: List<LessonQuarter> = listOf(),
    @SerializedName("sort") val sort: Int = 0,
    @SerializedName("type_id") val typeId: Int = 0,
    @SerializedName("type_idName") val typeIdName: String = "",
    @SerializedName("update_time") val updateTime: Int = 0,
    var select: Boolean = false
)

@Parcelize
data class Lesson(
    val id: String,//
    val lesson_quarter_id: String,//
    val name: String,//
    val image: String,
    val video: String,
    val video_duration: String,
    val is_video: String,
    val sort: String,
    val is_open: String,
    val update_time: String,
    val create_time: String,
    val del: String,
    val has_power: String,
    val has_power_msg: String,
    val study_is_success: Int,
    val answerAccuracy: Int,
    val questionsCount: Int
) : Parcelable

data class Photo(
    var path: String,//
    var title: String,//
    var desc: String,//
    var sort: String
)

data class LessonQuarter(
    var id: Int,//
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
    var index: Int,//
)


data class SmsCode(var code: String = "")

data class RegisterData(val token: String = "", val user_id: String = "", val status: Int = 0)
data class Password(val token: String = "", val user_id: String = "")


data class CityInfo(val list: List<Children>) : BaseModel()

data class GradeInfo(var listArr: List<String>) : BaseModel()

data class Children(
    val children: List<Children> = emptyList<Children>(),
    val value: Int = 0,
    val label: String = "-"
)

data class QuestionList(val list: List<Question>)
data class Question(
    var id: Int,
    var select: Boolean,//
    var typeid: Int,//1单选 2多选
    val has_power: Int,
    var name: String,//
    var desc: String,//
    var image: String,//
    var video: String,//
    var video_duration: Long,
    var sort: String,//
    var is_open: String,//
    var update_time: String,//
    var create_time: String,//
    var del: String,//
    var delName: String,//
    var delColor: String,//
    var typeName: String,//
    var typeColor: String,//
    var answerLastQuestionBankId: Int,//
    var answerLastQuestionId: Int,//
    var questionsCount: Int,//
    var questionsListPageIndex: Int,//
    var questionsList: List<QuestionBean>,//
)

data class QuestionBean(
    var id: Int,//
    var questions_bank_id: String,//
    var typeid: Int,//
    var title: String,//
    var title_mp3: String,//
    var title_img: String,//
    var answer_true: String,//
    var answer_mp3: String,//
    var answer_mp3_true: String,//
    var answer_mp3_false: String,//
    var answer_desc: String,//
    var sort: String,//
    var is_open: String,//
    var update_time: String,//
    var create_time: String,//
    var is_select: Boolean,
    var answersArr: List<AnswerBean>,//
    var answer_res_answer: String,
    var answer_res_true: String,
    var answer_res_time: String,
    var index: Int

)

data class AnswerBean(
    var select: Boolean,//
    var answer: String,//
    var desc: String,//
    var sort: String,//
    var key: String,//
    var is_true: Int, var answerType: Int

)












































