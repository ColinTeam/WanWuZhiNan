package com.colin.library.android.widget.banner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.colin.library.android.widget.base.BaseViewHolder

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-14 09:43
 *
 * Des   :BannerAdapter
 */
abstract class BannerAdapter<ITEM>(val list: List<ITEM>? = null, @LayoutRes val layoutRes: Int) :
    RecyclerView.Adapter<BaseViewHolder>() {
    private val cache = arrayListOf<ITEM>()
    override fun onCreateViewHolder(
        parent: ViewGroup, position: Int
    ): BaseViewHolder {
        return BaseViewHolder(LayoutInflater.from(parent.context).inflate(layoutRes, parent, false))
    }


    init {
        if (!list.isNullOrEmpty()) cache.addAll(list)
    }


    override fun getItemCount() = cache.size

}