package com.ssm.comm.ui.base

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.FloatRange
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.ssm.comm.R

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.ui.widget.dialog
 * ClassName: BaseDialogFrament
 * Author:ShiMing Shi
 * CreateDate: 2022/9/9 14:01
 * Email:shiming024@163.com
 * Description:DialogFragment基类
 */
abstract class BaseDialogFragment<VB: ViewDataBinding> : DialogFragment(){

    protected var mDataBinding: VB? = null
    protected var mContext: Context? = null
    protected var mActivity: AppCompatActivity? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setStyle(STYLE_NORMAL,R.style.BaseDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.mContext = activity
        this.mActivity = activity as AppCompatActivity
        dialog?.setCanceledOnTouchOutside(isOnTouchOutSide())
        mDataBinding = DataBindingUtil.inflate(inflater,getLayoutId(),container,false)
        mDataBinding!!.lifecycleOwner = this
        initViews()
        return mDataBinding!!.root
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        val wl = window?.attributes
        wl?.width =  getWindowWidth()
        wl?.height = getWindowHeight()
        wl?.gravity = showWindowGravity()
        window?.attributes = wl
        window?.setDimAmount(getWindowDimAmount())
    }

    open fun showWindowGravity(): Int{
        return Gravity.BOTTOM
    }

    open fun isOnTouchOutSide(): Boolean{
        return false
    }

    open fun getWindowWidth(): Int{
        return ViewGroup.LayoutParams.MATCH_PARENT
    }

    open fun getWindowHeight(): Int{
        return ViewGroup.LayoutParams.WRAP_CONTENT
    }

    @FloatRange(from = 0.0, to = 1.0)
    open fun getWindowDimAmount(): Float{
        return 0.5f
    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun initViews()
}