package com.wanwuzhinan.mingchang.ui.pop

import android.text.TextUtils
import android.view.Gravity
import com.colin.library.android.utils.ext.onClick
import com.ssm.comm.ext.dismissLoadingExt
import com.ssm.comm.ext.editChange
import com.ssm.comm.ext.initEditChange
import com.ssm.comm.ext.toastSuccess
import com.ssm.comm.ui.base.BaseDialogFragment
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.databinding.PopAddressBinding
import com.wanwuzhinan.mingchang.vm.UserViewModel


class EditAddressDialog constructor() : BaseDialogFragment<PopAddressBinding>(){

    lateinit var mViewModel: UserViewModel
    override fun initViews() {
        isCancelable = false
        mViewModel = UserViewModel()
        initClick()
        initRequest()
    }

    private fun initRequest(){
        mViewModel.editAddressLiveData.observeForever {
            if(mActivity!!.isFinishing||mActivity!!.isDestroyed) return@observeForever
            dismissLoadingExt()

            if(it.data==null){
                toastSuccess(it.msg)
                dismiss()
            }else{
                toastSuccess(it.msg)
                dismiss()
            }
        }
    }

    private fun initClick(){

        initEditChange(mDataBinding!!.tvUserName,mDataBinding!!.tvAddress,mDataBinding!!.tvPhone) {
            changeButtonBackground()
        }

        onClick(
            mDataBinding!!.tvSave,
            mDataBinding!!.ivCancel) {
            when (it) {
                mDataBinding!!.tvSave->{
                    var name=mDataBinding!!.tvUserName.text.toString().trim()
                    var phone=mDataBinding!!.tvPhone.text.toString().trim()
                    var address=mDataBinding!!.tvAddress.text.toString().trim()

                    if(TextUtils.isEmpty(name)){
                        toastSuccess("请填写收货人姓名")
                        return@onClick
                    }
                    if(TextUtils.isEmpty(phone)){
                        toastSuccess("请输入手机号")
                        return@onClick
                    }
                    if(TextUtils.isEmpty(address)){
                        toastSuccess("请输入收货地址")
                        return@onClick
                    }
                    mViewModel.editAddress(name,address,phone)
                }
                mDataBinding!!.ivCancel ->{
                    dismiss()
                }
            }
        }
    }


    private fun changeButtonBackground(){
        var editTips= editChange(mDataBinding!!.tvUserName,mDataBinding!!.tvAddress,mDataBinding!!.tvPhone)
        mDataBinding!!.tvSave.setBackgroundResource(if(editTips) R.drawable.bg_btn_save else R.drawable.shape_ffd8b0_23)
    }

    override fun showWindowGravity(): Int {
        return Gravity.CENTER
    }

    override fun getLayoutId(): Int {
        return R.layout.pop_address
    }

}