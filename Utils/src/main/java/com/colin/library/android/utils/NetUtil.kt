package com.colin.library.android.utils

import android.Manifest.permission
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.telephony.TelephonyManager
import androidx.annotation.IntDef
import androidx.annotation.RequiresPermission
import com.colin.library.android.utils.helper.UtilHelper

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Date  :2024-10-10
 * Des   :网络工具类
 */
@IntDef(
    NetType.NETWORK_NONE,
    NetType.NETWORK_MOBILE,
    NetType.NETWORK_WIFI,
    NetType.NETWORK_2G,
    NetType.NETWORK_3G,
    NetType.NETWORK_4G,
    NetType.NETWORK_5G,
    NetType.NETWORK_ETHERNET
)
@Retention(
    AnnotationRetention.SOURCE
)
annotation class NetType {
    companion object {
        const val NETWORK_NONE: Int = -1    // 没有网络连接
        const val NETWORK_MOBILE: Int = 0   // 手机流量
        const val NETWORK_WIFI: Int = 1     // wifi连接
        const val NETWORK_2G: Int = 2       // 2G
        const val NETWORK_3G: Int = 3       // 3G
        const val NETWORK_4G: Int = 4       // 4G
        const val NETWORK_5G: Int = 5       // 5G
        const val NETWORK_ETHERNET: Int = 6 // 以太网
    }
}

object NetUtil {
    private val connectivityManager: ConnectivityManager
        get() = UtilHelper.getApplication().getSystemService(ConnectivityManager::class.java)

    private val telephonyManager: TelephonyManager
        get() = UtilHelper.getApplication().getSystemService(TelephonyManager::class.java)

    @get:RequiresPermission(permission.ACCESS_NETWORK_STATE)
    private val activeNetwork: Network?
        get() = connectivityManager.activeNetwork

    @get:RequiresPermission(permission.ACCESS_NETWORK_STATE)
    private val networkCapabilities: NetworkCapabilities?
        get() = connectivityManager.getNetworkCapabilities(activeNetwork)

    @get:RequiresPermission(permission.ACCESS_NETWORK_STATE)
    private val isConnected: Boolean
        get() = isConnected(networkCapabilities)


    /*判断当前网络是否是以太网*/
    fun isEthernet(capabilities: NetworkCapabilities? = networkCapabilities): Boolean {
        return capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ?: false
    }

    /*判断当前网络是否是Wifi*/
    fun isWifi(capabilities: NetworkCapabilities? = networkCapabilities): Boolean {
        return capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
    }

    /*判断当前网络是否是移动网*/
    fun isCellular(capabilities: NetworkCapabilities? = networkCapabilities): Boolean {
        return capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false
    }

    /*判断是否联网*/
    fun isConnected(capabilities: NetworkCapabilities? = networkCapabilities): Boolean {
        return isEthernet(capabilities) || isWifi(capabilities) || isCellular(capabilities)
    }

    /**
     * 打开或关闭移动数据网
     *
     * @param enabled `true`: 打开
     * `false`: 关闭
     */
    fun setDataEnabled(enabled: Boolean) {
        val manager: TelephonyManager = UtilHelper.getSystemService(TelephonyManager::class.java)
        try {
            val setMobileDataEnabledMethod = manager.javaClass.getDeclaredMethod(
                "setDataEnabled", Boolean::class.javaPrimitiveType
            )
            setMobileDataEnabledMethod.invoke(manager, enabled)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    @NetType
    @RequiresPermission(permission.ACCESS_NETWORK_STATE)
    fun getNetType(capabilities: NetworkCapabilities? = networkCapabilities): Int {
        if (isEthernet(capabilities)) return NetType.NETWORK_ETHERNET
        if (isWifi(capabilities)) return NetType.NETWORK_WIFI
        if (isCellular(capabilities)) return NetType.NETWORK_MOBILE
        return NetType.NETWORK_NONE
    }


    val networkOperatorName: String?
        /**
         * 获取网络运营商名称 英文
         * China Telecom
         *
         * @return 运营商名称
         */
        get() {
            return if (telephonyManager.simState == TelephonyManager.SIM_STATE_READY) telephonyManager.networkOperatorName
            else null
        }

    val simOperatorName: String?
        /**
         * 获取电话卡运营商名字 中文 中国移动、如中国联通、中国电信
         *
         * @return
         */
        get() {
            return if (telephonyManager.simState == TelephonyManager.SIM_STATE_READY) telephonyManager.simOperatorName
            else null
        }
}