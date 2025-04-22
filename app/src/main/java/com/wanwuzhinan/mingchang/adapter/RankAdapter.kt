package com.wanwuzhinan.mingchang.adapter

import android.content.Context
import android.view.ViewGroup
import com.ad.img_load.glide.manager.GlideImgManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.ExchangeListData
import com.wanwuzhinan.mingchang.data.RankHomeData
import com.wanwuzhinan.mingchang.data.RankHomeData.RankData


class RankAdapter : BaseQuickAdapter<RankData,QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: RankData?) {
        holder.setText(R.id.tv_my_name,item!!.truename)
            .setText(R.id.tv_compass_num, "${item.question_compass}")
            .setText(R.id.tv_num, "${item.index}")
            .setGone(R.id.tv_num,if (position<3) true else false)
            .setGone(R.id.iv_num,if (position>=3) true else false)
            .setImageResource(R.id.iv_num,if (position==0) R.mipmap.ic_rank_one else (if (position==1) R.mipmap.ic_rank_two else R.mipmap.ic_rank_three))
        GlideImgManager.get().loadImg(item.headimg,holder.getView(R.id.riv_head),R.drawable.img_default_icon)


    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_rank_list, parent)
    }

}