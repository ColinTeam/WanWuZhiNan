package com.wanwuzhinan.mingchang.ui.fragment

import android.os.Bundle
import com.colin.library.android.utils.Log
import com.tencent.liteav.demo.superplayer.SuperPlayerView
import com.tencent.liteav.demo.superplayer.model.ISuperPlayerListener
import com.tencent.rtmp.TXVodPlayer
import com.wanwuzhinan.mingchang.app.AppFragment
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.FragmentVideoHomeBinding
import com.wanwuzhinan.mingchang.entity.LessonSubjectGroup
import com.wanwuzhinan.mingchang.ext.visible
import com.wanwuzhinan.mingchang.vm.MediaViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-11 18:01
 *
 * Des   :VideoHomeFragment
 */
class VideoHomeFragment : AppFragment<FragmentVideoHomeBinding, MediaViewModel>() {
    private var groupID = 0
    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {

        viewBinding.apply {
//            playerView.setSuperPlayerListener(superPlayerListener)
//            playerView.setPlayerViewCallback(superPlayerViewCallback)
        }
        viewModel.apply {
            mediaLessonSubjectGroup.observe {
                Log.i("mediaLessonSubjectGroup:$it")
                val groupSize = it.list.size
                var position = getGroupPositionValue()
                if (position >= groupSize) position = groupSize - 1
                else if (position < 0) position = 0
                updateGroupPosition(position)
            }
            groupPosition.observe {
                Log.i("groupPosition:$it")
                val group = getMediaLessonSubjectGroupValue() ?: return@observe
                getMediaLessonQuarter(group.list[it].groupId, 1)
                displayGroup(group)
            }
            mediaLessonQuarter.observe {
                Log.i("mediaLessonQuarter:$it")
                displayLesson(it)
            }
        }
    }

    fun displayGroup(group: LessonSubjectGroup) {


    }


    fun displayLesson(lesson: LessonSubjectGroup) {

    }

    private fun updateGroup(group: LessonSubjectGroup, index: Int = 0) {
        val size = group.list.size
        val position = if (index >= size) size - 1 else if (index < 0) 0 else index
        //组标题
        if (size > 1) {
            viewBinding.progress.max = size
            viewBinding.ivPro.visible(true)
            viewBinding.ivNext.visible(true)
            viewBinding.progress.visible(true)
            viewBinding.tvPage.visible(true)
        } else {
            viewBinding.ivPro.visible(false)
            viewBinding.ivNext.visible(false)
            viewBinding.progress.visible(false)
            viewBinding.tvPage.visible(false)
        }
        viewModel.updateGroupPosition(position)
    }

    fun selectedGroup(group: LessonSubjectGroup?, position: Int) {
        if (group == null || group.list.isEmpty() || group.list.size <= position) return
        val lesson = group.list[position]
        //考虑缓存不
        viewModel.getMediaLessonSubjectGroup()
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewModel.getMediaLessonSubjectGroup(ConfigApp.TYPE_VIDEO)
    }

    override fun onResume() {
        super.onResume()
    }


    override fun onPause() {
        super.onPause()
    }

    override fun onDestroyView() {

        super.onDestroyView()
    }

    private fun play(url: String) {

    }

    private val superPlayerListener = object : ISuperPlayerListener {
        override fun onVodPlayEvent(
            player: TXVodPlayer?, event: Int, param: Bundle?
        ) {
            Log.e("onVodPlayEvent event:$event\t param:$param")
        }

        override fun onVodNetStatus(
            player: TXVodPlayer?, status: Bundle?
        ) {
            Log.e("onVodNetStatus status:$status")
        }

        override fun onLivePlayEvent(event: Int, param: Bundle?) {
            Log.e("onLivePlayEvent event:$event\t param:$param")
        }

        override fun onLiveNetStatus(status: Bundle?) {
            Log.e("onLiveNetStatus status:$status")
        }
    }

    private val superPlayerViewCallback = object : SuperPlayerView.OnSuperPlayerViewCallback {
        override fun onStartFullScreenPlay() {
            Log.e("onShowCacheListClick")
        }

        override fun onStopFullScreenPlay() {
            Log.e("onShowCacheListClick")
        }

        override fun onClickFloatCloseBtn() {
            Log.e("onShowCacheListClick")
        }

        override fun onClickSmallReturnBtn() {
            Log.e("onShowCacheListClick")
        }

        override fun onStartFloatWindowPlay() {
            Log.e("onShowCacheListClick")
        }

        override fun onPlaying() {
            Log.e("onShowCacheListClick")
        }

        override fun onPlayEnd() {
            Log.e("onShowCacheListClick")
        }

        override fun onError(code: Int) {
            Log.e("onError code:$code")
        }

        override fun onShowCacheListClick() {
            Log.e("onShowCacheListClick")
        }

    }
}