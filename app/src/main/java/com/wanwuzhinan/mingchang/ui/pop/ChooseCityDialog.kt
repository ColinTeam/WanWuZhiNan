package com.wanwuzhinan.mingchang.ui.pop

import android.graphics.Color
import android.util.Log
import com.ad.img_load.pickerview.pv.adapter.ArrayWheelAdapter
import com.ad.img_load.pickerview.wheelview.view.WheelView
import com.ad.img_load.setOnClickNoRepeat
import com.ssm.comm.ui.base.BaseDialogFragment
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.ProvinceListData
import com.wanwuzhinan.mingchang.databinding.PopCityBinding


class ChooseCityDialog constructor(list:List<ProvinceListData>, callback: ViewClickCallBack.() ->Unit) : BaseDialogFragment<PopCityBinding>(){

    private var callback = ViewClickCallBack().also(callback)

    private var options1Items: List<ProvinceListData>? = null
    private val options2Items = ArrayList<ArrayList<ProvinceListData>>()
    private val options3Items = ArrayList<ArrayList<ArrayList<ProvinceListData>>>()

    private var proName = ""
    private var cityName = ""
    private var areaName = ""

    private var list:List<ProvinceListData>

    init {
        this.list = list
        if (list.size > 0) {
            /**
             * 添加省份数据
             * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
             * PickerView会通过getPickerViewText方法获取字符串显示出来。
             */
            this.options1Items = list

            //遍历省份
            for (i in list.indices) {
                val province_AreaList = ArrayList<ArrayList<ProvinceListData>>()
                //遍历该省份的所有城市
                val city_List = list[i].children
                if(city_List.isEmpty()){
                    city_List.add(ProvinceListData())
                }
                for (c in city_List.indices) {
                    val areas = city_List[c].children as ArrayList<ProvinceListData>
                        ?: continue
                    if (areas.isEmpty()) {
                        areas.add(ProvinceListData())
                    }
                    //添加该省所有地区数据
                    province_AreaList.add(areas)
                }

                //添加城市数据
                this.options2Items.add(city_List as ArrayList<ProvinceListData>)

                //添加地区数据
                this.options3Items.add(province_AreaList)
            }

        }
    }

    override fun initViews() {
        isCancelable = true
        mDataBinding!!.tvTitle.text="选择地区"

        setOnClickNoRepeat(mDataBinding!!.tvCancel , mDataBinding!!.tvSure){

            when(it){
                mDataBinding!!.tvSure ->{
//                    callback.onSure()
                    callback.onSure(proName,cityName,areaName)
                    dismiss()
                }
                mDataBinding!!.tvCancel ->{
                    dismiss()
                }
            }
        }
        setWheel(mDataBinding!!.wheelPro)
        setWheel(mDataBinding!!.wheelCity)
        setWheel(mDataBinding!!.wheelArea)

        initWheel()
    }

    private fun initWheel() {

        val sexAdapter = ArrayWheelAdapter(options1Items)
        proName = options1Items?.get(0)?.label.toString()
        cityName = options2Items?.get(0)?.get(0)?.label.toString()
        areaName = options3Items?.get(0)?.get(0)?.get(0)?.label.toString()


        mDataBinding!!.wheelPro.currentItem=0
        mDataBinding!!.wheelPro.adapter = sexAdapter
        mDataBinding!!.wheelPro.setOnItemSelectedListener {

            proName = options1Items?.get(it)?.label.toString()
            cityName = options2Items?.get(it)?.get(0)?.label.toString()
            areaName = options3Items?.get(it)?.get(0)?.get(0)?.label.toString()

            val cityAdapter = ArrayWheelAdapter(options2Items.get(it))
            mDataBinding!!.wheelCity.currentItem=0
            mDataBinding!!.wheelCity.adapter = cityAdapter

            val areaAdapter = ArrayWheelAdapter(options3Items.get(it).get(0))
            mDataBinding!!.wheelArea.currentItem=0
            mDataBinding!!.wheelArea.adapter = areaAdapter

        }

        val cityAdapter = ArrayWheelAdapter(options2Items.get(0))
        mDataBinding!!.wheelCity.currentItem=0
        mDataBinding!!.wheelCity.adapter = cityAdapter
        mDataBinding!!.wheelCity.setOnItemSelectedListener {

            cityName = options2Items.get(mDataBinding!!.wheelPro.currentItem).get(it)?.label.toString()
            areaName = options3Items.get(mDataBinding!!.wheelPro.currentItem).get(it)?.get(0)?.label.toString()

            val areaAdapter = ArrayWheelAdapter(options3Items.get( mDataBinding!!.wheelPro.currentItem).get(it))
            mDataBinding!!.wheelArea.currentItem=0
            mDataBinding!!.wheelArea.adapter = areaAdapter
        }

        val areaAdapter = ArrayWheelAdapter(options3Items.get(0).get(0))
        mDataBinding!!.wheelArea.currentItem=0
        mDataBinding!!.wheelArea.adapter = areaAdapter
        mDataBinding!!.wheelArea.setOnItemSelectedListener {
            areaName = options3Items.get(mDataBinding!!.wheelPro.currentItem).get(mDataBinding!!.wheelCity.currentItem).get(it)?.label.toString()
        }
    }

    private fun setWheel(view: WheelView){
        view.setItemsVisibleCount(6)
        view.setCyclic(false)
        view.setTextColorCenter(Color.BLACK)
        view.setTextColorOut(Color.parseColor("#bfbfbf"))
        view.setDividerType(WheelView.DividerType.FILL)
        view.setDividerColor(Color.parseColor("#00ffffff"))
        view.setLineSpacingMultiplier(2f)
    }

    class ViewClickCallBack{
        var onSure: (pro:String,city:String,are:String) -> Unit = { s: String, s1: String, s2: String ->

        }
    }

    override fun getLayoutId(): Int {
        return R.layout.pop_city
    }
}