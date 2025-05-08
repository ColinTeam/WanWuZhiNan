package com.colin.library.android.widget.skeleton

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-01 07:18
 *
 * Des   :ISkeletonScreen
 */
interface ISkeletonScreen {
    fun show()
    fun hide()
}



fun bind(view: View): ViewSkeletonScreen.Builder {
    return ViewSkeletonScreen.Builder(view)
}