package com.wanwuzhinan.mingchang.ui.pop

import android.graphics.Color
import androidx.core.graphics.toColorInt
import com.colin.library.android.utils.ext.onClick
import com.colin.library.android.widget.picker.adapter.ArrayWheelAdapter
import com.colin.library.android.widget.picker.wheel.WheelView
import com.ssm.comm.ui.base.BaseDialogFragment
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.databinding.PopSexBinding


class ChooseGradeDialog constructor(
    private var list: List<String>, callback: ViewClickCallBack.() -> Unit
) : BaseDialogFragment<PopSexBinding>() {

    private var gender = ""
    private var callback = ViewClickCallBack().also(callback)


    override fun initViews() {
        isCancelable = true
        mDataBinding!!.tvTitle.text = "选择年级"

        onClick(mDataBinding!!.tvCancel, mDataBinding!!.tvSure) {

            when (it) {
                mDataBinding!!.tvSure -> {
                    callback.onSure(gender)
                    dismiss()
                }

                mDataBinding!!.tvCancel -> {
                    dismiss()
                }
            }
        }
        setWheel(mDataBinding!!.wheelSex)
        initWheel()
    }

    private fun initWheel() {
        val sexAdapter = ArrayWheelAdapter(list)
        gender = list[0]
        mDataBinding!!.wheelSex.setCurrentItem(0)
        mDataBinding!!.wheelSex.setAdapter(sexAdapter)
        mDataBinding!!.wheelSex.setOnItemSelectedListener {
            gender = list.get(it)
        }
    }

    private fun setWheel(view: WheelView) {
        view.setItemsVisibleCount(5)
        view.setCyclic(false)
        view.setTextColorCenter(Color.BLACK)
        view.setTextColorOut("#bfbfbf".toColorInt())
        view.setDividerType(WheelView.DividerType.FILL)
        view.setDividerColor("#00ffffff".toColorInt())
        view.setLineSpacingMultiplier(2f)
    }

    class ViewClickCallBack {
        var onSure: (sex: String) -> Unit = {}
    }

    override fun getLayoutId(): Int {
        return R.layout.pop_sex
    }
}