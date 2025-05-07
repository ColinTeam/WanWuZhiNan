package com.wanwuzhinan.mingchang.net.repository

import com.comm.net_work.entity.ApiInfoResponse
import com.comm.net_work.entity.ApiListResponse
import com.comm.net_work.entity.ApiResponse
import com.comm.net_work.gson.GsonManager
import com.ssm.comm.data.VersionData
import com.ssm.comm.utils.LogUtils
import com.wanwuzhinan.mingchang.data.CityData
import com.wanwuzhinan.mingchang.data.CourseStudyData
import com.wanwuzhinan.mingchang.data.ExchangeCodeData
import com.wanwuzhinan.mingchang.data.ExchangeListData
import com.wanwuzhinan.mingchang.data.GoodsInfoData
import com.wanwuzhinan.mingchang.data.GradeData
import com.wanwuzhinan.mingchang.data.MedalListData
import com.wanwuzhinan.mingchang.data.QuestionListData
import com.wanwuzhinan.mingchang.data.QuestionLogData
import com.wanwuzhinan.mingchang.data.RankHomeData
import com.wanwuzhinan.mingchang.data.SubjectListData
import com.wanwuzhinan.mingchang.data.TextDescriptionData
import com.wanwuzhinan.mingchang.entity.Config
import com.wanwuzhinan.mingchang.entity.CourseInfoData
import com.wanwuzhinan.mingchang.entity.UploadImgData
import com.wanwuzhinan.mingchang.entity.UserInfo
import com.wanwuzhinan.mingchang.net.repository.comm.CommRepository
import com.wanwuzhinan.mingchang.thread.EaseThreadManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.File

/**
 * Company:AD
 * ProjectName: 绿色生态
 * Package: com.green.ecology.net.repository
 * ClassName: UserRepository
 * Author:ShiMing Shi
 * CreateDate: 2022/12/31 17:48
 * Email:shiming024@163.com
 * Description:
 */
class UserRepository : CommRepository() {

    //获取用户信息
    suspend fun getUserInfo(): ApiResponse<UserInfo> {
        return executeHttp { mService.getUserInfo() }
    }

    //修改用户信息
    suspend fun editUserInfo(map: HashMap<String, Any>): ApiResponse<MutableList<String>> {
        return executeHttp { mService.editUserInfo(map) }
    }

    //选择城市
    suspend fun getAllRegion(
    ): ApiResponse<CityData> {
        return executeHttp { mService.getAllRegion() }
    }

    //获取年级
    suspend fun getAllGrade(pid: Int = 16): ApiResponse<GradeData> {
        return executeHttp { mService.getAllGrade(pid) }
    }

    //音频视频科目
    suspend fun courseSubject(group_id: Int): ApiResponse<ApiListResponse<SubjectListData>> {
        return executeHttp { mService.courseSubject(group_id) }
    }

    //音频视频科目列表
    suspend fun courseSubjectList(
        id: String,
        need_lesson: Int
    ): ApiResponse<ApiInfoResponse<SubjectListData>> {
        return executeHttp { mService.courseSubjectList(id, need_lesson) }
    }

    //音频视频科目季度列表
    suspend fun courseQuarterList(
        lesson_subject_id: String,
        need_lesson: Int
    ): ApiResponse<ApiListResponse<SubjectListData>> {
        return executeHttp { mService.courseQuarterList(lesson_subject_id, need_lesson) }
    }

    //音频视频列表通过季度id获取
    suspend fun courseList(lesson_quarter_id: String): ApiResponse<ApiListResponse<SubjectListData.lessonBean>> {
        return executeHttp { mService.courseList(lesson_quarter_id) }
    }

    //课程兑换
    suspend fun courseExchange(code: String): ApiResponse<MutableList<String>> {
        return executeHttp { mService.courseExchange(code) }
    }

    //勋章列表
    suspend fun medalList(is_has: Int): ApiResponse<ApiListResponse<MedalListData>> {
        return executeHttp { mService.medalList(is_has) }
    }

    //卡牌列表
    suspend fun cardList(is_has: Int): ApiResponse<ApiListResponse<MedalListData>> {
        return executeHttp { mService.cardList(is_has) }
    }

    //学习课程 记录
    suspend fun courseStudy(
        lesson_id: String,
        start_second: Int,
        end_second: Int
    ): ApiResponse<CourseStudyData> {
        return executeHttp { mService.courseStudy(lesson_id, start_second, end_second) }
    }

    suspend fun getLessonInfo(id: String): ApiResponse<CourseInfoData> {
        return executeHttp { mService.getLessonInfo(id) }
    }

    //题库列表
    suspend fun questionList(typeid: Int): ApiResponse<ApiListResponse<QuestionListData>> {
        return executeHttp { mService.questionList(typeid) }
    }

    //题库答题
    suspend fun questionAdd(questions_id: String, answer: String): ApiResponse<QuestionLogData> {
        return executeHttp { mService.questionAdd(questions_id, answer) }
    }

    suspend fun questionClear(): ApiResponse<Any> {
        return executeHttp { mService.questionClear() }
    }


    //错题集列表
    suspend fun questionErrorList(): ApiResponse<ApiListResponse<QuestionListData.questionBean>> {
        return executeHttp { mService.questionErrorList() }
    }

    //题目详情
    suspend fun questionDetail(id: String): ApiResponse<ApiInfoResponse<QuestionListData>> {
        return executeHttp { mService.questionDetail(id) }
    }

    suspend fun questionPageDetail(
        id: String,
        question_id: String
    ): ApiResponse<ApiInfoResponse<QuestionListData>> {
        return executeHttp { mService.questionPageDetail(id, question_id) }
    }

    //获取配置
    suspend fun getConfig(): ApiResponse<Config> {
        return executeHttp { mService.getConfig() }
    }

    //课程兑换记录
    suspend fun exchangeList(group_id: Int): ApiResponse<ApiListResponse<ExchangeListData>> {
        return executeHttp { mService.exchangeList(group_id) }
    }

    //课程赠品记录
    suspend fun giveList(): ApiResponse<ApiListResponse<ExchangeListData>> {
        return executeHttp { mService.giveList() }
    }

    //课程赠品信息
    suspend fun goodsInfo(goods_id: String): ApiResponse<ApiInfoResponse<GoodsInfoData>> {
        return executeHttp { mService.goodsInfo(goods_id) }
    }

    suspend fun exchangeCodeList(group_id: Int): ApiResponse<ApiListResponse<ExchangeCodeData>> {
        return executeHttp { mService.exchangeCodeList(group_id) }
    }

    suspend fun feedbackAdd(
        typeid: Int,
        content: String,
        photos: String,
        version_name: String
    ): ApiResponse<MutableList<String>> {
        return executeHttp { mService.feedbackAdd(typeid, content, photos, version_name) }
    }

    suspend fun editAddress(
        contact_name: String,
        contact_address: String,
        contact_phone: String
    ): ApiResponse<MutableList<String>> {
        return executeHttp { mService.editAddress(contact_name, contact_address, contact_phone) }
    }

    suspend fun rankIndex(): ApiResponse<RankHomeData> {
        return executeHttp { mService.rankIndex() }
    }


    //获取版本号
    suspend fun getVersion(): ApiResponse<VersionData> {
        val signature = setSignStr()
        return executeHttp { mService.getVersion(signature) }
    }

    //上传图片
    suspend fun uploadImage(
        file: File
    ): ApiResponse<UploadImgData> {
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("file", file.name, requestBody)
        return executeHttp { mService.uploadImage(part) }
    }


    fun getTextDescription(
        key: String,
        showLoading: () -> Unit,
        hideLoading: () -> Unit,
        onSuccess: (data: TextDescriptionData.Result) -> Unit,
        onFailure: (error: String) -> Unit
    ) {
        showLoading()
        EaseThreadManager.getInstance().runOnIOThread {
            val signature = setSignStr(Pair("key", key))
            val call = mService.getTextDescription(signature, key)
            //调用enqueue方法异步返回结果
            call.enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    try {
                        val result = response.body()!!.string()
                        LogUtils.e("result===================>${result}")
                        val response =
                            GsonManager.get().getGson()
                                .fromJson(result, TextDescriptionData::class.java)
                        if (response.code == 200) {
                            EaseThreadManager.getInstance().runOnMainThread {
                                hideLoading()
                                onSuccess(response.result)
                            }
                        } else {
                            EaseThreadManager.getInstance().runOnMainThread {
                                hideLoading()
                                onFailure(response.msg)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        EaseThreadManager.getInstance().runOnMainThread {
                            hideLoading()
                            onFailure("说明文本内容获取失败")
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    EaseThreadManager.getInstance().runOnMainThread {
                        hideLoading()
                        onFailure(t.message.toString())
                    }
                }
            })
        }
    }
}