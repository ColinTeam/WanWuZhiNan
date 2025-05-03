package com.colin.library.android.utils.helper

import android.app.Application
import com.colin.library.android.utils.config.UtilConfig

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Date  :2024-10-10
 * Des   :工具类辅助类：app初始化记得初始化次对象
 */
object UtilHelper {
    private lateinit var config: UtilConfig

    fun init(app: Application, debug: Boolean = true) {
        init(UtilConfig.newBuilder(app, debug).build())
    }

    fun init(config: UtilConfig) {
        this.config = config
    }

    /**
     * 获取全局应用
     */
    fun getApplication() = config.getApplication()

    /**
     * 是否为debug环境
     */
    fun isDebug() = config.isDebug()

    fun <T> getSystemService(clazz: Class<T>): T = config.getApplication().getSystemService(clazz)

    fun getSp(name: String, mode: Int) = config.getApplication().getSharedPreferences(name, mode)


}