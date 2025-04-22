package com.ssm.comm.ext

import com.ssm.comm.config.Constant
import com.ssm.comm.utils.MMKVUtils

fun getToken(): String {
    return MMKVUtils.decodeString(Constant.TOKEN)
}

fun getUID(): String {
    return MMKVUtils.decodeString(Constant.USER_ID)
}
fun getClearAccount(): String {
    return MMKVUtils.decodeString(Constant.CLEAR_ACCOUNT)
}
fun getUIMG(): String {
    return MMKVUtils.decodeString(Constant.USER_IMG)
}

fun getUName(): String {
    return MMKVUtils.decodeString(Constant.USER_NICK_NAME)
}

fun getUMobile(): String {
    return MMKVUtils.decodeString(Constant.USER_MOBILE)
}

fun isVIP(): Boolean {
    return MMKVUtils.decodeBoolean(Constant.USER_IS_VIP)
}

fun isShowPrivacy(): Boolean {
    return MMKVUtils.decodeBoolean(Constant.IS_SHOW_PRIVACY_DIALOG)
}

fun setData(key: String, any: Any): Boolean {
    return MMKVUtils.encode(key, any)
}

fun getAudioData(key: String): Int? {
    return MMKVUtils.decodeInt(key)
}

fun removeKey(key: String = "") {
    MMKVUtils.removeKey(key)
}


fun clearAllData() {
    MMKVUtils.removeKey(Constant.TOKEN)
    MMKVUtils.removeKey(Constant.USER_ID)
    MMKVUtils.removeKey(Constant.USER_MOBILE)
    MMKVUtils.removeKey(Constant.USER_IS_VIP)
    MMKVUtils.removeKey(Constant.IS_SET_PW)
    MMKVUtils.removeKey(Constant.IS_REAL_NAME)
}
