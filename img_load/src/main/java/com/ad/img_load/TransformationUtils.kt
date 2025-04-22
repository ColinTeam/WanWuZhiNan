package com.ad.img_load

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.request.target.ImageViewTarget

open class TransformationUtils(private var target: ImageView) : ImageViewTarget<Bitmap>(target) {

    override fun setResource(resource: Bitmap?) {
        if (resource == null){
            return
        }
        view.setImageBitmap(resource)

        //获取原图的宽高
        val width = resource.width
        val height = resource.height

        //获取imageView的宽
        val imageViewWidth = target.width
        //计算缩放比例
        val sy: Float = (imageViewWidth * 0.1).toFloat() / (width * 0.1).toFloat()
        //计算图片等比例放大后的高
        val imageViewHeight = (height * sy).toInt()
        val params = target.layoutParams
        params.height = imageViewHeight
        target.layoutParams = params
    }

}