package com.wanwuzhinan.mingchang.ui.pop

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import com.colin.library.android.utils.ext.onClick
import com.comm.net_work.ByteFormatUtils
import com.ssm.comm.app.appContext
import com.ssm.comm.data.VersionData
import com.ssm.comm.ext.installApk
import com.ssm.comm.ext.isEmpty
import com.ssm.comm.ext.isWebUrlLegal
import com.ssm.comm.ext.toastError
import com.ssm.comm.ext.toastNormal
import com.ssm.comm.utils.LogUtils
import com.tbruyelle.rxpermissions2.RxPermissions
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.databinding.PopVersionBinding
import com.wanwuzhinan.mingchang.thread.EaseThreadManager
import com.zjh.download.download
import com.zjh.download.helper.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File
import java.net.URL

//删除地址
class VersionPop(var context: Activity) :BasePop<PopVersionBinding>(context){

    lateinit var mData:VersionData

    override fun initClick() {
        onClick(mDataBinding.tvUpgradation,mDataBinding.tvNot){
            when (it) {
                mDataBinding.tvUpgradation -> {//立即升级
                    mDataBinding.tvUpgradation.isEnabled = false
                    startDownloadApp(mData.android.version_url)
                }
                mDataBinding.tvNot -> {//暂不更新
                    dismiss()
                }
            }
        }

    }

    fun showPop(data: VersionData) {
        mData=data
        mDataBinding.tvNot.visibility=if(data.android.version_upgrade.equals("1")) View.INVISIBLE else View.VISIBLE
        mDataBinding.tvContent.text = data.android.version_desc

        getApkFileSize(data.android.version_url){
            mDataBinding.tvLength.text = "0/${it}"
        }
        showPop()
    }

    //获取文件大小
    private fun getApkFileSize(url: String, onSuccess: (size: String) -> Unit) {
        EaseThreadManager.getInstance().runOnIOThread {
            try {
                val mURL = URL(url)
                val urlConnection = mURL.openConnection()
                urlConnection.connect()
                val length = urlConnection.contentLength
                EaseThreadManager.getInstance().runOnMainThread {
                    onSuccess(ByteFormatUtils.bytes2kb(length.toLong()))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun startDownloadApp(url: String) {
        RxPermissions.getInstance(mContext)
            .request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .subscribe { granted ->
                if (!granted) {
                    toastNormal("请在设置中开启文件存储权限!")
                    return@subscribe
                }
                if (!isEmpty(url)) {
                    LogUtils.e("下载地址===========================>${url}")
                    if (url.endsWith(".apk")) {
                        downLoadFile(url)
                    } else {
                        launchBrowser(url)
                    }
                } else {
                    toastError("下载链接为空")
                }
            }
    }

    private fun downLoadFile(url: String) {
        val appName = appContext.resources!!.getString(R.string.app_name)
        val saveName = String.format("${appName}.apk")
        val savePath = appContext.filesDir.absolutePath
        val apkFile = File(savePath,saveName)
        if (apkFile.exists()){
            apkFile.delete()
        }
        val scope = CoroutineScope(Dispatchers.IO + Job())
        //创建下载任务
        val downloadTask = scope.download(url,saveName,savePath)
        downloadTask.remove(true)
        //状态监听
        downloadTask.state().onEach {
            when (it) {
                is State.None -> LogUtils.e("downloadTask====================>未开始任务")
                is State.Waiting -> {
                    LogUtils.e("downloadTask====================>等待中")
                    EaseThreadManager.getInstance().runOnMainThread {
                        mDataBinding!!.tvUpgradation.text = "等待中"
                    }
                }
                is State.Downloading -> {
                    EaseThreadManager.getInstance().runOnMainThread {
                        mDataBinding!!.tvUpgradation.text = "下载中"
                    }
                }
                is State.Failed -> {
                    EaseThreadManager.getInstance().runOnMainThread {
                        mDataBinding!!.tvUpgradation.text = "下载失败"
                    }
                }
                is State.Stopped -> {
                    EaseThreadManager.getInstance().runOnMainThread {
                        mDataBinding!!.tvUpgradation.text = "已暂停下载"
                    }
                }
                is State.Succeed -> {
                    EaseThreadManager.getInstance().runOnMainThread {
                        dismiss()
                        mDataBinding!!.tvUpgradation.text = "下载成功"
                        installApk(apkFile)
                    }
                }
            }
        }.launchIn(scope)

        //进度监听
        downloadTask.progress().onEach {
            EaseThreadManager.getInstance().runOnMainThread {
                mDataBinding.groupProgress.visibility=View.VISIBLE
                mDataBinding.tvNot.visibility=View.INVISIBLE

                mDataBinding.tvLength.text="${it.downloadSizeStr()}/${it.totalSizeStr()}"
                mDataBinding.progress.progress=it.percent().toInt()
            }
        }.launchIn(scope)

        //开始下载任务
        downloadTask.start()
    }

    //打开浏览器
    private fun launchBrowser(url: String) {
        if (isEmpty(url)) {
            toastNormal("网页地址为空")
            return
        }
        if (!isWebUrlLegal(url)) {
            toastNormal("网页地址地址不合法")
        }
        val intent = Intent()
        val uri: Uri = Uri.parse(url)
        intent.action = Intent.ACTION_VIEW
        intent.data = uri
        mContext.startActivity(intent)
        dismiss()
    }

    override fun getLayoutID(): Int {
        return R.layout.pop_version
    }
}