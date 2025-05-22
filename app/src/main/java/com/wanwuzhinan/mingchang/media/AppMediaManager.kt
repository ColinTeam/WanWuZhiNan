package com.wanwuzhinan.mingchang.media

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.CacheSpan
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.exoplayer.trackselection.TrackSelector
import com.colin.library.android.utils.helper.UtilHelper
import java.io.File

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-22 10:21
 *
 * Des   :AppMediaManager
 */
@OptIn(UnstableApi::class)
object AppMediaManager {
    var _player: ExoPlayer = createExoPlayer(UtilHelper.getApplication())

    // 缓存配置
    private val cacheDir by lazy { File(UtilHelper.getApplication().cacheDir, "media_cache") }
    private val mediaCache by lazy {
        SimpleCache(
            cacheDir, exoPlayerListener, null, null, false, false
        )
    }


    private fun createExoPlayer(context: Context): ExoPlayer {
        return ExoPlayer.Builder(context).setTrackSelector(createTrack(context))
            .setMediaSourceFactory(createSourceFactory(context)).build()
    }


    private fun createTrack(context: Context): TrackSelector {
        return DefaultTrackSelector(context)
    }

    private fun createSourceFactory(context: Context): MediaSource.Factory {
        val factory = CacheDataSource.Factory().setCache(mediaCache)
            .setUpstreamDataSourceFactory(DefaultDataSource.Factory(context))
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
        return DefaultMediaSourceFactory(factory)
    }

    private fun createHttpDataSource(context: Context): DefaultDataSource {
        return DefaultDataSource(context, "wwzn", true)
    }

    val player get() = _player


    fun onDestroy() {
        _player.release()
    }


    private val exoPlayerListener = object : IExoPlayListener {
        override fun requiresCacheSpanTouches(): Boolean {
            TODO("Not yet implemented")
        }

        override fun onCacheInitialized() {
            TODO("Not yet implemented")
        }

        override fun onStartFile(
            p0: Cache, p1: String, p2: Long, p3: Long
        ) {
            TODO("Not yet implemented")
        }

        override fun onSpanAdded(
            p0: Cache, p1: CacheSpan
        ) {
            TODO("Not yet implemented")
        }

        override fun onSpanRemoved(
            p0: Cache, p1: CacheSpan
        ) {
            TODO("Not yet implemented")
        }

        override fun onSpanTouched(
            p0: Cache, p1: CacheSpan, p2: CacheSpan
        ) {
            TODO("Not yet implemented")
        }

    }
}