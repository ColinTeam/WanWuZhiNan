package com.ad.img_load.pickerview.pv.configure;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.ad.img_load.pickerview.pv.listener.CustomListener;
import com.ad.img_load.pickerview.pv.listener.OnOptionsSelectChangeListener;
import com.ad.img_load.pickerview.pv.listener.OnOptionsSelectListener;
import com.ad.img_load.pickerview.pv.listener.OnTimeSelectChangeListener;
import com.ad.img_load.pickerview.pv.listener.OnTimeSelectListener;
import com.ad.img_load.pickerview.wheelview.view.WheelView;
import com.comm.img_load.R;

import java.util.Calendar;

/**
 * Build Options
 * Created by xiaosongzeem on 2018/3/8.
 */

public class PickerOptions {

    //constant
    //标题确定 按钮文字颜色
    private static final int PICKER_VIEW_BTN_COLOR_NORMAL = Color.parseColor("#3B3B3B");
    //标题背景色
    private static final int PICKER_VIEW_BG_COLOR_TITLE = Color.parseColor("#C6C6C6");
    //标题-中间文字颜色
    private static final int PICKER_VIEW_COLOR_TITLE = Color.parseColor("#000000");
    //滚轮背景色
    private static final int PICKER_VIEW_BG_COLOR_DEFAULT = Color.parseColor("#ffffff");

    public static final int TYPE_PICKER_OPTIONS = 1;
    public static final int TYPE_PICKER_TIME = 2;

    public OnOptionsSelectListener optionsSelectListener;
    public OnTimeSelectListener timeSelectListener;
    public View.OnClickListener cancelListener;

    public OnTimeSelectChangeListener timeSelectChangeListener;
    public OnOptionsSelectChangeListener optionsSelectChangeListener;
    public CustomListener customListener;

    //options picker
    public String label1, label2, label3;//单位字符
    public int option1, option2, option3;//默认选中项
    public int x_offset_one, x_offset_two, x_offset_three;//x轴偏移量

    public boolean cyclic1 = false;//是否循环，默认否
    public boolean cyclic2 = false;
    public boolean cyclic3 = false;

    public boolean isRestoreItem = false; //切换时，还原第一项


    //time picker
    public boolean[] type = new boolean[]{true, true, true, false, false, false};//显示类型，默认显示： 年月日

    public Calendar date;//当前选中时间
    public Calendar startDate;//开始时间
    public Calendar endDate;//终止时间
    public int startYear;//开始年份
    public int endYear;//结尾年份

    public boolean cyclic = false;//是否循环
    public boolean isLunarCalendar = false;//是否显示农历

    public String label_year, label_month, label_day, label_hours, label_minutes, label_seconds;//单位
    public int x_offset_year, x_offset_month, x_offset_day, x_offset_hours, x_offset_minutes, x_offset_seconds;//单位


    public PickerOptions(int buildType) {
        if (buildType == TYPE_PICKER_OPTIONS) {
            layoutRes = R.layout.pickerview_options;
        } else {
            layoutRes = R.layout.pickerview_time;
        }
    }

    //******* general field ******//
    public int layoutRes;
    public ViewGroup decorView;
    public int textGravity = Gravity.CENTER;
    public Context context;

    public String textContentConfirm;//确定按钮文字
    public String textContentCancel;//取消按钮文字
    public String textContentTitle;//标题文字

    public int textColorConfirm = PICKER_VIEW_BTN_COLOR_NORMAL;//确定按钮颜色
    public int textColorCancel = Color.parseColor("#8A8A8A");//取消按钮颜色
    public int textColorTitle = PICKER_VIEW_COLOR_TITLE;//标题颜色

    public int bgColorWheel = PICKER_VIEW_BG_COLOR_DEFAULT;//滚轮背景颜色
    public int bgColorTitle = PICKER_VIEW_BG_COLOR_TITLE;//标题背景颜色

    public int textSizeSubmitCancel = 17;//确定取消按钮大小
    public int textSizeTitle = 18;//标题文字大小
    public int textSizeContent = 18;//内容文字大小

    public int textColorOut = Color.parseColor("#454545"); //分割线以外的文字颜色
    public int textColorCenter = Color.parseColor("#FFFFFF"); //分割线之间的文字颜色
    public int dividerColor = Color.parseColor("#FFFFFF"); //分割线的颜色
    public int outSideColor = Color.parseColor("#FFFFFF"); //显示时的外部背景色颜色,默认是灰色

    public float lineSpacingMultiplier = 1.6f; // 条目间距倍数 默认1.6
    public boolean isDialog;//是否是对话框模式

    public boolean cancelable = true;//是否能取消
    public boolean isCenterLabel = false;//是否只显示中间的label,默认每个item都显示
    public Typeface font = Typeface.MONOSPACE;//字体样式
    public WheelView.DividerType dividerType = WheelView.DividerType.FILL;//分隔线类型
    public int itemsVisibleCount = 9; //最大可见条目数
    public boolean isAlphaGradient = false; //透明度渐变
}
