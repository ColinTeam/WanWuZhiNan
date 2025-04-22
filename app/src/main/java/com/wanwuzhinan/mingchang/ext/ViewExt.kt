package com.wanwuzhinan.mingchang.ext

import android.content.res.ColorStateList
import android.view.View
import android.widget.TextView

fun getTintColor(color:Int): ColorStateList {
    val colorStateList = ColorStateList.valueOf(color)
    return colorStateList
}

fun View.visible(visible: Boolean = true) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible(invisible: Boolean = true) {
    this.visibility = if (invisible) View.INVISIBLE else View.VISIBLE
}

fun TextView.setMessage(count:Int){
    setText(count.toString())
    visible(count>0)
}

