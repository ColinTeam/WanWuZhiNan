package com.wanwuzhinan.mingchang.view

import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.view.MotionEvent
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer
import com.wanwuzhinan.mingchang.R

/**
 * 无任何控制ui的播放
 * Created by guoshuyu on 2017/8/6.
 */
class EmptyControlVideo : StandardGSYVideoPlayer {
    constructor(context: Context?, fullFlag: Boolean?) : super(context, fullFlag)
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun getLayoutId(): Int {
        return R.layout.empty_control_video
    }

    override fun touchSurfaceMoveFullLogic(absDeltaX: Float, absDeltaY: Float) {
        super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY)
        //不给触摸快进，如果需要，屏蔽下方代码即可
        mChangePosition = false

        //不给触摸音量，如果需要，屏蔽下方代码即可
        mChangeVolume = false

        //不给触摸亮度，如果需要，屏蔽下方代码即可
        mBrightness = false
    }

    override fun touchDoubleUp(e: MotionEvent) {
        //super.touchDoubleUp();
        //不需要双击暂停
    }

    override fun startWindowFullscreen(
        context: Context?,
        actionBar: Boolean,
        statusBar: Boolean
    ): GSYBaseVideoPlayer {
        val gsyBaseVideoPlayer: GSYBaseVideoPlayer =
            super.startWindowFullscreen(context, actionBar, statusBar)
        val sampleCoverVideo: EmptyControlVideo = gsyBaseVideoPlayer as EmptyControlVideo

        return gsyBaseVideoPlayer
    }

    override fun showSmallVideo(
        size: Point?,
        actionBar: Boolean,
        statusBar: Boolean
    ): GSYBaseVideoPlayer {
        val sampleCoverVideo: EmptyControlVideo =
            super.showSmallVideo(size, actionBar, statusBar) as EmptyControlVideo

        return sampleCoverVideo
    }
}