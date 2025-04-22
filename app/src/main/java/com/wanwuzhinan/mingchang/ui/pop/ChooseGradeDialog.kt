package com.wanwuzhinan.mingchang.ui.pop

import android.graphics.Color
import com.ad.img_load.pickerview.pv.adapter.ArrayWheelAdapter
import com.ad.img_load.pickerview.wheelview.view.WheelView
import com.ad.img_load.setOnClickNoRepeat
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.databinding.PopSexBinding
import com.ssm.comm.ui.base.BaseDialogFragment


class ChooseGradeDialog constructor(list: List<String>,callback: ViewClickCallBack.() ->Unit) : BaseDialogFragment<PopSexBinding>(){

    private var gender = ""
    private var callback = ViewClickCallBack().also(callback)


    private var list:  List<String>

    init {
        this.list = list
    }

    override fun initViews() {
        isCancelable = true
        mDataBinding!!.tvTitle.text="选择年级"

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
        val sexAdapter = ArrayWheelAdapter(list)
        gender=list.get(0)
        mDataBinding!!.wheelSex.currentItem=0
        mDataBinding!!.wheelSex.adapter = sexAdapter
        mDataBinding!!.wheelSex.setOnItemSelectedListener {
            gender =list.get(it)
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