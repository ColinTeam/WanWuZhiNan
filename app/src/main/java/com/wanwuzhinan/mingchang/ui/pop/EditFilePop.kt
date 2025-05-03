package com.wanwuzhinan.mingchang.ui.pop

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import com.colin.library.android.image.glide.GlideImgManager
import com.colin.library.android.utils.ext.onClick
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.ssm.comm.ext.dismissLoadingExt
import com.ssm.comm.ext.showLoadingExt
import com.ssm.comm.ext.toastSuccess
import com.ssm.comm.media.MediaManager
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.ProvinceListData
import com.wanwuzhinan.mingchang.databinding.PopEditFileBinding
import com.wanwuzhinan.mingchang.entity.UserInfoData
import com.wanwuzhinan.mingchang.view.GlideEngine
import com.wanwuzhinan.mingchang.vm.UserViewModel
import java.io.File

//编辑用户资料
class EditFilePop(var context: Activity) :BasePop<PopEditFileBinding>(context){

    lateinit var mViewModel: UserViewModel
    var mAddressList:List<ProvinceListData>?=null
    var mHeadImg=""
    var mProvinceName=""
    var mCityName=""
    var mAreaName=""

    override fun initClick() {
        mViewModel= UserViewModel()
        initRequest()

        mDataBinding.tvUserName.addTextChangedListener {
            if(it.isNullOrEmpty()){
                mDataBinding.tvTopName.text = ""
            }else{
                mDataBinding.tvTopName.text = it
            }
        }
        onClick(mDataBinding.llNv,/*mDataBinding.linArea,*/mDataBinding.tvSave) {
            when (it) {
                mDataBinding.rivHead ->{//修改头像
                    MediaManager.selectSinglePhoto(
                        mContext,
                        GlideEngine.createGlideEngine(),
                        object : OnResultCallbackListener<LocalMedia> {
                            @RequiresApi(Build.VERSION_CODES.N)
                            @SuppressLint("NotifyDataSetChanged")
                            override fun onResult(result: ArrayList<LocalMedia>?) {
                                if (result != null) {
                                    val localMedia = result[0]
                                    val path = MediaManager.getSinglePhotoUri(localMedia) ?: ""

                                    GlideImgManager.get().loadImg(path,mDataBinding.rivHead,R.drawable.img_default_icon)
                                    context.showLoadingExt()
                                    mViewModel.uploadImage(File(path))
                                }
                            }

                            override fun onCancel() {
                            }
                        }
                    )
                }

                /*mDataBinding.linArea ->{//选择城市
                    if(mAddressList==null) {
                        toastSuccess("网络不佳，请退出重试")
                        return@onClick
                    }
                    ChooseCityUtils.showCityPickerView(mContext,"",mAddressList!!){
                            province,city,area ->
                        mProvinceName=province.label
                        mCityName=city.label
                        mAreaName=area.label
                        mDataBinding.tvArea.text = "${mProvinceName} ${mCityName} ${mAreaName}"
                    }
                }
                mDataBinding.tvSave->{
                    var name=mDataBinding.tvUserName.text.toString()
                    var sex=mDataBinding.tvSex.text.toString()
                    var city=mDataBinding.tvArea.text.toString()
                    var school=mDataBinding.tvSchool.text.toString()
                    var grade=mDataBinding.tvGrade.text.toString()

                    if(TextUtils.isEmpty(mHeadImg)){
                        toastSuccess("请上传头像")
                        return@onClick
                    }

                    if(TextUtils.isEmpty(name)){
                        toastSuccess("请填写真实姓名")
                        return@onClick
                    }

                    if(TextUtils.isEmpty(sex)){
                        toastSuccess("请选择性别")
                        return@onClick
                    }

                    if(TextUtils.isEmpty(city)){
                        toastSuccess("请选择地区")
                        return@onClick
                    }

                    if(TextUtils.isEmpty(school)){
                        toastSuccess("请填写学校")
                        return@onClick
                    }

                    if(TextUtils.isEmpty(grade)){
                        toastSuccess("请选择年纪")
                        return@onClick
                    }

                    context.showLoadingExt()
                    val  map = HashMap<String, Any>()
                    map["headimg"] = mHeadImg
                    map["truename"] = name
                    map["sex"] = sex
                    map["province_name"] = mProvinceName
                    map["city_name"] = mCityName
                    map["area_name"] = mAreaName
                    map["school_name"] = school
                    map["grade_name"] = grade

                    mViewModel.editUserInfo(map)
                }*/
            }
        }

        mViewModel.getAllRegion()
    }

    private fun initRequest(){
        mViewModel.allProvinceLiveData.observeForever{
            if(context.isFinishing||context.isDestroyed) return@observeForever
            mAddressList=it!!.data!!.list
        }
        mViewModel.uploadImgLiveData.observeForever {
            if(context.isFinishing||context.isDestroyed) return@observeForever
            dismissLoadingExt()

            if(it.data==null){
                toastSuccess(it.msg)
            }else{
                mHeadImg=it!!.data!!.file
            }
        }
        mViewModel.editUserInfoLiveData.observeForever {
            if(context.isFinishing||context.isDestroyed) return@observeForever
            dismissLoadingExt()
            dismiss()
        }
    }

    fun showPop(data:UserInfoData.infoBean) {
        mHeadImg=data.headimg
        GlideImgManager.get().loadImg(mHeadImg,mDataBinding.rivHead,R.drawable.img_default_icon)
        mDataBinding.tvTopName.text=data.truename
        mDataBinding.tvUserName.setText(data.truename)
      //  mDataBinding.tvArea.text="${data.province_name}${data.city_name}${data.area_name}"
        mDataBinding.tvSchool.setText(data.school_name)
        mDataBinding.tvGrade.setText(data.grade_name)

        showAllPop()
    }

    override fun getLayoutID(): Int {
        return R.layout.pop_edit_file
    }
}