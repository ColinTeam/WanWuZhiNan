package com.ssm.comm.ext

import android.view.Gravity
import androidx.annotation.StringRes
import com.hjq.toast.ToastUtils

fun toastSuccess(msg: String){
    msg.toShow()
}

fun toastSuccess(@StringRes msg: Int){
    msg.toShow()
}


fun toastNormal(msg: String){
    msg.toShow()
}

fun toastNormal(@StringRes msg: Int){
    msg.toShow()
}


fun toastError(msg: String){
    msg.toShow()
}

fun toastError(@StringRes msg: Int){
    msg.toShow()
}

private fun String.toShow(){
    ToastUtils.setGravity(Gravity.CENTER,0,0)
    ToastUtils.show(this)
}

private fun Int.toShow(){
    ToastUtils.setGravity(Gravity.CENTER,0,0)
    ToastUtils.show(this)
}


