package com.ssm.comm.ui.widget.recycler

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.ssm.comm.ui.widget.decoration.WrapContentLinearLayoutManager
import com.ssm.comm.adapter.rv.BaseRecycleAdapter
import com.ssm.comm.utils.LogUtils

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.ui.widget.recycler
 * ClassName: CommRecyclerView
 * Author:ShiMing Shi
 * CreateDate: 2022/9/13 13:41
 * Email:shiming024@163.com
 * Description:RecyclerView
 */
class CommRecyclerView : RecyclerView {

    private var listener: OnScrollChangeListener? = null
    private var slsScrolling = false
    private var mAdapter: BaseRecycleAdapter<*, *>? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(context)
    }

    private fun init(context: Context) {
        this.overScrollMode = OVER_SCROLL_NEVER
        this.addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                //newState有三种值:
                //1:SCROLL_STATE_IDLE = 0; （静止没有滚动）
                //2: SCROLL_STATE_DRAGGING = 1; 正在被外部拖拽,一般为用户正在用手指滚动）
                //3:int SCROLL_STATE_SETTLING = 2;（自动滚动）
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == SCROLL_STATE_DRAGGING || newState == SCROLL_STATE_SETTLING) {
                    slsScrolling = true
                    //GlideImgManager.get().pauseRequests();
                    LogUtils.d("图片列表暂停加载==================>")
                } else if (newState == SCROLL_STATE_IDLE) {
                    if (slsScrolling) {
                        LogUtils.d("图片列表恢复加载==================>")
                        //GlideImgManager.get().resumeRequests();
                    }
                    slsScrolling = false
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(-1)) {
                    if (listener != null) {
                        listener?.onScrolledToTop()
                    }
                } else if (!recyclerView.canScrollVertically(1)) {
                    if (listener != null) {
                        listener?.onScrolledToBottom()
                    }
                } else if (dy < 0) {
                    if (listener != null) {
                        listener?.onScrolledUp()
                    }
                } else if (dy > 0) {
                    if (listener != null) {
                        listener?.onScrolledDown()
                    }
                }
            }
        })
    }

    fun initLinearLayoutManager() {
        this.layoutManager = WrapContentLinearLayoutManager(context)
    }

    fun addOnScrollChangeListener(listener: OnScrollChangeListener) {
        this.listener = listener
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        if (adapter is BaseRecycleAdapter<*, *>) {
            mAdapter = adapter
        }
        super.setAdapter(adapter)
    }

    override fun getAdapter(): BaseRecycleAdapter<*,*>? {
        return mAdapter
    }

    interface OnScrollChangeListener {
        /**
         * 向上滑动
         */
        fun onScrolledUp()

        /**
         * 向下滑动
         */
        fun onScrolledDown()

        /**
         * 滑动到顶部
         */
        fun onScrolledToTop()

        /**
         * 滑动到底部
         */
        fun onScrolledToBottom()
    }
}