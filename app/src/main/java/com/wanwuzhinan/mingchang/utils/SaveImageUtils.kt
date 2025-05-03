package com.wanwuzhinan.mingchang.utils

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.View
import com.ssm.comm.R
import com.ssm.comm.app.appContext
import com.ssm.comm.ext.toastError
import com.ssm.comm.ext.toastSuccess
import com.tbruyelle.rxpermissions2.RxPermissions
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

object SaveImageUtils {
     fun saveBitmap(view:View, listener: SaveListener) {
        RxPermissions.getInstance(appContext).request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
            .subscribe { granted ->
                if (granted) {
                    listener.show()
                    val bmp = getBitmapByView(view)
                    startSaveBitmap(bmp,listener)
                } else {
                    toastError(R.string.permission_error)
                }
            }
    }

    fun getBitmapByView(view: View): Bitmap {
        val bmp = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return bmp
    }

    @Suppress("DEPRECATION")
    fun startSaveBitmap(bmp: Bitmap, listener: SaveListener) {
        // 新建目录appDir，并把图片存到其下
        val appDir = File(appContext.getExternalFilesDir(null)?.path + "BarcodeBitmap")
        if (!appDir.exists()) {
            appDir.mkdir()
        }
        val fileName = String.format("%s.jpg", System.currentTimeMillis())
        val file = File(appDir, fileName)
        try {
            val fos = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        // 把file里面的图片插入到系统相册中
        try {
            MediaStore.Images.Media.insertImage(
                appContext.contentResolver,
                file.absolutePath,
                fileName,
                null
            )
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        val mH = Handler(Looper.getMainLooper())
        mH.postDelayed({
            listener.dismiss()
            toastSuccess("图片已保存")
        }, 300)
        appContext.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)))
    }
}