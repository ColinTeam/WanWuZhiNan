package com.wanwuzhinan.mingchang.config

import com.ssm.comm.config.Constant
import com.wanwuzhinan.mingchang.utils.MMKVUtils

object ConfigApp {
    const val IS_SPLASH_VODEI = false
    const val BUGLY_APP_ID = "7531451148"
    const val LIVE_LICENSE_KEY = "d025b928c9f91abb9a3a354cad87af4b"
    const val WE_CHAT_APP_STATE = "wechat_sdk_demo_test_neng"
    const val WE_CHAT_APP_SCOPE = "snsapi_userinfo"
    const val WE_CHAT_APP_SECRET = "72e067e19f371d8dc3ac4bc9d9e69687"
    const val WE_CHAT_APP_ID = "wxd55ebb21dd8fe7a2"
    const val VIDEO_AES_KEY = "W1a2n3W4u5Z6h7i8N9a0n"
    const val MMKY_KEY_TXLIVE = "TXLiveBaseLicence"
    const val MMKY_KEY_PRIVACY = "privacy_state"

    const val EXTRAS_URL = "url"
    const val EXTRAS_POSITION = "position"
    const val EXTRAS_TITLE = "title"
    const val EXTRAS_HTML = "html"

    const val VIDEO_DEMO_1 =
        "https://s.wanwuzhinan.top/m3u8/1080p/1.1/100001/6fc6453a46e22186d989a50306c588a8.m3u8"//用户协议
    const val VIDEO_DEMO_2 =
        "https://s.wanwuzhinan.top/m3u8/1080p/1.2/100001/6fc6453a46e22186d989a50306c588a8.m3u8"//用户协议
    const val USER_AGREEMENT = "https://app.wanwuzhinan.top/common/Content/info?cat_id=1"//用户协议
    const val PRIVACY_POLICY = "https://app.wanwuzhinan.top/common/Content/info?cat_id=2"//隐私政策
    const val PRIVACY_CHILD = "https://app.wanwuzhinan.top/common/Content/info?cat_id=7"//隐私政策
    const val VIDEO_INTRODUCTION = "https://app.wanwuzhinan.top/common/Content/info?cat_id=5"//视频介绍
    const val SCREEN_CASTING = "https://app.wanwuzhinan.top/common/Content/info?cat_id=6"//投屏

    //跳转传值属性 type
    const val INTENT_TYPE = "type"
    const val INTENT_DATA = "data"
    const val INTENT_ID = "id"
    const val INTENT_NUMBER = "number"

    //播放类型
    const val TYPE_VIDEO = 1//视频
    const val TYPE_AUDIO = 2//音频

    //答题类型
    const val TYPE_ASK = 1//问
    const val TYPE_PRACTICE = 2//练

    //兑换记录类型
    const val COURSE_VIDEO = 1//视频
    const val COURSE_AUDIO = 2//音频
    const val COURSE_GIVE = 3//赠品

    var question_count_error = 0
    var question_compass = 0

    //为了解决mmkv 异步存储的方案，本地缓存
    var token: String = ""
        @Synchronized get() {
            if (field.isEmpty()) {
                field = MMKVUtils.decodeString(Constant.TOKEN)
            }
            return field
        }
        @Synchronized set(value: String) {
            field = value
        }
}