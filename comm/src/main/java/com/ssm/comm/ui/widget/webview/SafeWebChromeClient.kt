package com.ssm.comm.ui.widget.webview

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.ssm.comm.R
import com.ssm.comm.ext.isEmpty
import com.ssm.comm.ext.toastError
import com.ssm.comm.media.MediaManager
import com.ssm.comm.ui.widget.BaseToolBar
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tencent.smtt.export.external.interfaces.ConsoleMessage
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient
import com.tencent.smtt.export.external.interfaces.JsPromptResult
import com.tencent.smtt.export.external.interfaces.JsResult
import com.tencent.smtt.sdk.ValueCallback
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import java.io.File

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.ui.widget.webview
 * ClassName: SafeWebChromeClient
 * Author:ShiMing Shi
 * CreateDate: 2022/9/15 12:25
 * Email:shiming024@163.com
 * Description:
 */
class SafeWebChromeClient constructor(toolBar: BaseToolBar, progressBar: ProgressBar) :
    WebChromeClient() {

    private var mToolBar: BaseToolBar
    private var mProgressBar: ProgressBar
    private var uploadMessageAboveL: ValueCallback<Array<Uri>>? = null
    private var mUploadCallBack: ValueCallback<Array<Uri>>? = null

    init {
        this.mToolBar = toolBar
        this.mProgressBar = progressBar
    }

    //输出 Web 端日志
    override fun onConsoleMessage(msg: ConsoleMessage?): Boolean {
        return super.onConsoleMessage(msg)
    }

    //加载进度回调
    override fun onProgressChanged(webView: WebView?, newProgress: Int) {
        super.onProgressChanged(webView, newProgress)
        mProgressBar.progress = newProgress
    }

    //Js 中调用 alert() 函数，产生的对话框
    override fun onJsAlert(
        webView: WebView?,
        url: String?,
        message: String?,
        jsResult: JsResult?
    ): Boolean {
        return super.onJsAlert(webView, url, message, jsResult)
    }

    //处理 JS 中的 Confirm对话框
    override fun onJsConfirm(
        webView: WebView?,
        url: String?,
        message: String?,
        jsResult: JsResult?
    ): Boolean {
        return super.onJsConfirm(webView, url, message, jsResult)
    }


    //处理 JS 中的 Prompt对话框
    override fun onJsPrompt(
        webView: WebView?,
        url: String?,
        message: String?,
        defaultValue: String?,
        jsPromptResult: JsPromptResult?
    ): Boolean {
        return super.onJsPrompt(webView, url, message, defaultValue, jsPromptResult)
    }

    //获取网页标题
    override fun onReceivedTitle(webView: WebView?, title: String?) {
        super.onReceivedTitle(webView, title)
        mToolBar.setTitleText(title)
    }

    //获取网页icon
    override fun onReceivedIcon(webView: WebView?, bitmap: Bitmap?) {
        super.onReceivedIcon(webView, bitmap)
    }

    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        this@SafeWebChromeClient.uploadMessageAboveL = filePathCallback
        showFileChooser()
        return true
    }


    private fun showFileChooser() {
        RxPermissions.getInstance(mToolBar.context)
            .request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .subscribe { granted ->
                if (granted) {
                    performOpenAlbum()
                } else {
                    clearUploadMessage()
                    toastError(R.string.permission_error)
                }
            }
    }

    private fun performOpenAlbum() {
        MediaManager.openAlbum(
            mProgressBar.context as AppCompatActivity,
            object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: ArrayList<LocalMedia?>) {
                    val file = getResultFile(result)
                    if (file != null) {
                        val uriList = mutableListOf<Uri>()
                        uriList.add(Uri.parse(file.toString()))
                        val uriArray = uriList.toTypedArray()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            if (uploadMessageAboveL != null) {
                                uploadMessageAboveL!!.onReceiveValue(uriArray)
                                uploadMessageAboveL = null
                            }
                        } else if (mUploadCallBack != null) {
                            mUploadCallBack!!.onReceiveValue(uriArray)
                            mUploadCallBack = null
                        }
                    } else {
                        clearUploadMessage()
                    }
                }

                override fun onCancel() {
                    clearUploadMessage()
                }
            })
    }

    private fun clearUploadMessage() {
        if (uploadMessageAboveL != null) {
            uploadMessageAboveL!!.onReceiveValue(null)
            uploadMessageAboveL = null
        }
        if (mUploadCallBack != null) {
            mUploadCallBack!!.onReceiveValue(null)
            mUploadCallBack = null
        }
    }

    private fun getResultFile(result: MutableList<LocalMedia?>?): File? {
        if (result != null && result.size > 0) {
            val media = result[0]
            val path = media?.realPath
            if (isEmpty(path)) {
                return null
            }
            val file = File(path!!)
            if (!file.exists()) {
                return null
            }
            return file
        }
        return null
    }

}