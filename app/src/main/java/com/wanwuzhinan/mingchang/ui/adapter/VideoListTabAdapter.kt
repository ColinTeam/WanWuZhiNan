package com.wanwuzhinan.mingchang.ui.adapter

import android.annotation.SuppressLint
import com.colin.library.android.widget.base.BaseAdapter
import com.colin.library.android.widget.base.BaseViewHolder
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.entity.LessonSubject

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025/5/11 23:06
 *
 * Des   :VideoHomeListAdapter
 */
class VideoListTabAdapter(val layoutRes: Int = R.layout.item_video_list_tab) :
    BaseAdapter<LessonSubject>(layoutRes = layoutRes) {
    var selected: Int = 0
        @SuppressLint("NotifyDataSetChanged") set(value) {
            if (field == value) return
            field = value
            notifyDataSetChanged()
        }

    override fun bindListViewHolder(
        holder: BaseViewHolder, item: LessonSubject, position: Int, payloads: MutableList<Any>
    ) {
        holder.setText(R.id.tvName, item.name)
    }
}