package com.wanwuzhinan.mingchang.adapter

import com.colin.library.android.widget.base.BaseAdapter
import com.colin.library.android.widget.base.BaseViewHolder
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.entity.LessonSubject

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025/5/11 23:06
 *
 * Des   :VideoHomeListAdapter
 */
class VideoHomeCardAdapter() : BaseAdapter<LessonSubject>(layoutRes = R.layout.item_video_home) {
    //    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: SubjectListData.dataBean?) {
//        GlideImgManager.get().loadImg(item!!.image,holder.getView(R.id.riv_image),R.drawable.img_default_icon)
//        holder.setText(R.id.tv_number,"共${item.lessonCount}节")
//        holder.setText(R.id.tv_first,item.name)
//    }
//
//    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
//        return QuickViewHolder(R.layout.item_video_home, parent)
//    }
//
//    fun getViewByPosition(position: Int, @IdRes viewId: Int): View? {
//        val viewHolder = recyclerView.findViewHolderForLayoutPosition(position) as QuickViewHolder? ?: return null
//        return viewHolder.getViewOrNull(viewId)
//    }

    override fun bindListViewHolder(
        holder: BaseViewHolder,
        item: LessonSubject,
        payloads: MutableList<Any>
    ) {
    }
}