package com.wanwuzhinan.mingchang.ui.pop

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.View.OnLongClickListener
import com.ad.img_load.glide.manager.GlideImgManager
import com.ad.img_load.setOnClickNoRepeat
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.hpplay.glide.Glide
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.databinding.PopExchangeContactBinding
import com.wanwuzhinan.mingchang.ext.createQRCodeBitmap
import com.wanwuzhinan.mingchang.ext.getConfigData
import com.wanwuzhinan.mingchang.ext.launchWechatService
import com.wanwuzhinan.mingchang.utils.SaveImageUtils
import com.wanwuzhinan.mingchang.utils.SaveListener
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.HttpURLConnection
import java.net.URL


class ExchangeContactPop(var context: Activity) :BasePop<PopExchangeContactBinding>(context){

    override fun initClick() {

        mDataBinding.tvTitle.text = getConfigData().qrcode_desc
        mDataBinding.tvOpen.text = getConfigData().enterprise_btn_name

        GlideImgManager.get().loadFitCenterImg(getConfigData().qrcode_image,mDataBinding.ivCode,0)

        setOnClickNoRepeat(mDataBinding.toolBar.getLeftBack(),mDataBinding.tvOpen){
            when(it){
                mDataBinding.toolBar.getLeftBack()->{
                    dismiss()
                }
                mDataBinding.tvOpen->{
                    if (getConfigData().qrcode_type.toInt() == 1){

                        downloadImageAsBitmap(getConfigData().qrcode_image_download, callback = {
                            if (it != null) {
                                SaveImageUtils.startSaveBitmap(it, object : SaveListener {
                                    override fun show() {

                                    }

                                    override fun dismiss() {

                                    }
                                })
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

    fun downloadImageAsBitmap(url: String, callback: (Bitmap?) -> Unit) {
        Thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                val inputStream = response.body?.byteStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                callback(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
                callback(null)
            }
        }.start()
    }

    override fun getLayoutID(): Int {
        return R.layout.pop_exchange_contact
    }
}