package com.ssm.comm.ui.widget.dialog

import android.view.Gravity
import com.ssm.comm.R
import com.ssm.comm.databinding.DialogViewCommBinding
import com.ssm.comm.ext.isEmpty
import com.ssm.comm.ext.setOnClickNoRepeat
import com.ssm.comm.ui.base.BaseDialogFragment

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.ui.widget.dialog
 * ClassName: CommDialog
 * Author:ShiMing Shi
 * CreateDate: 2022/9/9 20:12
 * Email:shiming024@163.com
 * Description:统一的提示对话框
 */
class CommDialog constructor(msg: String = "温馨提示",callback: ViewClickCallBack.() ->Unit) : BaseDialogFragment<DialogViewCommBinding>() {

    private var msg: String
    private var callback = ViewClickCallBack().also(callback)

    override fun getLayoutId(): Int {
        return R.layout.dialog_view_comm
    }

    init{
        this.msg = msg
    }

    override fun initViews() {
        setOnClickNoRepeat(mDataBinding!!.cancel,mDataBinding!!.confirm){
            when(it){
                mDataBinding!!.cancel ->{
                    callback.onCancel()
                    dismiss()
                }
                mDataBinding!!.confirm ->{
                    callback.onConfirm()
                    dismiss()
                }
            }
        }
        if(!isEmpty(msg)){
            mDataBinding!!.title.text = msg
        }
    }

    override fun showWindowGravity(): Int {
        return Gravity.CENTER
    }

    class ViewClickCallBack{
        var onConfirm: () -> Unit = {}
        var onCancel: () -> Unit = {}
    }
}