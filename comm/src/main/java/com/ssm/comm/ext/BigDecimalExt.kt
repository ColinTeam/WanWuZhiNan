package com.ssm.comm.ext

import java.math.BigDecimal
import java.text.NumberFormat

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.ext
 * ClassName: BigDecimalExt
 * Author:ShiMing Shi
 * CreateDate: 2022/9/29 11:13
 * Email:shiming024@163.com
 * Description:
 */

/**
 * 比较大小 是否大于
 *
 * @param v1 被比较数
 * @param v2 比较数
 * @return 如果v1大于v2 则 返回true 否则false
 */
fun compare(v1: String?,v2: String?): Boolean{
    if(isEmpty(v1)){
        return false
    }
    if(isEmpty(v2)){
        return false
    }
    val b1 = BigDecimal(v1)
    val b2 = BigDecimal(v2)
    //-1,表示b1小于b2
    //0,表示b1等于b2
    //1,表示b1大于b2
    return b1 > b2
}

/**
 * 比较大小 是否大于
 *
 * @param v1 被比较数
 * @param v2 比较数
 * @return 如果v1大于v2 则 返回true 否则false
 */
fun compare2(v1: String?,v2: String?): Boolean{
    if(isEmpty(v1)){
        return false
    }
    if(isEmpty(v2)){
        return false
    }
    val b1 = BigDecimal(v1)
    val b2 = BigDecimal(v2)
    //-1,表示b1小于b2
    //0,表示b1等于b2
    //1,表示b1大于b2
    return b1 >= b2
}

/**
 * 提供精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入
 *
 * @param v1 被除数
 * @param v2 除数
 * @return 两个参数的商
 */
fun divFloat(v1: Int,v2: Int): Float{
    val b1 = BigDecimal(v1)
    val b2 = BigDecimal(v2)
    return b1.divide(b2,2, BigDecimal.ROUND_HALF_UP).toFloat()
}

/**
 * 提供精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入
 *
 * @param v1 被除数
 * @param v2 除数
 * @return 两个参数的商
 */
fun divString(v1: String?,v2: String?): String{
    if(isEmpty(v1) || isEmpty(v2)){
        return "0.0"
    }
    if(!compare(v2,"0")){
       return "0.0"
    }
    val b1 = BigDecimal(v1)
    val b2 = BigDecimal(v2)
    return b1.divide(b2,5, BigDecimal.ROUND_HALF_UP).toString()
}

/**
 * 提供精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入
 *
 * @param v1 被除数
 * @param v2 除数
 * @return 两个参数的商
 */
fun divString(v1: String?,v2: String?,scale: Int): String{
    if(isEmpty(v1) || isEmpty(v2)){
        return "0.0"
    }
    if(!compare(v2,"0")){
        return "0.0"
    }
    val b1 = BigDecimal(v1)
    val b2 = BigDecimal(v2)
    return b1.divide(b2,scale, BigDecimal.ROUND_HALF_UP).toString()
}

/**
 * 提供精确的加法运算
 *
 * @param v1    被加数
 * @param v2    加数
 * @param scale 保留scale 位小数
 * @return 两个参数的和
 */
fun addStr(v1: String?,v2: String?): String{
    return addStr(v1,v2,2)
}

/**
 * 提供精确的加法运算
 *
 * @param v1    被加数
 * @param v2    加数
 * @param scale 保留scale 位小数
 * @return 两个参数的和
 */
fun addStr(v1: String?,v2: String?,scale: Int): String{
    if(isEmpty(v1) || isEmpty(v2)){
        return "0.0"
    }
    val b1 = BigDecimal(v1)
    val b2 = BigDecimal(v2)
    return b1.add(b2).setScale(scale, BigDecimal.ROUND_HALF_UP).toString()
}

/**
 * 提供精确的乘法运算
 *
 * @param v1    被乘数
 * @param v2    乘数
 * @param scale 保留scale 位小数
 * @return 两个参数的积
 */
fun mulString(v1: String?,v2: String?): String{
    if(isEmpty(v1) || isEmpty(v2)){
        return "0.0"
    }
    val b1 = BigDecimal(v1)
    val b2 = BigDecimal(v2)
    return b1.multiply(b2).setScale(4, BigDecimal.ROUND_HALF_UP).toString()
}

/**
 * 提供精确的乘法运算
 *
 * @param v1    被乘数
 * @param v2    乘数
 * @param scale 保留scale 位小数
 * @return 两个参数的积
 */
fun mulString(v1: String?,v2: String?,scale: Int): String{
    if(isEmpty(v1) || isEmpty(v2)){
        return "0.0"
    }
    val b1 = BigDecimal(v1)
    val b2 = BigDecimal(v2)
    return b1.multiply(b2).setScale(scale, BigDecimal.ROUND_HALF_UP).toString()
}

/**
 * 提供精确的乘法运算
 *
 * @param v1    被乘数
 * @param v2    乘数
 * @return 两个参数的积
 */
fun mulInt(v1: String?,v2: String?): Int{
    if(isEmpty(v1) || isEmpty(v2)){
        return 0
    }
    val b1 = BigDecimal(v1)
    val b2 = BigDecimal(v2)
    return b1.multiply(b2).setScale(0, BigDecimal.ROUND_HALF_UP).toInt()
}

/**
 * 提供精确的剑法运算
 *
 * @param v1    被乘数
 * @param v2    乘数
 * @param scale 保留scale 位小数
 * @return 两个参数的积
 */
fun subString(v1: String?,v2: String?): String{
    if(isEmpty(v1) || isEmpty(v2)){
        return "0.0"
    }
    val b1 = BigDecimal(v1)
    val b2 = BigDecimal(v2)
    return b1.subtract(b2).setScale(4, BigDecimal.ROUND_HALF_UP).toString()
}

/**
 * 提供精确的剑法运算
 *
 * @param v1    被乘数
 * @param v2    乘数
 * @param scale 保留scale 位小数
 * @return 两个参数的积
 */
fun subString(v1: String?,v2: String?,scale: Int): String{
    if(isEmpty(v1) || isEmpty(v2)){
        return "0"
    }
    val b1 = BigDecimal(v1)
    val b2 = BigDecimal(v2)
    return b1.subtract(b2).setScale(scale, BigDecimal.ROUND_HALF_UP).toString()
}

/**
 * 小数格式化
 */
fun formatDouble(d: Double): String{
    return formatDouble(d,2)
}

/**
 * 小数格式化
 */
fun formatDouble(d: Double,ws: Int = 2): String{
    val nf = NumberFormat.getInstance()
    //设置保留多少位小数
    nf.maximumFractionDigits = ws
    //取消科学计数法
    nf.isGroupingUsed = false
    return nf.format(d)
}


fun formatNumber(number: String?,scale: Int = 0): String{
    if(isEmpty(number)){
        return "0"
    }
    var temp = ""
    if(number!!.indexOf('.') > 0){
        //正则表达 去掉后面无用的零
        //temp = number.replaceAll("0+?$","")
    }
    return "0"
}