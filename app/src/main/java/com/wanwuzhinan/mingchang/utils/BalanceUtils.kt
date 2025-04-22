package com.wanwuzhinan.mingchang.utils

import com.wanwuzhinan.mingchang.config.ConfigApp
import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.regex.Pattern

object BalanceUtils {

    var EARTH_RADIUS=6378.137

    //保留两位小数
    fun keepTwoDigits(balance:String) :String{
        if(isDoubleOrFloat(balance)){
            //保留两位小数
            var price = BigDecimal(balance).setScale(ConfigApp.KEEP_TWO_DIGITS, BigDecimal.ROUND_HALF_DOWN).toDouble()

            //不足两位则补0
            var decimalFormat = DecimalFormat("0.00#")

            return decimalFormat.format(price)
        }else{
            return  balance
        }
    }

    //对两个数字进行比较 看哪个大
    fun priceComparison(price1:String,price2:String ) :Boolean{
        if(isDoubleOrFloat(price1) && isDoubleOrFloat(price2)){
            return keepTwoDigits(price1).toDouble()>keepTwoDigits(price2).toDouble()
        }else{
            return false
        }
    }

    //两个double类型算法 保留两位小数
    fun priceAlgorithm(price:String,free:String,algorithm :Int):String{
        if(isDoubleOrFloat(price) && isDoubleOrFloat(free)){
            var end=0.00
            when(algorithm){
                1->{//相加
                    end=price.toDouble()+free.toDouble()
                }
                2->{//相减
                    end=price.toDouble()-free.toDouble()
                }
                3->{//相乘
                    end= price.toDouble()*free.toDouble()
                }
                4->{//相除
                    end= price.toDouble()/free.toDouble()
                }
            }
            return keepTwoDigits(end.toString())
        }else{
            return price
        }
    }

    //判断这个字符串是否是float或者double类型
    fun isDoubleOrFloat(float: String):Boolean {
        var pattern = Pattern.compile("^[-\\+]?[.\\d]*$")
        return pattern.matcher(float).matches()
    }

    fun getDistance(longitude1:Double,latitude1:Double,longitude2:Double,latitude2:Double):String{
        var Lat1=rad(latitude1)
        var Lat2=rad(latitude2)
        var a=Lat1-Lat2
        var b=rad(longitude1) - rad(longitude2)
        var s= 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2.0)
                + Math.cos(Lat1) * Math.cos(Lat2)
                * Math.pow(Math.sin(b / 2), 2.0)))

        s = s * EARTH_RADIUS
        //有小数的情况;注意这里的10000d中的“d”
        s = Math.round(s * 10000.0) / 10000.0

        if(s>1){
            s = Math.round(s / 100.0) / 10.0 //单位：千米 保留一位小数
            return "${s}km"
        }else{
            s = s * 1000;//单位：米
            return "${s}m"
        }
    }

    fun rad(d:Double):Double{
        return d * Math.PI / 180.0
    }
}