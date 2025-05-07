package com.wanwuzhinan.mingchang.ui.phone.fra


import android.annotation.SuppressLint
import android.text.TextUtils
import com.colin.library.android.image.glide.GlideImgManager
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.ssm.comm.event.MessageEvent
import com.ssm.comm.ext.editChange
import com.ssm.comm.ext.initEditChange
import com.ssm.comm.ext.observeState
import com.ssm.comm.ext.post
import com.ssm.comm.ext.setOnClickNoRepeat
import com.ssm.comm.ext.showLoadingExt
import com.ssm.comm.ext.toastSuccess
import com.ssm.comm.media.MediaManager
import com.ssm.comm.ui.base.BaseFragment
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.GradeData
import com.wanwuzhinan.mingchang.data.ProvinceListData
import com.wanwuzhinan.mingchang.databinding.FragmentEditFileBinding
import com.wanwuzhinan.mingchang.entity.UserInfoData
import com.wanwuzhinan.mingchang.ui.pop.ChooseAreaDialog
import com.wanwuzhinan.mingchang.ui.pop.ChooseDialog
import com.wanwuzhinan.mingchang.view.GlideEngine
import com.wanwuzhinan.mingchang.vm.UserViewModel
import java.io.File

//
class SettingFragment : BaseFragment<FragmentEditFileBinding, UserViewModel>(UserViewModel()) {
    private var TAG = "SettingFragment"

    var mAddressList: List<ProvinceListData>? = null
    var mProvinceName = ""
    var mCityName = ""
    var mAreaName = ""
    var mHeadImg = ""

    lateinit var mInfo: UserInfoData.InfoBean

    companion object {
        val instance: SettingFragment by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SettingFragment()
        }
    }

    override fun initView() {
        super.initView()


        mViewModel.getUserInfo()
        mViewModel.getAllRegion()
    }

    override fun initRequest() {

        mViewModel.getUserInfoLiveData.observeState(this) {
            onSuccess = { data, msg ->
                mInfo = data!!.info
                mProvinceName = mInfo.province_name
                mCityName = mInfo.city_name
                mAreaName = mInfo.area_name
                mHeadImg = mInfo.headimg
                GlideImgManager.get().loadImg(mHeadImg, mDataBinding.rivHead, R.mipmap.default_icon)
//                mDataBinding.tvIdT.text = (mInfo.id.toInt()+ getConfigData().home_all_number.toInt()).toString()
                mDataBinding.tvIdT.text = "用户账号：${maskPhoneNumber(mInfo.account)}"
                mDataBinding.tvUserName.setText(mInfo.truename)
                mDataBinding.tvSchool.setText(mInfo.school_name)
                mDataBinding.tvAddress.setText("${mInfo.province_name} ${mInfo.city_name} ${mInfo.area_name}")
                if (mInfo.city_name.contains(mInfo.province_name)) {
                    mDataBinding.tvAddress.setText("${mInfo.province_name} ${mInfo.area_name}")
                }
                mDataBinding.tvGrade.setText(mInfo.grade_name)
                if (mInfo.sex == "男") {
                    mDataBinding.llNv.isSelected = false
                    mDataBinding.ivNv.isSelected = false
                    mDataBinding.tvSexNv.isSelected = false

                    mDataBinding.llNan.isSelected = true
                    mDataBinding.ivNan.isSelected = true
                    mDataBinding.tvSexNan.isSelected = true
                } else if (mInfo.sex == "女") {
                    mDataBinding.llNv.isSelected = true
                    mDataBinding.ivNv.isSelected = true
                    mDataBinding.tvSexNv.isSelected = true

                    mDataBinding.llNan.isSelected = false
                    mDataBinding.ivNan.isSelected = false
                    mDataBinding.tvSexNan.isSelected = false
                }
                changeButtonBackground()
            }
        }
        mViewModel.getAllGradeLiveData.observeState(this) {
            onSuccess = { data, msg ->
                showGradeDialog(data)
            }

        }
        mViewModel.editUserInfoLiveData.observeState(this) {
            onSuccess = { data, msg ->
                toastSuccess("保存成功")
                post(MessageEvent.UPDATE_USERINFO, "")
            }
            onDataEmpty2 = {
                toastSuccess("保存成功")
                post(MessageEvent.UPDATE_USERINFO, "")
            }
        }
        mViewModel.allProvinceLiveData.observeState(this) {
            onSuccess = { data, msg ->
                mAddressList = data!!.list
            }

        }
        mViewModel.uploadImgLiveData.observeState(this) {
            onSuccess = { data, msg ->
                if (data == null) {
                    toastSuccess(msg)
                } else {
                    mHeadImg = data.file
                    changeButtonBackground()
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    override fun initClick() {

        initEditChange(mDataBinding.tvUserName) {
            changeButtonBackground()
        }

        setOnClickNoRepeat(
            mDataBinding.llNv,
            mDataBinding.llNan,
            mDataBinding.llAddress,
            mDataBinding.llYear,
            mDataBinding.tvSave
        ) {
            when (it) {
                mDataBinding.rivHead -> {//修改头像
                    MediaManager.selectSinglePhoto(
                        mActivity,
                        GlideEngine.createGlideEngine(),
                        object : OnResultCallbackListener<LocalMedia> {
                            override fun onResult(result: ArrayList<LocalMedia>?) {
                                if (result != null) {
                                    val localMedia = result[0]
                                    val path = MediaManager.getSinglePhotoUri(localMedia) ?: ""

                                    GlideImgManager.get()
                                        .loadImg(path, mDataBinding.rivHead, R.mipmap.default_icon)
                                    mActivity.showLoadingExt()
                                    mViewModel.uploadImage(File(path))
                                }
                            }

                            override fun onCancel() {
                            }
                        })
                }

                mDataBinding.llAddress -> {//选择城市
                    if (mAddressList == null) {
                        toastSuccess("网络不佳，请退出重试")
                        return@setOnClickNoRepeat
                    }
                    ChooseAreaDialog.newInstance(
                        mAddressList!!, mProvinceName, mCityName, mAreaName
                    ).apply {
                        sure = { province, city, area ->
                            mProvinceName = province
                            mCityName = city
                            mAreaName = area
                            mDataBinding.tvAddress.text = "$province $city $area"
                            if (city.contains(province)) {
                                mDataBinding.tvAddress.text = "$province $area"
                            }
                        }
                    }.show(this@SettingFragment)
//                    ChooseCityDialog(mAddressList!!, callback = {
//                        onSure = { s1, s2, s3 ->
//                            mProvinceName = s1
//                            mCityName = s2
//                            mAreaName = s3
//                            mDataBinding.tvAddress.text = "${s1} ${s2} ${s3}"
//                            if (s2.contains(s1)) {
//                                mDataBinding.tvAddress.text = "${s1} ${s3}"
//                            }
//                        }
//                    }).show(mActivity.supportFragmentManager, "CommDialog")

//                    ChooseCityUtils.showCityPickerView(context,"",mAddressList!!){
//                            province,city,area ->
//                        mProvinceName=province.label
//                        mCityName=city.label
//                        mAreaName=area.label
//                        mDataBinding!!.tvAddress.text = "${mProvinceName} ${mCityName} ${mAreaName}"
//                    }
                }

                mDataBinding.llNv -> {//性别
                    mDataBinding.llNv.isSelected = true
                    mDataBinding.ivNv.isSelected = true
                    mDataBinding.tvSexNv.isSelected = true

                    mDataBinding.llNan.isSelected = false
                    mDataBinding.ivNan.isSelected = false
                    mDataBinding.tvSexNan.isSelected = false

                }

                mDataBinding.llNan -> {//性别

                    mDataBinding.llNv.isSelected = false
                    mDataBinding.ivNv.isSelected = false
                    mDataBinding.tvSexNv.isSelected = false

                    mDataBinding.llNan.isSelected = true
                    mDataBinding.ivNan.isSelected = true
                    mDataBinding.tvSexNan.isSelected = true

                }

                mDataBinding.llYear -> {//年级
                    mActivity.showLoadingExt()
                    mViewModel.getAllGrade()
                }

                mDataBinding.tvSave -> {
                    var name = mDataBinding.tvUserName.text.toString().trim()
                    var sex = if (mDataBinding.llNan.isSelected) "男" else "女"
                    var school = mDataBinding.tvSchool.text.toString().trim()
                    var grade = mDataBinding.tvGrade.text.toString().trim()

                    if (TextUtils.isEmpty(mHeadImg)) {
                        toastSuccess("请上传头像")
                        return@setOnClickNoRepeat
                    }

//                    if(TextUtils.isEmpty(mCityName)){
//                        toastSuccess("请选择地区")
//                        return@setOnClickNoRepeat
//                    }

                    if (TextUtils.isEmpty(name)) {
                        toastSuccess("请填写真实姓名")
                        return@setOnClickNoRepeat
                    }
                    if (name.length > 6) {
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

                    showBaseLoading()
                    val map = HashMap<String, Any>()
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

    private fun changeButtonBackground() {
        var editTips = editChange(mDataBinding.tvUserName)

        mDataBinding.tvSave.setBackgroundResource(if (editTips && !TextUtils.isEmpty(mHeadImg)) R.drawable.bg_default22_click else R.drawable.shape_ffd8b0_23)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_edit_file
    }

    fun maskPhoneNumber(phone: String): String {
        return if (phone.length == 11) {
            val prefix = phone.substring(0, 3)
            val suffix = phone.substring(7)
            "$prefix****$suffix"
        } else {
            phone // 非 11 位手机号不处理
        }
    }


    override fun onDestroy() {
        super.onDestroy()

    }

    fun showGradeDialog(data: GradeData?) {
        val list = data?.listArr ?: return
        ChooseDialog.newInstance(getString(R.string.choose_title_grade), list).apply {
            sure = {
                mDataBinding.tvGrade.text = list[it]
            }
        }.show(this)
    }

}