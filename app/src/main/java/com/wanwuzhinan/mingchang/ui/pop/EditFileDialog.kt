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
import com.ssm.comm.event.MessageEvent
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.databinding.PopEditFileBinding
import com.wanwuzhinan.mingchang.entity.UserInfoData
import com.wanwuzhinan.mingchang.vm.UserViewModel
import com.ssm.comm.ext.dismissLoadingExt
import com.ssm.comm.ext.editChange
import com.ssm.comm.ext.initEditChange
import com.ssm.comm.ext.observeState
import com.ssm.comm.ext.post
import com.ssm.comm.ext.showLoadingExt
import com.ssm.comm.ext.toastSuccess
import com.ssm.comm.media.MediaManager
import com.ssm.comm.ui.base.BaseDialogFragment
import com.wanwuzhinan.mingchang.data.ProvinceListData
import com.wanwuzhinan.mingchang.ext.getConfigData
import com.wanwuzhinan.mingchang.utils.ChooseCityUtils
import com.wanwuzhinan.mingchang.view.GlideEngine
import java.io.File


class EditFileDialog constructor(data: UserInfoData.infoBean) : BaseDialogFragment<PopEditFileBinding>(){

    lateinit var mViewModel: UserViewModel
    var mAddressList:List<ProvinceListData>?=null
    var mProvinceName=""
    var mCityName=""
    var mAreaName=""
    var mHeadImg=""

    private var data:  UserInfoData.infoBean

    init {
        this.data = data
    }

    override fun initViews() {
        isCancelable = false
        mViewModel= UserViewModel()

        setData()
        initClick()
        initRequest()

        mViewModel.getAllRegion()
    }

    private fun initRequest(){
        mViewModel.getAllGradeLiveData.observeForever {
            if(mActivity!!.isFinishing||mActivity!!.isDestroyed) return@observeForever
            dismissLoadingExt()

            ChooseGradeDialog(it.data!!.listArr,callback = {
                onSure ={
                    mDataBinding!!.tvGrade.text=it
                }
            }).show(mActivity!!.supportFragmentManager,"CommDialog")
        }
        mViewModel.editUserInfoLiveData.observeForever {
            if(mActivity!!.isFinishing||mActivity!!.isDestroyed) return@observeForever
            dismissLoadingExt()
            dismiss()
            post(MessageEvent.UPDATE_USERINFO, "")
        }
        mViewModel.allProvinceLiveData.observeForever{
            if(mActivity!!.isFinishing||mActivity!!.isDestroyed) return@observeForever
            mAddressList=it!!.data!!.list
        }
        mViewModel.uploadImgLiveData.observeForever {
            if(mActivity!!.isFinishing||mActivity!!.isDestroyed) return@observeForever
            dismissLoadingExt()

            if(it.data==null){
                toastSuccess(it.msg)
            }else{
                mHeadImg=it!!.data!!.file
                changeButtonBackground()
            }
        }

//        mViewModel.editUserInfoLiveData.observeState(this){
//            onDataEmpty2={
//                dismiss()
//            }
//        }
    }

    private fun initClick(){

        initEditChange(mDataBinding!!.tvUserName) {
            changeButtonBackground()
        }

        setOnClickNoRepeat(
            mDataBinding!!.llNv,
            mDataBinding!!.llNan,
            mDataBinding!!.llAddress,
            mDataBinding!!.llYear,
            mDataBinding!!.tvSave) {
            when (it) {
                mDataBinding!!.rivHead ->{//修改头像
                    MediaManager.selectSinglePhoto(
                        mActivity,
                        GlideEngine.createGlideEngine(),
                        object : OnResultCallbackListener<LocalMedia> {
                            @RequiresApi(Build.VERSION_CODES.N)
                            @SuppressLint("NotifyDataSetChanged")
                            override fun onResult(result: ArrayList<LocalMedia>?) {
                                if (result != null) {
                                    val localMedia = result[0]
                                    val path = MediaManager.getSinglePhotoUri(localMedia) ?: ""

                                    GlideImgManager.get().loadImg(path,mDataBinding!!.rivHead,com.comm.img_load.R.mipmap.default_icon)
                                    mActivity!!.showLoadingExt()
                                    mViewModel.uploadImage(File(path))
                                }
                            }

                            override fun onCancel() {
                            }
                        }
                    )
                }
                mDataBinding!!.llAddress ->{//选择城市
                    if(mAddressList==null) {
                        toastSuccess("网络不佳，请退出重试")
                        return@setOnClickNoRepeat
                    }

                    ChooseCityDialog(mAddressList!!, callback = {
                        onSure = {s1,s2,s3 ->
                            mProvinceName = s1
                            mCityName = s2
                            mAreaName = s3
                            mDataBinding!!.tvAddress.text = "${s1} ${s2} ${s3}"
                            if (s2.contains(s1)){
                                mDataBinding!!.tvAddress.text = "${s1} ${s3}"
                            }
                        }
                    }).show(mActivity!!.supportFragmentManager,"CommDialog")

//                    ChooseCityUtils.showCityPickerView(context,"",mAddressList!!){
//                            province,city,area ->
//                        mProvinceName=province.label
//                        mCityName=city.label
//                        mAreaName=area.label
//                        mDataBinding!!.tvAddress.text = "${mProvinceName} ${mCityName} ${mAreaName}"
//                    }
                }

                mDataBinding!!.llNv -> {//性别
                    mDataBinding!!.llNv.isSelected = true
                    mDataBinding!!.ivNv.isSelected = true
                    mDataBinding!!.tvSexNv.isSelected = true

                    mDataBinding!!.llNan.isSelected = false
                    mDataBinding!!.ivNan.isSelected = false
                    mDataBinding!!.tvSexNan.isSelected = false

                }
                mDataBinding!!.llNan -> {//性别

                    mDataBinding!!.llNv.isSelected = false
                    mDataBinding!!.ivNv.isSelected = false
                    mDataBinding!!.tvSexNv.isSelected = false

                    mDataBinding!!.llNan.isSelected = true
                    mDataBinding!!.ivNan.isSelected = true
                    mDataBinding!!.tvSexNan.isSelected = true

                }
                mDataBinding!!.llYear->{//年级
                    mActivity!!.showLoadingExt()
                    mViewModel.getAllGrade()
                }

                mDataBinding!!.tvSave->{
                    var name=mDataBinding!!.tvUserName.text.toString().trim()
                    var sex = if (mDataBinding!!.llNan.isSelected ) "男" else "女"
                    var school=mDataBinding!!.tvSchool.text.toString().trim()
                    var grade=mDataBinding!!.tvGrade.text.toString().trim()

                    if(TextUtils.isEmpty(mHeadImg)){
                        toastSuccess("请上传头像")
                        return@setOnClickNoRepeat
                    }

//                    if(TextUtils.isEmpty(mCityName)){
//                        toastSuccess("请选择地区")
//                        return@setOnClickNoRepeat
//                    }

                    if(TextUtils.isEmpty(name)){
                        toastSuccess("请填写真实姓名")
                        return@setOnClickNoRepeat
                    }
                    if (name.length > 6){
                        toastSuccess("姓名最多6个字")
                        return@setOnClickNoRepeat
                    }

//                    if(TextUtils.isEmpty(sex)){
//                        toastSuccess("请选择性别")
//                        return@setOnClickNoRepeat
//                    }
//                    if(TextUtils.isEmpty(school)){
//                        toastSuccess("请填写学校")
//                        return@setOnClickNoRepeat
//                    }

//                    if(TextUtils.isEmpty(grade)){
//                        toastSuccess("请选择年级")
//                        return@setOnClickNoRepeat
//                    }

                    mActivity!!.showLoadingExt()
                    val  map = HashMap<String, Any>()
                    map["headimg"] = mHeadImg
                    map["province_name"] = mProvinceName
                    map["city_name"] = mCityName
                    map["area_name"] = mAreaName
                    map["truename"] = name
                    map["sex"] = sex
                    map["nickname"] = name
                    map["school_name"] = school
                    map["grade_name"] = grade

                    mViewModel.editUserInfo(map)
                }
            }
        }
    }

    private fun setData(){
        mHeadImg=data.headimg
        GlideImgManager.get().loadImg(mHeadImg,mDataBinding!!.rivHead,com.comm.img_load.R.mipmap.default_icon)
        mDataBinding!!.tvTopName.text=" ${data.account}"
        mDataBinding!!.tvTopName.text=data.nickname
        mDataBinding!!.tvUserName.setText(data.truename)
        mDataBinding!!.tvSchool.setText(data.school_name)
        mDataBinding!!.tvGrade.setText(data.grade_name)
        mDataBinding!!.tvAddress.text = "${data.province_name} ${data.city_name} ${data.area_name}"
        if (data.city_name.contains(data.province_name)){
            mDataBinding!!.tvAddress.text = "${data.province_name} ${data.area_name}"
        }
        changeButtonBackground()
    }

    private fun changeButtonBackground(){
        var editTips= editChange(mDataBinding!!.tvUserName)

        mDataBinding!!.tvSave.setBackgroundResource(if(editTips&&!TextUtils.isEmpty(mHeadImg)) R.drawable.bg_default22_click else R.drawable.shape_ffd8b0_23)
    }

    override fun showWindowGravity(): Int {
        return Gravity.CENTER
    }

    override fun getLayoutId(): Int {
        return R.layout.pop_edit_file
    }

}