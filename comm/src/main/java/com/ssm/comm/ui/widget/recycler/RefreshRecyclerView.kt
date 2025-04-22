package com.ssm.comm.ui.widget.recycler

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.ssm.comm.R
import com.ssm.comm.adapter.rv.BaseRecycleAdapter
import com.ssm.comm.databinding.BaseRefreshListBinding

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.ui.widget.recycler
 * ClassName: RefreshRecyclerView
 * Author:ShiMing Shi
 * CreateDate: 2022/9/13 13:40
 * Email:shiming024@163.com
 * Description:带刷新的列表
 */
class RefreshRecyclerView : FrameLayout, OnRefreshListener{

    private var mDataBinding: BaseRefreshListBinding? = null
    private var mItemDecoration: ItemDecoration? = null
    private var isEnableRefresh = true
    private var isEnableLoadMore = true
    private var mCommOnRefreshListener: CommOnRefreshListener? = null
    private var mCommOnLoadMoreListener: CommOnLoadMoreListener? = null
    private var mCommRefreshAndLoadMoreListener: CommRefreshAndLoadMoreListener? = null
    private var listenerBuilder: RefreshAndLoadMoreBuilder? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        mDataBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.base_refresh_list,
                this,
                true
            )
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RefreshRecyclerView)
        val count = typedArray.indexCount
        for (i in 0 until count) {
            when (val index = typedArray.getIndex(i)) {
                R.styleable.RefreshRecyclerView_is_enable_refresh -> {
                    isEnableRefresh = typedArray.getBoolean(index, true)
                   // mDataBinding!!.baseRefreshLayout.isEnableRefresh = isEnableRefresh
                }
                R.styleable.RefreshRecyclerView_is_enable_load_more -> {
                    isEnableLoadMore = typedArray.getBoolean(index, true)
                 //   mDataBinding!!.baseRefreshLayout.isEnableLoadmore = isEnableLoadMore
                }
            }
        }
        typedArray.recycle()

        if (isEnableRefresh) {
            mDataBinding!!.baseRefreshLayout.setOnRefreshListener(this)
        }
        if (isEnableLoadMore) {
           // mDataBinding!!.baseRefreshLayout.setOnLoadmoreListener(this)
        }
    }



    fun addCommOnRefreshListener(listener: CommOnRefreshListener) {
        this.mCommOnRefreshListener = listener
    }


    fun finishRefreshList() {
        mDataBinding?.baseRefreshLayout?.finishRefresh()
    }

    fun resetNoMoreData() {
        mDataBinding?.baseRefreshLayout?.resetNoMoreData()
    }


    fun addCommOnLoadMoreListener(listener: CommOnLoadMoreListener) {
        this.mCommOnLoadMoreListener = listener
    }

    fun addCommRefreshAndLoadMoreListener(listener: CommRefreshAndLoadMoreListener) {
        this.mCommRefreshAndLoadMoreListener = listener
    }

    fun addCommRefreshAndLoadMoreBuilder(listenerBuilder: RefreshAndLoadMoreBuilder.() -> Unit) {
        this.listenerBuilder = RefreshAndLoadMoreBuilder().also(listenerBuilder)
    }

    fun addItemDecoration(decor: ItemDecoration?) {
        if (mItemDecoration != null) {
            mDataBinding!!.baseRecyclerview.removeItemDecoration(mItemDecoration!!)
        }
        mItemDecoration = decor
        mDataBinding!!.baseRecyclerview.addItemDecoration(mItemDecoration!!)
    }

    fun setLayoutManager(manager: LayoutManager?) {
        mDataBinding!!.baseRecyclerview.layoutManager = manager
    }

    fun initLinearLayoutManager() {
        mDataBinding!!.baseRecyclerview.initLinearLayoutManager()
    }

    fun <T : Any, VB : ViewDataBinding> setAdapter(adapter: BaseRecycleAdapter<T, VB>?) {
        mDataBinding!!.baseRecyclerview.adapter = adapter
    }

    interface CommOnRefreshListener {
        fun onRefresh(refreshLayout: RefreshLayout?)
    }

    interface CommOnLoadMoreListener {
        fun onLoadMore(refreshLayout: RefreshLayout?)
    }

    interface CommRefreshAndLoadMoreListener {
        fun onRefresh(refreshLayout: RefreshLayout?)
        fun onLoadMore(refreshLayout: RefreshLayout?)
    }

    inner class RefreshAndLoadMoreBuilder {

        var onRefresh: (refreshLayout: RefreshLayout?) -> Unit = {

        }
        var onLoadMore: (refreshLayout: RefreshLayout?) -> Unit = {

        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {

    }
}