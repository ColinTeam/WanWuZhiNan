package com.ssm.comm.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import com.ssm.comm.R
import com.ssm.comm.app.appContext
import com.ssm.comm.ext.toastError
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.uitil
 * ClassName: DeviceUtils
 * Author:ShiMing Shi
 * CreateDate: 2022/9/19 14:55
 * Email:shiming024@163.com
 * Description:
 */
object DeviceUtils {

    @Suppress("DEPRECATION")
    @SuppressLint("HardwareIds", "CheckResult")
    fun getDeviceId(): String{
        var deviceId = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure.getString(appContext.contentResolver,Settings.Secure.ANDROID_ID)
        }else{
            val tm: TelephonyManager = appContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            RxPermissions.getInstance(appContext)
                .request(
                    Manifest.permission.READ_PHONE_STATE
                )
                .subscribe { granted ->
                    if (granted) {
                        if (tm.deviceId != null){
                            deviceId = tm.imei
                        }
                    } else {
                        toastError(R.string.permission_error)
                    }
                }
        }
        return deviceId
    }

    fun getDeviceName(): String{
        return Build.DEVICE
    }
}