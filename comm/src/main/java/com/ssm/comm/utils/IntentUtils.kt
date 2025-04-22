@file:Suppress("UNCHECKED_CAST")

package com.ssm.comm.utils

import android.content.Intent
import android.os.Parcelable
import java.io.Serializable

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.uitil
 * ClassName: IntentUtils2
 * Author:ShiMing Shi
 * CreateDate: 2022/9/20 15:24
 * Email:shiming024@163.com
 * Description:
 */
object IntentUtils {

    /**
     * 填充intent数据
     * @param intent intent
     * @param pairs  pairs
     */
    fun fillIntent(intent: Intent, vararg pairs: Pair<String?, Any?>?) {
        for (i in pairs.indices) {
            val value = pairs[i]?.second
            val key = pairs[i]?.first
            //判断不同的类型，进行强转和存放
            when (value) {
                is Boolean -> {
                    intent.putExtra(key, value)
                }
                is Byte -> {
                    intent.putExtra(key, value)
                }
                is Short -> {
                    intent.putExtra(key, value)
                }
                is Long -> {
                    intent.putExtra(key, value)
                }
                is Float -> {
                    intent.putExtra(key, value)
                }
                is Double -> {
                    intent.putExtra(key, value)
                }
                is Int -> {
                    intent.putExtra(key, value)
                }
                is String -> {
                    intent.putExtra(key, value)
                }
                is Parcelable -> {
                    intent.putExtra(key, value)
                }
                is Serializable -> {
                    intent.putExtra(key, value)
                }
            }
        }
    }
}