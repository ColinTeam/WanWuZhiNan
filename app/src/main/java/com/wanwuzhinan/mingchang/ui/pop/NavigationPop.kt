package com.wanwuzhinan.mingchang.ui.pop

import android.app.Activity
import com.ad.img_load.setOnClickNoRepeat
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.databinding.PopNavigationBinding
import com.wanwuzhinan.mingchang.utils.MapUtil
import com.ssm.comm.ext.toastError

class NavigationPop(var context: Activity) :BasePop<PopNavigationBinding>(context){

    var mLat=0.0
    var mLon=0.0
    var mAddress=""

    override fun initClick() {
        setOnClickNoRepeat(mDataBinding.tvGdMap,mDataBinding.tvBdMap,mDataBinding.tvTencentMap,mDataBinding.tvCancel){
            dismiss()
            when(it){
                mDataBinding.tvGdMap ->{//高德
                    if (MapUtil.isGdMapInstalled(mContext)) {
                        MapUtil.openGaoDeNavi(mContext, 0.0, 0.0, "", mLat, mLon, mAddress)
                    } else {
                        toastError("未安装高德地图")
                    }
                }
                mDataBinding.tvBdMap ->{//百度
                    if (MapUtil.isBaiduMapInstalled(mContext)) {
                        MapUtil.openBaiDuNavi(mContext, 0.0, 0.0, "", mLat, mLon, mAddress)
                    } else {
                        toastError("未安装百度地图")
                    }
                }
                mDataBinding.tvTencentMap ->{//腾讯
                    if (MapUtil.isTencentMapInstalled(mContext)) {
                        MapUtil.openTencentMap(mContext, 0.0, 0.0, "", mLat, mLon, mAddress)
                    } else {
                        toastError("未安装腾讯地图")
                    }
                }
            }
        }
    }

    fun showPop(lat: Double,lon:Double,address:String) {
        mLat=lat
        mLon=lon
        mAddress=address
        showBottomPop()

    }
    override fun getLayoutID(): Int {
        return R.layout.pop_navigation
    }
}