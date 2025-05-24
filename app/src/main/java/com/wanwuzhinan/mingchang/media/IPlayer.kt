package com.wanwuzhinan.mingchang.media

import android.net.Uri

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-24 09:56
 *
 * Des   :IPlayer 定义标准 播放器的作用
 * 1. 播放
 * 2. 暂停
 * 3. 切换播放、暂停
 * 4.
 * 4. mute
 * 5. vole
 * 6. seekTo
 *
 * //    const val STATE_IDLE: Int = 1
 * //    const val STATE_BUFFERING: Int = 2
 * //    const val STATE_READY: Int = 3
 * //    const val STATE_ENDED: Int = 4
 */
interface IPlayer {
    fun play()
    fun play(uri: Uri?)
    fun pause()
    fun toggle()
    fun getState(): Int
    fun mute()
    fun volume()
    fun seekTo(progress: Float)
}