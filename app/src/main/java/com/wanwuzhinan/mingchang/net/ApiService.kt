package com.wanwuzhinan.mingchang.net

import com.ssm.comm.data.VersionData
import com.ssm.comm.response.ApiResponse
import com.wanwuzhinan.mingchang.data.AppResponse
import com.wanwuzhinan.mingchang.data.CourseStudyData
import com.wanwuzhinan.mingchang.data.ExchangeCodeData
import com.wanwuzhinan.mingchang.data.ExchangeListData
import com.wanwuzhinan.mingchang.data.GoodsInfoData
import com.wanwuzhinan.mingchang.data.MedalListData
import com.wanwuzhinan.mingchang.data.QuestionListData
import com.wanwuzhinan.mingchang.data.QuestionLogData
import com.wanwuzhinan.mingchang.data.RankHomeData
import com.wanwuzhinan.mingchang.data.RegisterData
import com.wanwuzhinan.mingchang.entity.CityInfo
import com.wanwuzhinan.mingchang.entity.CityInfoResponse
import com.wanwuzhinan.mingchang.entity.Config
import com.wanwuzhinan.mingchang.entity.ConfigDataResponse
import com.wanwuzhinan.mingchang.entity.CourseInfoData
import com.wanwuzhinan.mingchang.entity.GradeInfo
import com.wanwuzhinan.mingchang.entity.GradeResponse
import com.wanwuzhinan.mingchang.entity.Lesson
import com.wanwuzhinan.mingchang.entity.LessonInfoResponse
import com.wanwuzhinan.mingchang.entity.LessonSubject
import com.wanwuzhinan.mingchang.entity.LessonSubjectGroupResponse
import com.wanwuzhinan.mingchang.entity.QuestionListResponse
import com.wanwuzhinan.mingchang.entity.RegisterResponse
import com.wanwuzhinan.mingchang.entity.SmsCodeResponse
import com.wanwuzhinan.mingchang.entity.UploadImgData
import com.wanwuzhinan.mingchang.entity.UserInfo
import com.wanwuzhinan.mingchang.entity.UserInfoResponse
import com.wanwuzhinan.mingchang.net.repository.ApiInfoResponse
import com.wanwuzhinan.mingchang.net.repository.ApiListResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    //获取验证码
    @FormUrlEncoded
    @POST("/api/SmsCode/send")
    suspend fun newCode(@Field("phone") phone: String): SmsCodeResponse

    //登录
    @FormUrlEncoded
    @POST("/api/UserLogin/index")
    suspend fun newLogin(
        @Field("phone") phone: String,
        @Field("phone_code") phone_code: String,
        @Field("device_type") device_type: Int,
        @Field("action") action: Int
    ): RegisterResponse

    @FormUrlEncoded
    @POST("/api/UserLogin/index")
    suspend fun newLoginPwd(
        @Field("phone") phone: String,
        @Field("phone_code") sms: String?,
        @Field("pwd") pwd: String,
        @Field("pwds") pwds: String,
        @Field("action") action: Int,
        @Field("device_type") device_type: Int,
        @Field("confirm") confirm: Int
    ): RegisterResponse

    //音频视频科目
    @FormUrlEncoded
    @POST("/api/LessonSubject/index")
    suspend fun newMediaLessonSubjectGroup(
        @Field("group_id") group_id: Int
    ): LessonSubjectGroupResponse

    //音频视频科目列表
    @FormUrlEncoded
    @POST("/api/LessonSubject/info")
    suspend fun newLessonSubject(
        @Field("id") id: Int, @Field("need_lesson") need_lesson: Int
    ): LessonSubjectGroupResponse

    //音频视频科目季度列表
    @FormUrlEncoded
    @POST("/api/LessonQuarter/index")
    suspend fun newAudioLessonQuarter(
        @Field("lesson_subject_id") lesson_subject_id: Int, @Field("need_lesson") need_lesson: Int
    ): LessonSubjectGroupResponse

    @FormUrlEncoded
    @POST("/api/Lesson/infoV2")
    suspend fun newLessonInfo(
        @Field("id") id: Int,
    ): LessonInfoResponse

    //学习课程 记录
    @FormUrlEncoded
    @POST("/api/LessonStudyLog/add")
    suspend fun newLessonStudyLog(
        @Field("lesson_id") lesson_id: String,
        @Field("video_duration") start_second: Int,
        @Field("end_second") end_second: Int
    ): ApiResponse<CourseStudyData>

    //获取全部省份
    @POST("/api/District/indexPicker")
    suspend fun newCityInfo(): CityInfoResponse

    //获取年级
    @FormUrlEncoded
    @POST("/api/Fragment/index")
    suspend fun newGrade(
        @Field("pid") pid: Int
    ): GradeResponse

    //修改用户信息
    @FormUrlEncoded
    @POST("/api/User/edit")
    suspend fun newEditUserInfo(@FieldMap map: HashMap<String, String>): AppResponse<List<String>>

    //题库列表
    @FormUrlEncoded
    @POST("/api/QuestionsBank/index")
    suspend fun newQuestionList(@Field("typeid") typeid: Int): QuestionListResponse


    //获取年级
    @FormUrlEncoded
    @POST("/api/Fragment/index")
    suspend fun getAllGrade(
        @Field("pid") pid: Int
    ): ApiResponse<GradeInfo>

    //获取验证码
    @FormUrlEncoded
    @POST("/api/SmsCode/send")
    fun getCode(@Field("phone") mobile: String): Call<ResponseBody>

    //登录
    @FormUrlEncoded
    @POST("/api/UserLogin/index")
    suspend fun login(
        @Field("phone") phone: String,
        @Field("phone_code") phone_code: String,
        @Field("device_type") device_type: String
    ): ApiResponse<RegisterData>

    //获取用户信息
    @POST("/api/User/info")
    suspend fun getUserInfo(): ApiResponse<UserInfo>

    //获取用户信息
    @POST("/api/User/info")
    suspend fun newUserInfo(): UserInfoResponse

    //获取配置
    @POST("/api/Systems/index")
    suspend fun newConfig(): ConfigDataResponse


    //错误日志上报
    @FormUrlEncoded
    @POST("api/Video/errorlog")
    suspend fun newErrorLog(@Field("error") error: String): AppResponse<Nothing>

    //修改用户信息
    @FormUrlEncoded
    @POST("/api/User/edit")
    suspend fun editUserInfo(@FieldMap map: HashMap<String, Any>): ApiResponse<MutableList<String>>

    //获取全部省份
    @POST("/api/District/indexPicker")
    suspend fun getAllRegion(
    ): ApiResponse<CityInfo>


    //上传图片
    @Multipart
    @POST("/api/Upload/one")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): ApiResponse<UploadImgData>

    //音频视频科目
    @FormUrlEncoded
    @POST("/api/LessonSubject/index")
    suspend fun courseSubject(
        @Field("group_id") group_id: Int
    ): ApiResponse<ApiListResponse<LessonSubject>>

    //音频视频科目列表
    @FormUrlEncoded
    @POST("/api/LessonSubject/info")
    suspend fun courseSubjectList(
        @Field("id") id: Int, @Field("need_lesson") need_lesson: Int
    ): ApiResponse<ApiInfoResponse<LessonSubject>>

    //音频视频科目季度列表
    @FormUrlEncoded
    @POST("/api/LessonQuarter/index")
    suspend fun courseQuarterList(
        @Field("lesson_subject_id") lesson_subject_id: Int, @Field("need_lesson") need_lesson: Int
    ): ApiResponse<ApiListResponse<LessonSubject>>

    //视频列表通过季度id获取
    @FormUrlEncoded
    @POST("/api/Lesson/index")
    suspend fun courseList(
        @Field("lesson_quarter_id") lesson_subject_id: Int,
    ): ApiResponse<ApiListResponse<Lesson>>

    //课程兑换
    @FormUrlEncoded
    @POST("/api/LessonActivation/add")
    suspend fun courseExchange(
        @Field("code") code: String
    ): ApiResponse<MutableList<String>>

    //勋章列表
    @FormUrlEncoded
    @POST("/api/Medal/index")
    suspend fun medalList(@Field("is_has") is_has: Int): ApiResponse<ApiListResponse<MedalListData>>

    //卡牌列表
    @FormUrlEncoded
    @POST("/api/MedalCard/index")
    suspend fun cardList(@Field("is_has") is_has: Int): ApiResponse<ApiListResponse<MedalListData>>

    //学习课程 记录
    @FormUrlEncoded
    @POST("/api/LessonStudyLog/add")
    suspend fun courseStudy(
        @Field("lesson_id") lesson_id: String,
        @Field("video_duration") start_second: Int,
        @Field("end_second") end_second: Int
    ): ApiResponse<CourseStudyData>

    @FormUrlEncoded
    @POST("/api/Lesson/infoV2")
    suspend fun getLessonInfo(
        @Field("id") id: String,
    ): ApiResponse<CourseInfoData>

    //题库列表
    @FormUrlEncoded
    @POST("/api/QuestionsBank/index")
    suspend fun questionList(@Field("typeid") typeid: Int): ApiResponse<ApiListResponse<QuestionListData>>

    //题库答题
    @FormUrlEncoded
    @POST("/api/QuestionsLogs/add")
    suspend fun questionAdd(
        @Field("questions_id") questions_id: Int, @Field("answer") answer: String
    ): ApiResponse<QuestionLogData>

    //题库答题记录清理
    @POST("/api/QuestionsLogs/clear")
    suspend fun questionClear(): ApiResponse<Any>

    //错题记
    @POST("/api/QuestionsLogs/index")
    suspend fun questionErrorList(
    ): ApiResponse<ApiListResponse<QuestionListData.QuestionBean>>


    //题目详情
    @FormUrlEncoded
    @POST("/api/QuestionsBank/info")
    suspend fun questionDetail(@Field("id") id: Int): ApiResponse<ApiInfoResponse<QuestionListData>>

    //题目详情分页
    @FormUrlEncoded
    @POST("/api/QuestionsBank/infoPage")
    suspend fun questionPageDetail(
        @Field("id") id: Int, @Field("question_id") question_id: String
    ): ApiResponse<ApiInfoResponse<QuestionListData>>


    //获取配置
    @POST("/api/Systems/index")
    suspend fun getConfig(): ApiResponse<Config>


    //课程兑换记录
    @FormUrlEncoded
    @POST("/api/LessonCode/indexLesson")
    suspend fun exchangeList(@Field("group_id") group_id: Int): ApiResponse<ApiListResponse<ExchangeListData>>

    //课程兑换码 记录
    @FormUrlEncoded
    @POST("/api/LessonCode/index")
    suspend fun exchangeCodeList(@Field("group_id") group_id: Int): ApiResponse<ApiListResponse<ExchangeCodeData>>

    //赠品列表
    @POST("/api/LessonCodeGoods/index")
    suspend fun giveList(): ApiResponse<ApiListResponse<ExchangeListData>>

    //赠品详情
    @FormUrlEncoded
    @POST("/api/LessonCodeGoods/info")
    suspend fun goodsInfo(@Field("id") group_id: String): ApiResponse<ApiInfoResponse<GoodsInfoData>>


    //意见反馈
    @FormUrlEncoded
    @POST("/api/OnlineFeedback/add")
    suspend fun feedbackAdd(
        @Field("typeid") typeid: Int,
        @Field("content") content: String,
        @Field("photos") photos: String,
        @Field("version_name") version_name: String,
    ): ApiResponse<MutableList<String>>

    @FormUrlEncoded
    @POST("/api/User/editAddress")
    suspend fun editAddress(
        @Field("contact_name") contact_name: String,
        @Field("contact_address") contact_address: String,
        @Field("contact_phone") contact_phone: String,
    ): ApiResponse<MutableList<String>>

    @POST("/api/User/indexRank")
    suspend fun rankIndex(): ApiResponse<RankHomeData>


    //获取版本号
    @POST("/v1/common/version")
    suspend fun getVersion(@Header("signature") signature: String): ApiResponse<VersionData>


}