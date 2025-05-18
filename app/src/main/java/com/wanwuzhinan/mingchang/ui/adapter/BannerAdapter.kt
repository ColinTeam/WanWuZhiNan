package com.wanwuzhinan.mingchang.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.ui.adapter.BannerViewHolder.FullScreenViewHolder

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-18 23:40
 *
 * Des   :BannerAdapter
 */
class BannerAdapter() : RecyclerView.Adapter<BannerViewHolder>() {
    val items = listOf(
        R.mipmap.bg_login, // 全屏图
        R.mipmap.bg_ask_home,      // 小卡片图 1
        R.mipmap.bg_practice_home,      // 小卡片图 2
        R.mipmap.bg_video_home       // 小卡片图 3
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        return FullScreenViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_video_home_bg, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val realPosition = position % items.size
        val item = items[realPosition]
        holder.bind(item)
    }

    override fun getItemCount(): Int = Integer.MAX_VALUE

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0 else 1
    }
}

sealed class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(item: Int)

    class FullScreenViewHolder(view: View) : BannerViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.ivBg)

        override fun bind(item: Int) {
            imageView.setImageResource(item)
        }
    }

    class CardViewHolder(view: View) : BannerViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.ivBg)
        private val textView: TextView = view.findViewById(R.id.tvText)

        override fun bind(item: Int) {
            imageView.setImageResource(item)
            textView.text = "Item $item"
        }
    }
}
