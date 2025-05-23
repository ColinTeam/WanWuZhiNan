package com.wanwuzhinan.mingchang.ext

import android.content.res.Configuration
import android.util.Log
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.data.SubjectListData
import com.wanwuzhinan.mingchang.ui.phone.*
import com.google.gson.Gson
import com.ssm.comm.app.appContext
import com.ssm.comm.ext.getScreenHeight2
import com.ssm.comm.ext.getScreenWidth2
import com.ssm.comm.ui.base.IWrapView
import com.tencent.rtmp.TXLiveBase
import com.wanwuzhinan.mingchang.ui.pad.AudioHomeIpadActivity
import com.wanwuzhinan.mingchang.ui.pad.MainIpadActivity
import com.wanwuzhinan.mingchang.ui.pad.MainIpadWidthActivity
import com.wanwuzhinan.mingchang.ui.pad.VideoListPadActivity

//判断当前设备是手机还是平板
fun isPhone(): Boolean {
    val config: Configuration = appContext.resources!!.configuration
    return !(config.smallestScreenWidthDp >= 600)
}

//登录页
fun IWrapView.launchLoginActivity() {
    launchActivity(LoginActivity::class.java)
}

//首页
fun IWrapView.launchMainActivity() {
    val dd = getScreenWidth2()/ (getScreenHeight2()*1.0f)
    Log.e("TAG", "getScreenWidth2()/ getScreenHeight2(): "+dd )
    if (dd > 2.0) {
        launchActivity(MainActivity::class.java)
    }else if (dd >= (16/9.0)) {
        launchActivity(MainIpadWidthActivity::class.java)
    }else{
        launchActivity(MainIpadActivity::class.java)
    }
}

//视频主页
fun IWrapView.launchVideoHomeActivity() {
    launchActivity(VideoHomeActivity::class.java)
}

//视频列表
fun IWrapView.launchVideoListActivity(type: Int, id: String, selectId: String) {
    val dd = getScreenWidth2()/ (getScreenHeight2()*1.0f)
    if (dd >= (16/9.0)) {
        launchActivity(
            VideoListActivity::class.java,
            Pair(ConfigApp.INTENT_TYPE, type),
            Pair(ConfigApp.INTENT_ID, id),
            Pair(ConfigApp.INTENT_NUMBER, selectId)
        )
    }else{
        launchActivity(
            VideoListPadActivity::class.java,
            Pair(ConfigApp.INTENT_TYPE, type),
            Pair(ConfigApp.INTENT_ID, id),
            Pair(ConfigApp.INTENT_NUMBER, selectId)
        )
    }
}

//音频主页
fun IWrapView.launchAudioHomeActivity() {

    val dd = getScreenWidth2()/ (getScreenHeight2()*1.0f)
    if (dd >= (16/9.0)) {
        launchActivity(AudioHomeActivity::class.java)
    }else{
        launchActivity(AudioHomeIpadActivity::class.java)
    }
}


//音频播放
fun IWrapView.launchAudioPlayInfoActivity(data: String,title:String) {
    launchActivity(
        AudioPlayInfoActivity::class.java,
        Pair(ConfigApp.INTENT_DATA, data),
        Pair(ConfigApp.INTENT_ID,title)
    )
}
//视频播放
fun IWrapView.launchVideoPlayActivity(list: ArrayList<SubjectListData.lessonBean>, id: String) {
    launchActivity(
        VideoPlayActivity::class.java,
        Pair(ConfigApp.INTENT_DATA, Gson().toJson(list)),
        Pair(ConfigApp.INTENT_ID, id)
    )
}

//视频播放
fun IWrapView.launchVideoAnswerActivity(id: String) {
    launchActivity(
        VideoAnswerActivity::class.java,
        Pair(ConfigApp.INTENT_ID, id)
    )
}




//答题主页
fun IWrapView.launchQuestionHomeActivity() {
    launchActivity(QuestionHomeActivity::class.java)
}

//答题列表
fun IWrapView.launchQuestionListActivity(type: Int) {
    if (type == ConfigApp.TYPE_ASK) {
        launchActivity(QuestionListAskActivity::class.java, Pair(ConfigApp.INTENT_TYPE, type))
    } else {
        launchActivity(QuestionListPracticeActivity::class.java, Pair(ConfigApp.INTENT_TYPE, type))
    }
}

//答题 物理十万问

fun IWrapView.launchAnswerPracticeActivity(id: String) {
    launchActivity(AnswerPracticeActivity::class.java, Pair(ConfigApp.INTENT_ID, id))
}

//答题 龙门题库
fun IWrapView.launchAnswerAskActivity(id: String) {
    launchActivity(AnswerAskActivity::class.java, Pair(ConfigApp.INTENT_ID, id))
}

fun IWrapView.launchAnswerErrorAskActivity() {
    launchActivity(AnswerErrorAskActivity::class.java)
}




//答题 观看视频
fun IWrapView.launchQuestionVideoActivity(name: String,url:String) {
    launchActivity(QuestionVideoActivity::class.java, Pair(ConfigApp.INTENT_ID, name), Pair(ConfigApp.INTENT_DATA, url))
}

//荣誉墙主页
fun IWrapView.launchHonorHomeActivity() {
    launchActivity(HonorHomeActivity::class.java)
}

fun IWrapView.launchRankHomeActivity() {
    launchActivity(RankActivity::class.java)
}


//荣誉墙列表
fun IWrapView.launchHonorListActivity() {
    launchActivity(HonorListActivity::class.java)
}

//设置
fun IWrapView.launchSettingActivity(index:Int) {
    launchActivity(SettingActivity::class.java, Pair(ConfigApp.INTENT_TYPE, index))
}

//课程兑换
fun IWrapView.launchExchangeActivity() {
    launchActivity(ExchangeActivity::class.java)
}

//课程兑换记录
fun IWrapView.launchExchangeCourseActivity() {
    launchActivity(ExchangeCourseActivity::class.java)
}