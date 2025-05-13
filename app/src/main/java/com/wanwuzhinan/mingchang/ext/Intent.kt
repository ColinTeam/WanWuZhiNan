package com.wanwuzhinan.mingchang.ext

import android.content.res.Configuration
import com.google.gson.Gson
import com.ssm.comm.app.appContext
import com.ssm.comm.ext.getScreenHeight2
import com.ssm.comm.ext.getScreenWidth2
import com.ssm.comm.ext.toastError
import com.ssm.comm.ui.base.IWrapView
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.entity.Lesson
import com.wanwuzhinan.mingchang.ui.pad.VideoListPadActivity
import com.wanwuzhinan.mingchang.ui.phone.AnswerAskActivity
import com.wanwuzhinan.mingchang.ui.phone.AnswerErrorAskActivity
import com.wanwuzhinan.mingchang.ui.phone.AnswerPracticeActivity
import com.wanwuzhinan.mingchang.ui.phone.AudioPlayInfoActivity
import com.wanwuzhinan.mingchang.ui.phone.ExchangeActivity
import com.wanwuzhinan.mingchang.ui.phone.ExchangeCourseActivity
import com.wanwuzhinan.mingchang.ui.phone.HonorHomeActivity
import com.wanwuzhinan.mingchang.ui.phone.HonorListActivity
import com.wanwuzhinan.mingchang.ui.phone.QuestionListAskActivity
import com.wanwuzhinan.mingchang.ui.phone.QuestionListPracticeActivity
import com.wanwuzhinan.mingchang.ui.phone.QuestionVideoActivity
import com.wanwuzhinan.mingchang.ui.phone.RankActivity
import com.wanwuzhinan.mingchang.ui.phone.VideoAnswerActivity
import com.wanwuzhinan.mingchang.ui.phone.VideoListActivity
import com.wanwuzhinan.mingchang.ui.phone.VideoPlayActivity
import com.wanwuzhinan.mingchang.utils.getAudioData

//判断当前设备是手机还是平板
fun isPhone(): Boolean {
    val config: Configuration = appContext.resources!!.configuration
    return !(config.smallestScreenWidthDp >= 600)
}
//视频列表
fun IWrapView.launchVideoListActivity(type: Int, id: Int, selectId: Int) {
    val dd = getScreenWidth2() / (getScreenHeight2() * 1.0f)
    if (dd >= (16 / 9.0)) {
        launchActivity(
            VideoListActivity::class.java,
            Pair(ConfigApp.INTENT_TYPE, type),
            Pair(ConfigApp.INTENT_ID, id),
            Pair(ConfigApp.INTENT_NUMBER, selectId)
        )
    } else {
        launchActivity(
            VideoListPadActivity::class.java,
            Pair(ConfigApp.INTENT_TYPE, type),
            Pair(ConfigApp.INTENT_ID, id),
            Pair(ConfigApp.INTENT_NUMBER, selectId)
        )
    }
}



//音频播放
fun IWrapView.launchAudioPlayInfoActivity(data: String, title: String) {
    launchActivity(
        AudioPlayInfoActivity::class.java,
        Pair(ConfigApp.INTENT_DATA, data),
        Pair(ConfigApp.INTENT_ID, title)
    )
}

//视频播放
fun IWrapView.launchVideoPlayActivity(list: ArrayList<Lesson>, id: String) {
    if (getAudioData("TXLiveBaseLicence") == 1) {
        launchActivity(
            VideoPlayActivity::class.java,
            Pair(ConfigApp.INTENT_DATA, Gson().toJson(list)),
            Pair(ConfigApp.INTENT_ID, id)
        )
    } else {
        toastError("网络未连接，请检查网络后重试")
    }

}

//视频播放
fun IWrapView.launchVideoAnswerActivity(id: String) {
    launchActivity(
        VideoAnswerActivity::class.java,
        Pair(ConfigApp.INTENT_ID, id)
    )
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
fun IWrapView.launchAnswerPracticeActivity(id: Int) {
    launchActivity(AnswerPracticeActivity::class.java, Pair(ConfigApp.INTENT_ID, id))
}

//答题 龙门题库
fun IWrapView.launchAnswerAskActivity(id: Int) {
    launchActivity(AnswerAskActivity::class.java, Pair(ConfigApp.INTENT_ID, id))
}

fun IWrapView.launchAnswerErrorAskActivity() {
    launchActivity(AnswerErrorAskActivity::class.java)
}


//答题 观看视频
fun IWrapView.launchQuestionVideoActivity(name: String, url: String) {
    launchActivity(
        QuestionVideoActivity::class.java,
        Pair(ConfigApp.INTENT_ID, name),
        Pair(ConfigApp.INTENT_DATA, url)
    )
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


//课程兑换
fun IWrapView.launchExchangeActivity() {
    launchActivity(ExchangeActivity::class.java)
}

//课程兑换记录
fun IWrapView.launchExchangeCourseActivity() {
    launchActivity(ExchangeCourseActivity::class.java)
}