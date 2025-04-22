package com.wanwuzhinan.mingchang.data

/**
 * author:
 * date: 2024/7/15
 * constraints:
 */
data class GetCodeData(val  code : Int , val  msg : String,val  data : CodeData?)


data class CodeData (var code :String){

}