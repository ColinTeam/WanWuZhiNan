package com.colin.library.android.image.glide

import android.app.Application
import androidx.annotation.FloatRange

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.ad.img_load
 * ClassName: DiskCacheOptions
 * Author:ShiMing Shi
 * CreateDate: 2022/9/5 11:31
 * Email:shiming024@163.com
 * Description:定义缓存配置项
 */
class DiskCacheOptions private constructor(builder: Builder){

    val bitmapPoolSize: Float
    val memoryCacheSize: Float
    val diskCacheDirPath: String?
    val diskCacheFolderName: String?
    val diskCacheSize: Long
    var placeholder: Int
    var error: Int
    var fallback: Int
    var context: Application?
    var baseUrl: String?

    init{
        this.bitmapPoolSize = builder.bitmapPoolSize
        this.memoryCacheSize = builder.memoryCacheSize
        this.diskCacheDirPath = builder.diskCacheDirPath
        this.diskCacheFolderName = builder.diskCacheFolderName
        this.diskCacheSize = builder.diskCacheSize
        this.placeholder = builder.placeholder
        this.error = builder.error
        this.fallback = builder.fallback
        this.context = builder.context
        this.baseUrl = builder.baseUrl
    }

    data class Builder(
        var bitmapPoolSize: Float = .0f,
        var memoryCacheSize: Float = .0f,
        var diskCacheDirPath: String? = null,
        var diskCacheFolderName: String? = null,
        var diskCacheSize: Long = 1 * 1024 * 1024,
        var placeholder: Int = 0,
        var error: Int = 0,
        var fallback: Int = 0,
        var context: Application? = null,
        var baseUrl: String? = null
    ){
        /**
         * Bitmap池size, Bitmap池，值范围 1-3，建议在 Application 中设置
         */
        fun setBitmapPoolSize(@FloatRange(from = 1.0,to = 3.0) bitmapPoolSize: Float) = apply {
            this.bitmapPoolSize = bitmapPoolSize
        }

        /**
         * 内存缓存size, 默认内存缓存, 值范围 1-2 建议在 Application 中设置
         */
        fun setMemoryCacheSize(@FloatRange(from = 1.0,to = 2.0) memoryCacheSize: Float) = apply {
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
        fun loadPlaceholderRes(resId: Int) = apply {
            this.placeholder = resId
        }

        /**
         * loadErrorRes
         */
        fun loadErrorRes(resId: Int) = apply {
            this.error = resId
        }

        /**
         * loadFallbackRes
         */
        fun loadFallbackRes(resId: Int) = apply {
            this.fallback = resId
        }

        /**
         * withContext
         */
        fun withContext(context: Application) = apply {
            this.context = context
        }

        /**
         * baseUrl
         */
        fun bindBaseUrl(url: String) = apply{
            this.baseUrl = url
        }

        fun build() = DiskCacheOptions(this)
    }
















}