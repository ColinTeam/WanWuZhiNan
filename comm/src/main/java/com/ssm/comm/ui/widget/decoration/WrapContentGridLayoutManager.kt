package com.ssm.comm.ui.widget.decoration

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssm.comm.adapter.rv.BaseRecycleAdapter

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.ui.widget.decoration
 * ClassName: WrapContentLinearLayoutManager
 * Author:ShiMing Shi
 * CreateDate: 2022/9/7 12:22
 * Email:shiming024@163.com
 * Description:
 */
open class WrapContentGridLayoutManager : GridLayoutManager {

    constructor(context: Context,spanCount: Int) : super(context,spanCount){

    }

    constructor(context: Context,spanCount: Int,orientation: Int,reverseLayout: Boolean) : super(context,spanCount,orientation, reverseLayout){

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int,defStyleRes: Int) : super(context,attrs, defStyleAttr, defStyleRes){

    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try{
            super.onLayoutChildren(recycler, state)
        }catch(e: IndexOutOfBoundsException){
            e.printStackTrace()
        }
    }

    fun setSpanSizeLookup(mAdapter: BaseRecycleAdapter<*,*>?) {
        if (mAdapter == null){
            return
        }
        val mSpanSizeLookup = object: SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                if(mAdapter.listener.isShowEmpty() && mAdapter.dataList.isEmpty()){
                   return spanCount
                }
                return 1
            }
        }
        super.setSpanSizeLookup(mSpanSizeLookup)
    }
}