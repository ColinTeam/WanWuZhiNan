package com.wanwuzhinan.mingchang.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.viewpager2.widget.ViewPager2
import com.colin.library.android.utils.Log
import com.colin.library.android.widget.page.transformer.VideoPageTransformer
import com.wanwuzhinan.mingchang.app.AppActivity
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.FragmentVideoHomeBinding
import com.wanwuzhinan.mingchang.entity.LessonSubject
import com.wanwuzhinan.mingchang.entity.LessonSubjectGroup
import com.wanwuzhinan.mingchang.ui.adapter.BannerAdapter
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
//    var bgAdapter:VideoH

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        tab = getExtrasPosition(bundle, savedInstanceState)
        viewBinding.apply {
            onClick(ivPro, ivNext) {
                if (it == ivPro) selectedGroup(tab + 1)
                else selectedGroup(tab - 1)
            }
        }


        val adapter = BannerAdapter()
        viewBinding.pager.adapter = adapter
        viewBinding.pager.setPageTransformer(VideoPageTransformer())
        viewBinding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, delayMillis)
            }
        })
        viewBinding.pager.offscreenPageLimit = 5 // 预加载前后各一页
        handler.postDelayed(runnable, delayMillis)
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewModel.apply {
            mediaLessonTab.observe {
                Log.i("mediaLessonTab:$it")
                selectedGroup(tab, it)
            }
            mediaLessonTabChild.observe {
                Log.i("mediaLessonTabChild:$it")
                updateLessonUI(it.list)
            }
        }
    }

    override fun loadData(refresh: Boolean) {
        super.loadData(refresh)
        viewModel.getMediaLessonTab(ConfigApp.TYPE_VIDEO)
    }

    private fun selectedGroup(
        selected: Int, group: LessonSubjectGroup? = viewModel.getMediaLessonTabValue()
    ) {
        if (group == null || group.list.isEmpty()) {
            viewModel.getMediaLessonTab(ConfigApp.TYPE_VIDEO)
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
            viewModel.getMediaLessonTabChild(id, 0)
        } else updateLessonUI(lessonInfo.list)
    }

    private fun updateGroupUI(tab: Int, group: LessonSubjectGroup) {
        val size = group.list.size
        val isMultiPage = size > 1
        viewBinding.progress.max = size
        viewBinding.ivPro.visible(isMultiPage)
        viewBinding.ivNext.visible(isMultiPage)
        viewBinding.progress.visible(isMultiPage)
        viewBinding.tvPage.visible(isMultiPage)
    }

    private fun updateLessonUI(list: List<LessonSubject>) {

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