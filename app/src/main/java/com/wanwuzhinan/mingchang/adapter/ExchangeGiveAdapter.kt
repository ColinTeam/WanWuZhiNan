package com.wanwuzhinan.mingchang.adapter

import android.content.Context
import android.view.ViewGroup
import com.ad.img_load.glide.manager.GlideImgManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.ExchangeListData
import com.wanwuzhinan.mingchang.ext.convertTimestampToDateTime


class ExchangeGiveAdapter : BaseQuickAdapter<ExchangeListData,QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: ExchangeListData?) {
        holder.setText(R.id.tv_name,item!!.title)
            .setText(R.id.tv_price,"￥${item.price}")
        GlideImgManager.get().loadImg(item.image,holder.getView(R.id.riv_image),R.drawable.img_default_icon)
//        holder.setText(R.id.tv_exchange_time,"兑换时间：${convertTimestampToDateTime(item.has_power_start)}")
//        holder.setText(R.id.tv_effective_time,"有效期至：${convertTimestampToDateTime(item.has_power_end)}")
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_exchange_give, parent)
    }

}