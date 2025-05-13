package com.wanwuzhinan.mingchang.ui

import android.os.Bundle
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.ext.onClick
import com.wanwuzhinan.mingchang.app.AppActivity
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.FragmentVideoHomeBinding
import com.wanwuzhinan.mingchang.entity.LessonSubject
import com.wanwuzhinan.mingchang.entity.LessonSubjectGroup
import com.wanwuzhinan.mingchang.ext.visible
import com.wanwuzhinan.mingchang.vm.MediaViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-13 16:43
 *
 * Des   :VideoHomeActivity
 */
class VideoHomeActivity : AppActivity<FragmentVideoHomeBinding, MediaViewModel>() {
    var position = 0
    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        position = getExtrasPosition(bundle, savedInstanceState)
        viewBinding.apply {
            onClick(ivPro, ivNext) {
                if (it == ivPro) selectedGroup(position + 1)
                else selectedGroup(position - 1)
            }
        }
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewModel.apply {
            mediaLessonSubjectGroup.observe {
                Log.i("mediaLessonSubjectGroup:$it")
                selectedGroup(position, it)
            }
            mediaLessonInfo.observe {
                Log.i("mediaLessonInfo:$it")
                updateLessonUI(it.list)
            }
        }
        viewModel.getMediaLessonSubjectGroup(ConfigApp.TYPE_AUDIO)
    }


    private fun selectedGroup(
        selected: Int, group: LessonSubjectGroup? = viewModel.getMediaLessonSubjectGroupValue()
    ) {
        if (group == null || group.list.isEmpty()) {
            viewModel.getMediaLessonSubjectGroup(ConfigApp.TYPE_AUDIO)
            return
        }
        val groupSize = group.list.size
        var position = selected
        if (position >= groupSize) position = groupSize - 1
        if (selected < 0) position = 0
        this.position = position
        val group = group.list[position]
        selectedLesson(group.id)
    }

    fun selectedLesson( id: Int) {
        val lessonInfo = viewModel.getMediaLessonInfoValue(id)
        if (lessonInfo == null || lessonInfo.list.isEmpty()) {
            viewModel.getMediaLessonInfo(id)
        } else updateLessonUI(lessonInfo.list)

    }

    private fun updateGroupUI(group: LessonSubjectGroup) {
        val size = group.list.size
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
    }

    private fun updateLessonUI(subject: List<LessonSubject>) {

    }
}