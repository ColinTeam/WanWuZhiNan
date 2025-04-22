package com.wanwuzhinan.mingchang.vm

import androidx.lifecycle.viewModelScope
import com.comm.net_work.entity.ApiConfigInfoResponse
import com.wanwuzhinan.mingchang.data.CityData
import com.wanwuzhinan.mingchang.data.CourseInfoData
import com.wanwuzhinan.mingchang.data.GradeData
import com.wanwuzhinan.mingchang.data.MedalListData
import com.wanwuzhinan.mingchang.data.SubjectListData
import com.wanwuzhinan.mingchang.data.*
import com.wanwuzhinan.mingchang.entity.UploadImgData
import com.wanwuzhinan.mingchang.entity.UserInfoData
import com.wanwuzhinan.mingchang.net.repository.UserRepository
import com.comm.net_work.entity.ApiInfoResponse
import com.comm.net_work.entity.ApiListResponse
import com.hpplay.common.asyncmanager.AsyncFileParameter.In
import com.ssm.comm.data.VersionData
import com.ssm.comm.ext.StateMutableLiveData
import com.ssm.comm.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import java.io.File


class UserViewModel : BaseViewModel<UserInfoData, UserRepository>(UserRepository()) {

    val getUserInfoLiveData = StateMutableLiveData<UserInfoData>()
    val editUserInfoLiveData = StateMutableLiveData<MutableList<String>>()
    val getVersionLiveData = StateMutableLiveData<VersionData>()
    val uploadImgLiveData = StateMutableLiveData<UploadImgData>()
    val allProvinceLiveData = StateMutableLiveData<CityData>()
    val getAllGradeLiveData = StateMutableLiveData<GradeData>()
    val courseSubjectLiveData = StateMutableLiveData<ApiListResponse<SubjectListData>>()
    val courseSubjectListLiveData = StateMutableLiveData<ApiInfoResponse<SubjectListData>>()
    val courseQuarterListLiveData = StateMutableLiveData<ApiListResponse<SubjectListData>>()
    val courseListLiveData = StateMutableLiveData<ApiListResponse<SubjectListData.lessonBean>>()
    val courseExchangeLiveData = StateMutableLiveData<MutableList<String>>()
    val medalListLiveData = StateMutableLiveData<ApiListResponse<MedalListData>>()
    val courseStudyLiveData = StateMutableLiveData<CourseStudyData>()
    val questionListLiveData = StateMutableLiveData<ApiListResponse<QuestionListData>>()
    val questionDetailLiveData = StateMutableLiveData<ApiInfoResponse<QuestionListData>>()
    val questionAddLiveData = StateMutableLiveData<QuestionLogData>()
    val questionClearLiveData = StateMutableLiveData<Any>()
    val questionErrorLiveData = StateMutableLiveData<ApiListResponse<QuestionListData.questionBean>>()
    val getLessonInfoLiveData = StateMutableLiveData<CourseInfoData>()
    val getConfigLiveData = StateMutableLiveData<ApiConfigInfoResponse<ConfigData>>()
    val exchangeListLiveData = StateMutableLiveData<ApiListResponse<ExchangeListData>>()
    val giveListLiveData = StateMutableLiveData<ApiListResponse<ExchangeListData>>()
    val feedbackLiveData = StateMutableLiveData<MutableList<String>>()
    val editAddressLiveData = StateMutableLiveData<MutableList<String>>()
    val exchangeCodeLiveData = StateMutableLiveData<ApiListResponse<ExchangeCodeData>>()
    val rankIndexLiveData = StateMutableLiveData<RankHomeData>()
    val goodsInfoLiveData = StateMutableLiveData<ApiInfoResponse<GoodsInfoData>>()

    //获取用户信息
    fun getUserInfo() {
        viewModelScope.launch {
            //请求到的数据用livedata包裹
            getUserInfoLiveData.value = repository.getUserInfo()
        }
    }

    //修改用户信息
    fun editUserInfo(map: HashMap<String, Any>) {
        viewModelScope.launch {
            //请求到的数据用livedata包裹
            editUserInfoLiveData.value = repository.editUserInfo(map)
        }
    }

    //选择城市
    fun getAllRegion(){
        viewModelScope.launch {
            allProvinceLiveData.value = repository.getAllRegion()
        }
    }

    //获取年级
    fun getAllGrade(){
        viewModelScope.launch {
            getAllGradeLiveData.value = repository.getAllGrade()
        }
    }

    //音频视频科目
    fun courseSubject(group_id: Int){
        viewModelScope.launch {
            courseSubjectLiveData.value = repository.courseSubject(group_id)
        }
    }

    //音频视频科目列表
    fun courseSubjectList(id: String){
        viewModelScope.launch {
            courseSubjectListLiveData.value = repository.courseSubjectList(id,0)
        }
    }
    //音频视频科目列表和章节视频列表
    fun courseSubjectAndVideoList(id: String){
        viewModelScope.launch {
            courseSubjectListLiveData.value = repository.courseSubjectList(id,1)
        }
    }

    //音频视频科目季度列表
    fun courseQuarterList(lesson_subject_id:String,need_lesson: Int=1){
        viewModelScope.launch {
            courseQuarterListLiveData.value = repository.courseQuarterList(lesson_subject_id,need_lesson)
        }
    }

    //音频视频科目季度列表
    fun courseList(lesson_quarter_id:String){
        viewModelScope.launch {
            courseListLiveData.value = repository.courseList(lesson_quarter_id)
        }
    }

    //课程兑换
    fun courseExchange(code:String){
        viewModelScope.launch {
            courseExchangeLiveData.value = repository.courseExchange(code)
        }
    }

    //勋章列表
    fun medalList(is_has: Int){
        viewModelScope.launch {
            medalListLiveData.value = repository.medalList(is_has)
        }
    }

    //卡牌列表
    fun cardList(is_has: Int){
        viewModelScope.launch {
            medalListLiveData.value = repository.cardList(is_has)
        }
    }

    //学习课程 记录
    fun courseStudy(lesson_id: String,start_second: Int,end_second: Int){
        viewModelScope.launch {
            courseStudyLiveData.value = repository.courseStudy(lesson_id,start_second, end_second)
        }
    }

    fun feedbackLiveData(typeid: Int,content:String,photos:String){
        viewModelScope.launch {
            feedbackLiveData.value = repository.feedbackAdd(typeid, content, photos)
        }
    }

    fun exchangeCodeList(group_id: Int){
        viewModelScope.launch {
            exchangeCodeLiveData.value = repository.exchangeCodeList(group_id)
        }
    }

    fun editAddress(contact_name:String,contact_address:String,contact_phone:String){
        viewModelScope.launch {
            editAddressLiveData.value = repository.editAddress(contact_name, contact_address, contact_phone)
        }
    }

    fun rankIndex(){
        viewModelScope.launch {
            rankIndexLiveData.value = repository.rankIndex()
        }
    }




    //题目详情
    fun getLessonInfo(info: String) {
        viewModelScope.launch {
            //请求到的数据用livedata包裹
            getLessonInfoLiveData.value = repository.getLessonInfo(info)
        }
    }

    //获取配置
    fun getConfig() {
        viewModelScope.launch {
            //请求到的数据用livedata包裹
            getConfigLiveData.value = repository.getConfig()
        }
    }

    //课程兑换记录
    fun exchangeList(group_id: Int) {
        viewModelScope.launch {
            //请求到的数据用livedata包裹
            exchangeListLiveData.value = repository.exchangeList(group_id)
        }
    }

    //课程商品信息
    fun goodsInfo(goods_id: String) {
        viewModelScope.launch {
            //请求到的数据用livedata包裹
            goodsInfoLiveData.value = repository.goodsInfo(goods_id)
        }
    }

    //课程赠品记录
    fun giveList() {
        viewModelScope.launch {
            //请求到的数据用livedata包裹
            giveListLiveData.value = repository.giveList()
        }
    }

    //题库列表
    fun questionList(typeid: Int){
        viewModelScope.launch {
            questionListLiveData.value = repository.questionList(typeid)
        }
    }
    //题库答题
    fun questionAdd(questions_id: String,answer:String){
        viewModelScope.launch {
            questionAddLiveData.value = repository.questionAdd(questions_id,answer)
        }
    }
    fun questionClear(){
        viewModelScope.launch {
            questionClearLiveData.value = repository.questionClear()
        }
    }

    fun questionErrorList(){
        viewModelScope.launch {
            questionErrorLiveData.value = repository.questionErrorList()
        }
    }

    //题目详情
    fun questionDetail(id: String){
        viewModelScope.launch {
            questionDetailLiveData.value = repository.questionDetail(id)
        }
    }

    //获取版本号
    fun getVersion() {
        viewModelScope.launch {
            //请求到的数据用livedata包裹
            getVersionLiveData.value = repository.getVersion()
        }
    }

    //上传图片
    fun uploadImage(file: File) {
        viewModelScope.launch {
            //请求到的数据用livedata包裹
            uploadImgLiveData.value = repository.uploadImage(file)
        }
    }

}