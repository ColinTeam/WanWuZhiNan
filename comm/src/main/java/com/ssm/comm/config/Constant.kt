package com.ssm.comm.config

import android.os.Environment
import com.ssm.comm.R
import com.ssm.comm.app.appContext
import java.io.File

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.config
 * ClassName: Constant
 * Author:ShiMing Shi
 * CreateDate: 2022/9/12 21:09
 * Email:shiming024@163.com
 * Description:
 */
object Constant {

    const val EXTRAS_ACTION_ID = "EXTRAS_ACTION_ID"
    const val USER_ID = "user_id"
    const val TOKEN = "token"
    const val VERSION = "version"
    const val IS_SET_PW = "is_set_pw"
    const val USER_MOBILE = "user_mobile"
    const val IS_REAL_NAME = "is_real_name"

    const val AUDIO_PLAY_ID = "audio_play_id"
    const val AUDIO_PLAY_PAGE = "audio_play_page"

    const val USER_NICK_NAME = "user_nick_name"
    const val USER_IMG = "user_img"
    const val USER_IS_VIP = "user_is_vip"
    const val CONFIG_DATA = "config_data"
    const val USER_INFO = "user_info"
    const val USER_COUNT = "user_count"
    const val CLEAR_ACCOUNT = "CLEAR_ACCOUNT"
    //是否已显示隐私政策对话框
    const val IS_SHOW_PRIVACY_DIALOG = "is_show_privacy_dialog"

    private var APP_NAME: String = appContext.resources!!.getString(R.string.app_name)

    //app下载路径
    var PATH = String.format("%s%s.apk", File.separator, APP_NAME)

    //指定apk缓存路径和文件名 默认是在SD卡中的Download文件夹
    var APK_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + PATH

    private fun getSDPath(): String{
        val sdDir: File?
        val sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
        sdDir = if(sdCardExist){
            //Android10之后
            //获取应用所在根目录/Android/data/your.app.name/file/ 也可以根据沙盒机制传入自己想传的参数，存放在指定目录
            appContext.getExternalFilesDir(null)
        }else{
            // 获取SD卡根目录
            Environment.getExternalStorageDirectory()
        }
        return sdDir.toString()
    }

    fun getAPKPath(): String{
        val parent = getSDPath()
        return String.format("%s%s",parent, PATH)
    }


    const val H5_URL = "h5_url"
    const val URL_TYPE = "url_type"
    const val WEB_TITLE = "web_title"
    const val WEB_CONTENT = "web_content"
    const val USER_AGREEMENT_TYPE = 1
    const val PRIVACY_POLICY_TYPE = 2
    const val OTHER_TYPE = 3
    const val HELP_CENTER = 4

    const val APP_UPDATE_TYPE = 2

    const val UPDATE_NICK_NAME_REQUEST_CODE = 100
    const val UPDATE_NICK_NAME_RESULT_CODE = 101
    const val QUICK_PAY_REQUEST_CODE = 102
    const val QUICK_PAY_NAME_RESULT_CODE = 103

    const val BANK_PAY_REQUEST_CODE = 104
    const val BANK_PAY_NAME_RESULT_CODE = 105

    const val SMS_TIME = 60L
    const val PAGE_SIZE = 20
    const val INIT_PAGE = 1

    const val HOME_RULE = 119
    const val NOTICE_ID = "notice_id"
    const val GOODS_ID = "goods_id"

    const val PRODUCT_DETAIL_TYPE = "product_detail_type"
    const val PRODUCT_DETAIL_BUY_GOODS = 1
    const val PRODUCT_DETAIL_RESERVE_GOODS = 2

    const val MT_TEAM_RULE_TYPE = 117
    const val MY_BALANCE_RULE_TYPE = 118

    const val SPLASH_AD_TYPE = 1
    const val VIDEO_AD_TYPE = 2
    const val INTERSTITIAL_AD_TYPE = 3

    const val TEST_IMG = "https://img2.baidu.com/it/u=1003272215,1878948666&fm=253&app=120&size=w931&n=0&f=JPEG&fmt=auto?sec=1670432400&t=e9cc11285ce93cd3d0ee4c36f89d0f37"

    const val GOODS_TYPE_BOX = 1
    const val GOODS_TYPE_PRODUCT = 2
}