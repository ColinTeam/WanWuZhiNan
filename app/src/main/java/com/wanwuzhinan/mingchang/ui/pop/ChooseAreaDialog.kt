package com.wanwuzhinan.mingchang.ui.pop

import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.colin.library.android.utils.Constants
import com.colin.library.android.utils.ext.onClick
import com.colin.library.android.widget.wheel.ArrayWheelAdapter
import com.colin.library.android.widget.wheel.TextFormatter
import com.colin.library.android.widget.wheel.WheelView
import com.wanwuzhinan.mingchang.app.AppDialogFragment
import com.wanwuzhinan.mingchang.data.ProvinceListData
import com.wanwuzhinan.mingchang.databinding.DialogChooseAreaBinding

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-03 09:22
 *
 * Des   :ChooseAreaDialog
 */
class ChooseAreaDialog private constructor(
    private val array: List<ProvinceListData>,
    private val province: String? = null,
    private val city: String? = null,
    private val area: String? = null
) : AppDialogFragment<DialogChooseAreaBinding>() {

    var sure: ((String, String, String) -> Unit) = { _, _, _ -> Unit }
    var cancel: ((View) -> Unit) = { }
    var provinceIndex = -1
    var cityIndex = -1
    var areaIndex = -1

    companion object {
        const val TYPE_SEX = 0
        const val TYPE_GRADE = 1
        const val EXTRAS_TITLE = "EXTRAS_TITLE"

        @JvmStatic
        fun newInstance(
            array: List<ProvinceListData>, province: String?, city: String?, area: String?
        ): ChooseAreaDialog {
            val fragment = ChooseAreaDialog(array, province, city, area)
            fragment.height = 0.75f
            fragment.gravity = Gravity.BOTTOM
            return fragment
        }
    }

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewBinding.apply {
            provinceIndex = if (provinceIndex in 0 until array.size) provinceIndex else 0

            wheelProvince.setOnItemSelectedListener {
                provinceIndex = it
                updateCity()
            }
            wheelCity.setOnItemSelectedListener {
                cityIndex = it
                updateArea()
            }
            wheelArea.setOnItemSelectedListener {
                areaIndex = it
            }
        }


        onClick(viewBinding.textCancel, viewBinding.textSure) {
            when (it) {
                viewBinding.textCancel -> {
                    cancel.invoke(it)
                    dismiss()
                }

                viewBinding.textSure -> {
                    sure.invoke(
                        array[provinceIndex].label,
                        array[provinceIndex].children[cityIndex].label,
                        array[provinceIndex].children[cityIndex].children[areaIndex].label
                    )
                    dismiss()
                }
            }
        }
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        updateProvince()
    }

    private fun updateProvince() {
        if (provinceIndex == Constants.INVALID) {
            for ((index, data) in array.withIndex()) {
                if (data.label == province) {
                    provinceIndex = index
                    break
                }
            }
        }
        provinceIndex = if (provinceIndex in 0 until array.size) provinceIndex else 0
        initWheel(viewBinding.wheelProvince, array, provinceIndex)
        updateCity()
    }

    private fun updateCity() {
        val cityList = array[provinceIndex].children
        if (cityIndex == Constants.INVALID) {
            for ((index, data) in cityList.withIndex()) {
                if (data.label == city) {
                    cityIndex = index
                    break
                }
            }
        }
        cityIndex = if (cityIndex in 0 until cityList.size) cityIndex else 0
        initWheel(viewBinding.wheelCity, cityList, cityIndex)
        updateArea()
    }

    private fun updateArea() {
        val areaList = array[provinceIndex].children[cityIndex].children
        if (areaIndex == Constants.INVALID) {
            for ((index, data) in areaList.withIndex()) {
                if (data.label == area) {
                    areaIndex = index
                    break
                }
            }
        }
        areaIndex = if (areaIndex in 0 until areaList.size) areaIndex else 0
        initWheel(viewBinding.wheelArea, areaList, areaIndex)
    }

    private fun initWheel(view: WheelView, data: List<ProvinceListData>, selected: Int) {
        view.setAdapter(ArrayWheelAdapter(data))
        view.setTextFormatter(FormatArea())
        view.setSelectedPosition(selected, true)
    }


    private class FormatArea() : TextFormatter {
        override fun formatText(item: Any?): String {
            return if (item is ProvinceListData) item.label else "-"
        }

    }
}