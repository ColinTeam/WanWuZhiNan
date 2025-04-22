package com.ssm.comm.ui.widget.sv

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.TranslateAnimation
import androidx.core.widget.NestedScrollView

/**
 * Company:AD
 * ProjectName: V号玩家
 * Package: com.metaverse.vn.ui.widget
 * ClassName: StretchScrollView
 * Author:ShiMing Shi
 * CreateDate: 2022/11/19 14:08
 * Email:shiming024@163.com
 * Description:自定义ScrollView阻尼回弹
 */
class StretchScrollView : NestedScrollView {

    //子View
    private var innerView: View? = null

    //上次手势事件的y坐标
    private var mLastY: Float = .0f

    //记录子View的正常位置
    private var normal: Rect = Rect()

    constructor(context: Context) : this(context, null) {}

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        //initViews()
    }

    /**
     * 获取ScrollView的子布局
     */
    private fun initViews() {
        // 去除原本ScrollView滚动到边界时的阴影效果
        this.overScrollMode = OVER_SCROLL_NEVER
        if (getChildAt(0) != null) {
            innerView = getChildAt(0)
        }
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_UP -> {
                // 手指松开恢复
                if (!normal.isEmpty) {
                    planAnimation()
                    normal.setEmpty()
                    mLastY = .0f
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val currentY = ev.y
                // 滑动距离
                val distanceY = (mLastY - currentY).toInt()
                // 处理Y轴的滚动事件，当滚动到最上或者最下时需要移动布局
                // 手指刚触及屏幕时，也会触发此事件，此时mLastY的值还是0，会立即触发一个比较大的移动。这里过滤掉这种情况
                if (isNeedTranslate() && mLastY != .0f) {
                    if (normal.isEmpty) {
                        // 保存正常的布局位置
                        normal.set(
                            innerView!!.left,
                            innerView!!.top,
                            innerView!!.right,
                            innerView!!.bottom
                        )
                    }
                    // 移动布局， 使distance / 2 防止平移过快
                    innerView!!.layout(
                        innerView!!.left,
                        innerView!!.top - distanceY / 2,
                        innerView!!.right,
                        innerView!!.bottom - distanceY / 2
                    )
                }
                mLastY = currentY
            }
        }
        return super.onTouchEvent(ev)
    }

    /**
     * 回缩动画
     */
    private fun planAnimation() {
        // 开启移动动画
        val animation =
            TranslateAnimation(.0f, .0f, innerView!!.top.toFloat(), normal.top.toFloat())
        animation.duration = 200
        innerView!!.startAnimation(animation)
        // 补间动画并不会真正修改innerView的位置，这里需要设置使得innerView回到正常的布局位置
        innerView!!.layout(normal.left, normal.top, normal.right, normal.bottom)
    }

    /**
     * 是否需要Y移动布局
     */
    private fun isNeedTranslate(): Boolean {
        val offset = innerView!!.measuredHeight - height
        val mScrollY = scrollY
        // 顶部或者底部
        return mScrollY == 0 || mScrollY == offset
    }

    override fun onFinishInflate() {
        initViews()
        super.onFinishInflate()
    }
}