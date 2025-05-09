package com.colin.library.android.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-01 08:09
 *
 * Des   :ScopeExt
 */
/**
 * 倒计时
 */
fun LifecycleOwner.countDown(
    time: Int = 5,
    start: (scope: CoroutineScope) -> Unit,
    next: (time: Int) -> Unit,
    end: () -> Unit
) = countDown(lifecycleScope, time, start, next, end)

fun countDown(
    scope: CoroutineScope,
    time: Int = 5,
    start: (scope: CoroutineScope) -> Unit,
    next: (time: Int) -> Unit,
    finish: () -> Unit
) {
    scope.launch {
        flow {
            (time downTo 0).forEach {
                Log.e("countDown downTo:${it}")
                delay(Constants.ONE_SECOND.toLong())
                emit(it)
            }
        }.onStart {
            Log.e("countDown onStart:$this")
            // 倒计时开始 ，在这里可以让Button 禁止点击状态
            start(this@launch)
        }.onCompletion {
            // 倒计时结束 ，在这里可以让Button 恢复点击状态
            Log.e("countDown finish:${it}")
            finish()
        }.catch {
            // 处理异常，例如记录日志
            Log.e("countDown catch:${it}")
        }.collect {
            // 在这里 更新值来显示到UI
            Log.e("countDown collect:$it")
            next(it)
        }
    }
}

/**
 * 倒计时
 */
fun LifecycleOwner.countDown(
    total: Int, onNext: (Int) -> Unit, onStart: (() -> Unit) = {}, onFinish: (() -> Unit) = {}
): Job {
    return countDown(
        this.lifecycleScope, total, onNext, onStart, onFinish
    )
}

fun countDown(
    scope: CoroutineScope,
    total: Int,
    onNext: (Int) -> Unit,
    onStart: (() -> Unit) = {},
    onFinish: (() -> Unit)? = {},
): Job {
    return flow {
        for (i in total downTo 0) {
            emit(i)
            delay(1000)
        }
    }.flowOn(Dispatchers.Main).onStart { onStart.invoke() }.onCompletion { onFinish?.invoke() }
        .onEach { onNext.invoke(it) }.launchIn(scope)
}