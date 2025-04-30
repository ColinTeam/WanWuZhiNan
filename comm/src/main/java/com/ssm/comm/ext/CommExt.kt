package com.ssm.comm.ext

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.InputFilter
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ssm.comm.app.appContext
import com.ssm.comm.event.EntityDataEvent
import com.ssm.comm.event.MessageEvent
import com.ssm.comm.ui.base.BaseModel
import org.greenrobot.eventbus.EventBus
import java.nio.CharBuffer
import java.util.regex.Pattern

//手机号脱敏处理
fun String.mobileFormat(): String {
    if (isEmpty(this) && this.length < 11) {
        return ""
    }
    val sb = StringBuilder()
    sb.append(this.subSequence(0, 3))
    for (i in this.indices) {
        sb.append("*")
    }
    sb.append(this.substring(this.length - 2))
    return sb.toString()
}

//邮箱脱敏
fun String.emailFormat(): String {
    if (isEmpty(this)) {
        return ""
    }
    if (!this.contains("@")) {
        return mobileFormat()
    }
    val index = this.indexOf("@")
    val end = this.substring(index)
    val sb = StringBuilder()
    sb.append(this.subSequence(0, 1))
    for (i in this.indices) {
        sb.append("*")
    }
    sb.append(end)
    return sb.toString()
}

fun CharBuffer.charAt(index: Int): Char {
    return this[position() + index]
}

fun post(type: Int, msg: String = "") {
    EventBus.getDefault().post(MessageEvent(type, msg))
}

fun post(type: Int, data: BaseModel) {
    EventBus.getDefault().post(EntityDataEvent(type, data))
}

fun unregisterBus(subscriber: Any) {
    if (EventBus.getDefault().isRegistered(subscriber)) {
        EventBus.getDefault().unregister(subscriber)
    }
}

fun registerBus(subscriber: Any) {
    if (!EventBus.getDefault().isRegistered(subscriber)) {
        EventBus.getDefault().register(subscriber)
    }
}

/**
 * 复制
 * @param mActivity Context
 * @param content String?
 */
fun copyString(mActivity: Context, content: String?) {
    val cm =
        mActivity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    // 创建普通字符型ClipData
    val mClipData = ClipData.newPlainText("Label", content)
    // 将ClipData内容放到系统剪贴板里。
    cm.setPrimaryClip(mClipData)
    toastSuccess("内容复制成功")
}

fun String.urlFormat(): String {
    if (isWebUrlLegal(this)) {
        return this
    }
    //    return String.format("%s%s",,this)
    return String.format("%s%s", com.comm.net_work.BuildConfig.IMG_HOST, this)
}

fun isWebUrlLegal(url: String): Boolean {
    if (isEmpty(url)) {
        return false
    }
    val regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"
    return Pattern.compile(regex).matcher(url).matches()
}

fun isEmpty(str: String?): Boolean {
    return TextUtils.isEmpty(str)
}


/**
 *  打开浏览器
 * @param url String
 */
fun Activity.toUri(url: String) {
    //设置浏览器
    val intent = Intent()
    intent.action = "android.intent.action.VIEW"
    val uri = Uri.parse(url)
    intent.data = uri
    startActivity(intent)
}

/**
 * 设置 editText 输入标记 并且隐藏/显示
 * @param editText PasswordEditText
 * @param type Int
 * @param isShow Boolean 设置是否是明文 还是密码显示
 */
fun setEidTextInputType(editText: EditText, type: Int, isShow: Boolean) {
    //editText.addInputType(type)
    if (isShow) {//明文显示密码
        editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
    } else { //隐藏密码
        editText.transformationMethod = PasswordTransformationMethod.getInstance();
    }

}

/**
 * 设置EditText 长度
 * @receiver EditText
 * @param maxLength Int 设置 密码长度
 */
fun EditText.setMaxLength(maxLength: Int) {
    filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength)) //最大输入长度
}

/**
 * 关闭键盘
 */
fun EditText.hideKeyboard() {
    val imm = appContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(
        this.windowToken,
        InputMethodManager.HIDE_IMPLICIT_ONLY
    )
}

/**
 * 打开键盘
 */
fun EditText.openKeyboard() {
    this.apply {
        isFocusable = true
        isFocusableInTouchMode = true
        requestFocus()
    }
    val inputManager =
        appContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.showSoftInput(this, 0)
}

/**
 * 关闭键盘焦点
 */
fun Activity.hideOffKeyboard() {
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (imm.isActive && this.currentFocus != null) {
        if (this.currentFocus?.windowToken != null) {
            imm.hideSoftInputFromWindow(
                this.currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}

fun toStartActivity(clz: Class<*>) {
    val intent = Intent(appContext, clz)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    appContext.startActivity(intent)
}

fun toStartActivity(clz: Class<*>, bundle: Bundle) {
    val intent = Intent(appContext, clz)
    intent.apply {
        putExtras(bundle)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    appContext.startActivity(intent)
}

fun toStartActivity(
    activity: Activity,
    clz: Class<*>,
    code: Int,
    bundle: Bundle,
) {
    activity.startActivityForResult(Intent(appContext, clz).putExtras(bundle), code)
}

@Suppress("DEPRECATION")
fun toStartActivity(
    fragment: Fragment,
    clz: Class<*>,
    code: Int,
    bundle: Bundle,
) {
    fragment.startActivityForResult(Intent(appContext, clz).putExtras(bundle), code)
}

fun toStartActivity(activity: Activity, intent: Intent, code: Int) {
    activity.startActivityForResult(intent, code)
}

fun toStartActivity(
    type: Any,
    clz: Class<*>,
    code: Int,
    bundle: Bundle,
) {
    if (type is Activity) {
        toStartActivity(type, clz, code, bundle)
    } else if (type is Fragment) {
        toStartActivity(type, clz, code, bundle)
    }
}

/**
 * 隐藏状态栏
 */
@Suppress("DEPRECATION")
fun hideStatusBar(activity: Activity) {
    val attrs = activity.window.attributes
    attrs.flags = attrs.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
    activity.window.attributes = attrs
}

/**
 * 显示状态栏
 */
@Suppress("DEPRECATION")
fun showStatusBar(activity: Activity) {
    val attrs = activity.window.attributes
    attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
    activity.window.attributes = attrs
}

/**
 * 横竖屏
 */
fun isLandscape(context: Context) =
    context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE


/**
 * 字符串相等
 */
fun isEqualStr(value: String?, defaultValue: String?) =
    if (value.isNullOrEmpty() || defaultValue.isNullOrEmpty()) false else TextUtils.equals(
        value,
        defaultValue
    )

/**
 *
 * @receiver TextView
 * @param id Int Drawable 图片
 * @return TextView
 * @attr ref android.R.styleable#TextView_drawableLeft
 * @attr ref android.R.styleable#TextView_drawableTop
 * @attr ref android.R.styleable#TextView_drawableRight
 * @attr ref android.R.styleable#TextView_drawableBottom
 */
fun TextView.drawabImg(id: Int, type: Int = Gravity.LEFT): TextView {
    if (id == 0) {
        this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        return this
    }
    when (type) {
        Gravity.LEFT -> {
            this.setCompoundDrawablesWithIntrinsicBounds(getDrawableExt(id), null, null, null)
        }

        Gravity.TOP -> {
            this.setCompoundDrawablesWithIntrinsicBounds(null, getDrawableExt(id), null, null)
        }

        Gravity.RIGHT -> {
            this.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawableExt(id), null)
        }

        Gravity.BOTTOM -> {
            this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawableExt(id))
        }
    }
    return this
}

/**
 * 保存图片到指定路径  保存二维码到本地
 * @param bitmap   要保存的图片
 * @param fileName 自定义图片名称
 * @param context
 * @return
 */
fun saveBitmapToFile(bitmap: Bitmap, activity: Activity): Boolean {
    return true
    /*
    // 保存图片至指定路径
    if(bitmap == null)
        return false
    val storePath = AppConfig.APP_CACHE_PIC_PATH //图片保存地址
    val fileName = System.currentTimeMillis().toString() + ".jpg"
    val appDir = File(storePath)
    if (!appDir.exists()) {
        appDir.mkdirs()
    }
    val path = File(appDir, fileName)
    try {
        val fos = FileOutputStream(path)
        //通过io流的方式来压缩保存图片(80代表压缩20%)
        val isSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos) //CompressFormat.PNG
        fos.flush()
        fos.close()
        //发送广播通知系统图库刷新数据
//        val uri: Uri = Uri.fromFile(path)
//        context?.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))

//        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
//        val uri = Uri.fromFile(path)
//        intent.data = uri
//        activity?.sendBroadcast(intent)
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(
                activity?.contentResolver,
                path.absolutePath, fileName, null
            )
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        activity?.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.fromFile(File(path.path))) )

        return isSuccess
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return false

    */
}

fun EditText.setMaxInput(len: Int) {
    this.filters = arrayOf(InputFilter.LengthFilter(len))
}


/**
 * Int类型相等
 *
 */
fun isEqualIntExt(value: Int, defaultValue: Int) = value == defaultValue

fun getDrawableExt(id: Int): Drawable? = ContextCompat.getDrawable(appContext, id)

fun getColorExt(id: Int): Int = ContextCompat.getColor(appContext, id)

fun getStringExt(id: Int) = appContext.resources!!.getString(id)

fun getStringArrayExt(id: Int): Array<String> = appContext.resources!!.getStringArray(id)

fun getIntArrayExt(id: Int) = appContext.resources!!.getIntArray(id)

fun getDimensionExt(id: Int) = appContext.resources!!.getDimension(id)


