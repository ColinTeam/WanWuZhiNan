package com.ad.img_load.pickerview;

import android.content.Context;
import android.graphics.Color;

import com.ad.img_load.pickerview.bean.CityData;
import com.ad.img_load.pickerview.bean.CityListData;
import com.ad.img_load.pickerview.pv.builder.OptionsPickerBuilder;
import com.ad.img_load.pickerview.pv.builder.TimePickerBuilder;
import com.ad.img_load.pickerview.pv.listener.OnDismissListener;
import com.ad.img_load.pickerview.pv.listener.OnOptionsSelectListener;
import com.ad.img_load.pickerview.pv.listener.OnTimeSelectListener;
import com.ad.img_load.pickerview.pv.view.OptionsPickerView;
import com.ad.img_load.pickerview.pv.view.TimePickerView;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Company:
 * FileName:PickerViewUtils
 * Author:android
 * Mail:2898682029@qq.com
 * Date:20-2-7 下午12:26
 * Description:${DESCRIPTION}  https://github.com/Bigkoo/Android-PickerView
 */
public class PickerViewUtils {

    /**
     * 自定义数据 城市选择器
     */
    private static List<CityData> options1Items;
    private static final ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private static final ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    public static void showCityPickerView(Context context, OnCitySelectListener listener) {
        showCityPickerView(context, "城市选择", listener);
    }

    public static void showCityPickerView(Context context, String title, OnCitySelectListener listener) {
        //https://github.com/Bigkoo/Android-PickerView
        initJsonData(context);
        WeakReference<Context> reference = new WeakReference<>(context);
        int defaultP = (int) SPSecuredUtils.newInstance(context).get("province_index", 0);
        int defaultC = (int) SPSecuredUtils.newInstance(context).get("city_index", 0);
        int defaultD = (int) SPSecuredUtils.newInstance(context).get("district_index", 0);

        OptionsPickerView pvOptions = create(context, (options1, options2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            String opt1tx = options1Items.size() > 0 ?
                    options1Items.get(options1).getName() : "";

            String opt2tx = options2Items.size() > 0
                    && options2Items.get(options1).size() > 0 ?
                    options2Items.get(options1).get(options2) : "";

            String opt3tx = options2Items.size() > 0
                    && options3Items.get(options1).size() > 0
                    && options3Items.get(options1).get(options2).size() > 0 ?
                    options3Items.get(options1).get(options2).get(options3) : "";
            SPSecuredUtils.newInstance(context).put("province_index", options1);
            SPSecuredUtils.newInstance(context).put("city_index", options2);
            SPSecuredUtils.newInstance(context).put("district_index", options3);
            if (listener != null) {
                listener.onCitySelect(opt1tx, opt2tx, opt3tx);
            }
        },title,defaultP,defaultC,defaultD);
        //添加数据源
        pvOptions.setPicker(options1Items, options2Items, options3Items);
        pvOptions.show();
    }

    private static OptionsPickerView create(Context context, OnOptionsSelectListener listener, String title, int def1) {
        WeakReference<Context> reference = new WeakReference<>(context);
        OptionsPickerView pvOptions = new OptionsPickerBuilder(reference.get(),listener)
                //分隔线颜色。
                .setDividerColor(Color.parseColor("#BBBBBB"))
                //设置标题
                .setTitleText(title)
                //设置默认选中项目
                .setSelectOptions(def1)
                //设置选中项文字颜色
                .setTextColorCenter(Color.BLACK)
                //设置外部遮罩颜色
//                .setOutSideColor(0x00000000)
                .setContentTextSize(25)
                .build();
        return pvOptions;
    }

    private static OptionsPickerView create(Context context,OnOptionsSelectListener listener,String title,int def1,int def2) {
        WeakReference<Context> reference = new WeakReference<>(context);
        OptionsPickerView pvOptions = new OptionsPickerBuilder(reference.get(),listener)
                //分隔线颜色。
                .setDividerColor(Color.parseColor("#BBBBBB"))
                //设置标题
                .setTitleText(title)
                //设置默认选中项目
                .setSelectOptions(def1,def2)
                //设置选中项文字颜色
                .setTextColorCenter(Color.BLACK)
                //设置外部遮罩颜色
//                .setOutSideColor(0x00000000)
                .setContentTextSize(25)
                .build();
        return pvOptions;
    }

    private static OptionsPickerView create(Context context, OnOptionsSelectListener listener, String title, int def1, int def2, int def3) {
        WeakReference<Context> reference = new WeakReference<>(context);
        return new OptionsPickerBuilder(reference.get(),listener)
                //分隔线颜色。
                .setDividerColor(Color.parseColor("#BBBBBB"))
                //设置标题
                .setTitleText(title)
                //设置默认选中项目
                .setSelectOptions(def1,def2,def3)
                //设置选中项文字颜色
                .setTextColorCenter(Color.BLACK)
                //设置外部遮罩颜色
//                .setOutSideColor(0x00000000)
                .setContentTextSize(25)
                .build();
    }

    //性别选择器
    public static void showSexPickerView(Context context, List<String> list, OnSexSelectListener listener) {
        WeakReference<Context> reference = new WeakReference<>(context);
        int defaultP = (int) SPSecuredUtils.newInstance(context).get("sex_index", 0);
        OptionsPickerView pvOptions = create(reference.get(), (options1, options2, options3, v) -> {
            //返回的分别是1个级别的选中位置
            String opt1tx = list.size() > 0 ? list.get(options1) : "保密";
            SPSecuredUtils.newInstance(context).put("sex_index", options1);
            if (listener != null) {
                listener.onSexSelect(opt1tx);
            }
        },"请选择性别",defaultP);
        //添加数据源
        pvOptions.setPicker(list);
        pvOptions.show();
        pvOptions.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                reference.clear();
            }
        });
    }

    //时间选择器
    public static void showDatePickerView(Context context, OnTimeSelectListener listener){
        showDatePickerView(context,"",listener);
    }

    //时间选择器
    public static void showDatePickerView(Context context,String title,OnTimeSelectListener listener){
        boolean[] types = new boolean[]{true, true, true,false,false,false};
        showDatePickerView(context,types,listener);
    }

    //时间选择器
    public static void showDatePickerView(Context context,boolean[] types ,OnTimeSelectListener listener){
        WeakReference<Context> reference = new WeakReference<>(context);
        TimePickerView pvTime = new TimePickerBuilder(reference.get(), listener)
                .setType(types)
                .setTitleText("请选择年月")
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .setLabel("年","月","日","时","分","秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .isCyclic(false)//是否循环滚动
                .setTextColorCenter(Color.BLACK)
                .setDividerColor(Color.GRAY)
                .build();
        pvTime.show();
    }

    //解析数据
    private static void initJsonData(Context context) {
//        List<CityBean> cityInfoList = SharedPreferencesUtils.getInctance().getListData(CRASH_CITY_INFO_NAME,CityBean.class);
//        Gson gson = GsonManager.newInstance().getGson();
//        JsonArray array = new JsonArray();
//        for (int i = 0;i <cityInfoList.size();i++){
//            JsonElement obj = gson.toJsonTree(cityInfoList.get(i));
//            array.add(obj);
//        }

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = JsonUtils.getJson("province.json",context);//获取assets目录下的json文件数据

        //用Gson 转成实体
//        ArrayList<CityBean> jsonBean = parseData(array.toString());
        ArrayList<CityData> jsonBean = parseData(JsonData);

        if (jsonBean.size() <= 0) {
            return;
        }

        /**
         * 添加省份数据
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        //遍历省份
        for (int i = 0; i < jsonBean.size(); i++) {
            //该省的城市列表（第二级）
            ArrayList<String> cityList = new ArrayList<>();
            //该省的所有地区列表（第三极）
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();
            //遍历该省份的所有城市
            List<CityListData> city_List = jsonBean.get(i).getCity();
            if (city_List == null) {
                continue;
            }
            for (int c = 0; c < city_List.size(); c++) {
                String cityName = city_List.get(c).getName();
                cityList.add(cityName);//添加城市
                //该城市的所有地区列表
//                ArrayList<String> city_AreaList = new ArrayList<>();
                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                ArrayList<String> areas = (ArrayList<String>) jsonBean.get(i).getCity().get(c).getArea();
                if (areas == null) {
                    continue;
                }
                if (areas.isEmpty()) {
                    areas.add("-");
                }
                //添加该省所有地区数据
                province_AreaList.add(areas);
            }

            //添加城市数据
            options2Items.add(cityList);

            //添加地区数据
            options3Items.add(province_AreaList);
        }
    }

    private static ArrayList<CityData> parseData(String result) {//Gson 解析
        ArrayList<CityData> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                CityData entity = gson.fromJson(data.optJSONObject(i).toString(), CityData.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

}
