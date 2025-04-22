package com.wanwuzhinan.mingchang.ui.pop

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.View.OnLongClickListener
import com.ad.img_load.glide.manager.GlideImgManager
import com.ad.img_load.setOnClickNoRepeat
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.databinding.PopExchangeContactBinding
import com.wanwuzhinan.mingchang.ext.createQRCodeBitmap
import com.wanwuzhinan.mingchang.ext.getConfigData
import com.wanwuzhinan.mingchang.ext.launchWechatService
import com.wanwuzhinan.mingchang.utils.SaveImageUtils
import com.wanwuzhinan.mingchang.utils.SaveListener


class ExchangeContactPop(var context: Activity) :BasePop<PopExchangeContactBinding>(context){

    override fun initClick() {

        mDataBinding.tvTitle.text = getConfigData().qrcode_desc
        mDataBinding.tvOpen.text = getConfigData().enterprise_btn_name

        if (getConfigData().qrcode_type.toInt() == 1){
            GlideImgManager.get().loadFitCenterImg(getConfigData().qrcode_image_download,mDataBinding.ivCode,0)
        }else{
            GlideImgManager.get().loadFitCenterImg(getConfigData().qrcode_image,mDataBinding.ivCode,0)
        }

        setOnClickNoRepeat(mDataBinding.toolBar.getLeftBack(),mDataBinding.tvOpen){
            when(it){
                mDataBinding.toolBar.getLeftBack()->{
                    dismiss()
                }
                mDataBinding.tvOpen->{
                    if (getConfigData().qrcode_type.toInt() == 1){
                        SaveImageUtils.saveBitmap(mDataBinding.ivCode, object : SaveListener {
                            override fun show() {

                            }

                            override fun dismiss() {

                            }
                        })
                    }else {
                        launchWechatService()
                    }
                }
            }
        }
        mDataBinding.ivCode.setOnLongClickListener(object :OnLongClickListener{
            override fun onLongClick(p0: View?): Boolean {
//                launchWechatService()
                return false
            }
        })
    }

    override fun getLayoutID(): Int {
        return R.layout.pop_exchange_contact
    }
}