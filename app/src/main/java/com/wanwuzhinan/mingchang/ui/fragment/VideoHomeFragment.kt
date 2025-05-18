package com.wanwuzhinan.mingchang.ui.fragment

import android.os.Bundle
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.ext.onClick
import com.wanwuzhinan.mingchang.app.AppFragment
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.FragmentVideoHomeBinding
import com.wanwuzhinan.mingchang.entity.LessonSubject
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
        viewModel.getMediaLessonSubjectGroup(ConfigApp.TYPE_AUDIO)
    }


    private fun selectedGroup(
        selectedTab: Int, group: LessonSubjectGroup? = viewModel.getMediaLessonSubjectGroupValue()
    ) {
        if (group == null || group.list.isEmpty()) {
            viewModel.getMediaLessonSubjectGroup(ConfigApp.TYPE_AUDIO)
            return
        }
        val tabSize = group.list.size
        var selected = selectedTab
        if (selected >= tabSize) selected = tabSize - 1
        if (selected <= 0) selected = 0
        this.tab = selected
        val group = group.list[selected]
        selectedLesson(group.id)
    }

    /**
     * 实现缓存
     * 缓存中获取，无网络请求
     */
    fun selectedLesson(groupId: Int) {
        val lessonInfo = viewModel.getMediaLessonInfoValue(groupId)
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