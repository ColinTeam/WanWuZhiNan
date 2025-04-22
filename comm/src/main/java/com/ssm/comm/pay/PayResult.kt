package com.ssm.comm.pay

import com.ssm.comm.ext.isEqualStr

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.pay
 * ClassName: PayResult
 * Author:ShiMing Shi
 * CreateDate: 2022/9/26 11:40
 * Email:shiming024@163.com
 * Description:
 */
class PayResult(rawResult: Map<String, String>?) {

    var resultStatus: String? = null
        set(value) {
            field = value
        }
        get() {
            return field
        }

    var result: String? = null
        set(value) {
            field = value
        }
        get() {
            return field
        }

    var memo: String? = null
        set(value) {
            field = value
        }
        get() {
            return field
        }

    init {
        rawResult?.forEach { (key, value) ->
            if (isEqualStr(key, "resultStatus")) {
                resultStatus = value
            }
            if (isEqualStr(key, "result")) {
                result = value
            }
            if (isEqualStr(key, "memo")) {
                memo = value
            }
        }
    }

//    fun getResult(): String{
//        return result
//    }
}