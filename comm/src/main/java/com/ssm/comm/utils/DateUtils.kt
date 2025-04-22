package com.ssm.comm.utils

import com.ssm.comm.ext.toastNormal
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.uitil
 * ClassName: DateUtils
 * Author:ShiMing Shi
 * CreateDate: 2022/9/20 17:09
 * Email:shiming024@163.com
 * Description:日期工具类
 */
object DateUtils {

    //字符串转时间戳
    fun dateToTimeStr(dateStr: String, pattern: String = "yyyy-MM-dd HH:mm:ss"): Long{
        try {
            return SimpleDateFormat(pattern,Locale.CHINA).parse(dateStr)?.time ?: 0L
        }catch (e: ParseException){
            toastNormal("日期转换异常")
            e.printStackTrace()
        }
        return 0L
    }

    //Date转换时期字符窜
    fun dateToTimeStr(date: Date, pattern: String = "yyyy-MM-dd"): String{
        return SimpleDateFormat(pattern,Locale.CHINA).format(date)
    }


    //时间戳转字符串
    fun timeStrToDate(timestamp: Long, format: String = "yyyy-MM-dd HH:mm:ss"): String{
        return SimpleDateFormat(format,Locale.CHINA).format(Date(timestamp))
    }

    fun timeParse(s: Long): String{
        var time = ""
        //小于1分钟
        time = if(s < 60L){
            String.format("00:%02d",s % 60)
        }
        //小于1个小时
        else if(s < 3600L){
            String.format("%02d:%02d",s / 60,s % 60)
        }
        //小于一天
        else if(s < 3600 * 24){
            String.format("%02d:%02d:%02d",s / 3600,s % 3600 / 60,s % 60)
        }
        //大于一天
        else {
            String.format("%02d天%02d:%02d:%02d",s / 86400,s % 86400 / 3600,s % 86400 % 3600 / 60,s % 86400 % 3600 % 60)
        }
        return time
    }

    //获取以后第N天的时间戳
    fun getAfterTimeStamp(day: Int = 0,format: String = "yyyy-MM-dd HH:mm:ss"): Long{
        val dateTime = getAfterTime(day,format)
        return dateToTimeStr(dateTime,format)
    }

    //获取以后第N天的日期
    fun getAfterTime(day: Int = 0,format: String = "yyyy-MM-dd HH:mm:ss"): String{
        val c = Calendar.getInstance()
        c.time = Date()
        //第1天，第x天，照加。如果是负数，表示前n天
        c.add(Calendar.DAY_OF_MONTH,day)
        return SimpleDateFormat(format).format(c.time)
    }

    //根据当前日期获得是星期几
    fun getWeekDay(timestamp: Long): String{
        val mDate = Date(timestamp)
        val c = Calendar.getInstance()
        c.time = mDate
        when(c.get(Calendar.DAY_OF_WEEK)){
            1 ->{
                return "周日"
            }
            2 ->{
                return "周一"
            }
            3 ->{
                return "周二"
            }
            4 ->{
                return "周三"
            }
            5 ->{
                return "周四"
            }
            6 ->{
                return "周五"
            }
            7 ->{
                return "周六"
            }
        }
        return "周日"
    }

    //获取当前月份
    fun getMonth(): Int{
        val c = Calendar.getInstance()
        //显示当前月份要+1 因为是从0开始计算的 使用时需要-1
        return c.get(Calendar.MONTH) + 1
    }

    //获取当前日期
    fun getCurrentDateTime(pattern: String = "yyyy-MM-dd"): String{
        val sdf = SimpleDateFormat(pattern)
        val c = Calendar.getInstance()
        return sdf.format(c.time)
    }

}