package com.colin.library.android.widget.base

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import com.colin.library.android.utils.Constants
import java.util.Collections

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Date  :2024-09-09
 *
 * Des   :Android RecyclerView 适配器基类
 */


abstract class BaseAdapter<ITEM>(
    val items: ArrayList<ITEM> = arrayListOf(),
    @LayoutRes private val layoutRes: Int,
) : RecyclerView.Adapter<BaseViewHolder>() {
    private val defaultDiff = object : ItemCallback<ITEM>() {
        override fun areItemsTheSame(oldItem: ITEM & Any, newItem: ITEM & Any) = oldItem == newItem

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ITEM & Any, newItem: ITEM & Any) =
            oldItem == newItem

    }

    constructor(@LayoutRes layoutRes: Int) : this(arrayListOf(), layoutRes)

    lateinit var context: Context

    /**
     * 空布局资源ID，如果不为ID_NULL,说明支持空布局
     */
    @LayoutRes
    var empty: Int = Constants.ZERO

    /**
     * 尾部布局资源ID，如果不为ID_NULL,说明支持尾部布局
     */
    @LayoutRes
    var footer: Int = Constants.ZERO


    /**
     * Item点击事件监听
     */
    var onItemClickListener: ((View, ITEM, Int) -> Unit)? = null

    /**
     * Item长按事件监听
     */
    var onItemLongClickListener: ((view: View, item: ITEM) -> Boolean)? = { _, _ -> false }

    /**
     * 计算Adapter 适配器总共大小
     */
    final override fun getItemCount(): Int {
        return if (shouldDisplayEmpty()) 1
        else if (shouldDisplayFoot()) items.size + 1
        else items.size
    }

    final override fun getItemViewType(position: Int): Int {
        return if (shouldDisplayEmpty(position)) empty
        else if (shouldDisplayFoot(position)) footer
        else layoutRes
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        context = parent.context
        return BaseViewHolder(LayoutInflater.from(context).inflate(viewType, parent, false))
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder, position: Int, payloads: MutableList<Any>
    ) {
        if (shouldDisplayEmpty(position)) bindEmptyViewHolder(holder, position, payloads)
        else if (shouldDisplayFoot(position)) bindFooterViewHolder(holder, position, payloads)
        else bindListViewHolder(holder, items[position], position, payloads)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {

    }

    /**
     * 清空数据
     */
    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<ITEM>?) {
        items.clear()
        list?.let { items.addAll(it) }
        notifyDataSetChanged()
    }

    /**
     * 末尾添加item
     */
    fun addData(item: ITEM) {
        // 如果之前在显示空布局，需要先移除
        val showEmpty = shouldDisplayEmpty()
        if (showEmpty) notifyItemRemoved(0)
        items.add(item)
        notifyItemInserted(items.size - 1)
        //如果之前显示了空布局，如果需要显示尾部布局，则显示尾部布局
        if (showEmpty && shouldDisplayFoot()) notifyItemInserted(items.size)
    }

    /**
     * 添加指定位置的Item
     */
    fun addData(@IntRange(from = 0) position: Int, item: ITEM) {
        // 如果之前在显示空布局，需要先移除
        val showEmpty = shouldDisplayEmpty()
        if (showEmpty) notifyItemRemoved(0)
        items.add(position, item)
        notifyItemInserted(items.size - 1)
        //如果之前显示了空布局，如果需要显示尾部布局，则显示尾部布局
        if (showEmpty && shouldDisplayFoot()) notifyItemInserted(items.size)
    }

    /**
     * 删除指定对象
     */
    fun remove(item: ITEM) {
        val indexOf = items.indexOf(item)
        if (indexOf != Constants.INVALID) return
    }

    /**
     * 删除指定下标的Item
     */
    fun removeAt(@IntRange(from = 0) position: Int) {
        if (position < items.size) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
        //移除之后，需要检查是否应该显示空布局
        if (shouldDisplayEmpty()) {
            if (shouldDisplayFoot()) notifyItemRemoved(0)
            notifyItemInserted(0)
        }
    }

    /**
     * Item swap 数据位置交换
     */
    fun swap(fromPosition: Int, toPosition: Int) {
        if (fromPosition in items.indices && toPosition in items.indices) {
            Collections.swap(items, fromPosition, toPosition)
            notifyItemChanged(fromPosition)
            notifyItemChanged(toPosition)
        }
    }

    /**
     * item 位置的移动
     */
    open fun move(fromPosition: Int, toPosition: Int) {
        if (fromPosition in items.indices && toPosition in items.indices) {
            val item = items.removeAt(fromPosition)
            items.add(toPosition, item)
            notifyItemMoved(fromPosition, toPosition)
        }
    }

    /**
     * 绑定空布局数据
     */
    open fun bindEmptyViewHolder(
        holder: BaseViewHolder, position: Int, payloads: MutableList<Any>
    ) {

    }

    /**
     * 绑定尾部布局数据
     */
    open fun bindFooterViewHolder(
        holder: BaseViewHolder, position: Int, payloads: MutableList<Any>
    ) {

    }

    /**
     * 判断当前Adapter是否需要显示空布局
     * ps:当list 数组为空显示，切empty 布局ID不为空
     */
    open fun shouldDisplayEmpty(): Boolean {
        return empty != Constants.ZERO && items.isEmpty()
    }

    open fun shouldDisplayEmpty(position: Int): Boolean {
        return shouldDisplayEmpty() && position == 0
    }

    /**
     * 判断当前Adapter是否需要显示尾部布局
     * ps:当list 数组为空显示，切empty 布局ID不为空
     */
    open fun shouldDisplayFoot(): Boolean {
        return footer != Constants.ZERO
    }

    open fun shouldDisplayFoot(position: Int): Boolean {
        return shouldDisplayFoot() && position + 1 == items.size
    }

    abstract fun bindListViewHolder(
        holder: BaseViewHolder, item: ITEM, position: Int, payloads: MutableList<Any>
    )


}