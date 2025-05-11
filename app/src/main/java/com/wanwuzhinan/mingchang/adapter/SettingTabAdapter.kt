package com.wanwuzhinan.mingchang.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.colin.library.android.utils.Constants
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.ext.onClick
import com.colin.library.android.widget.base.BaseAdapter
import com.colin.library.android.widget.base.BaseViewHolder
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.TabBean

class SettingTabAdapter(layoutRes: Int = R.layout.item_setting) : BaseAdapter<TabBean>(layoutRes) {

    var selected: Int = 0
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            if (field == value) return
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(
        holder: BaseViewHolder, position: Int, payloads: MutableList<Any>
    ) {
        items[position].let {
            val isSelected = selected == position
            holder.getView<View>(R.id.lin).apply {
                this.isSelected = isSelected
            }
            holder.getView<ImageView>(R.id.image).apply {
                if (it.id != Constants.ZERO) this.setImageResource(it.id)
                this.isSelected = isSelected
            }
            holder.getView<TextView>(R.id.tv_title).apply {
                this.text = it.title
                this.isSelected = isSelected
            }
            onClick(holder.getView<View>(R.id.lin)) {
                Log.e("onItemClickListener$it")
                it.tag = position
                onItemClickListener?.invoke(it, items[position])
            }
        }
    }

    override fun bindListViewHolder(
        holder: BaseViewHolder, item: TabBean, payloads: MutableList<Any>
    ) {


    }


}