package com.wanwuzhinan.mingchang.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.PagerSnapHelper
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.ext.onClick
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppActivity
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.FragmentVideoHomeBinding
import com.wanwuzhinan.mingchang.entity.LessonQuarter
import com.wanwuzhinan.mingchang.entity.LessonSubjectGroup
import com.wanwuzhinan.mingchang.ext.visible
import com.wanwuzhinan.mingchang.ui.adapter.VideoHomeAdapter
import com.wanwuzhinan.mingchang.vm.MediaViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-13 16:43
 *
 * Des   :VideoHomeActivity
 */
class VideoHomeActivity : AppActivity<FragmentVideoHomeBinding, MediaViewModel>() {
    private var tab = 0
    private lateinit var bgAdapter: VideoHomeAdapter
    private lateinit var cardAdapter: VideoHomeAdapter
    private val pagerSnapHelper: PagerSnapHelper by lazy {
        PagerSnapHelper()
    }

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        bgAdapter = VideoHomeAdapter(R.layout.item_video_home_bg)
        cardAdapter = VideoHomeAdapter(R.layout.item_video_home_item).apply {
            onItemClickListener = { _, item, position ->
                VideoListActivity.start(
                    this@VideoHomeActivity, item.lesson_subject_id.toInt(), position
                )
            }
        }
        tab = getExtrasPosition(bundle, savedInstanceState)
        viewBinding.apply {
            page.adapter = bgAdapter
            list.adapter = cardAdapter
            pagerSnapHelper.attachToRecyclerView(list)
            onClick(ivPro, ivNext) {
                if (it == ivPro) selectedGroup(selected = tab + 1, smooth = true)
                else selectedGroup(selected = tab - 1, smooth = true)
            }
        }
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewModel.apply {
            mediaLessonTab.observe {
                Log.i("mediaLessonTab:$it")
                selectedGroup(it, tab, false)
            }
            mediaLessonTabChild.observe {
                Log.i("mediaLessonTabChild:$it")
                updateChildUI(it.info.lessonQuarter, 0, false)
            }
        }
    }

    override fun loadData(refresh: Boolean) {
        super.loadData(refresh)
        viewModel.getMediaLessonTab(ConfigApp.TYPE_VIDEO)
    }

    private fun selectedGroup(
        group: LessonSubjectGroup? = viewModel.getMediaLessonTabValue(),
        selected: Int,
        smooth: Boolean = false
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
        updateGroupUI(tab, group)
        selectedChild(lesson.id, smooth)
    }

    fun selectedChild(
        id: Int, smooth: Boolean = false
    ) {
        val lessonInfo = viewModel.getMediaLessonTabChildValue(id)?.info
        if (lessonInfo?.lessonQuarter.isNullOrEmpty()) {
            viewModel.getMediaLessonTabChild(id, 0)
        } else updateChildUI(lessonInfo!!.lessonQuarter, 0, smooth = smooth)
    }

    private fun updateGroupUI(tab: Int, group: LessonSubjectGroup) {
        val size = group.list.size
        val isMultiPage = size > 1
        viewBinding.progress.max = size
        viewBinding.tvPage.text = "$tab"
        viewBinding.ivPro.visible(isMultiPage)
        viewBinding.ivNext.visible(isMultiPage)
        viewBinding.progress.visible(isMultiPage)
        viewBinding.tvPage.visible(isMultiPage)
    }

    private fun updateChildUI(
        list: List<LessonQuarter>, position: Int = 0, smooth: Boolean = false
    ) {
        bgAdapter.submitList(list)
        cardAdapter.submitList(list)
        viewBinding.page.offscreenPageLimit = 2
        viewBinding.page.setCurrentItem(position, smooth)
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