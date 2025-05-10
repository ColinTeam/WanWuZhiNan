package com.wanwuzhinan.mingchang.ui.fragment

import android.os.Bundle
import com.colin.library.android.utils.Log
import com.wanwuzhinan.mingchang.app.AppFragment
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.FragmentAudioBinding
import com.wanwuzhinan.mingchang.vm.MediaViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 22:34
 *
 * Des   :AudioFragment
 */
class SettingTabFragment : AppFragment<FragmentAudioBinding, MediaViewModel>() {

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewModel.apply {
            audioLessonSubjectGroup.observe {
                Log.i("audioLessonSubjectGroup:$it")
            }
            audioLessonQuarter.observe {
                Log.i("audioLessonQuarter:$it")
            }
            lessonInfo.observe {
                Log.i("lessonInfo:$it")
            }
            groupPosition.observe {
                Log.i("groupPosition:$it")
            }
        }
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewModel.apply {
            getAudioLessonSubjectGroup(ConfigApp.TYPE_AUDIO)
            getAudioLessonQuarter(getGroupPositionValue(), 1)
        }
    }

}