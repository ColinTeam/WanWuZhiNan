package com.comm.banner.util

import android.util.Log
import com.comm.banner.BuildConfig

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.banner.util
 * ClassName: LogUtils
 * Author:ShiMing Shi
 * CreateDate: 2022/10/8 14:30
 * Email:shiming024@163.com
 * Description:
 */
object LogUtils {

    private const val TAG = "app_log"
    private val DEBUG = BuildConfig.DEBUG

    fun d(msg: String){
        loge (msg){
            Log.d(TAG,it)
        }
    }

    fun e(msg: String){
        loge (msg){
            Log.e(TAG,it)
        }
    }

    fun i(msg: String){
        loge (msg){
            Log.i(TAG,it)
        }
    }


    fun v(msg: String){
        loge (msg){
            Log.v(TAG,it)
        }
    }


    fun w(msg: String){
        loge (msg){
            Log.w(TAG,it)
        }
    }


    //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
    // 把4*1024的MAX字节打印长度改为2001字符数
    private fun loge(str: String = "",print:(msg: String) ->Unit) {
        if (DEBUG) {
            val len = 2001
            //大于4000时
            var temp = str
            while (temp.length > len) {
                print(temp.substring(0, len))
                temp = temp.substring(len)
            }
            //剩余部分
            print(temp)
        }
    }

}