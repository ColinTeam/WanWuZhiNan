package com.ssm.comm.ui.base

import android.content.Context
import android.content.Intent
import android.content.res.TypedArray
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.ssm.comm.ext.*
import com.ssm.comm.ui.widget.dialog.CommDialog
import com.ssm.comm.utils.IntentUtils
import java.util.*


/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.ui.act.base
 * ClassName: WrapViewImpl
 * Author:ShiMing Shi
 * CreateDate: 2022/9/3 11:21
 * Email:shiming024@163.com
 * Description:
 */
class WrapViewImpl(private var activity: AppCompatActivity) : IWrapView {

    override fun getEditText(editText: EditText): String {
        return editText.text.toString()
    }

    override fun <T : AppCompatActivity> launchActivity(clz: Class<T>?) {
        activity.startActivity(Intent(activity, clz))
        to()
    }

    override fun <T : AppCompatActivity> launchActivity(
        clz: Class<T>?,
        vararg pairs: Pair<String, Any?>,
    ) {
        val intent = Intent(activity, clz)
        IntentUtils.fillIntent(intent, *pairs)
        activity.startActivity(intent)
        to()
    }

    override fun <T : AppCompatActivity> launchActivityForResult(
        clz: Class<T>?,
        requestCode: Int
    ) {
        activity.startActivityForResult(Intent(activity, clz), requestCode)
        to()
    }

    override fun <T : AppCompatActivity> launchActivityForResult(
        clz: Class<T>?,
        requestCode: Int,
        vararg pairs: Pair<String, Any?>,
    ) {
        val intent = Intent(activity, clz)
        IntentUtils.fillIntent(intent, *pairs)
        activity.startActivityForResult(intent, requestCode)
        to()
    }

    override fun showKeyboard() {
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    override fun showKeyWord(editText: EditText?) {
        val imm: InputMethodManager = getInputMethodManager()
        editText!!.requestFocus()
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                imm.showSoftInput(editText, 0)
            }
        }, 100)
    }

    override fun closeKeyboard() {
        val imm: InputMethodManager = getInputMethodManager()
        if (activity.currentFocus != null) {
            imm.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        }
    }

    private fun getInputMethodManager(): InputMethodManager {
        return activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun hideSoftInput() {
        val imm = getInputMethodManager()
        if (activity.currentFocus != null) {
            imm.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        }
    }

    override fun performLaunchWebPage(url: String?) {
        if (isEmpty(url)) {
            toastNormal("url地址不能为空")
            return
        }
        val dialog = CommDialog("确定是否立即跳转外部浏览器?", callback = {
            onConfirm = {
                val uri: Uri = Uri.parse(url)
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.data = uri
                //是否安装浏览器
                if (intent.resolveActivity(activity.packageManager) != null) {
                    val componentName = intent.resolveActivity(activity.packageManager)
                    //默认浏览器
                    activity.startActivity(intent);
                } else {

                }
            }
        })
        dialog.show(activity.supportFragmentManager,"CommDialog")
    }

    override fun getArrayImages(id: Int): IntArray {
        //获取图片资源
        val ta: TypedArray = activity.resources.obtainTypedArray(id)
        val icons = IntArray(ta.length())
        for (i in 0 until ta.length()) {
            icons[i] = ta.getResourceId(i, 0)
        }
        ta.recycle()
        return icons;
    }

    override fun getLayoutId(): Int {
        TODO("Not yet implemented")
    }

    override fun getResult(resultCode: Int) {
        val intent = activity.intent
        activity.setResult(resultCode, intent)
        activity.finish()
    }

    override fun getResult(resultCode: Int, bundle: Bundle?) {
        val intent = activity.intent
        if (bundle != null) {
            intent.putExtras(bundle)
            activity.setResult(resultCode, intent)
            activity.finish()
        } else {
            getResult(resultCode)
        }
    }

    override fun getBundle(): Bundle? {
        return activity.intent.extras
    }

    override fun initView() {
        TODO("Not yet implemented")
    }

    override fun initView(savedInstanceState: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun showBaseLoading() {
        showLoadingExt()
    }

    override fun showBaseLoading(text: String?) {
        showLoadingExt()
    }

    override fun dismissBaseLoading() {
        dismissLoadingExt()
    }

    override fun copy(content: String) {
        copyString(activity, content)
    }

    override fun getCurrentActivity(): AppCompatActivity {
        return activity
    }

    private fun to() {

    }

    private fun out() {

    }
}