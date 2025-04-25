package com.ad.img_load

import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.nio.CharBuffer


fun CharBuffer.charAt(index: Int) = this[position() + index]

/**
 * 设置防止重复点击事件
 * @param views 需要设置点击事件的view
 * @param interval 时间间隔 默认0.5秒
 * @param onClick 点击触发的方法
 */
fun setOnClickNoRepeat(vararg views: View?, interval: Long = 500, onClick: (View) -> Unit) {
    views.forEach {
        it?.clickNoRepeat(interval = interval) { view ->
            onClick.invoke(view)
        }
    }
}

/**
 * 防止重复点击事件 默认0.5秒内不可重复点击
 * @param interval 时间间隔 默认0.5秒
 * @param action 执行方法
 */
var lastClickTime = 0L
private var vibrator: Vibrator? = null


fun View.clickNoRepeat(interval: Long = 100, action: (view: View) -> Unit) {
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (lastClickTime != 0L && (currentTime - lastClickTime < interval)) {
            return@setOnClickListener
        }
        if (vibrator == null) {
            vibrator = context.getSystemService(VIBRATOR_SERVICE) as Vibrator
        }
        if (vibrator!!.hasVibrator()) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // 使用更复杂的震动效果
                val effect = VibrationEffect.createOneShot(100, 30)
                vibrator!!.vibrate(effect)
            } else {
                vibrator!!.vibrate(100) // 震动 500 毫秒
            }
        }else{
            Log.e("TAG", "clickNoRepeat: 111111nooooooo" )
        }


        lastClickTime = currentTime
        action.invoke(it)
    }
}

fun View.startVibrate(){
    if (vibrator == null) {
        vibrator = context.getSystemService(VIBRATOR_SERVICE) as Vibrator
    }
    if (vibrator!!.hasVibrator()) {

        // 使用更复杂的震动效果
        val effect = VibrationEffect.createOneShot(100, 30)
        vibrator!!.vibrate(effect)
    }
}


fun AppCompatActivity.countDown(
    time: Int = 5, start: (scop: CoroutineScope) -> Unit,
    end: () -> Unit,
    next: (time: Int) -> Unit
) {
    this.lifecycleScope.launch {
        // 在这个范围内启动的协程会在Lifecycle被销毁的时候自动取消
        flow {
            (time downTo 0).forEach {
                delay(1000)
                emit(it)
            }
        }
            .onStart {
                // 倒计时开始 ，在这里可以让Button 禁止点击状态
                start(this@launch)
            }
            .onCompletion {
                // 倒计时结束 ，在这里可以让Button 恢复点击状态
                end()
            }
            .catch {
                //错误
            }.collect {
                // 在这里 更新值来显示到UI
                next(it)
            }
    }
}



