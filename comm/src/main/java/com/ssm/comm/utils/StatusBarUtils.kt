package com.ssm.comm.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.annotation.IntDef
import androidx.core.view.ViewCompat
import java.lang.reflect.Field
import java.lang.reflect.Method


/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.uitil
 * ClassName: StatusBarUtils
 * Author:ShiMing Shi
 * CreateDate: 2022/9/3 12:07
 * Email:shiming024@163.com
 * Description:
 */
object StatusBarUtils {

    private const val TYPE_MIUI = 0
    private const val TYPE_FLYME = 1
    private const val TYPE_M = 3 //6.0
    private const val TAG_FAKE_STATUS_BAR_VIEW = "statusBarView"
    private const val TAG_MARGIN_ADDED = "marginAdded"

    @IntDef(TYPE_MIUI, TYPE_FLYME, TYPE_M)
//    @Retention(RetentionPolicy.SOURCE)


    internal annotation class ViewType


    /**
     * 设置状态栏透明
     * @param activity
     * @param hideStatusBarBackground
     * @param dark 字体颜色 黑色/白色
     */
    @Suppress("DEPRECATION")
    fun translucentStatusBar(activity: Activity, hideStatusBarBackground: Boolean, dark: Boolean) {
        val window = activity.window
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (hideStatusBarBackground) {
            //如果为全透明模式，取消设置Window半透明的Flag
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //设置状态栏为透明
            window.statusBarColor = Color.TRANSPARENT
            //设置window的状态栏不可见
            if (dark) {
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            }
        } else {
            //如果为半透明模式，添加设置Window半透明的Flag
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //设置系统状态栏处于可见状态
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
        //view不根据系统窗口来调整自己的布局
        val contentView = window.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
        val childView = contentView.getChildAt(0)
        if (childView != null) {
            childView.fitsSystemWindows = false
            ViewCompat.requestApplyInsets(childView)
        }
    }

    private fun removeFakeStatusBarViewIfExist(activity: Activity) {
        val window = activity.window
        val decorView = window.decorView as ViewGroup
        val fakeView = decorView.findViewWithTag<View>(StatusBarUtils.TAG_FAKE_STATUS_BAR_VIEW)
        if (fakeView != null) {
            decorView.removeView(fakeView)
        }
    }

    private fun removeMarginTopOfContentChild(contentChild: View?, statusBarHeight: Int) {
        if (contentChild == null) {
            return
        }
        if (TAG_MARGIN_ADDED == contentChild.tag) {
            val lp = contentChild.layoutParams as FrameLayout.LayoutParams
            lp.topMargin -= statusBarHeight
            contentChild.layoutParams = lp
            contentChild.tag = null
        }
    }

    /**
     * 修改状态栏颜色，支持4.4以上版本
     *
     * @param colorId 颜色
     */
    @Suppress("DEPRECATION")
    fun setStatusBarColor(activity: Activity, colorId: Int) {
        val window: Window = activity.window
        window.statusBarColor = colorId
    }


    /**
     * 设置状态栏透明
     */
    @Suppress("DEPRECATION")
    fun setTranslucentStatus(activity: Activity) {
        //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
        val window: Window = activity.window
        val decorView: View = window.decorView
        //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
        val option: Int = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        decorView.systemUiVisibility = option
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
        //导航栏颜色也可以正常设置
        //window.setNavigationBarColor(Color.TRANSPARENT);
    }

    /**
     * 设置状态栏深色浅色切换
     */
    fun setStatusBarDarkTheme(activity: Activity?, dark: Boolean): Boolean {
        if (activity != null) {
            setStatusBarFontIconDark(activity, TYPE_M, dark)
        }
        return true
    }


    /**
     * 设置 状态栏深色浅色切换
     */
    private fun setStatusBarFontIconDark(activity: Activity, @ViewType type: Int, dark: Boolean): Boolean {
        return when (type) {
            TYPE_MIUI -> setMiuiUI(activity, dark)
            TYPE_FLYME -> setFlymeUI(activity, dark)
            TYPE_M -> setCommonUI(activity, dark)
            else -> setCommonUI(activity, dark)
        }
    }

    //设置6.0 状态栏深色浅色切换
    @Suppress("DEPRECATION")
    private fun setCommonUI(activity: Activity, dark: Boolean): Boolean {
        val decorView = activity.window.decorView
        var vis = decorView.systemUiVisibility
        vis = if (dark) {
            vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        if (decorView.systemUiVisibility != vis) {
            decorView.systemUiVisibility = vis
        }
        return true
    }


    //设置Flyme 状态栏深色浅色切换
    private fun setFlymeUI(activity: Activity, dark: Boolean): Boolean {

        return try {
            val window = activity.window
            val lp = window.attributes
            val darkFlag: Field =
                WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
            val meizuFlags: Field =
                WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
            darkFlag.isAccessible = true
            meizuFlags.isAccessible = true
            val bit: Int = darkFlag.getInt(null)
            var value: Int = meizuFlags.getInt(lp)
            value = if (dark) {
                value or bit
            } else {
                value and bit.inv()
            }
            meizuFlags.setInt(lp, value)
            window.attributes = lp
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    //设置MIUI 状态栏深色浅色切换
    private fun setMiuiUI(activity: Activity, dark: Boolean): Boolean {
        return try {
            val window = activity.window
            val clazz: Class<*> = activity.window.javaClass
            @SuppressLint("PrivateApi") val layoutParams =
                Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val field: Field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            val darkModeFlag: Int = field.getInt(layoutParams)
            val extraFlagField: Method = clazz.getDeclaredMethod(
                "setExtraFlags",
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType
            )
            extraFlagField.isAccessible = true
            //状态栏亮色且黑色字体
            if (dark) {
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag)
            } else {
                extraFlagField.invoke(window, 0, darkModeFlag)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    //获取状态栏高度
    @SuppressLint("InternalInsetResource")
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId: Int = context.resources.getIdentifier(
            "status_bar_height", "dimen", "android"
        )
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}