package com.wanwuzhinan.mingchang.ext

import android.graphics.Bitmap
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.colin.library.android.image.glide.GlideImgManager
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.kongzue.dialogx.dialogs.CustomDialog
import com.kongzue.dialogx.interfaces.OnBindView
import com.ssm.comm.config.Constant
import com.ssm.comm.ext.toastError
import com.ssm.comm.ui.base.IWrapView
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.entity.ConfigData
import com.wanwuzhinan.mingchang.entity.UserInfo
import com.wanwuzhinan.mingchang.thread.EaseThreadManager
import com.wanwuzhinan.mingchang.ui.publics.WebGoodsViewActivity
import com.wanwuzhinan.mingchang.utils.MMKVUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Hashtable
import java.util.Locale


fun IWrapView.performLaunchGoodsDetail(goodsId: String) {
    launchActivity(
        WebGoodsViewActivity::class.java, Pair(
            Constant.GOODS_ID, goodsId
        )
    )
}


fun convertTimestampToDateTime(timestamp: Long): String {
    val date = Date(timestamp * 1000)
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return format.format(date)
}

fun getUserInfo(): UserInfo {
    val decodeString = MMKVUtils.decodeString(Constant.USER_INFO)
    return if (decodeString.isEmpty()) {
        UserInfo()
    } else {
        Gson().fromJson(decodeString, UserInfo::class.java)
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



fun editTips(vararg edit: TextView?): Boolean {
    edit.forEach {
        val text = it?.text.toString()
        val hint = it?.hint.toString()
        if (TextUtils.isEmpty(text)) {
            toastError(hint)
            return false
        }
    }

    return true
}





fun showCardImage(image: String, complete: () -> Unit) {
    EaseThreadManager.getInstance().runOnMainThread {
        val dialog = CustomDialog.build()
            .setCustomView(object : OnBindView<CustomDialog>(R.layout.pop_card_medal) {
                override fun onBind(dialog: CustomDialog, v: View) {
                    var imageView = v.findViewById<ImageView>(R.id.iv_cover)
                    var textView = v.findViewById<TextView>(R.id.tv_sure)
                    textView.setOnClickListener {
                        complete()
                        dialog.dismiss()
                    }
                    GlideImgManager.get().loadFitCenterImg(image, imageView, R.mipmap.default_icon)
                }
            }).show()
    }
}