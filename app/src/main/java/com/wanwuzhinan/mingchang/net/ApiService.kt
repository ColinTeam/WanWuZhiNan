package com.wanwuzhinan.mingchang.net

import com.comm.net_work.entity.ApiConfigInfoResponse
import com.wanwuzhinan.mingchang.data.*
import com.comm.net_work.entity.ApiResponse
import com.wanwuzhinan.mingchang.entity.UploadImgData
import com.wanwuzhinan.mingchang.entity.UserInfoData
import com.comm.net_work.entity.ApiInfoResponse
import com.comm.net_work.entity.ApiListResponse
import com.ssm.comm.data.VersionData
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
    fun getCode(@Field("phone") mobile: String): Call<ResponseBody>

    //登录
    @FormUrlEncoded
    @POST("/api/UserLogin/index")
    suspend fun login(
        @Field("phone") phone: String,
        @Field("phone_code") phone_code: String,
        @Field("device_type") device_type:String
    ): ApiResponse<RegisterData>

    //获取用户信息
    @POST("/api/User/info")
    suspend fun getUserInfo(): ApiResponse<UserInfoData>

    //修改用户信息
    @FormUrlEncoded
    @POST("/api/User/edit")
    suspend fun editUserInfo(@FieldMap map: HashMap<String, Any>): ApiResponse<MutableList<String>>

    //获取全部省份
    @POST("/api/District/indexPicker")
    suspend fun getAllRegion(
    ): ApiResponse<CityData>

    //获取年级
    @FormUrlEncoded
    @POST("/api/Fragment/index")
    suspend fun getAllGrade(
        @Field("pid") pid: Int
    ): ApiResponse<GradeData>

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
    ): ApiResponse<ApiListResponse<SubjectListData>>

    //音频视频科目列表
    @FormUrlEncoded
    @POST("/api/LessonSubject/info")
    suspend fun courseSubjectList(
        @Field("id") id: String,
        @Field("need_lesson") need_lesson: Int
    ): ApiResponse<ApiInfoResponse<SubjectListData>>

    //音频视频科目季度列表
    @FormUrlEncoded
    @POST("/api/LessonQuarter/index")
    suspend fun courseQuarterList(
        @Field("lesson_subject_id") lesson_subject_id: String,
        @Field("need_lesson") need_lesson: Int
    ): ApiResponse<ApiListResponse<SubjectListData>>

    //视频列表通过季度id获取
    @FormUrlEncoded
    @POST("/api/Lesson/index")
    suspend fun courseList(
        @Field("lesson_quarter_id") lesson_subject_id: String,
    ): ApiResponse<ApiListResponse<SubjectListData.lessonBean>>

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
    suspend fun courseStudy(@Field("lesson_id") lesson_id: String,
                            @Field("video_duration") start_second: Int,
                            @Field("end_second") end_second: Int): ApiResponse<CourseStudyData>

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
    suspend fun questionAdd(@Field("questions_id") questions_id: String,@Field("answer") answer: String): ApiResponse<QuestionLogData>

    //题库答题记录清理
    @POST("/api/QuestionsLogs/clear")
    suspend fun questionClear(): ApiResponse<Any>

    //错题记
    @POST("/api/QuestionsLogs/index")
    suspend fun questionErrorList(
    ): ApiResponse<ApiListResponse<QuestionListData.questionBean>>


    //题目详情
    @FormUrlEncoded
    @POST("/api/QuestionsBank/info")
    suspend fun questionDetail(@Field("id") id: String): ApiResponse<ApiInfoResponse<QuestionListData>>

    //获取配置
    @POST("/api/Systems/index")
    suspend fun getConfig(): ApiResponse<ApiConfigInfoResponse<ConfigData>>

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


    //注册
    @FormUrlEncoded
    @POST("/v1/common/register")
    suspend fun register(
        @Header("signature") signature: String,
        @Field("mobile") mobile: String,
        @Field("code") code: String,
        @Field("password") password: String,
        @Field("pay_password") pay_password: String,
        @Field("parent") parent: String
    ): ApiResponse<RegisterData>


    //忘记密码
    @FormUrlEncoded
    @POST("/v1/common/forget_password")
    suspend fun forgetPass(
        @Header("signature") signature: String,
        @Field("mobile") mobile: String,
        @Field("password") password: String,
        @Field("code") code: String
    ): ApiResponse<MutableList<String>>


    //修改支付密码
    @FormUrlEncoded
    @POST("/v1/user/edit_pay_password")
    suspend fun changePayPass(
        @Header("signature") signature: String,
        @Field("new_password") new_password: String,
        @Field("new_password2") new_password2: String,
        @Field("code") code: String
    )
            : ApiResponse<MutableList<String>>

    //修改登录密码
    @FormUrlEncoded
    @POST("/v1/user/edit_pay_password")
    suspend fun changeLoginPass(
        @Header("signature") signature: String,
        @Field("password") password: String,
        @Field("code") code: String
    ) : ApiResponse<MutableList<String>>

    //获取版本号
    @POST("/v1/common/version")
    suspend fun getVersion(@Header("signature") signature: String): ApiResponse<VersionData>


    //获取说明
    @FormUrlEncoded
    @POST("/v1/illustrate/get_index")
    fun getTextDescription(
        @Header("signature") signature: String,
        @Field("key") key: String
    ): Call<ResponseBody>

    //获取地址列表
    @POST("/v1/address/get_index")
    suspend fun getAddressList(@Header("signature") signature: String): ApiResponse<MutableList<AddressData>>

    //添加地址
    @FormUrlEncoded
    @POST("/v1/address/add")
    suspend fun addAddress(
        @Header("signature") signature: String,
        @FieldMap map: MutableMap<String, Any>
    ): ApiResponse<MutableList<String>>


    //删除地址
    @FormUrlEncoded
    @POST("/v1/address/del")
    suspend fun deleteAddress(
        @Header("signature") signature: String,
        @Field("address_id") address_id: String
    ): ApiResponse<MutableList<String>>

    //获取默认地址
    @POST("/v1/address/get_moren")
    suspend fun getDefaultAddress(@Header("signature") signature: String): ApiResponse<AddressData>

    //获取城市列表
    @FormUrlEncoded
    @POST("/v1/gaode/get_city")
    suspend fun getCityList(
        @Header("signature") signature: String,
        @Field("city_id") city_id: String
    ): ApiResponse<MutableList<CityListData>>

}