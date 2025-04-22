package com.comm.banner.holder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.banner.holder
 * ClassName: BannerImageHolder
 * Author:ShiMing Shi
 * CreateDate: 2022/10/9 11:40
 * Email:shiming024@163.com
 * Description:
 */
class BannerImageHolder(view: View) : RecyclerView.ViewHolder(view) {

    var imageView: ImageView

    init {
        this.imageView = view as ImageView
    }

}