package com.comm.banner.itemdecoration

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.banner.itemdecoration
 * ClassName: WrapContentLinearLayoutManager
 * Author:ShiMing Shi
 * CreateDate: 2022/10/9 12:12
 * Email:shiming024@163.com
 * Description:
 */
open class WrapContentLinearLayoutManager: LinearLayoutManager {

    constructor(context: Context): super(context)

    constructor(context: Context,orientation: Int,reverseLayout: Boolean): super(context, orientation, reverseLayout)

    constructor(context: Context,attributeSet: AttributeSet?,defStyleAttr: Int,defStyleRes: Int): super(context,attributeSet,defStyleAttr, defStyleRes)

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }
    }
}