package com.ssm.comm.ui.widget.decoration

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
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
class SpaceItemDecoration : RecyclerView.ItemDecoration {

    private var leftRight: Int = 0
    private var topBottom: Int = 0
    private var position: Int = -1
    private var mPaint: Paint? = null
    private val mPaintDividerLength: Int = 10

    constructor(leftRight: Int,topBottom: Int) : this(leftRight, topBottom, -1)

    constructor(leftRight: Int,topBottom: Int,position: Int){
        this.leftRight = leftRight
        this.topBottom = topBottom
        this.position = position
        this.mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        this.mPaint!!.color = Color.parseColor("#1D131A")
        this.mPaint!!.style = Paint.Style.FILL
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if(position != -1){
            //左边：到父容器的left内间距位置值
            val left = parent.paddingLeft
            //右边：到父容器的right内间距位置值
            val right = parent.measuredWidth - parent.paddingRight
            val child = parent.getChildAt(position)
            val params: RecyclerView.LayoutParams = child.layoutParams as RecyclerView.LayoutParams
            //上边：具体的某条分割线的左边以child的(bottom+bottomMargin)位置值
            val top = child.bottom + params.bottomMargin
            //下边：top加上指定的高度
            val bottom =  top + mPaintDividerLength
            c.drawRect(left.toFloat(),top.toFloat(),right.toFloat(),bottom.toFloat(),mPaint!!)
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val layoutManager: LinearLayoutManager = parent.layoutManager as LinearLayoutManager
        if(layoutManager.orientation == LinearLayoutManager.VERTICAL){
           if(parent.getChildAdapterPosition(view) == layoutManager.itemCount - 1){
              outRect.bottom = topBottom
           }
           outRect.top = topBottom
           outRect.left = leftRight
           outRect.right = leftRight
           if(position != -1 && parent.getChildAdapterPosition(view) == position){
              outRect.set(0,0,mPaintDividerLength,0)
           }
        }else{
           if(parent.getChildAdapterPosition(view) == layoutManager.itemCount - 1){
              outRect.right = leftRight
           }
           outRect.top = topBottom
           outRect.left = leftRight
           outRect.bottom = topBottom
        }
    }
}