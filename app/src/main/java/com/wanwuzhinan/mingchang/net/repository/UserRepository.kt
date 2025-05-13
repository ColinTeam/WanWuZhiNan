package com.wanwuzhinan.mingchang.net.repository

import com.ssm.comm.response.ApiResponse
import com.wanwuzhinan.mingchang.data.CourseStudyData
import com.wanwuzhinan.mingchang.data.ExchangeCodeData
import com.wanwuzhinan.mingchang.data.ExchangeListData
import com.wanwuzhinan.mingchang.data.GoodsInfoData
import com.wanwuzhinan.mingchang.data.MedalListData
import com.wanwuzhinan.mingchang.data.QuestionListData
import com.wanwuzhinan.mingchang.data.QuestionLogData
import com.wanwuzhinan.mingchang.data.RankHomeData
import com.wanwuzhinan.mingchang.entity.CityInfo
import com.wanwuzhinan.mingchang.entity.Config
import com.wanwuzhinan.mingchang.entity.CourseInfoData
import com.wanwuzhinan.mingchang.entity.GradeInfo
import com.wanwuzhinan.mingchang.entity.Lesson
import com.wanwuzhinan.mingchang.entity.LessonSubject
import com.wanwuzhinan.mingchang.entity.UploadImgData
import com.wanwuzhinan.mingchang.entity.UserInfo
import com.wanwuzhinan.mingchang.net.repository.comm.CommRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
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
    ): ApiResponse<CityInfo> {
        return executeHttp { mService.getAllRegion() }
    }

    //获取年级
    suspend fun getAllGrade(pid: Int = 16): ApiResponse<GradeInfo> {
        return executeHttp { mService.getAllGrade(pid) }
    }

    //音频视频科目
    suspend fun courseSubject(group_id: Int): ApiResponse<ApiListResponse<LessonSubject>> {
        return executeHttp { mService.courseSubject(group_id) }
    }

    //音频视频科目列表
    suspend fun courseSubjectList(
        id: Int, need_lesson: Int
    ): ApiResponse<ApiInfoResponse<LessonSubject>> {
        return executeHttp { mService.courseSubjectList(id, need_lesson) }
    }

    //音频视频科目季度列表
    suspend fun courseQuarterList(
        lesson_subject_id: Int, need_lesson: Int
    ): ApiResponse<ApiListResponse<LessonSubject>> {
        return executeHttp { mService.courseQuarterList(lesson_subject_id, need_lesson) }
    }

    //音频视频列表通过季度id获取
    suspend fun courseList(lesson_quarter_id: Int): ApiResponse<ApiListResponse<Lesson>> {
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
        lesson_id: String, start_second: Int, end_second: Int
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
    suspend fun questionAdd(questions_id: Int, answer: String): ApiResponse<QuestionLogData> {
        return executeHttp { mService.questionAdd(questions_id, answer) }
    }

    suspend fun questionClear(): ApiResponse<Any> {
        return executeHttp { mService.questionClear() }
    }


    //错题集列表
    suspend fun questionErrorList(): ApiResponse<ApiListResponse<QuestionListData.QuestionBean>> {
        return executeHttp { mService.questionErrorList() }
    }

    //题目详情
    suspend fun questionDetail(id: Int): ApiResponse<ApiInfoResponse<QuestionListData>> {
        return executeHttp { mService.questionDetail(id) }
    }

    suspend fun questionPageDetail(
        id: Int, question_id: Int
    ): ApiResponse<ApiInfoResponse<QuestionListData>> {
        return executeHttp {
            mService.questionPageDetail(
                id, if (question_id == 0) "" else question_id.toString()
            )
        }
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
        typeid: Int, content: String, photos: String, version_name: String
    ): ApiResponse<MutableList<String>> {
        return executeHttp { mService.feedbackAdd(typeid, content, photos, version_name) }
    }

    suspend fun editAddress(
        contact_name: String, contact_address: String, contact_phone: String
    ): ApiResponse<MutableList<String>> {
        return executeHttp { mService.editAddress(contact_name, contact_address, contact_phone) }
    }

    suspend fun rankIndex(): ApiResponse<RankHomeData> {
        return executeHttp { mService.rankIndex() }
    }


    //上传图片
    suspend fun uploadImage(
        file: File
    ): ApiResponse<UploadImgData> {
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("file", file.name, requestBody)
        return executeHttp { mService.uploadImage(part) }
    }
}