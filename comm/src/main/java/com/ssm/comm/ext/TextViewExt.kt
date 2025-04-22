package com.ssm.comm.ext

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.ext
 * ClassName: TextViewExt
 * Author:ShiMing Shi
 * CreateDate: 2022/9/30 11:52
 * Email:shiming024@163.com
 * Description:
 */

fun TextView.setColorSpan(content: String?,start: Int = 0,end: Int = 0,color: String = "#6CF8FA",span: ClickableSpan?){
    if(isEmpty(color) || isEmpty(content)){
        return
    }
    if(!checkIndex(content!!,start) || !checkIndex(content,end)){
        return
    }
    val colorSpan = ForegroundColorSpan(Color.parseColor(color))
    val ss = SpannableString(content)
    ss.setSpan(colorSpan,start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    if (span != null){
        ss.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    this.text = ss
    this.movementMethod = LinkMovementMethod.getInstance()
}

fun TextView.setColorSpan(content: String?,start: Int = 0,end: Int = 0,color: String = "#6CF8FA"){
    setColorSpan(content, start, end, color,null)
}

private fun checkIndex(content: String,index: Int): Boolean{
    return index <= content.length && index >= 0
}

fun TextView.countDownByFlow( max: Int,
                              scope: CoroutineScope,
                              onTick: (Int) -> Unit,
                              onFinish: (() -> Unit)? = null): Job{
    return  flow {
        for (num in max downTo 0) {
            emit(num)
            if (num != 0) delay(1000)
        }
    }.flowOn(Dispatchers.Main)
        .onEach { onTick.invoke(it) }
        .onCompletion { cause -> if (cause == null) onFinish?.invoke() }
        .launchIn(scope) //保证在一个协程中执行
}