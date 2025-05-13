package com.wanwuzhinan.mingchang.ui.adapter

import android.annotation.SuppressLint
import android.widget.ImageView
import com.colin.library.android.utils.ext.onClick
import com.colin.library.android.widget.base.BaseAdapter
import com.colin.library.android.widget.base.BaseViewHolder
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.TabBean

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-13 14:00
 *
 * Des   :ChooseAvatar
 */
class ChooseAvatarAdapter(
    val item: ArrayList<TabBean>, layoutRes: Int = R.layout.item_choose_avatar
) : BaseAdapter<TabBean>(item, layoutRes) {

    var selected: Int = 0
        @SuppressLint("NotifyDataSetChanged") set(value) {
            if (field == value) return
            field = value
            notifyDataSetChanged()
        }

    override fun bindListViewHolder(
        holder: BaseViewHolder, item: TabBean, position: Int, payloads: MutableList<Any>
    ) {
        holder.getView<ImageView>(R.id.ivAvatar).setImageResource(item.id)
        holder.itemView.onClick {
            onItemClickListener?.invoke(it, items[position], position)
        }
    }
}