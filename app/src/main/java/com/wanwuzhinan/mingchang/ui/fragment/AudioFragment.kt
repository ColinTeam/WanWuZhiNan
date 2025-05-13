package com.wanwuzhinan.mingchang.ui.fragment

import android.os.Bundle
import com.colin.library.android.utils.Log
import com.wanwuzhinan.mingchang.app.AppFragment
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.FragmentAudioBinding
import com.wanwuzhinan.mingchang.entity.LessonSubjectGroup
import com.wanwuzhinan.mingchang.vm.MediaViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 22:34
 *
 * Des   :AudioFragment
 */
class AudioFragment : AppFragment<FragmentAudioBinding, MediaViewModel>() {

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
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
            mediaLessonInfo.observe {
                Log.i("mediaLessonQuarter:$it")
                displayLesson(it)
            }
        }
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewModel.apply {
            getMediaLessonSubjectGroup(ConfigApp.TYPE_AUDIO)
        }
    }

    fun displayGroup(group: LessonSubjectGroup) {


    }


    fun displayLesson(lesson: LessonSubjectGroup) {

    }
}