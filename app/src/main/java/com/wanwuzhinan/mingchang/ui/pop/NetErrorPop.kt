package com.wanwuzhinan.mingchang.ui.pop

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.View
import com.ad.img_load.setOnClickNoRepeat
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.databinding.PopNetErrorBinding


class NetErrorPop(var context: Activity) :BasePop<PopNetErrorBinding>(context){

    override fun initClick() {

        setOnClickNoRepeat(mDataBinding.ivCancel,mDataBinding.tvSure){
            when(it){
                mDataBinding.ivCancel,mDataBinding.tvSure->{
                    dismiss()
                }
            }
        }
    }

    fun showPop(type:Int,title:String,content:String){
        Log.e("TAG", "initClick: "+title+"1111"+content )
        mDataBinding.tvTitle.text = title
        mDataBinding.tvContent.visibility = View.VISIBLE
        mDataBinding.tvContent.text = content
        mDataBinding.tvTips.text = ""
        mDataBinding.tvSure.visibility = View.GONE
        //  type 1成功 0失败
        mDataBinding.tvContent.setBackgroundResource(if (type == 1) R.mipmap.alert_content_bg else R.mipmap.alert_content_error_bg)
        mDataBinding.tvContent.setTextColor(if (type == 1) Color.parseColor("#333333") else Color.parseColor("#ffffff"))

        showHeightPop()
    }

    fun showPop(title:String,tips:String,content:String){
        Log.e("TAG", "initClick: "+title+"1111"+content )
        mDataBinding.tvTitle.text = title.replace("\n", "")
        mDataBinding.tvContent.text = content
        mDataBinding.tvTips.text = tips
        mDataBinding.tvSure.visibility = View.VISIBLE
        //  type 1成功 0失败
        mDataBinding.tvContent.setBackgroundResource(R.mipmap.alert_content_error_bg)
        mDataBinding.tvContent.setTextColor(Color.parseColor("#ffffff"))

        showHeightPop()
    }

    fun showVideoPop(title:String,tips:String,tipsSub:String){
        mDataBinding.tvTitle.text = title
        mDataBinding.tvContent.visibility = View.GONE
        mDataBinding.tvTips.text = tips
        mDataBinding.tvTipsSub.text = tipsSub
        mDataBinding.tvTipsSub.visibility = View.VISIBLE
        mDataBinding.tvSure.visibility = View.GONE

        showHeightPop()
    }

    fun showLogout(onSure: () -> Unit){
        mDataBinding.tvTitle.text = "退出登录"
        mDataBinding.tvTips.text = "确定要退出登录吗？"
        val typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        mDataBinding.tvTips.typeface = typeface
        mDataBinding.tvTipsSub.text = "您的数据随账号保存"
        mDataBinding.tvTipsSub.visibility = View.VISIBLE
        mDataBinding.tvSure.text = "确定"
        showHeightPop()

        setOnClickNoRepeat(mDataBinding.ivCancel,mDataBinding.tvSure){
            when(it){
                mDataBinding.ivCancel->{
                    dismiss()
                }
                mDataBinding.tvSure->{
                    onSure()
                }
            }
        }
    }

    fun showUpdate(mast:String, onSure: () -> Unit,onCancel:() -> Unit){
        mDataBinding.ivCancel.visibility = if (mast.toInt() == 1) View.GONE else View.VISIBLE
        mDataBinding.tvSure.visibility = if (mast.toInt() == 1) View.GONE else View.VISIBLE
        mDataBinding.tvTitle.text = "新版本"
        mDataBinding.tvTips.text = "✨新版本上线啦！"
        val typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        mDataBinding.tvTips.typeface = typeface
        mDataBinding.tvTipsSub.text = "请去应用商店下载最新版本！🚀"
        mDataBinding.tvTipsSub.visibility = View.VISIBLE
        mDataBinding.tvSure.text = "我知道了"
        showHeightPop()

        setOnClickNoRepeat(mDataBinding.ivCancel,mDataBinding.tvSure){
            when(it){
                mDataBinding.ivCancel->{
                    dismiss()
                    onCancel()
                }
                mDataBinding.tvSure->{
                    dismiss()
                    onSure()
                }
            }
        }
    }


    override fun getLayoutID(): Int {
        return R.layout.pop_net_error
    }
}