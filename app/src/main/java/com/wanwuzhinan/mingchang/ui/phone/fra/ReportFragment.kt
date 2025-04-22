package com.wanwuzhinan.mingchang.ui.phone.fra

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.annotation.RequiresApi
import com.ad.img_load.glide.manager.GlideImgManager
import com.ad.img_load.setOnClickNoRepeat
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.ssm.comm.ext.dismissLoadingExt
import com.ssm.comm.ext.editChange
import com.ssm.comm.ext.initEditChange
import com.ssm.comm.ext.observeState
import com.ssm.comm.ext.showLoadingExt
import com.ssm.comm.ext.toastSuccess
import com.ssm.comm.media.MediaManager
import com.ssm.comm.ui.base.BaseFragment
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.adapter.ExchangeCourseAdapter
import com.wanwuzhinan.mingchang.adapter.SettingAdapter
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.data.ProvinceListData
import com.wanwuzhinan.mingchang.databinding.FragmentEditFileBinding
import com.wanwuzhinan.mingchang.databinding.FragmentExchangeCourseBinding
import com.wanwuzhinan.mingchang.databinding.FragmentReportBinding
import com.wanwuzhinan.mingchang.entity.UserInfoData
import com.wanwuzhinan.mingchang.ext.editTips
import com.wanwuzhinan.mingchang.ui.pop.ChooseCityDialog
import com.wanwuzhinan.mingchang.ui.pop.ChooseGradeDialog
import com.wanwuzhinan.mingchang.ui.pop.EditAddressDialog
import com.wanwuzhinan.mingchang.ui.pop.EditFileDialog
import com.wanwuzhinan.mingchang.ui.pop.ExchangeContactPop
import com.wanwuzhinan.mingchang.utils.SkeletonUtils
import com.wanwuzhinan.mingchang.view.GlideEngine
import com.wanwuzhinan.mingchang.vm.UserViewModel
import java.io.File

//
class ReportFragment :
    BaseFragment<FragmentReportBinding, UserViewModel>(UserViewModel()) {
    private var TAG = "ReportFragment"

    private var mSelectType= -1

    private var mSelectList:ArrayList<LocalMedia> = ArrayList()
    private var mImageList:ArrayList<String> = ArrayList()

    companion object {
        val instance: ReportFragment by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ReportFragment()
        }
    }

    override fun initView() {
        super.initView()
        initList()

        setSelect(ConfigApp.COURSE_VIDEO)

        mViewModel.uploadImgLiveData.observeForever {
            if(mActivity!!.isFinishing||mActivity!!.isDestroyed) return@observeForever
            dismissLoadingExt()

            if(it.data==null){
                toastSuccess(it.msg)
            }else{
                mImageList.add(it!!.data!!.file)
               if (mImageList.size == mImageList.size && mImageList.size > 0){
                   mViewModel.feedbackLiveData(mSelectType,mDataBinding.edContent.text.trim().toString(),"[${TextUtils.join(",",mImageList)}]")
               }
            }
        }

        mViewModel.feedbackLiveData.observeState(this){
            onSuccess={data, msg ->
                toastSuccess("反馈成功")
                mDataBinding.edContent.setText("")
                mSelectList.clear()
                refreshImageShow()
            }
            onFailed={code, msg ->
                toastSuccess(msg.toString())
            }
            onDataEmpty2={msg: String ->
                toastSuccess(msg)
            }
        }
    }

    private fun initList(){

    }

    private fun setSelect(type:Int){
        if(mSelectType==type) return
        mSelectType=type


        mDataBinding.tvVideo.setBackgroundResource(if(type==ConfigApp.COURSE_VIDEO) R.drawable.shape_fc982e_16 else R.drawable.shape_ffeacc_16)
        mDataBinding.tvAudio.setBackgroundResource(if(type==ConfigApp.COURSE_AUDIO) R.drawable.shape_fc982e_16 else  R.drawable.shape_ffeacc_16)
        mDataBinding.tvClass.setBackgroundResource(if(type==ConfigApp.COURSE_GIVE) R.drawable.shape_fc982e_16 else  R.drawable.shape_ffeacc_16)

        mDataBinding.tvVideo.setTextColor(if(type==ConfigApp.COURSE_VIDEO) resources!!.getColor(R.color.white) else resources!!.getColor(R.color.color_ff9424))
        mDataBinding.tvAudio.setTextColor(if(type==ConfigApp.COURSE_AUDIO) resources!!.getColor(R.color.white) else resources!!.getColor(R.color.color_ff9424))
        mDataBinding.tvClass.setTextColor(if(type==ConfigApp.COURSE_GIVE) resources!!.getColor(R.color.white) else resources!!.getColor(R.color.color_ff9424))

    }

    override fun initClick() {

        setOnClickNoRepeat(
            mDataBinding.tvAudio,
            mDataBinding.tvVideo,
            mDataBinding.tvClass,
            mDataBinding.iv1,
            mDataBinding.iv2,
            mDataBinding.iv3,
            mDataBinding.ivUpload,
            mDataBinding.tvSubmit,
        ){
            when(it){
                mDataBinding.tvAudio ->{
                    setSelect(ConfigApp.COURSE_AUDIO)
                }
                mDataBinding.tvVideo ->{
                    setSelect(ConfigApp.COURSE_VIDEO)
                }
                mDataBinding.tvClass ->{
                    setSelect(ConfigApp.COURSE_GIVE)
                }
                mDataBinding.tvSubmit ->{

                    var tips= editTips(mDataBinding.edContent)
                    if(!tips) return@setOnClickNoRepeat
                    showBaseLoading()
                    if (mSelectList.size > 0){
                        mImageList.clear()
                        for (localMedia in mSelectList){
                            val path = MediaManager.getSinglePhotoUri(localMedia) ?: ""
                            mActivity!!.showLoadingExt()
                            mViewModel.uploadImage(File(path))
                        }
                    }else{
                        mViewModel.feedbackLiveData(mSelectType,mDataBinding.edContent.text.trim().toString(),"")
                    }

                }

                mDataBinding.iv1 ->{
                    mSelectList.removeAt(0)
                    refreshImageShow()
                }
                mDataBinding.iv2 ->{
                    mSelectList.removeAt(1)
                    refreshImageShow()
                }
                mDataBinding.iv3 ->{
                    mSelectList.removeAt(2)
                    refreshImageShow()
                }

                mDataBinding.ivUpload ->{//修改头像
                    MediaManager.selectMultiplePhoto(
                        mActivity,
                        3,
                        mSelectList,
                        GlideEngine.createGlideEngine(),
                        object : OnResultCallbackListener<LocalMedia> {
                            override fun onResult(result: ArrayList<LocalMedia>?) {
                                if (result != null) {
                                    mSelectList = result
                                    refreshImageShow()
                                }
                            }

                            override fun onCancel() {
                            }
                        }
                    )
                }

            }
        }
    }

    fun refreshImageShow(){
        mDataBinding.iv1.visibility = View.GONE
        mDataBinding.iv2.visibility = View.GONE
        mDataBinding.iv3.visibility = View.GONE
        mDataBinding.ivUpload.visibility = View.VISIBLE

        if (mSelectList.size > 0){
            GlideImgManager.get().loadImg(MediaManager.getSinglePhotoUri(mSelectList[0]) ?: "",mDataBinding.iv1,com.comm.img_load.R.mipmap.default_icon)
            mDataBinding.iv1.visibility = View.VISIBLE
        }
        if (mSelectList.size > 1){
            GlideImgManager.get().loadImg(MediaManager.getSinglePhotoUri(mSelectList[1]) ?: "",mDataBinding.iv2,com.comm.img_load.R.mipmap.default_icon)
            mDataBinding.iv2.visibility = View.VISIBLE
        }
        if (mSelectList.size > 2){
            GlideImgManager.get().loadImg(MediaManager.getSinglePhotoUri(mSelectList[2]) ?: "",mDataBinding.iv3,com.comm.img_load.R.mipmap.default_icon)
            mDataBinding.iv3.visibility = View.VISIBLE
            mDataBinding.ivUpload.visibility = View.GONE
        }

        mDataBinding.tvNum.text = "${mSelectList.size}/3"
    }

    override fun initRequest() {

    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_report
    }

}