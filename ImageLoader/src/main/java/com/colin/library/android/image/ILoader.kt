package com.colin.library.android.image

import android.content.Context
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.colin.library.android.image.config.ImageOptions

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-01 03:06
 *
 * Des   :ILoader
 */
interface ILoader {

    fun load(view: ImageView?, @DrawableRes res: Int)
    fun load(
        view: ImageView?, @DrawableRes res: Int, @DrawableRes replacer: Int, @DrawableRes error: Int
    )

    fun load(view: ImageView?, url: String?)
    fun load(view: ImageView?, url: String?, @DrawableRes replacer: Int, @DrawableRes error: Int)

    fun newBuilder(context: Context, baseUrl: String) =
        ImageOptions.Builder(context = context, baseUrl = baseUrl)

}