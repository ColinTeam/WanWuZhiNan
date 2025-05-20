package com.wanwuzhinan.mingchang.ui.adapter

import android.widget.ImageView
import com.colin.library.android.widget.base.BaseAdapter
import com.colin.library.android.widget.base.BaseViewHolder
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.entity.LessonSubject
import com.wanwuzhinan.mingchang.utils.load

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-18 23:40
 *
 * Des   :VideoHomePageAdapter
 */
class VideoHomePageAdapter() : BaseAdapter<LessonSubject>(layoutRes = R.layout.item_video_home_bg) {
//    val items = listOf(
//        R.mipmap.bg_login, R.mipmap.bg_ask_home, R.mipmap.bg_practice_home, R.mipmap.bg_video_home
//    )

    override fun bindListViewHolder(
        holder: BaseViewHolder, item: LessonSubject, position: Int, payloads: MutableList<Any>
    ) {
        holder.getView<ImageView>(R.id.ivBg).apply {
            this.load(item.image, R.mipmap.bg_video_home)
        }
    }

}
