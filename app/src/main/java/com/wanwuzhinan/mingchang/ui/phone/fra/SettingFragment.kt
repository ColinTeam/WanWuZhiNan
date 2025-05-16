package com.wanwuzhinan.mingchang.ui.phone.fra


import android.annotation.SuppressLint
import android.text.TextUtils
import com.colin.library.android.image.glide.GlideImgManager
import com.colin.library.android.utils.ext.onClick
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.ssm.comm.config.Constant
import com.ssm.comm.event.MessageEvent
import com.ssm.comm.ext.editChange
import com.ssm.comm.ext.initEditChange
import com.ssm.comm.ext.observeState
import com.ssm.comm.ext.post
import com.ssm.comm.ext.showLoadingExt
import com.ssm.comm.ext.toastSuccess
import com.ssm.comm.media.MediaManager
import com.ssm.comm.ui.base.BaseFragment
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.databinding.FragmentEditFileBinding
import com.wanwuzhinan.mingchang.entity.Children
import com.wanwuzhinan.mingchang.entity.GradeInfo
import com.wanwuzhinan.mingchang.entity.UserInfo
import com.wanwuzhinan.mingchang.ui.PasswordActivity
import com.wanwuzhinan.mingchang.ui.pop.ChooseAreaDialog
import com.wanwuzhinan.mingchang.ui.pop.ChooseDialog
import com.wanwuzhinan.mingchang.utils.MMKVUtils
import com.wanwuzhinan.mingchang.view.GlideEngine
import com.wanwuzhinan.mingchang.vm.UserViewModel
import java.io.File

//
class SettingFragment : BaseFragment<FragmentEditFileBinding, UserViewModel>(UserViewModel()) {
    var mAddressList: List<Children>? = null
    var mProvinceName = ""
    var mCityName = ""
    var mAreaName = ""
    var mHeadImg = ""

    lateinit var mInfo: UserInfo

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

    @SuppressLint("SetTextI18n")
    override fun initRequest() {
        mViewModel.getUserInfoLiveData.observeState(this) {
            onSuccess = { data, msg ->
                mInfo = data!!
                mProvinceName = mInfo.province_name
                mCityName = mInfo.city_name
                mAreaName = mInfo.area_name
                mHeadImg = mInfo.headimg
                GlideImgManager.get()
                    .loadImg(mHeadImg, mDataBinding.ivAvatar, R.mipmap.default_icon)
                mDataBinding.tvPhone.text = getString(R.string.setting_account, mInfo.account)
                mDataBinding.tvNickLabel.text = mInfo.truename
                mDataBinding.etSchool.setText(mInfo.school_name)
                mDataBinding.tvArea.text =
                    "${mInfo.province_name} ${mInfo.city_name} ${mInfo.area_name}"
                if (mInfo.city_name.contains(mInfo.province_name)) {
                    mDataBinding.tvArea.text = "${mInfo.province_name} ${mInfo.area_name}"
                }
                mDataBinding.tvGrade.text = mInfo.grade_name
                selectSex(mInfo.sex)
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
        initEditChange(mDataBinding.etNick) {
            changeButtonBackground()
        }
        onClick(
            mDataBinding.ivAvatar,
            mDataBinding.tvMan,
            mDataBinding.tvWoman,
            mDataBinding.tvArea,
            mDataBinding.tvGrade,
            mDataBinding.tvPassword,
            mDataBinding.btSave
        ) {
            when (it) {
                mDataBinding.ivAvatar -> {//修改头像
                    MediaManager.selectSinglePhoto(
                        mActivity,
                        GlideEngine.createGlideEngine(),
                        object : OnResultCallbackListener<LocalMedia> {
                            override fun onResult(result: ArrayList<LocalMedia>?) {
                                if (result != null) {
                                    val localMedia = result[0]
                                    val path = MediaManager.getSinglePhotoUri(localMedia) ?: ""

                                    GlideImgManager.get()
                                        .loadImg(path, mDataBinding.ivAvatar, R.mipmap.default_icon)
                                    mActivity.showLoadingExt()
                                    mViewModel.uploadImage(File(path))
                                }
                            }

                            override fun onCancel() {
                            }
                        })
                }

                mDataBinding.tvArea -> {//选择城市
                    if (mAddressList == null) {
                        toastSuccess("网络不佳，请退出重试")
                        return@onClick
                    }
                    ChooseAreaDialog.newInstance(
                        mAddressList!!, mProvinceName, mCityName, mAreaName
                    ).apply {
                        choose = { province, city, area ->
                            mProvinceName = province
                            mCityName = city
                            mAreaName = area
                            mDataBinding.tvArea.text = "$province $city $area"
                            if (city.contains(province)) {
                                mDataBinding.tvArea.text = "$province $area"
                            }
                        }
                    }.show(this@SettingFragment)
                }

                mDataBinding.tvWoman -> {//性别
                    selectSex(getString(R.string.setting_women))

                }

                mDataBinding.tvMan -> {//性别
                    selectSex(getString(R.string.setting_man))
                }

                mDataBinding.tvGrade -> {//年级
                    mActivity.showLoadingExt()
                    mViewModel.getAllGrade()
                }

                mDataBinding.tvPassword -> {//年级
                    PasswordActivity.start(
                        requireActivity(), MMKVUtils.getString(Constant.USER_MOBILE)
                    )
                }

                mDataBinding.btSave -> {
                    var name = mDataBinding.etNick.text.toString().trim()
                    var sex =
                        if (mDataBinding.tvWoman.isSelected) getString(R.string.setting_man) else getString(
                            R.string.setting_women
                        )
                    var school = mDataBinding.etSchool.text.toString().trim()
                    var grade = mDataBinding.tvGrade.text.toString().trim()

                    if (TextUtils.isEmpty(mHeadImg)) {
                        toastSuccess("请上传头像")
                        return@onClick
                    }

                    if (TextUtils.isEmpty(name)) {
                        toastSuccess("请填写真实姓名")
                        return@onClick
                    }
                    if (name.length > 6) {
                        toastSuccess("姓名最多6个字")
                        return@onClick
                    }

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
        var editTips = editChange(mDataBinding.etNick)
        mDataBinding.btSave.setBackgroundResource(if (editTips && !TextUtils.isEmpty(mHeadImg)) R.drawable.bg_default22_click else R.drawable.shape_ffd8b0_23)
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

    fun showGradeDialog(data: GradeInfo?) {
        val list = data?.listArr ?: return
        ChooseDialog.newInstance(getString(R.string.choose_title_grade), list).apply {
            choose = {
                mDataBinding.tvGrade.text = list[it]
            }
        }.show(this)
    }

    private fun selectSex(sex: String?) {
        if (sex == getString(R.string.man) || sex == getString(R.string.setting_man)) {
            mDataBinding.tvMan.isSelected = true
            mDataBinding.ivMan.isSelected = true
            mDataBinding.tvWoman.isSelected = false
            mDataBinding.ivWoman.isSelected = false
        } else if (sex == getString(R.string.woman) || sex == getString(R.string.setting_women)) {
            mDataBinding.tvMan.isSelected = false
            mDataBinding.ivMan.isSelected = false
            mDataBinding.tvWoman.isSelected = true
            mDataBinding.ivWoman.isSelected = true
        } else {
            mDataBinding.tvMan.isSelected = false
            mDataBinding.ivMan.isSelected = false
            mDataBinding.tvWoman.isSelected = false
            mDataBinding.ivWoman.isSelected = false
        }
    }

}