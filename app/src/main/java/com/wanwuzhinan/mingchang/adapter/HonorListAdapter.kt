package com.wanwuzhinan.mingchang.adapter

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.colin.library.android.image.glide.GlideImgManager
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.MedalListData


class HonorListAdapter : BaseQuickAdapter<MedalListData,QuickViewHolder>() {

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: MedalListData?) {
//        GlideImgManager.get().loadImg(item!!.image_show,holder.getView(R.id.image),R.drawable.img_default_icon)
        GlideImgManager.get().loadImg(if( item!!.is_has == 1) item.image else item.image_selected ,holder.getView(R.id.image),R.drawable.img_default_icon)
        holder.setText(R.id.tv_name,item.name)
            .setText(R.id.tv_share,if (item.is_has == 1) "去分享" else "去了解")

    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_honor_list, parent)
    }

}