package com.wanwuzhinan.mingchang.media

import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.CacheEvictor

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-22 10:33
 *
 * Des   :IExoPlayListener
 */
@UnstableApi
interface IExoPlayListener : CacheEvictor,Player.Listener {

}