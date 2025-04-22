package com.comm.banner.adapter

import android.view.ViewGroup
import android.widget.ImageView
import com.comm.banner.holder.BannerImageHolder

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.banner.adapter
 * ClassName: BannerImageAdapter
 * Author:ShiMing Shi
 * CreateDate: 2022/10/9 11:31
 * Email:shiming024@163.com
 * Description:
 */
abstract class BannerImageAdapter<T> constructor(datas: MutableList<T>?): BannerAdapter<T, BannerImageHolder>(datas) {

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerImageHolder {
        val imageView = ImageView(parent.context)
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.layoutParams = params
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return BannerImageHolder(imageView)
    }


}