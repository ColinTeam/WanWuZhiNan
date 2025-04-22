package com.comm.net_work

import java.text.DecimalFormat

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.net_work
 * ClassName: ByteFormatUtils
 * Author:ShiMing Shi
 * CreateDate: 2022/9/20 18:47
 * Email:shiming024@163.com
 * Description:
 */
object ByteFormatUtils {

    private const val GB = 1024 * 1024 * 1024

    private const val MB = 1024 * 1024

    private const val KB = 1024

    fun bytes2kb(bytes: Long): String{
        //格式化小数
        val format = DecimalFormat("###.0")
        return if(bytes / GB >= 1){
            String.format("%sGB",format.format(bytes / GB))
        }else if(bytes / MB >= 1){
            String.format("%sMB",format.format(bytes / MB))
        }else if(bytes / KB >= 1){
            String.format("%sKB",format.format(bytes / KB))
        }else{
            String.format("%sB",bytes)
        }
    }
}