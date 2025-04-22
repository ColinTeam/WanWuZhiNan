package com.wanwuzhinan.mingchang.ui.pop

import android.app.Activity
import android.graphics.Color
import android.view.View
import com.ad.img_load.glide.manager.GlideImgManager
import com.ad.img_load.setOnClickNoRepeat
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.MedalListData
import com.wanwuzhinan.mingchang.databinding.PopShareBinding
import com.wanwuzhinan.mingchang.ext.createQRCodeBitmap
import com.wanwuzhinan.mingchang.ext.getConfigData
import com.wanwuzhinan.mingchang.ext.getUserInfo


class SharePop(var context: Activity) :BasePop<PopShareBinding>(context){

    override fun initClick() {

        var bitmap = createQRCodeBitmap(
            getConfigData().qrcode_image, 200, 200,
            "UTF-8", "H", "0", Color.BLACK, Color.WHITE
        )
        mDataBinding.ivCode.setImageBitmap(bitmap)
    }

    fun showPop(bean:MedalListData,onSure: (view:View,type:Int) -> Unit){
        GlideImgManager.get().loadImg(bean.image_show,mDataBinding!!.ivCover,0)
        GlideImgManager.get().loadImg(getConfigData().qrcode_image,mDataBinding!!.ivCode,0)
        mDataBinding.tvName.text= "我在万物指南获得${bean.name}！"

        setOnClickNoRepeat(mDataBinding.llSave,mDataBinding.llWx,mDataBinding.llQuan){
            dismiss()
            when (it){
                mDataBinding.llSave ->{
                    onSure(mDataBinding.cl,0)
                }
                mDataBinding.llWx ->{
                    onSure(mDataBinding.cl,1)
                }
                mDataBinding.llQuan ->{
                    onSure(mDataBinding.cl,2)
                }
            }

        }
        showHeightPop()
    }

    override fun getLayoutID(): Int {
        return R.layout.pop_share
    }

}