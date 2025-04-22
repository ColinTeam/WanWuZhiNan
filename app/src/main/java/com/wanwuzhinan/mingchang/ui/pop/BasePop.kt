package com.wanwuzhinan.mingchang.ui.pop

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ssm.comm.utils.NavigationBarUtil
import com.wanwuzhinan.mingchang.R

abstract class BasePop<DB : ViewDataBinding>(var mContext : Activity) : PopupWindow() {

    var mXAxis: Int = 0//偏移的x轴
    var mYAxis: Int = 0//偏移的y轴
    var mBgDrawable: Int = 0x0000000//背景色
    lateinit var mDataBinding: DB//布局binding
    var mShowAlpha: Float = 0.7f//显示时候背景透明度
    var mDismissAlpha: Float = 1f//消失时候背景透明度
    var mAnimationStyle: Int = R.style.take_photo_anim//默认的动画
    lateinit var mView:View
    var popupHeight=0
    var popupWidth=0

    init {
        initView()
        initClick()
    }

    open fun initClick() {}

    protected abstract fun getLayoutID(): Int

    fun initView() {
        mView = LayoutInflater.from(mContext).inflate(getLayoutID(), null)
        mDataBinding = DataBindingUtil.bind(mView)!!
        // 导入布局
        this.contentView = mView
        //防止事件穿透，但是会点击外部消失
        isFocusable = true
        //默认不可点击外部
        this.isOutsideTouchable = false
       // isClippingEnabled=false
        val dw = ColorDrawable(mBgDrawable)
        setBackgroundDrawable(dw)
    }

    //普通展示 默认在中间
    open fun showPop(gravity: Int = Gravity.CENTER) {
        if (!isShowing) {
            setBackground(mShowAlpha)
            // 设置宽高
            this.width = WindowManager.LayoutParams.MATCH_PARENT
            this.height = WindowManager.LayoutParams.WRAP_CONTENT
//            mContext.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            mContext.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
            showAtLocation(mContext.window.decorView, gravity, mXAxis, mYAxis)
        }
    }


    //普通展示 默认在中间
    open fun showHeightPop(gravity: Int = Gravity.CENTER) {
        if (!isShowing) {

//            mContext.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            mContext.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
//            NavigationBarUtil.hideNavigationBar(mContext.window)
            setBackground(mShowAlpha)
            // 设置宽高
            this.width = WindowManager.LayoutParams.MATCH_PARENT
            this.height = WindowManager.LayoutParams.MATCH_PARENT

            showAtLocation(mContext.window.decorView, gravity, mXAxis, mYAxis)
        }
    }

    //普通展示带动画 默认从底部往上弹出
    open fun showBottomPop() {
        if (!isShowing) {
            setBackground(mShowAlpha)
            animationStyle = mAnimationStyle
            // 设置宽高
            this.width = WindowManager.LayoutParams.MATCH_PARENT
            this.height = WindowManager.LayoutParams.WRAP_CONTENT
//            mContext.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            mContext.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
            showAtLocation(mContext.window.decorView, Gravity.BOTTOM, mXAxis, mYAxis)
        }
    }

    open fun showAllPop() {
        if (!isShowing) {
            setBackground(mShowAlpha)
            // 设置宽高
            this.width = WindowManager.LayoutParams.MATCH_PARENT
            this.height = WindowManager.LayoutParams.MATCH_PARENT
//            mContext.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            mContext.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
            showAtLocation(mContext.window.decorView, Gravity.BOTTOM, mXAxis, mYAxis)
        }
    }

    //根据某个view展示
    open fun showView(view: View) {
        if (!isShowing) {
            // 设置宽高
            this.width = WindowManager.LayoutParams.WRAP_CONTENT
            this.height = WindowManager.LayoutParams.WRAP_CONTENT
            showAsDropDown(view)
        }
    }

    //根据某个view展示
    open fun showUp(view: View) {
        if (!isShowing) {
            // 设置宽高
            this.width = WindowManager.LayoutParams.WRAP_CONTENT
            this.height = WindowManager.LayoutParams.WRAP_CONTENT


            // 设置PopupWindow的位置
            val location = IntArray(2)
            view.getLocationOnScreen(location)
            val x = location[0]
            val y: Int = location[1] - getHeight()

            mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            popupHeight = mView.getMeasuredHeight()
            popupWidth = mView.getMeasuredWidth()

            /*showAtLocation(view, Gravity.NO_GRAVITY, (x+view.getWidth()/2)- popupWidth/2,
                y-popupHeight)*/
            showAtLocation(view, Gravity.NO_GRAVITY, x- popupWidth/2,
                y-popupHeight)
        }
    }

    override fun dismiss() {
        super.dismiss()
        setBackground(mDismissAlpha)
    }

    private fun setBackground(alpha: Float) {
        val lp1 = mContext.window.attributes
        lp1.alpha = alpha
        mContext.window.attributes = lp1
    }
}