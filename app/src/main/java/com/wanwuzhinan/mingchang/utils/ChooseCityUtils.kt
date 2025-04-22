package com.wanwuzhinan.mingchang.utils

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import com.ad.img_load.pickerview.bean.CityData
import com.ad.img_load.pickerview.pv.builder.OptionsPickerBuilder
import com.ad.img_load.pickerview.pv.listener.OnOptionsSelectListener
import com.ad.img_load.pickerview.pv.view.OptionsPickerView
import com.wanwuzhinan.mingchang.data.ProvinceListData
import com.google.gson.Gson
import org.json.JSONArray
import java.lang.ref.WeakReference

/**
 * Company:
 * FileName:PickerViewUtils
 * Author:android
 * Mail:2898682029@qq.com
 * Date:20-2-7 下午12:26
 * Description:${DESCRIPTION}  https://github.com/Bigkoo/Android-PickerView
 */
object ChooseCityUtils {
    /**
     * 自定义数据 城市选择器
     */
    private var options1Items: List<ProvinceListData>? = null
    private val options2Items = ArrayList<ArrayList<ProvinceListData>>()
    private val options3Items = ArrayList<ArrayList<ArrayList<ProvinceListData>>>()


    fun showCityPickerView(context: Context?, title: String, list : List<ProvinceListData>, change: ((province: ProvinceListData, city : ProvinceListData, area : ProvinceListData) -> Unit)) {
        //https://github.com/Bigkoo/Android-PickerView
        initData(context,list)
        val reference = WeakReference(context)
        val defaultP = 0
        val defaultC = 0
        val defaultD = 0
        val pvOptions = create(context, { options1: Int, options2: Int, options3: Int, v: View? ->
            //返回的分别是三个级别的选中位置
            val opt1tx = if (options1Items!!.size > 0) options1Items!![options1] else ProvinceListData()
            val opt2tx = if (options2Items.size > 0
                && options2Items[options1].size > 0
            ) options2Items[options1][options2] else ProvinceListData()
            val opt3tx =
                if (options2Items.size > 0 && options3Items[options1].size > 0 && options3Items[options1][options2].size > 0) options3Items[options1][options2][options3] else ProvinceListData()
            change.invoke(opt1tx, opt2tx, opt3tx)
        }, title, defaultP, defaultC, defaultD)
        //添加数据源
        pvOptions.setPicker(options1Items as List<Nothing>?,
            options2Items as List<Nothing>?, options3Items as List<Nothing>?
        )

        pvOptions.show()
    }

    private fun create(
        context: Context?,
        listener: OnOptionsSelectListener,
        title: String,
        def1: Int
    ): OptionsPickerView<*> {
        val reference =
            WeakReference(context)
        return OptionsPickerBuilder(reference.get(), listener) //分隔线颜色。
            .setDividerColor(Color.parseColor("#BBBBBB")) //设置标题
            .setTitleText(title) //设置默认选中项目
            .setSelectOptions(def1) //设置选中项文字颜色
            .setTextColorCenter(Color.BLACK) //设置外部遮罩颜色
            //                .setOutSideColor(0x00000000)
            .setContentTextSize(25)
            .build<Any>()
    }

    private fun create(
        context: Context,
        listener: OnOptionsSelectListener,
        title: String,
        def1: Int,
        def2: Int
    ): OptionsPickerView<*> {
        val reference =
            WeakReference(context)
        return OptionsPickerBuilder(reference.get(), listener) //分隔线颜色。
            .setDividerColor(Color.parseColor("#BBBBBB")) //设置标题
            .setTitleText(title) //设置默认选中项目
            .setSelectOptions(def1, def2) //设置选中项文字颜色
            .setTextColorCenter(Color.BLACK) //设置外部遮罩颜色
            //                .setOutSideColor(0x00000000)
            .setContentTextSize(25)
            .build<Any>()
    }

    private fun create(
        context: Context?,
        listener: OnOptionsSelectListener,
        title: String,
        def1: Int,
        def2: Int,
        def3: Int
    ): OptionsPickerView<*> {
        val reference = WeakReference(context)
        return OptionsPickerBuilder(reference.get(), listener) //分隔线颜色。
            .setDividerColor(Color.parseColor("#BBBBBB")) //设置标题
            .setTitleText(title)
            .setTitleBgColor(Color.parseColor("#ffffff"))
            .setTitleColor(Color.parseColor("#604A11"))
            .setCancelColor(Color.parseColor("#999999"))
            .setSubmitColor(Color.parseColor("#51A8F8"))
            .setTextColorCenter(Color.BLACK) //设置外部遮罩颜色
            .setTextColorOut(Color.parseColor("#999999")) //
            .setLineSpacingMultiplier(2.6f)
            .setSelectOptions(def1, def2, def3) //设置选中项文字颜色
            .setBgColor(Color.parseColor("#ffffff"))
            //                .setOutSideColor(0x00000000)
            .setContentTextSize(18)
            .build<Any>()
    }

    //解析数据
    private fun initData(context: Context?,list : List<ProvinceListData>) {

        if (list.size <= 0) {
            return
        }
        /**
         * 添加省份数据
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = list

        //遍历省份
        for (i in list.indices) {
            val cityList = ArrayList<ProvinceListData>()
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
            options2Items.add(city_List as ArrayList<ProvinceListData>)

            //添加地区数据
            options3Items.add(province_AreaList)
        }
    }

    private fun parseData(result: String): ArrayList<CityData> { //Gson 解析
        val detail = ArrayList<CityData>()
        try {
            val data = JSONArray(result)
            val gson = Gson()
            for (i in 0 until data.length()) {
                val entity = gson.fromJson(data.optJSONObject(i).toString(), CityData::class.java)
                detail.add(entity)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return detail
    }
}