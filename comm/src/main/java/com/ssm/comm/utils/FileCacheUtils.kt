package com.ssm.comm.utils

import com.ssm.comm.app.appContext
import java.io.File
import java.io.IOException
import java.text.DecimalFormat

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.uitil
 * ClassName: FileCacheUtils
 * Author:ShiMing Shi
 * CreateDate: 2022/9/27 11:44
 * Email:shiming024@163.com
 * Description:
 */
object FileCacheUtils {

    fun getTotalFileSize(): Long {
        return getFileSize(appContext.externalCacheDir) +
                getFileSize(appContext.cacheDir) +
                getFileSize(appContext.codeCacheDir)
    }

    fun getFileSize(file: File?): Long {
        var size = 0L
        if (file == null) {
            return 0L
        }
        val fList = file.listFiles() ?: return 0L
        for (element in fList) {
            size += if (element.isDirectory) {
                getFileSize(element)
            } else {
                element.length()
            }
        }
        return size
    }

    fun formatFileSize(fileSize: Long): String {
        if (fileSize <= 0L) {
            return "0kb"
        }
        val df = DecimalFormat("#.00")
        return if (fileSize < 1024) {
            String.format("%sB", df.format(fileSize.toDouble()))
        } else if (fileSize < 1024 * 1024) {
            String.format("%sK", df.format(fileSize.toDouble() / 1024))
        } else if (fileSize < 1024 * 1024 * 1024) {
            String.format("%sM", df.format(fileSize.toDouble() / 1024 / 1024))
        } else {
            String.format("%sG", df.format(fileSize.toDouble() / 1024 / 1024 / 1024))
        }
    }

    fun clearCache(): Boolean {
        return trimCache() && trimCodeCache() && trimExternalCache()
    }

    private fun trimCache(): Boolean {
        return performCache(appContext.cacheDir)
    }

    private fun trimCodeCache(): Boolean {
        return performCache(appContext.codeCacheDir)
    }

    private fun trimExternalCache(): Boolean {
        return performCache(appContext.externalCacheDir)
    }

    private fun performCache(dir: File?): Boolean {
        try {
            if (dir != null && dir.isDirectory) {
                return deleteDir(dir)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    private fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            if (children != null) {
                for (element in children) {
                    val success = deleteDir(File(dir, element))
                    if (!success) {
                        return false
                    }
                }
            }
        }
        return dir?.delete() ?: false
    }

}