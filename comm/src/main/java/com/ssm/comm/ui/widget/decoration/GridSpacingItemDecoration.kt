package com.ssm.comm.ui.widget.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.ui.widget.decoration
 * ClassName: SpaceItemDecoration
 * Author:ShiMing Shi
 * CreateDate: 2022/9/8 14:16
 * Email:shiming024@163.com
 * Description:
 */
class GridSpacingItemDecoration(
    private var spanCount: Int,//间隔
    private var spacing: Int,//是否包含边缘
    private var includeEdge: Boolean
) : RecyclerView.ItemDecoration() {


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        //这里是关键，需要根据你有几列来判断

        //这里是关键，需要根据你有几列来判断
        val position = parent.getChildAdapterPosition(view) // item position

        val column = position % spanCount // item column


        if (includeEdge) {
            outRect.left =
                spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
            outRect.right =
                (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)
            if (position < spanCount) { // top edge
                outRect.top = spacing
            }
            outRect.bottom = spacing // item bottom
        } else {
            outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
            outRect.right =
                spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacing // item top
            }
        }
    }
}