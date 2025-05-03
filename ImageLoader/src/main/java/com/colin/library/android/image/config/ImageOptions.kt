package com.colin.library.android.image.config

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-01 16:02
 *
 * Des   :ImageOptions
 */
class ImageOptions private constructor(builder: Builder) {
    val baseUrl: String = builder.baseUrl
    val bitmapPoolSize: Float = builder.bitmapPoolSize
    val memoryCacheSize: Float = builder.memoryCacheSize
    val diskCacheDirPath: String? = builder.diskCacheDirPath
    val diskCacheFolderName: String? = builder.diskCacheFolderName
    val diskCacheSize: Long = builder.diskCacheSize

    @DrawableRes
    val placeholder: Int = builder.placeholder

    @DrawableRes
    val error: Int = builder.placeholder

    @DrawableRes
    val fallback: Int = builder.placeholder

    data class Builder(
        val context: Context,
        val baseUrl: String,
        var bitmapPoolSize: Float = .0f,
        var memoryCacheSize: Float = .0f,
        var diskCacheDirPath: String? = null,
        var diskCacheFolderName: String? = null,
        var diskCacheSize: Long = 1 * 1024 * 1024,
        var placeholder: Int = 0,
        var error: Int = 0,
        var fallback: Int = 0
    ) {
        /**
         * Bitmap池size, Bitmap池，值范围 1-3，建议在 Application 中设置
         */
        fun setBitmapPoolSize(@FloatRange(from = 1.0, to = 3.0) bitmapPoolSize: Float) = apply {
            this.bitmapPoolSize = bitmapPoolSize
        }

        /**
         * 内存缓存size, 默认内存缓存, 值范围 1-2 建议在 Application 中设置
         */
        fun setMemoryCacheSize(@FloatRange(from = 1.0, to = 2.0) memoryCacheSize: Float) = apply {
            this.memoryCacheSize = memoryCacheSize
        }

        /**
         * 磁盘缓存文件夹地址
         */
        fun setDiskCacheDirPath(dirPath: String) = apply {
            this.diskCacheDirPath = dirPath
        }

        /**
         * 磁盘缓存文件夹目录，默认 image
         */
        fun setDiskCacheFolderName(folderName: String) = apply {
            this.diskCacheFolderName = folderName
        }

        /**
         * 磁盘缓存size，默认 1G
         */
        fun setDiskCacheSize(size: Long) = apply {
            this.diskCacheSize = size
        }

        /**
         * loadPlaceholderRes
         */
        fun loadPlaceholderRes(@DrawableRes res: Int) = apply {
            this.placeholder = res
        }

        /**
         * loadErrorRes
         */
        fun loadErrorRes(@DrawableRes res: Int) = apply {
            this.error = res
        }

        /**
         * loadFallbackRes
         */
        fun loadFallbackRes(@DrawableRes res: Int) = apply {
            this.fallback = res
        }


        fun build() = ImageOptions(this)
    }
}






