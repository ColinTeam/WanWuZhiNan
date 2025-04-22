package com.wanwuzhinan.mingchang.ui.phone

import android.graphics.Bitmap
import android.util.Log
import com.ad.img_load.glide.manager.GlideImgManager
import com.ad.img_load.setOnClickNoRepeat
import com.google.gson.Gson
import com.ssm.comm.BuildConfig
import com.ssm.comm.app.CommApplication
import com.ssm.comm.ext.observeState
import com.ssm.comm.ui.base.BaseActivity
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXImageObject
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.data.MedalListData
import com.wanwuzhinan.mingchang.databinding.ActivityHonorDetailBinding
import com.wanwuzhinan.mingchang.ext.*
import com.wanwuzhinan.mingchang.ui.pop.SharePop
import com.wanwuzhinan.mingchang.utils.SaveImageUtils
import com.wanwuzhinan.mingchang.utils.SaveListener
import com.wanwuzhinan.mingchang.vm.UserViewModel


class HonorDetailActivity  : BaseActivity<ActivityHonorDetailBinding, UserViewModel>(UserViewModel()) {

    lateinit var listData:MedalListData

    override fun initView() {

        val data = intent.getStringExtra(ConfigApp.INTENT_DATA)
        Log.e("TAG", "initView: "+data )

        listData = Gson().fromJson(data, MedalListData::class.java)

        mDataBinding.tvNum.text = "${ConfigApp.question_compass}个指南针"
        mDataBinding.tvName.text = listData.name
        GlideImgManager.get().loadDefaultImg(listData.image,mDataBinding.ivCover,0)

        mDataBinding.tvShareName.text = listData.name
        GlideImgManager.get().loadDefaultImg(listData.image,mDataBinding.ivShareCover,0)
        GlideImgManager.get().loadDefaultImg(getConfigData().qrcode_image,mDataBinding.ivCode,0)

    }


    override fun initClick() {
        setOnClickNoRepeat(mDataBinding.linMy,mDataBinding.llShare) {
            when(it){
                mDataBinding.linMy->{
                    finish()
                }
                mDataBinding.llShare->{
                    SharePop(this).showPop(listData){view,int ->
                        if (int == 0) {
                            SaveImageUtils.saveBitmap(mDataBinding.llCl, object : SaveListener {
                                override fun show() {
                                    showBaseLoading()
                                }

                                override fun dismiss() {
                                    dismissBaseLoading()
                                }
                            })
                        }else {
                            val bmp = SaveImageUtils.getBitmapByView(mDataBinding.llCl)

                            //初始化 WXImageObject 和 WXMediaMessage 对象
                            val imgObj: WXImageObject = WXImageObject(bmp)
                            val msg = WXMediaMessage()
                            msg.mediaObject = imgObj
                            //构造一个Req
                            val req = SendMessageToWX.Req()
                            req.message = msg
                            req.scene = if (int == 1) SendMessageToWX.Req.WXSceneSession else SendMessageToWX.Req.WXSceneTimeline
                            req.userOpenId = BuildConfig.WE_CHAT_APP_ID
                            //调用api接口，发送数据到微信
                            CommApplication.instance.api!!.sendReq(req)
                        }
                    }
                }
            }
        }
    }

    override fun initRequest() {
        mViewModel.medalListLiveData.observeState(this){
            onSuccess={data, msg ->

            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_honor_detail
    }
}