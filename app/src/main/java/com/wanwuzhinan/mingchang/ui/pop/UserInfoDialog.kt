package com.wanwuzhinan.mingchang.ui.pop

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.colin.library.android.utils.Constants
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.ToastUtil
import com.colin.library.android.utils.ext.onClick
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.ssm.comm.media.MediaManager
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppDialogFragment
import com.wanwuzhinan.mingchang.databinding.DialogUserInfoBinding
import com.wanwuzhinan.mingchang.entity.CityInfo
import com.wanwuzhinan.mingchang.entity.GradeInfo
import com.wanwuzhinan.mingchang.entity.HTTP_CONFIRM
import com.wanwuzhinan.mingchang.entity.HTTP_SUCCESS
import com.wanwuzhinan.mingchang.entity.UserInfo
import com.wanwuzhinan.mingchang.utils.load
import com.wanwuzhinan.mingchang.view.GlideEngine
import com.wanwuzhinan.mingchang.vm.UserInfoViewModel
import kotlinx.coroutines.launch
import java.io.File

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-16 16:39
 *
 * Des   :UserInfoDialog
 */
class UserInfoDialog private constructor(
    private var user: UserInfo
) : AppDialogFragment<DialogUserInfoBinding>() {
    private var image: String? = null
    private var provinceName: String? = null
    private var cityName: String? = null
    private var areaName: String? = null
    private var updateUser = false
    var success: ((Boolean) -> Unit) = { false }

    private val viewModel by lazy {
        ViewModelProvider.create(requireActivity())[UserInfoViewModel::class.java]
    }

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {

        viewBinding.apply {
            etNick.doAfterTextChanged { updateButton() }
            etSchool.doAfterTextChanged { updateButton() }
            onClick(
                ivAvatar, llNv, llNan, tvAddress, tvGrade, tvSave
            ) {
                when (it) {
                    ivAvatar -> {
                        chooseAvatar()
                    }

                    llNan -> {
                        selectSex(getString(R.string.setting_man), true)
                    }

                    llNv -> {
                        selectSex(getString(R.string.setting_women), true)
                    }

                    tvAddress -> {
                        chooseArea()
                    }

                    tvGrade -> {
                        chooseGrade()
                    }

                    tvSave -> {
                        updateUserInfo()
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.showToast.collect {
                    if (it.code != HTTP_CONFIRM) {
                        ToastUtil.show(it.msg)
                    }
                }
                viewModel.httpAction.collect {
                    if (updateUser) {
                        success.invoke(it.code == HTTP_SUCCESS)
                        dismiss()
                    }
                }
            }
        }
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewModel.apply {
            cityInfo.observe {
                Log.e("cityInfo:$it")
                chooseArea(it)
            }
            gradeInfo.observe {
                Log.e("gradeInfo:$it")
                chooseGrade(it)
            }
            updateFile.observe {
                Log.i("updateFile:$it")
                image = it
                viewBinding.ivAvatar.load(it, R.mipmap.default_icon)
                updateButton()
            }

            showLoading.observe {
                showLoading(it)
            }
        }
        updateUserInfo(user)
    }

    private fun selectSex(sex: String?, update: Boolean = false) {
        viewBinding.llSex.tag = sex
        if (sex == getString(R.string.man) || sex == getString(R.string.setting_man)) {
            viewBinding.llNan.isSelected = true
            viewBinding.tvSexNan.isSelected = true
            viewBinding.ivNan.isSelected = true
            viewBinding.llNv.isSelected = false
            viewBinding.tvSexNv.isSelected = false
            viewBinding.ivNv.isSelected = false
        } else if (sex == getString(R.string.woman) || sex == getString(R.string.setting_women)) {
            viewBinding.llNan.isSelected = false
            viewBinding.tvSexNan.isSelected = false
            viewBinding.ivNan.isSelected = false
            viewBinding.llNv.isSelected = true
            viewBinding.tvSexNv.isSelected = true
            viewBinding.ivNv.isSelected = true
        } else {
            viewBinding.llNan.isSelected = false
            viewBinding.tvSexNan.isSelected = false
            viewBinding.ivNan.isSelected = false
            viewBinding.llNv.isSelected = false
            viewBinding.tvSexNv.isSelected = false
            viewBinding.ivNv.isSelected = false
        }
        if (update) updateButton()
    }

    private fun updateButton() {
        viewBinding.apply {
            if (image != user.headimg) {
                viewBinding.tvSave.isSelected = true
                return@apply
            }
            val nick = etNick.text.toString().trim()
            if (user.truename != nick) {
                viewBinding.tvSave.isSelected = true
                return@apply
            }
            val sex = llSex.tag as String
            if (sex != user.sex) {
                viewBinding.tvSave.isSelected = true
                return@apply
            }
            val grade = tvGrade.text.toString().trim()
            if (grade != user.grade_name) {
                viewBinding.tvSave.isSelected = true
                return@apply
            }
            val school = etSchool.text.toString().trim()
            if (school != user.school_name) {
                viewBinding.tvSave.isSelected = true
                return@apply
            }
            val area = getArea(user.province_name, user.city_name, user.area_name)
            if (area != tvAddress.text.toString().trim()) {
                viewBinding.tvSave.isSelected = true
                return@apply
            }
            viewBinding.tvSave.isSelected = false

        }
    }

    private fun chooseAvatar() {
        MediaManager.selectSinglePhoto(
            requireActivity(),
            GlideEngine.createGlideEngine(),
            object : OnResultCallbackListener<LocalMedia> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResult(result: ArrayList<LocalMedia>?) {
                    if (result != null) {
                        val localMedia = result[0]
                        val path = MediaManager.getSinglePhotoUri(localMedia) ?: ""
                        viewModel.uploadImage(File(path))
                    }
                }

                override fun onCancel() {
                }
            })
    }

    private fun chooseArea() {
        val info = viewModel.getCityInfoValue()
        if (info == null || info.list.isEmpty()) {
            viewModel.getCityInfo()
        } else chooseArea(info)
    }

    @SuppressLint("SetTextI18n")
    private fun chooseArea(cityInfo: CityInfo, info: UserInfo? = viewModel.getUserInfoValue()) {
        ChooseAreaDialog.newInstance(
            cityInfo.list, info?.province_name, info?.city_name, info?.area_name
        ).apply {
            choose = { province, city, area ->
                chooseArea(province, city, area, true)
            }
        }.show(this@UserInfoDialog)
    }

    private fun chooseArea(province: String, city: String, area: String, update: Boolean = false) {
        provinceName = province
        cityName = city
        areaName = area
        viewBinding.tvAddress.text = getArea(province, city, area)
        if (update) updateButton()
    }

    private fun getArea(province: String, city: String, area: String): String {
        return if (city.contains(province)) "$province $area" else "$province $city $area"
    }

    private fun chooseGrade() {
        val info = viewModel.getGradeInfoValue()
        if (info == null || info.listArr.isEmpty()) {
            viewModel.getGradeInfo()
        } else chooseGrade(info)
    }

    private fun chooseGrade(info: GradeInfo) {
        ChooseDialog.newInstance(getString(R.string.choose_title_grade), info.listArr).apply {
            choose = {
                if (it > Constants.INVALID) {
                    this@UserInfoDialog.viewBinding.tvGrade.text = info.listArr[it]
                    updateButton()
                }
            }
        }.show(this)
    }

    private fun updateUserInfo(mInfo: UserInfo) {
        viewBinding.apply {
            viewBinding.ivAvatar.load(mInfo.headimg, R.mipmap.default_icon)
            tvTopName.text = getString(R.string.setting_account, mInfo.account)
            selectSex(mInfo.sex, false)
            etNick.setText(mInfo.truename)
            chooseArea(mInfo.province_name, mInfo.city_name, mInfo.area_name, false)
            etSchool.setText(mInfo.school_name)
            tvGrade.text = mInfo.grade_name
        }
    }


    private fun updateUserInfo() {
        var name = viewBinding.etNick.text.toString().trim()
        var sex = viewBinding.llSex.tag as? String ?: ""
        var school = viewBinding.etSchool.text.toString().trim()
        var grade = viewBinding.tvGrade.text.toString().trim()
        if (TextUtils.isEmpty(name)) {
            ToastUtil.show("请填写真实姓名")
            return
        }
        if (name.length > 6) {
            ToastUtil.show("姓名最多6个字")
            return
        }

        val map = HashMap<String, String>()
        map["headimg"] = image ?: ""
        map["province_name"] = provinceName ?: ""
        map["city_name"] = cityName ?: ""
        map["area_name"] = areaName ?: ""
        map["truename"] = name
        map["sex"] = sex
        map["nickname"] = name
        map["school_name"] = school
        map["grade_name"] = grade
        updateUser = true
        viewModel.editUserInfo(map)
    }


    companion object {
        @JvmStatic
        fun newInstance(user: UserInfo): UserInfoDialog {
            val fragment = UserInfoDialog(user)
            fragment.gravity = Gravity.CENTER
            fragment.isCancelable = false
            return fragment
        }
    }
}