package com.wanwuzhinan.mingchang.ui

import android.content.Context
import android.content.Intent
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
    var tab = 0

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        tab = getExtrasPosition(bundle, savedInstanceState)
        viewBinding.apply {
            onClick(ivPro, ivNext) {
                if (it == ivPro) selectedGroup(tab + 1)
                else selectedGroup(tab - 1)
            }
        }
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewModel.apply {
            mediaLessonSubjectGroup.observe {
                Log.i("mediaLessonSubjectGroup:$it")
                selectedGroup(tab, it)
            }
            mediaLessonInfo.observe {
                Log.i("mediaLessonInfo:$it")
                updateLessonUI(it.list)
            }
        }

    }

    override fun loadData(refresh: Boolean) {
        super.loadData(refresh)
        viewModel.getMediaLessonSubjectGroup(ConfigApp.TYPE_AUDIO)
    }

    private fun selectedGroup(
        selected: Int, group: LessonSubjectGroup? = viewModel.getMediaLessonSubjectGroupValue()
    ) {
        if (group == null || group.list.isEmpty()) {
            viewModel.getMediaLessonSubjectGroup(ConfigApp.TYPE_AUDIO)
            return
        }
        //校验tab有效性
        val groupSize = group.list.size
        var tab = selected
        if (tab >= groupSize) tab = groupSize - 1
        if (tab < 0) tab = 0
        this.tab = tab
        val lesson = group.list[tab]
        selectedLesson(lesson.id)
        updateGroupUI(tab, group)
    }

    fun selectedLesson(id: Int) {
        val lessonInfo = viewModel.getMediaLessonInfoValue(id)
        if (lessonInfo == null || lessonInfo.list.isEmpty()) {
            viewModel.getMediaLessonInfo(id)
        } else updateLessonUI(lessonInfo.list)
    }

    private fun updateGroupUI(tab: Int, group: LessonSubjectGroup) {
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

    companion object {
        @JvmStatic
        fun start(context: Context, tab: Int = 0) {
            val starter = Intent(context, VideoHomeActivity::class.java).putExtra(
                ConfigApp.EXTRAS_POSITION, tab
            )
            context.startActivity(starter)
        }
    }
}