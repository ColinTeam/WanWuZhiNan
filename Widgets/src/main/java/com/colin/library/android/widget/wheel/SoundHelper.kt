package com.colin.library.android.widget.wheel

import android.content.Context
import android.media.SoundPool
import androidx.annotation.RawRes
import kotlin.math.max
import kotlin.math.min

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-05 23:13
 *
 * Des   :SoundHelper
 */
class SoundHelper private constructor() {

    private val soundPool: SoundPool = SoundPool.Builder().build()
    private var soundId: Int = 0
    /**
     * 音频播放音量 range 0.0-1.0
     */
    var soundPlayVolume: Float = 0f
        set(value) { field = min(1f, max(value, 0f)) }

    companion object {
        /**
         * 初始化 SoundHelper
         *
         * @return SoundHelper 对象
         */
        @JvmStatic
        fun obtain(): SoundHelper {
            return SoundHelper()
        }
    }

    /**
     * 加载音频资源
     *
     * @param context 上下文
     * @param resId   音频资源 [RawRes]
     */
    fun load(context: Context, @RawRes resId: Int) {
        soundId = soundPool.load(context, resId, 1)
    }

    /**
     * 播放声音效果
     */
    fun playSoundEffect() {
        if (soundId != 0) { soundPool.play(soundId, soundPlayVolume, soundPlayVolume, 1, 0, 1f) }
    }

    /**
     * 释放SoundPool
     */
    fun release() {
        soundPool.release()
    }
}