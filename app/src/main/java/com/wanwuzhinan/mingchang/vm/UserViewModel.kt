package com.wanwuzhinan.mingchang.vm

import androidx.lifecycle.viewModelScope
import com.comm.net_work.entity.ApiInfoResponse
import com.comm.net_work.entity.ApiListResponse
import com.ssm.comm.data.VersionData
import com.ssm.comm.ui.base.BaseViewModel
import com.wanwuzhinan.mingchang.data.CourseStudyData
import com.wanwuzhinan.mingchang.data.ExchangeCodeData
import com.wanwuzhinan.mingchang.data.ExchangeListData
import com.wanwuzhinan.mingchang.data.GoodsInfoData
import com.wanwuzhinan.mingchang.data.MedalListData
import com.wanwuzhinan.mingchang.data.QuestionListData
import com.wanwuzhinan.mingchang.data.QuestionLogData
import com.wanwuzhinan.mingchang.data.RankHomeData
import com.wanwuzhinan.mingchang.data.SubjectListData
import com.wanwuzhinan.mingchang.entity.CityInfo
import com.wanwuzhinan.mingchang.entity.Config
import com.wanwuzhinan.mingchang.entity.CourseInfoData
import com.wanwuzhinan.mingchang.entity.GradeInfo
import com.wanwuzhinan.mingchang.entity.UploadImgData
import com.wanwuzhinan.mingchang.entity.UserInfo
import com.wanwuzhinan.mingchang.net.repository.UserRepository
import kotlinx.coroutines.launch
import java.io.File


class UserViewModel : BaseViewModel<UserInfo, UserRepository>(UserRepository()) {

    val getUserInfoLiveData = com.ssm.comm.ext.StateMutableLiveData<UserInfo>()
    val editUserInfoLiveData = com.ssm.comm.ext.StateMutableLiveData<MutableList<String>>()
    val getVersionLiveData = com.ssm.comm.ext.StateMutableLiveData<VersionData>()
    val uploadImgLiveData = com.ssm.comm.ext.StateMutableLiveData<UploadImgData>()
    val allProvinceLiveData = com.ssm.comm.ext.StateMutableLiveData<CityInfo>()
    val getAllGradeLiveData = com.ssm.comm.ext.StateMutableLiveData<GradeInfo>()
    val courseSubjectLiveData = com.ssm.comm.ext.StateMutableLiveData<ApiListResponse<SubjectListData>>()
    val courseSubjectListLiveData = com.ssm.comm.ext.StateMutableLiveData<ApiInfoResponse<SubjectListData>>()
    val courseQuarterListLiveData = com.ssm.comm.ext.StateMutableLiveData<ApiListResponse<SubjectListData>>()
    val courseListLiveData = com.ssm.comm.ext.StateMutableLiveData<ApiListResponse<SubjectListData.lessonBean>>()
    val courseExchangeLiveData = com.ssm.comm.ext.StateMutableLiveData<MutableList<String>>()
    val medalListLiveData = com.ssm.comm.ext.StateMutableLiveData<ApiListResponse<MedalListData>>()
    val courseStudyLiveData = com.ssm.comm.ext.StateMutableLiveData<CourseStudyData>()
    val questionListLiveData = com.ssm.comm.ext.StateMutableLiveData<ApiListResponse<QuestionListData>>()
    val questionDetailLiveData = com.ssm.comm.ext.StateMutableLiveData<ApiInfoResponse<QuestionListData>>()
    val questionPageDetailLiveData = com.ssm.comm.ext.StateMutableLiveData<ApiInfoResponse<QuestionListData>>()
    val questionAddLiveData = com.ssm.comm.ext.StateMutableLiveData<QuestionLogData>()
    val questionClearLiveData = com.ssm.comm.ext.StateMutableLiveData<Any>()
    val questionErrorLiveData = com.ssm.comm.ext.StateMutableLiveData<ApiListResponse<QuestionListData.questionBean>>()
    val getLessonInfoLiveData = com.ssm.comm.ext.StateMutableLiveData<CourseInfoData>()
    val getConfigLiveData = com.ssm.comm.ext.StateMutableLiveData<Config>()
    val exchangeListLiveData = com.ssm.comm.ext.StateMutableLiveData<ApiListResponse<ExchangeListData>>()
    val giveListLiveData = com.ssm.comm.ext.StateMutableLiveData<ApiListResponse<ExchangeListData>>()
    val feedbackLiveData = com.ssm.comm.ext.StateMutableLiveData<MutableList<String>>()
    val editAddressLiveData = com.ssm.comm.ext.StateMutableLiveData<MutableList<String>>()
    val exchangeCodeLiveData = com.ssm.comm.ext.StateMutableLiveData<ApiListResponse<ExchangeCodeData>>()
    val rankIndexLiveData = com.ssm.comm.ext.StateMutableLiveData<RankHomeData>()
    val goodsInfoLiveData = com.ssm.comm.ext.StateMutableLiveData<ApiInfoResponse<GoodsInfoData>>()

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

    fun feedbackLiveData(typeid: Int,content:String,photos:String,version_name:String){
        viewModelScope.launch {
            feedbackLiveData.value = repository.feedbackAdd(typeid, content, photos, version_name)
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

    fun questionPageDetail(id: String,question_id: String){
        viewModelScope.launch {
            questionPageDetailLiveData.value = repository.questionPageDetail(id,question_id)
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