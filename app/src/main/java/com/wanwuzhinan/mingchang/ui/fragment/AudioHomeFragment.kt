package com.wanwuzhinan.mingchang.ui.fragment

import android.os.Bundle
import com.colin.library.android.utils.Log
import com.wanwuzhinan.mingchang.app.AppFragment
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.FragmentAudioBinding
import com.wanwuzhinan.mingchang.entity.LessonSubject
import com.wanwuzhinan.mingchang.entity.LessonSubjectGroup
import com.wanwuzhinan.mingchang.vm.MediaViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 22:34
 *
 * Des   :AudioHomeFragment
 */
class AudioHomeFragment : AppFragment<FragmentAudioBinding, MediaViewModel>() {
    private var tab = 0
    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        tab = getExtrasPosition(bundle, savedInstanceState)

        viewModel.apply {
            mediaLessonTab.observe {
                Log.i("mediaLessonTab:$it")
                selectedGroup(tab, it)
            }

            mediaLessonTab.observe {
                Log.i("mediaLessonTab:$it")
                updateLessonUI(it.list)
            }
        }
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewModel.getMediaLessonTab(ConfigApp.TYPE_AUDIO)
    }

    private fun selectedGroup(
        selected: Int, group: LessonSubjectGroup? = viewModel.getMediaLessonTabValue()
    ) {
        if (group == null || group.list.isEmpty()) {
            viewModel.getMediaLessonTab(ConfigApp.TYPE_AUDIO)
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
        val lessonInfo = viewModel.getMediaLessonTabChildValue(id)
        if (lessonInfo == null || lessonInfo.list.isEmpty()) {
            viewModel.getMediaQuarterChild(id)
        } else updateLessonUI(lessonInfo.list)
    }

    private fun updateGroupUI(tab: Int, group: LessonSubjectGroup) {

    }

    private fun updateLessonUI(list: List<LessonSubject>) {

    }
}