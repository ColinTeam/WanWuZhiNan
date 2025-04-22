package com.wanwuzhinan.mingchang.ui.pop

import android.graphics.Color
import com.ad.img_load.pickerview.pv.adapter.ArrayWheelAdapter
import com.ad.img_load.pickerview.wheelview.view.WheelView
import com.ad.img_load.setOnClickNoRepeat
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.databinding.PopSexBinding
import com.ssm.comm.ui.base.BaseDialogFragment


class ChooseSexDialog constructor(callback: ViewClickCallBack.() ->Unit) : BaseDialogFragment<PopSexBinding>(){

    private var gender = ""
    private var callback = ViewClickCallBack().also(callback)

    override fun initViews() {
        isCancelable = true

        setOnClickNoRepeat(mDataBinding!!.tvCancel , mDataBinding!!.tvSure){

            when(it){
                mDataBinding!!.tvSure ->{
                    callback.onSure(gender)
                    dismiss()
                }
                mDataBinding!!.tvCancel ->{
                    dismiss()
                }
            }
        }
        setWheel(mDataBinding!!.wheelSex)
        initWheel()
    }

    private fun initWheel() {
        var sexList = mutableListOf<String>("男","女")
        gender=sexList.get(0)
        val sexAdapter = ArrayWheelAdapter(sexList)
        mDataBinding!!.wheelSex.currentItem=0
        mDataBinding!!.wheelSex.adapter = sexAdapter
        mDataBinding!!.wheelSex.setOnItemSelectedListener {
            gender =sexList.get(it)
        }
    }

    private fun setWheel(view: WheelView){
        view.setItemsVisibleCount(5)
        view.setCyclic(false)
        view.setTextColorCenter(Color.BLACK)
        view.setTextColorOut(Color.parseColor("#bfbfbf"))
        view.setDividerType(WheelView.DividerType.FILL)
        view.setDividerColor(Color.parseColor("#00ffffff"))
        view.setLineSpacingMultiplier(2f)
    }

    class ViewClickCallBack{
        var onSure: (sex:String) -> Unit = {}
    }

    override fun getLayoutId(): Int {
        return R.layout.pop_sex
    }
}