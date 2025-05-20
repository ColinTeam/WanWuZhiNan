package com.wanwuzhinan.mingchang.ui.adapter

import com.colin.library.android.widget.base.BaseAdapter
import com.colin.library.android.widget.base.BaseViewHolder
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.entity.Lesson

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025/5/11 23:06
 *
 * Des   :VideoHomeListAdapter
 */
class VideoListAdapter(val layoutRes: Int = R.layout.item_video_list) :
    BaseAdapter<Lesson>(layoutRes = layoutRes) {


    override fun bindListViewHolder(
        holder: BaseViewHolder, item: Lesson, position: Int, payloads: MutableList<Any>
    ) {
    }
}