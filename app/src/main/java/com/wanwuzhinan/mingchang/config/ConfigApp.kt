package com.wanwuzhinan.mingchang.config

import com.colin.library.android.utils.Log
import com.ssm.comm.config.Constant
import com.wanwuzhinan.mingchang.utils.MMKVUtils

object ConfigApp {

    //保留两位小数
    const val KEEP_TWO_DIGITS = 2

    const val VIDEO_DEMO_1 =
        "https://s.wanwuzhinan.top/m3u8/1080p/1.1/100001/6fc6453a46e22186d989a50306c588a8.m3u8"//用户协议
    const val VIDEO_DEMO_2 =
        "https://s.wanwuzhinan.top/m3u8/1080p/1.2/100001/6fc6453a46e22186d989a50306c588a8.m3u8"//用户协议
    const val USER_AGREEMENT = "https://app.wanwuzhinan.top/common/Content/info?cat_id=1"//用户协议
    const val PRIVACY_POLICY = "https://app.wanwuzhinan.top/common/Content/info?cat_id=2"//隐私政策
    const val PRIVACY_CHILD = "https://app.wanwuzhinan.top/common/Content/info?cat_id=7"//隐私政策
    const val VIDEO_INTRODUCTION = "https://app.wanwuzhinan.top/common/Content/info?cat_id=5"//视频介绍
    const val SCREEN_CASTING = "https://app.wanwuzhinan.top/common/Content/info?cat_id=6"//投屏

    //验证码类型
    const val CODE_TYPE_REGISTER = "register"//注册
    const val CODE_TYPE_LOGIN = "login"//登录
    const val CODE_TYPE_FORGET_PASSWORD = "forget_password"//忘记密码
    const val CODE_TYPE_EDIT_PASSWORDS = "edit_passwords"//修改密码
    const val CODE_TYPE_SET_PAY_PASS = "set_pay_info"//设置支付密码

    //跳转传值属性 type
    const val INTENT_TYPE = "type"
    const val INTENT_DATA = "data"
    const val INTENT_ID = "id"
    const val INTENT_NUMBER = "number"

    //支付方式
    const val PAY_TYPE_ALIPAY = 1//支付支付
    const val PAY_TYPE_WECHAT = 2//微信支付
    const val PAY_TYPE_BANK = 3//银行卡

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

    const val MMKV_SPLASH_TIME = "MMKV_SPLASH_TIME"
    const val MMKV_LOGIN_MOBILE = "MMKV_LOGIN_MOBILE"

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
            Log.e("save token Synchronized")
        }
}