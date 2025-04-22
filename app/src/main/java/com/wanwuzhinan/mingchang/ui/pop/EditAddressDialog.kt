package com.wanwuzhinan.mingchang.ui.pop

import android.annotation.SuppressLint
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import com.ad.img_load.glide.manager.GlideImgManager
import com.ad.img_load.setOnClickNoRepeat
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.databinding.PopEditFileBinding
import com.wanwuzhinan.mingchang.entity.UserInfoData
import com.wanwuzhinan.mingchang.vm.UserViewModel
import com.ssm.comm.ext.dismissLoadingExt
import com.ssm.comm.ext.editChange
import com.ssm.comm.ext.initEditChange
import com.ssm.comm.ext.observeState
import com.ssm.comm.ext.showLoadingExt
import com.ssm.comm.ext.toastSuccess
import com.ssm.comm.media.MediaManager
import com.ssm.comm.ui.base.BaseDialogFragment
import com.wanwuzhinan.mingchang.data.ProvinceListData
import com.wanwuzhinan.mingchang.databinding.PopAddressBinding
import com.wanwuzhinan.mingchang.utils.ChooseCityUtils
import com.wanwuzhinan.mingchang.view.GlideEngine
import java.io.File


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

        setOnClickNoRepeat(
            mDataBinding!!.tvSave,
            mDataBinding!!.ivCancel) {
            when (it) {
                mDataBinding!!.tvSave->{
                    var name=mDataBinding!!.tvUserName.text.toString().trim()
                    var phone=mDataBinding!!.tvPhone.text.toString().trim()
                    var address=mDataBinding!!.tvAddress.text.toString().trim()

                    if(TextUtils.isEmpty(name)){
                        toastSuccess("请填写收货人姓名")
                        return@setOnClickNoRepeat
                    }
                    if(TextUtils.isEmpty(phone)){
                        toastSuccess("请输入手机号")
                        return@setOnClickNoRepeat
                    }
                    if(TextUtils.isEmpty(address)){
                        toastSuccess("请输入收货地址")
                        return@setOnClickNoRepeat
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