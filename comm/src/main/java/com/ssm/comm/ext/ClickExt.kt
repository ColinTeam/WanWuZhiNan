package com.ssm.comm.ext

import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView

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
fun View.clickNoRepeat(interval: Long = 500, action: (view: View) -> Unit) {
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
                val effect = VibrationEffect.createOneShot(100, 40)
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