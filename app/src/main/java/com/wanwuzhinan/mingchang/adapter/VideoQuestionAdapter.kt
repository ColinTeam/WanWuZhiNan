package com.wanwuzhinan.mingchang.adapter

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.widget.ImageView
import com.ad.img_load.glide.manager.GlideImgManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.QuestionListData
import com.wanwuzhinan.mingchang.data.SubjectListData
import java.text.SimpleDateFormat


class VideoQuestionAdapter : BaseQuickAdapter<SubjectListData.lessonBean,QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: SubjectListData.lessonBean?) {
        holder.setText(R.id.tv_title,item!!.name)
            .setText(R.id.tv_time,format(item!!.video_duration.toLong()*1000))
            .setImageResource(R.id.iv_quest_score_1,if(item.study_is_success==1) R.mipmap.ic_quest_sorce_s else R.mipmap.ic_quest_sorce_n)
            .setImageResource(R.id.iv_quest_score_2,if(item.study_is_success==1) R.mipmap.ic_quest_sorce_s else R.mipmap.ic_quest_sorce_n)
            .setImageResource(R.id.iv_quest_score_3,if(item.study_is_success==1) R.mipmap.ic_quest_sorce_s else R.mipmap.ic_quest_sorce_n)
             .setTextColor(R.id.tv_title,if(item.study_is_success==1) Color.parseColor("#403210") else Color.parseColor("#81765B"))
            .setTextColor(R.id.tv_subtitle,if(item.study_is_success==1) Color.parseColor("#403210") else Color.parseColor("#81765B"))
            .setGone(R.id.ll_no_study,item.study_is_success==1)
            .setGone(R.id.ll_answer,item.study_is_success!=1)
            .setImageResource(R.id.iv_star_1,if (item.answerAccuracy > 10) R.drawable.ic_star_s else R.drawable.ic_star_n)
            .setImageResource(R.id.iv_star_2,if (item.answerAccuracy > 30) R.drawable.ic_star_s else R.drawable.ic_star_n)
            .setImageResource(R.id.iv_star_3,if (item.answerAccuracy > 50) R.drawable.ic_star_s else R.drawable.ic_star_n)
            .setImageResource(R.id.iv_star_4,if (item.answerAccuracy > 70) R.drawable.ic_star_s else R.drawable.ic_star_n)
            .setImageResource(R.id.iv_star_5,if (item.answerAccuracy > 90) R.drawable.ic_star_s else R.drawable.ic_star_n)
            .setGone(R.id.iv_cover_cover,item.study_is_success==1)
            .setBackgroundResource(R.id.cl_bg,if (item.is_video.toInt() == 0) R.mipmap.bg_video_quesiton_item_n else R.mipmap.bg_video_question_item)
            .setGone(R.id.tv_cover,item.is_video.toInt() != 0)
            .setImageResource(R.id.iv_play,if (item.is_video.toInt() == 0) R.mipmap.ic_video_question_play_gray else R.mipmap.ic_video_question_play)

        var ivCover = holder.getView<ImageView>(R.id.iv_cover)
        GlideImgManager.get().loadImg(item!!.image,ivCover,R.mipmap.class_default)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_video_question, parent)
    }

    fun format(position: Long): String {
        val sdf = SimpleDateFormat("mm:ss")
        return sdf.format(position)
    }

}