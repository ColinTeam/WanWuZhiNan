package com.ssm.comm.ui.widget.recycler

import android.content.Context
import android.util.AttributeSet
import com.scwang.smartrefresh.layout.SmartRefreshLayout

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.ui.widget.recycler
 * ClassName: CommRecyclerView
 * Author:ShiMing Shi
 * CreateDate: 2022/9/13 13:41
 * Email:shiming024@163.com
 * Description:刷新控件
 */
class CommRefreshLayout : SmartRefreshLayout {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )
}