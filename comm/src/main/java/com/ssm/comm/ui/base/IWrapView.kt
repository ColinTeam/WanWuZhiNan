package com.ssm.comm.ui.base

import android.os.Bundle
import android.widget.EditText
import androidx.annotation.ArrayRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity


/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.ui.act.base
 * ClassName: IWrapView
 * Author:ShiMing Shi
 * CreateDate: 2022/9/3 11:16
 * Email:shiming024@163.com
 * Description:
 */
interface IWrapView {

    fun getEditText(editText: EditText) : String

    fun <T : AppCompatActivity> launchActivity(clz: Class<T>?)

    fun <T : AppCompatActivity> launchActivity(clz: Class<T>?, vararg pairs: Pair<String, Any?>)

    fun <T : AppCompatActivity> launchActivityForResult(clz: Class<T>?, requestCode: Int)

    fun <T : AppCompatActivity> launchActivityForResult(
        clz: Class<T>?,
        requestCode: Int,
        vararg pairs: Pair<String, Any?>
    )

    fun getCurrentActivity(): AppCompatActivity

    fun showKeyboard()

    fun showKeyWord(editText: EditText?)

    fun closeKeyboard()

    fun hideSoftInput()

    fun performLaunchWebPage(url: String?)

    fun getArrayImages(@ArrayRes id: Int): IntArray?

    @LayoutRes
    fun getLayoutId(): Int

    fun getResult(resultCode: Int)

    fun getResult(resultCode: Int, bundle: Bundle?)

    fun getBundle(): Bundle?

    fun initView()

    fun initView(savedInstanceState: Bundle?)

    fun showBaseLoading()

    fun showBaseLoading(text: String?)

    fun dismissBaseLoading()

    fun copy(content: String)
}