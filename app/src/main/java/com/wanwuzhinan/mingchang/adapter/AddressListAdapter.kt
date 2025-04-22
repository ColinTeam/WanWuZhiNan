package com.wanwuzhinan.mingchang.adapter

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.AddressData

//地址列表
class AddressListAdapter : BaseQuickAdapter<AddressData,QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: AddressData?) {
        holder.setText(R.id.tv_address,"${item!!.provinceName}${item!!.cityName}${item!!.areaName}")
        holder.setText(R.id.tv_address_info,item.cont)
        holder.setText(R.id.tv_name,item!!.name)
        holder.setText(R.id.tv_phone,item!!.mobile)
        holder.setGone(R.id.tv_default,item!!.is_moren==0)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_address, parent)
    }

}