package com.wanwuzhinan.mingchang.adapter

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.MineListData


class MineListAdapter : BaseQuickAdapter<MineListData,QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: MineListData?) {
        holder.setImageResource(R.id.image,item!!.image)
        holder.setText(R.id.text,context.getString(item!!.title))
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_mine, parent)
    }

}