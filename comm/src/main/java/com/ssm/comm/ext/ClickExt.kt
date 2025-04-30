package com.ssm.comm.ext

import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat

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
fun View.clickNoRepeat(interval: Long = 500, action: (view: View) -> Unit) {
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (lastClickTime != 0L && (currentTime - lastClickTime < interval)) {
            return@setOnClickListener
        }
        val effect = VibrationEffect.createOneShot(100, 30)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.getSystemService<VibratorManager>(context, VibratorManager::class.java)
                ?.vibrate(CombinedVibration.createParallel(effect))
        } else {
            ContextCompat.getSystemService<Vibrator>(context, Vibrator::class.java)?.vibrate(effect)
        }

        lastClickTime = currentTime
        action.invoke(it)
    }
}

fun editChange(vararg edit: TextView?): Boolean {
    edit.forEach {
        val text = it?.text.toString()
        if(TextUtils.isEmpty(text)){
            return false
        }
    }

    return true
}

//输入框有变动的时候 修改按钮颜色
fun initEditChange(vararg editable: EditText, onTextChange: () -> Unit){
    editable.forEach {
        it.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onTextChange()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }
}


/**
 * 设置点击事件
 * @param views 需要设置点击事件的view
 * @param onClick 点击触发的方法
 */
fun setOnclick(vararg views: View?, onClick: (View) -> Unit) {
    views.forEach {
        it?.setOnClickListener { view ->
            onClick.invoke(view)
        }
    }
}