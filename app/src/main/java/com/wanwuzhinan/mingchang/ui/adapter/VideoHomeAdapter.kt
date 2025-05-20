package com.wanwuzhinan.mingchang.ui.adapter

import com.colin.library.android.utils.ext.onClick
import com.colin.library.android.widget.base.BaseAdapter
import com.colin.library.android.widget.base.BaseViewHolder
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.entity.LessonQuarter
import com.wanwuzhinan.mingchang.utils.load

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025/5/11 23:06
 *
 * Des   :VideoHomeListAdapter
 */
class VideoHomeAdapter(val layoutRes: Int = R.layout.item_video_home_bg) :
    BaseAdapter<LessonQuarter>(layoutRes = layoutRes) {
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
        holder: BaseViewHolder, item: LessonQuarter, position: Int, payloads: MutableList<Any>
    ) {
        holder.getImageView(R.id.ivBg).load(item.image, R.mipmap.bg_video_home)
        holder.itemView.onClick {
            onItemClickListener?.invoke(it, item, position)
        }
    }
}