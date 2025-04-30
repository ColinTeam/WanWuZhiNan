package com.wanwuzhinan.mingchang.ext

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ad.img_load.countDown
import com.ad.img_load.dialog.CodeDialog
import com.ad.img_load.glide.manager.GlideImgManager
import com.wanwuzhinan.mingchang.data.CodeData
import com.wanwuzhinan.mingchang.data.ConfigData
import com.wanwuzhinan.mingchang.data.TextDescriptionData
import com.wanwuzhinan.mingchang.entity.UserInfoData
import com.wanwuzhinan.mingchang.net.repository.LoginRepository
import com.wanwuzhinan.mingchang.net.repository.UserRepository
import com.wanwuzhinan.mingchang.ui.phone.LoginActivity
import com.wanwuzhinan.mingchang.ui.pop.SurePop
import com.wanwuzhinan.mingchang.ui.publics.WebViewActivity
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.kongzue.dialogx.dialogs.CustomDialog
import com.kongzue.dialogx.interfaces.OnBindView
import com.ssm.comm.app.CommApplication
import com.ssm.comm.config.Constant
import com.ssm.comm.ext.*
import com.ssm.comm.global.AppActivityManager
import com.ssm.comm.ui.base.IWrapView
import com.ssm.comm.utils.MMKVUtils
import com.tencent.mm.opensdk.constants.Build
import com.tencent.mm.opensdk.modelbiz.WXOpenCustomerServiceChat
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.net.RetrofitClient
import com.wanwuzhinan.mingchang.thread.EaseThreadManager
import com.wanwuzhinan.mingchang.ui.publics.WebGoodsViewActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Hashtable
import java.util.Locale


//打开h5链接
fun IWrapView.performLaunchH5Agreements(url: String , title: String) {
    launchActivity(
        WebViewActivity::class.java,
        Pair(Constant.URL_TYPE, Constant.OTHER_TYPE),
        Pair(Constant.H5_URL, url),
        Pair(
            Constant.WEB_TITLE,
            title
        ),
    )
}

fun IWrapView.performLaunchGoodsDetail( goodsId: String) {
    launchActivity(
        WebGoodsViewActivity::class.java,
        Pair(
            Constant.GOODS_ID,
            goodsId
        )
    )
}


fun convertTimestampToDateTime(timestamp: Long): String {
    val date = Date(timestamp*1000)
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return format.format(date)
}

//跳转企业微信
fun launchWechatService(){
    // 判断当前版本是否支持拉起客服会话
    if (CommApplication.instance.api!!.getWXAppSupportAPI() >= Build.SUPPORT_OPEN_CUSTOMER_SERVICE_CHAT) {
        val req = WXOpenCustomerServiceChat.Req()
        req.corpId = getConfigData().enterprise_WeChat_id // 企业ID
        req.url = getConfigData().enterprise_WeChat_url // 客服URL
        CommApplication.instance.api!!.sendReq(req)
    }
}

fun getUserInfo(): UserInfoData.infoBean {
    val decodeString = MMKVUtils.decodeString(Constant.USER_INFO)
    return if (decodeString.isEmpty()) {
        UserInfoData.infoBean()
    } else {
        Gson().fromJson(decodeString, UserInfoData.infoBean::class.java)
    }
}

fun `getConfigData`(): ConfigData {
    val decodeString = MMKVUtils.decodeString(Constant.CONFIG_DATA)
    return if (decodeString.isEmpty()) {
        ConfigData()
    } else {
        Gson().fromJson(decodeString, ConfigData::class.java)
    }
}

fun Int.genderText() = when (this) {
    2 -> {
        "女孩"
    }

    1 -> {
        "男孩"
    }

    else -> {
        "男孩"
    }
}


/**
 * 生成简单二维码
 * @param content  字符串内容
 * @param width 二维码宽度
 * @param height  二维码高度
 * @param character_set 编码方式（一般使用UTF-8）
 * @param error_correction_level 容错率 L：7% M：15% Q：25% H：35%
 * @param margin 空白边距（二维码与边框的空白区域）
 * @param color_black 黑色色块
 * @param color_white 白色色块
 * @return Bitmap
 */
fun createQRCodeBitmap(
    content: String = "",
    width: Int = 200,
    height: Int = 200,
    character_set: String = "",
    error_correction_level: String = "",
    margin: String = "",
    color_black: Int = 0,
    color_white: Int = 0
): Bitmap? {
    try {
        /** 1.设置二维码相关配置 */
        val hints = Hashtable<EncodeHintType, String>()
        // 字符转码格式设置
        hints[EncodeHintType.CHARACTER_SET] = character_set
        // 容错率设置
        hints[EncodeHintType.ERROR_CORRECTION] = error_correction_level
        // 空白边距设置
        hints[EncodeHintType.MARGIN] = margin
        /** 2.将配置参数传入到QRCodeWriter的encode方法生成BitMatrix(位矩阵)对象 */
        val matrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints)

        /** 3.创建像素数组,并根据BitMatrix(位矩阵)对象为数组元素赋颜色值 */
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            for (x in 0 until width) {
                //bitMatrix.get(x,y)方法返回true是黑色色块，false是白色色块
                if (matrix.get(x, y)) {
                    //黑色色块像素设置
                    pixels[y * width + x] = color_black
                } else {
                    // 白色色块像素设置
                    pixels[y * width + x] = color_white
                }
            }
        }
        /** 4.创建Bitmap对象,根据像素数组设置Bitmap每个像素点的颜色值,并返回Bitmap对象 */
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    } catch (e: WriterException) {
        e.printStackTrace()
        return null
    }
}

//打开隐私政策
fun IWrapView.performLaunchPrivacy() {
    launchActivity(
        WebViewActivity::class.java,
        Pair(
            Constant.URL_TYPE,
            Constant.PRIVACY_POLICY_TYPE
        )
    )
}


//获取说明
fun IWrapView.getTextDescription(
    key: String,
    repository: UserRepository,
    success: (data: TextDescriptionData.Result) -> Unit
) {
    repository.getTextDescription(key,
        showLoading = {
            showBaseLoading()
        },
        hideLoading = {
            dismissBaseLoading()
        },
        onSuccess = {
            success(it)
        },
        onFailure = {
            toastError(it)
        }
    )
}

fun IWrapView.jumpLoginActivity(isClear: Boolean = true) {
    if (isClear) {
        clearAllData()
        RetrofitClient.addOkHttpCommBuilder(Constant.TOKEN, "")
    }
    val mH = Handler(Looper.getMainLooper())
    mH.postDelayed({
        // 进行内存优化，销毁掉所有的界面
        launchActivity(LoginActivity::class.java)
        AppActivityManager.getInstance().finishAllActivities()
    }, 300)
}


fun editTips(vararg edit: TextView?): Boolean {
    edit.forEach {
        val text = it?.text.toString()
        val hint = it?.hint.toString()
        if(TextUtils.isEmpty(text)){
            toastError(hint)
            return false
        }
    }

    return true
}

fun showSurePop(context: Activity, content:String,title:String="提示",cancel:String="取消",sure:String="确定",is_show:Boolean=true,onSure: () -> Unit){
    var pop = SurePop(context)
    pop.showPop(content,title,cancel,sure,is_show){
        onSure()
    }
}

fun showCancelPop(context: Activity, content:String,is_show:Boolean=true,title:String="提示",cancel:String="取消",sure:String="确定",onSure: () -> Unit,onCancel: () -> Unit){
    var pop = SurePop(context)
    pop.showCancelPop(content,title,cancel,sure,is_show,
        onSure = {
            onSure()
        },
        onCancel = {
            onCancel()
        })
}

//打开用户协议
fun IWrapView.performLaunchUserAgreement() {
    launchActivity(
        WebViewActivity::class.java,
        Pair(
            Constant.URL_TYPE,
            Constant.USER_AGREEMENT_TYPE
        )
    )
}

//打开h5链接
fun IWrapView.performLaunchH5Agreements(url: String) {
    launchActivity(
        WebViewActivity::class.java,
        Pair(Constant.URL_TYPE, Constant.OTHER_TYPE),
        Pair(Constant.H5_URL, url)
    )
}

//打开h5链接
fun Activity.performLaunchH5Agreement(url: String, title: String) {
    var intent= Intent(this,WebViewActivity::class.java)
    intent.putExtra(Constant.URL_TYPE, Constant.OTHER_TYPE)
    intent.putExtra(Constant.H5_URL, url)
    if (title.isNotEmpty()) {
        intent.putExtra(Constant.WEB_TITLE, title)
    }
    startActivity(intent)
}


//保存用户数据
fun setUserData(token:String,user_id:String,mobile:String){
    setData(Constant.TOKEN, token)
    setData(Constant.USER_ID, user_id)
    setData(Constant.USER_MOBILE, mobile)
}

//获取验证码
fun IWrapView.getCode(mobile: String, sendTv: TextView, repository: LoginRepository, success: (data: CodeData?) -> Unit = {}) {
    repository.getCode(mobile,
        showLoading = {
            showBaseLoading()
        },
        hideLoading = {
            dismissBaseLoading()
        },
        onSuccess = {
            success(it)
            getCurrentActivity().startCountDowntime(sendTv)
        },
        onFailure = {
            toastError(it)
        }
    )
}

fun AppCompatActivity.startCountDowntime(sendTv: TextView) {
    val time = Constant.SMS_TIME.toInt()
    countDown(time,
        start = {
            sendTv.text = String.format("获取验证码(%s)", time)
            sendTv.alpha = 0.3f
            sendTv.isEnabled = false
        },
        end = {
            sendTv.text = "获取验证码"
            sendTv.alpha = 1.0f
            sendTv.isEnabled = true
        },
        next = {
            sendTv.text = String.format("获取验证码(%s)", it.toString())
            sendTv.alpha = 0.3f
            sendTv.isEnabled = false
        }
    )
}



/*fun IWrapView.getCode(
    mobile: String,
    repository: LoginRepository,
    complete: (code: String, type: String) -> Unit
) {
    repository.getCode(mobile,
        showLoading = {
            showBaseLoading()
        },
        hideLoading = {
            dismissBaseLoading()
        },
        onSuccess = {
            toastSuccess(it)
            showCodeDialog(type, complete)
        },
        onFailure = {
            toastError(it)
        }
    )
}*/

fun IWrapView.showCodeDialog(type: String, complete: (code: String, type: String) -> Unit) {
    val currentActivity = this.getCurrentActivity()
    if (currentActivity.isFinishing) {
        return
    }
    val dialog = CodeDialog(currentActivity) {
        onDialogTitle = { "短信验证码" }
        onCodeMobile = { getUMobile() }
        onCodeType = { type }
        onCodeTime = { 60 }
        onComplete = { code, type ->
            complete(code, type)
        }
    }
    dialog.show()
}

fun showCardImage(image:String,complete: () -> Unit){
    EaseThreadManager.getInstance().runOnMainThread{
        val dialog = CustomDialog.build()
            .setCustomView(object : OnBindView<CustomDialog>(R.layout.pop_card_medal) {
                override fun onBind(dialog: CustomDialog, v: View) {
                    var imageView = v.findViewById<ImageView>(R.id.iv_cover)
                    var textView = v.findViewById<TextView>(R.id.tv_sure)
                    textView.setOnClickListener {
                        complete()
                        dialog.dismiss()
                    }
                    GlideImgManager.get().loadFitCenterImg(image,imageView,com.comm.img_load.R.mipmap.default_icon)
                }
            })
            .show()
    }
}