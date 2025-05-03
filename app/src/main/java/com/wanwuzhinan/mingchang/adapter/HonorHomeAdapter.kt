package com.wanwuzhinan.mingchang.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ProgressBar
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.colin.library.android.image.glide.GlideImgManager
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.MedalListData
import com.wanwuzhinan.mingchang.view.CardAdapterHelper


class HonorHomeAdapter : BaseQuickAdapter<MedalListData,QuickViewHolder>() {

    private val mCardAdapterHelper = CardAdapterHelper()

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: MedalListData?) {
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, itemCount)
        GlideImgManager.get().loadDefaultImg(item!!.image_show,holder.getView(R.id.image),R.drawable.img_default_icon)
        holder.setText(R.id.tv_name,item.name)
        holder.setText(R.id.tv_day,item.needMsg)

        var progress=holder.getView<ProgressBar>(R.id.progress)
        progress.max=(item.needMin*1000).toInt()
        progress.progress=(item.needHas*1000).toInt()

        holder.setVisible(R.id.ic_flower,item.is_has==1)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        val quickViewHolder = QuickViewHolder(R.layout.item_honor_home, parent)
        mCardAdapterHelper.onCreateViewHolder(parent, quickViewHolder.itemView)
        return quickViewHolder
    }

}