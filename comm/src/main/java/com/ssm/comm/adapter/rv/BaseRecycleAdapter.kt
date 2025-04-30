package com.ssm.comm.adapter.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.ssm.comm.R
import com.ssm.comm.config.Constant
import com.ssm.comm.ext.setOnClickNoRepeat

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.meitukanglv.app.wj.adapter.rv.base
 * ClassName: BaseRecycleAdapter
 * Author:ShiMing Shi
 * CreateDate: 2022/10/18 21:50
 * Email:shiming024@163.com
 * Description:
 */
@Suppress("UNCHECKED_CAST")
class BaseRecycleAdapter<T : Any, VB : ViewDataBinding> constructor(
    listenerBuilder: BuildListener<VB, T>.() -> Unit,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var dataList: MutableList<T>
    var listener: BuildListener<VB, T>

    init {
        this.dataList = arrayListOf()
        this.listener = BuildListener<VB, T>().also(listenerBuilder)
    }

    companion object {
        const val TYPE_EMPTY = 1
        const val TYPE_HEADER = 2
        const val TYPE_FOOTER = 3
    }

    fun updateList(list: MutableList<T>?) {
        if (list == null || list.isEmpty()) {
            this.clear()
            return
        }
        this.dataList = list
        this.notifyItemRangeChanged(0, dataList.size)
    }

    fun updateList(data: T?, position: Int) {
        if (data == null || position >= dataList.size) {
            return
        }
        this.dataList[position] = data
        this.notifyItemChanged(position)
    }

    fun updateList(position: Int) {
        if (position >= dataList.size) {
            return
        }
        this.notifyItemChanged(position)
    }

    fun addList(page: Int, list: MutableList<T>?) {
        if (page <= Constant.INIT_PAGE) {
            updateList(list)
            return
        }
        if (list == null) {
            return
        }
        val start = dataList.size
        dataList.addAll(list)
        this.notifyItemRangeInserted(start, dataList.size)
    }

    fun addDataToList(data: T?) {
        if (data == null) {
            return
        }
        dataList.add(data)
        this.notifyItemInserted(dataList.size)
    }

    fun addDataToFirst(data: T?) {
        if (data == null) {
            return
        }
        dataList.add(0,data)
        this.notifyItemInserted(1)
    }

    fun clear() {
        if (dataList.isNotEmpty()) {
            val start = dataList.size
            dataList.clear()
            this.notifyItemRangeRemoved(0, start)
        }
    }

    fun remove(position: Int) {
        if (position < dataList.size) {
            dataList.removeAt(position)
            this.notifyItemRemoved(position)
        }
    }

    fun remove(data: T) {
        dataList.remove(data)
        this.notifyDataSetChanged()
    }


    override fun getItemViewType(position: Int): Int {
        if (listener.isShowHeader() && dataList.isNotEmpty() && position == 0) {
            return TYPE_HEADER
        }
        if (listener.isShowFooter() && dataList.isNotEmpty() && dataList.size - 1 == position) {
            return TYPE_FOOTER
        }
        if (listener.isShowEmpty() && dataList.isEmpty()) {
            return TYPE_EMPTY
        }
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_HEADER -> {
                return ViewHolderHeader<VB>(parent, listener.headerLayoutResId())
            }
            TYPE_FOOTER -> {
                return ViewHolderFooter<VB>(parent, listener.footerLayoutResId())
            }
            TYPE_EMPTY -> {
                return ViewHolderEmpty<VB>(parent, listener.emptyLayoutResId())
            }
        }
        return ViewHolder<VB>(parent, listener.layoutResId())
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder<*> -> {
                val len = if (listener.isShowHeader()) position - 1 else position
                val data = dataList[position]
                val bind = holder.bind
                listener.onBindViewHolder(bind as VB, data, len)
                checkedViewListener(bind, position, data)
            }
            is ViewHolderEmpty<*> -> {
                listener.onBindView((holder as ViewHolderEmpty<VB>).bind)
            }
            is ViewHolderFooter<*> -> {
                listener.onBindView((holder as ViewHolderFooter<VB>).bind)
            }
            is ViewHolderHeader<*> -> {
                listener.onBindView((holder as ViewHolderHeader<VB>).bind)
            }
        }
    }

    override fun getItemCount(): Int {
        var size = 0
        if (listener.isShowHeader() && dataList.isNotEmpty()) {
            return ++size
        }
        if (listener.isShowFooter() && dataList.isNotEmpty()) {
            return ++size
        }
        if (listener.isShowEmpty() && dataList.isEmpty()) {
            return 1
        }
        return dataList.size
    }

    class ViewHolder<VB : ViewDataBinding> constructor(parent: ViewGroup, @LayoutRes layout: Int) :
        BasicHolder<VB>(parent, layout)

    class ViewHolderEmpty<VB : ViewDataBinding> constructor(
        parent: ViewGroup,
        @LayoutRes layout: Int,
    ) : BasicHolder<VB>(parent, layout)

    class ViewHolderHeader<VB : ViewDataBinding>(parent: ViewGroup, @LayoutRes layout: Int) :
        BasicHolder<VB>(parent, layout)

    class ViewHolderFooter<VB : ViewDataBinding> constructor(
        parent: ViewGroup,
        @LayoutRes layout: Int,
    ) : BasicHolder<VB>(parent, layout)

    open class BasicHolder<VB : ViewDataBinding> constructor(
        parent: ViewGroup,
        @LayoutRes layout: Int,
    ) :
        RecyclerView.ViewHolder(
            DataBindingUtil.inflate<VB>(
                LayoutInflater.from(parent.context),
                layout,
                parent,
                false
            ).root
        ) {
        var bind: VB = DataBindingUtil.getBinding(this.itemView)!!
    }

    private fun checkedViewListener(bind: VB, position: Int, data: T) {
        if (listener.isUseDefaultListener() && dataList.isNotEmpty()) {
            if (listener.isOpenClickListener()) {
                setOnClickNoRepeat(bind.root) {
                    listener.onItemClick(data, position, this)
                    listener.onItemClick2(bind,data, position, this)
                }
            } else if (listener.isOpenLongClickListener()) {
                bind.root.setOnLongClickListener { listener.onItemLongClick(data, position) }
            }
        }
    }

    class BuildListener<VB : ViewDataBinding, T : Any> {
        var layoutResId: () -> Int = { R.layout.base_empty_layout }
        var headerLayoutResId: () -> Int = { R.layout.base_empty_layout }
        var footerLayoutResId: () -> Int = { R.layout.base_empty_layout }
        var emptyLayoutResId: () -> Int = { R.layout.base_empty_layout }
        var onBindViewHolder: (bind: VB, data: T, position: Int) -> Unit =
            { bind, data, position -> }
        var onBindView: (bind: ViewDataBinding) -> Unit = { bind -> }
        var isShowHeader: () -> Boolean = { false }
        var isShowFooter: () -> Boolean = { false }
        var isShowEmpty: () -> Boolean = { true }
        var isUseDefaultListener: () -> Boolean = { true }
        var isOpenClickListener: () -> Boolean = { false }
        var isOpenLongClickListener: () -> Boolean = { false }

        var onItemClick: (data: T, position: Int, adapter: BaseRecycleAdapter<T, VB>) -> Unit =
            { data, position, adapter -> }

        var onItemClick2: (bind: VB, data: T, position: Int, adapter: BaseRecycleAdapter<T, VB>) -> Unit =
            { bind,data, position, adapter -> }

        var onItemLongClick: (data: T, position: Int) -> Boolean =
            { data, position -> false }
    }
}