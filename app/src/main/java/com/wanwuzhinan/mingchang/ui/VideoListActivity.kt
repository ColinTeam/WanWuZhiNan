package com.wanwuzhinan.mingchang.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.colin.library.android.utils.Log
import com.wanwuzhinan.mingchang.app.AppActivity
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.FragmentVideoListBinding
import com.wanwuzhinan.mingchang.entity.LessonSubjectGroup
import com.wanwuzhinan.mingchang.ui.adapter.VideoListAdapter
import com.wanwuzhinan.mingchang.ui.adapter.VideoListTabAdapter
import com.wanwuzhinan.mingchang.vm.MediaViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-13 16:43
 *
 * Des   :VideoHomeActivity
 */
class VideoListActivity : AppActivity<FragmentVideoListBinding, MediaViewModel>() {
    private var id = 0
    private var position = 0
    private lateinit var tabAdapter: VideoListTabAdapter
    private lateinit var listAdapter: VideoListAdapter
    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        id = bundle?.getInt(ConfigApp.EXTRAS_ID, 0) ?: 0
        position = getExtrasPosition(bundle, savedInstanceState)
        tabAdapter = VideoListTabAdapter()
        listAdapter = VideoListAdapter()
        viewBinding.apply {
            listChapter.adapter = tabAdapter
            list.adapter = listAdapter
        }
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewModel.apply {
            mediaLessonTab.observe {
                Log.i("mediaLessonTab:$it")
                selectedGroup(it, position)
            }
        }
    }

    override fun loadData(refresh: Boolean) {
        viewModel.getMediaLessonQuarter(id, 1)
    }

    private fun selectedGroup(
        group: LessonSubjectGroup? = viewModel.getMediaLessonTabValue(), selected: Int
    ) {
        val list = group?.list ?: return
        //校验tab有效性
        val groupSize = list.size
        var tab = selected
        if (tab >= groupSize) tab = groupSize - 1
        if (tab < 0) tab = 0
//        this.position = tab
//        tabAdapter.submitList(group.list)
//        tabAdapter.selected = position
//        updateGroupUI(tab, group)
    }

    private fun updateGroupUI(position: Int, group: LessonSubjectGroup) {
        listAdapter.submitList(group.list[position].lessons)
    }

    companion object {
        @JvmStatic
        fun start(context: Context, id: Int, position: Int = 0) {
            val starter = Intent(context, VideoListActivity::class.java).putExtra(
                ConfigApp.EXTRAS_POSITION, position
            ).putExtra(
                ConfigApp.EXTRAS_ID, id
            )
            context.startActivity(starter)
        }
    }
}