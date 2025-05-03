package com.colin.library.android.image.glide

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Bitmap
import android.os.Build
import android.text.TextUtils
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorInt
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.colin.library.android.image.glide.transform.GlideCircleTransform
import com.colin.library.android.image.glide.transform.RoundedCornersTransform
import java.io.File
import java.io.FileInputStream
import java.util.Locale
import java.util.regex.Pattern

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025/5/1 21:29
 *
 * Des   :ImageLoader
 */
class GlideImageLoader {

    companion object {
        @Volatile
        private var INSTANCE: GlideImageLoader? = null

        fun getDefault(): GlideImageLoader = INSTANCE ?: synchronized(this) {
            GlideImageLoader().also {
                INSTANCE = it
            }
        }


        fun onLowMemory(application: Application) {
            Glide.get(application).onLowMemory()
        }

        fun onTrimMemory(application: Application, level: Int) {
            Glide.get(application).onTrimMemory(level)
        }

        private fun isWebUrlLegal(url: String): Boolean {
            if (TextUtils.isEmpty(url)) {
                return false
            }
            val regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"
            return Pattern.compile(regex).matcher(url).matches()
        }

        @SuppressLint("CheckResult")
        fun loadImage(
            view: ImageView, source: Any? = null, width: Int? = -1,
            height: Int? = -1, cacheEnable: Boolean? = true, isCircle: Boolean? = false,
            radius: Float? = .0f, borderWidth: Float? = .0f, @ColorInt borderColor: Int? = 0,
        ) {
            val placeholder = getDefault().diskCacheOptions.placeholder
            val error = getDefault().diskCacheOptions.error
            val fallback = getDefault().diskCacheOptions.fallback

            // 计算位图尺寸，如果位图尺寸固定，加载固定大小尺寸的图片，如果位图未设置尺寸，那就加载原图，Glide加载原图时，override参数设置 -1 即可。
            val widthSize = (if ((width ?: 0) > 0) width else view.width) ?: -1
            val heightSize = (if ((height ?: 0) > 0) height else view.height) ?: -1
            // 根据定义的 cacheEnable 参数来决定是否缓存
            val diskCacheStrategy =
                if (cacheEnable == true) DiskCacheStrategy.AUTOMATIC else DiskCacheStrategy.NONE
            // 设置编码格式，在Android 11(R)上面使用高清无损压缩格式 WEBP_LOSSLESS ， Android 11 以下使用PNG格式，PNG格式时会忽略设置的 quality 参数。
            val encodeFormat =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Bitmap.CompressFormat.WEBP_LOSSLESS else Bitmap.CompressFormat.PNG
            val manager = Glide.with(view)

            val build = if (source is String && !isWebUrlLegal(source)) {
                manager.load(
                    String.format(
                        Locale.US, "%s%s", getDefault().diskCacheOptions.baseUrl, source
                    )
                )
            } else {
                manager.load(source)
            }
            val operation = build.placeholder(placeholder).error(error).fallback(fallback)
                .override(widthSize, heightSize)

            if (isCircle == true) {
                if ((borderWidth ?: .0f) > 0 && (borderColor ?: 0) >= 0) {
                    operation.transform(
                        GlideCircleTransform(
                            borderWidth!!.toFloat(), borderColor!!
                        )
                    )
                } else {
                    operation.transform(GlideCircleTransform(.0f, 0))
                }
            } else if ((radius ?: .0f) > 0) {
                if ((borderWidth ?: .0f) > 0 && (borderColor ?: 0) >= 0) {
                    operation.transform(
                        RoundedCornersTransform(
                            COMPLEX_UNIT_DIP,
                            borderWidth!!.toFloat(),
                            RoundedCornersTransform.Border(
                                radius!!.toFloat(), borderColor!!.toInt()
                            )
                        )
                    )
                } else {
                    operation.transform(
                        RoundedCornersTransform(
                            COMPLEX_UNIT_DIP, 0f, RoundedCornersTransform.Border(
                                radius!!.toFloat(), borderColor!!.toInt()
                            )
                        )
                    )
                }
            }
            operation.skipMemoryCache(false)
            //operation.sizeMultiplier(0.0f)
            operation.format(DecodeFormat.PREFER_ARGB_8888)
            operation.encodeFormat(encodeFormat)
            operation.diskCacheStrategy(diskCacheStrategy)
            operation.transition(DrawableTransitionOptions.withCrossFade())
            operation.into(view)
        }


        fun loadImage(view: ImageView, source: Any? = null) {
            val placeholder = getDefault().diskCacheOptions.placeholder
            val error = getDefault().diskCacheOptions.error
            val fallback = getDefault().diskCacheOptions.fallback

            // 根据定义的 cacheEnable 参数来决定是否缓存
            val diskCacheStrategy = DiskCacheStrategy.NONE
            // 设置编码格式，在Android 11(R)上面使用高清无损压缩格式 WEBP_LOSSLESS ， Android 11 以下使用PNG格式，PNG格式时会忽略设置的 quality 参数。
            val encodeFormat =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Bitmap.CompressFormat.WEBP_LOSSLESS else Bitmap.CompressFormat.PNG
            val manager = Glide.with(view)
            val build = manager.load(source)
            val operation = build.placeholder(placeholder)
            operation.error(error)
            operation.fallback(fallback)
            //operation.thumbnail(0.33f)
            //operation.centerCrop()
            //operation.override(widthSize, heightSize)

            operation.skipMemoryCache(false)
            //operation.sizeMultiplier(0.0f)
            operation.format(DecodeFormat.PREFER_ARGB_8888)
            operation.encodeFormat(encodeFormat)
            operation.diskCacheStrategy(diskCacheStrategy)
            operation.transition(DrawableTransitionOptions.withCrossFade())
            operation.into(view)
        }

        fun loadImageRound(view: ImageView, source: Any? = null, radius: Float? = .0f) {
            val placeholder = getDefault().diskCacheOptions.placeholder
            val error = getDefault().diskCacheOptions.error
            val fallback = getDefault().diskCacheOptions.fallback

            // 根据定义的 cacheEnable 参数来决定是否缓存
            val diskCacheStrategy = DiskCacheStrategy.NONE
            // 设置编码格式，在Android 11(R)上面使用高清无损压缩格式 WEBP_LOSSLESS ， Android 11 以下使用PNG格式，PNG格式时会忽略设置的 quality 参数。
            val encodeFormat =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Bitmap.CompressFormat.WEBP_LOSSLESS else Bitmap.CompressFormat.PNG
            val manager = Glide.with(view)
            val build = if (source is String && !isWebUrlLegal(source)) {
                manager.load(String.format("%s%s", getDefault().diskCacheOptions.baseUrl, source))
            } else {
                manager.load(source)
            }
            val operation = build.placeholder(placeholder)
            operation.error(error)
            operation.fallback(fallback)
            //operation.thumbnail(0.33f)
            //operation.centerCrop()
            //operation.override(widthSize, heightSize)

            if ((radius ?: .0f) > 0) {
                operation.transform(
                    RoundedCornersTransform(
                        COMPLEX_UNIT_DIP, 0f, RoundedCornersTransform.Border(radius!!.toFloat(), 0)
                    )
                )
            }
            operation.skipMemoryCache(false)
            //operation.sizeMultiplier(0.0f)
            operation.format(DecodeFormat.PREFER_ARGB_8888)
            operation.encodeFormat(encodeFormat)
            operation.diskCacheStrategy(diskCacheStrategy)
            operation.transition(DrawableTransitionOptions.withCrossFade())
            operation.into(view)
        }

        fun loadCircleImg(imageView: ImageView, imageUrl: Any) {
            loadImage(
                imageView,
                imageUrl,
                -1,
                -1,
                false,
                isCircle = true,
                radius = 0.0f,
                borderWidth = 0.0f,
                borderColor = 0
            )
        }
    }


    val diskCacheOptions by lazy {
        DiskCacheOptions.Builder()
    }

    /**
     * 暂停当前上下文中的Glide请求
     */
    fun pauseRequests(context: Application) {
        Glide.with(context).pauseRequests()
    }

    /**
     * 暂停所有Glide请求
     */
    fun pauseAllRequests(context: Application) {
        Glide.with(context).pauseAllRequests()
    }

    /**
     * 恢复Glide请求
     */
    fun resumeRequests(context: Application) {
        Glide.with(context).resumeRequests()
    }

    /**
     * 清除Glide的磁盘缓存
     */
    fun clearDiskCache(context: Application) {
        Glide.get(context).clearDiskCache()
    }

    /**
     * 清除Glide的磁盘缓存，与上面函数作用一致。获取上下文的方式不同而已。
     */
    fun clear(view: View) {
        Glide.with(view).clear(view)
    }

    /**
     * 清除Glide的内存缓存
     */
    fun clearMemory(context: Application) {
        Glide.get(context).clearMemory()
    }

    /**
     * 清除一些内存缓存，具体数量取决于给应用分配的级别。
     */
    fun trimMemory(context: Application, level: Int) {
        Glide.get(context).trimMemory(level)
    }

    /**
     * 获取磁盘缓存的数据大小，单位：KB
     */
    fun getDiskCacheSize(context: Application): Long {
        val options = diskCacheOptions.build()
        val diskCacheDirPath = options.diskCacheDirPath ?: context.filesDir.path
        val diskCacheFolderName = options.diskCacheFolderName
        val file = File("$diskCacheDirPath${File.separator}$diskCacheFolderName")
        var blockSize = 0L
        try {
            blockSize = if (file.isDirectory) getFileSizes(file) else getFileSize(file)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return blockSize
    }

    private fun getFileSizes(file: File): Long {
        var size = 0L
        file.listFiles()?.forEach {
            if (it.isDirectory) {
                size += getFileSizes(it)
            } else {
                try {
                    size += getFileSize(it)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return size
    }

    private fun getFileSize(file: File): Long {
        var size = 0L
        if (file.exists()) {
            FileInputStream(file).use {
                size = it.available().toLong()
            }
        }
        return size
    }

}