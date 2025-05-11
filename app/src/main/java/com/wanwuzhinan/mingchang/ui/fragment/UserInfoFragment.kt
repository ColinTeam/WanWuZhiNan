package com.wanwuzhinan.mingchang.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.colin.library.android.utils.Constants
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.ToastUtil
import com.colin.library.android.utils.ext.onClick
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppFragment
import com.wanwuzhinan.mingchang.databinding.FragmentEditFileBinding
import com.wanwuzhinan.mingchang.entity.CityInfo
import com.wanwuzhinan.mingchang.entity.GradeInfo
import com.wanwuzhinan.mingchang.entity.HTTP_SUCCESS
import com.wanwuzhinan.mingchang.entity.UserInfo
import com.wanwuzhinan.mingchang.ui.pop.ChooseAreaDialog
import com.wanwuzhinan.mingchang.ui.pop.ChooseDialog
import com.wanwuzhinan.mingchang.utils.load
import com.wanwuzhinan.mingchang.vm.UserInfoViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-11 10:09
 *
 * Des   :UserInfoFragment
 */
class UserInfoFragment : AppFragment<FragmentEditFileBinding, UserInfoViewModel>() {
    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewBinding.apply {
            etNick.doAfterTextChanged { updateButton() }
            onClick(
                ivAvatar, tvMan, tvWoman, tvArea, tvGrade, tvPassword, btSave
            ) {
                when (it) {
                    ivAvatar -> {
                        chooseAvatar()
                    }

                    tvMan -> {
                        selectSex(getString(R.string.setting_man), true)
                    }

                    tvWoman -> {
                        selectSex(getString(R.string.setting_women), true)
                    }

                    tvArea -> {
                        chooseArea()
                    }

                    tvGrade -> {
                        chooseGrade()
                    }

                    tvSchool -> {
                        chooseSchool()
                    }

                    tvPassword -> {
                        findNavController().navigate(R.id.action_toPassword)
                    }

                    btSave -> {
                        updateUserInfo()
                    }
                }
            }
        }
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewModel.apply {
            userInfo.observe {
                Log.e("userInfo:$it")
                updateUserInfo(it)
                updateButton(it)
            }
            cityInfo.observe {
                Log.e("cityInfo:$it")
                chooseArea(it)
            }
            gradeInfo.observe {
                Log.e("gradeInfo:$it")
                chooseGrade(it)
            }
            showToast.observe {
                Log.e("showToast:$it")
                if (it.code == HTTP_SUCCESS) {
                    viewModel.getUserInfo()
                }
            }
        }
        viewModel.getUserInfo()
    }

    private fun selectSex(sex: String?, update: Boolean = false) {
        viewBinding.tvSex.tag = sex
        if (sex == getString(R.string.man) || sex == getString(R.string.setting_man)) {
            viewBinding.tvMan.isSelected = true
            viewBinding.ivMan.isSelected = true
            viewBinding.tvWoman.isSelected = false
            viewBinding.ivWoman.isSelected = false
        } else if (sex == getString(R.string.woman) || sex == getString(R.string.setting_women)) {
            viewBinding.tvMan.isSelected = false
            viewBinding.ivMan.isSelected = false
            viewBinding.tvWoman.isSelected = true
            viewBinding.ivWoman.isSelected = true
        } else {
            viewBinding.tvMan.isSelected = false
            viewBinding.ivMan.isSelected = false
            viewBinding.tvWoman.isSelected = false
            viewBinding.ivWoman.isSelected = false
        }
        if (update) updateButton()
    }

    private fun updateButton(info: UserInfo? = viewModel.getUserInfoValue()) {
        if (info == null) {
            viewBinding.btSave.isSelected = false
            return
        }
        viewBinding.apply {
            val nick = etNick.text.toString().trim()
            if (info.truename != nick) {
                viewBinding.btSave.isSelected = true
                return@apply
            }
            val sex = tvSex.tag as String
            if (sex != info.sex) {
                viewBinding.btSave.isSelected = true
                return@apply
            }
            val grade = tvGrade.text.toString().trim()
            if (grade != info.grade_name) {
                viewBinding.btSave.isSelected = true
                return@apply
            }
            val school = tvSchool.text.toString().trim()
            if (school != info.school_name) {
                viewBinding.btSave.isSelected = true
                return@apply
            }
            val area = getArea(info.province_name, info.city_name, info.area_name)
            if (area != tvArea.text.toString().trim()) {
                viewBinding.btSave.isSelected = true
                return@apply
            }
            viewBinding.btSave.isSelected = false

        }
    }

    private fun chooseAvatar() {

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
            sure = { province, city, area ->
                chooseArea(province, city, area, true)
            }
        }.show(this@UserInfoFragment)
    }

    private fun chooseArea(province: String, city: String, area: String, update: Boolean = false) {
        provinceName = province
        cityName = city
        areaName = area
        viewBinding.tvArea.text = getArea(province, city, area)
        if (update) updateButton()
    }

    private fun getArea(province: String, city: String, area: String): String {
        return if (city.contains(province)) "$province $area" else "$province $city $area"
    }

    private fun chooseSchool() {

    }

    private fun chooseGrade() {
        val info = viewModel.getGradeInfoValue()
        if (info == null || info.listArr.isEmpty()) {
            viewModel.getGradeInfo()
        } else chooseGrade(info)
    }

    private fun chooseGrade(info: GradeInfo) {
        ChooseDialog.newInstance(getString(R.string.choose_title_grade), info.listArr).apply {
            sure = {
                if (it > Constants.INVALID) {
                    this@UserInfoFragment.viewBinding.tvGrade.text = info.listArr[it]
                    updateButton()
                }
            }
        }.show(this)
    }

    private fun updateUserInfo(mInfo: UserInfo) {
        viewBinding.apply {
            ivAvatar.load(mInfo.headimg)
            tvPhone.text = getString(R.string.setting_account, mInfo.account)
            selectSex(mInfo.sex, false)
            etNick.setText(mInfo.truename)
            chooseArea(mInfo.province_name, mInfo.city_name, mInfo.area_name, false)
            tvSchool.text = mInfo.school_name
            tvGrade.text = mInfo.grade_name
        }
    }

    private var provinceName: String? = null
    private var cityName: String? = null
    private var areaName: String? = null
    private fun updateUserInfo() {
        var name = viewBinding.etNick.text.toString().trim()
        var sex = viewBinding.tvSex.tag as? String ?: ""
        var school = viewBinding.tvSchool.text.toString().trim()
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
        map["province_name"] = provinceName ?: ""
        map["city_name"] = cityName ?: ""
        map["area_name"] = areaName ?: ""
        map["truename"] = name
        map["sex"] = sex
        map["nickname"] = name
        map["school_name"] = school
        map["grade_name"] = grade
        viewModel.editUserInfo(map)
    }

    companion object {
        @JvmStatic
        fun newInstance(): UserInfoFragment {
            val fragment = UserInfoFragment()
            return fragment
        }
    }
}


