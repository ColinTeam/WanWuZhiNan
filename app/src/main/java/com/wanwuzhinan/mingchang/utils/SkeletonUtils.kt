package com.wanwuzhinan.mingchang.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.wanwuzhinan.mingchang.R
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen

object SkeletonUtils {

    var mSkeletonList : RecyclerViewSkeletonScreen? = null
    var mSkeletonView : SkeletonScreen? = null

    fun showList(reList:RecyclerView,adapter : Adapter<RecyclerView.ViewHolder>,layout:Int){
        mSkeletonList = Skeleton.bind(reList)
            .adapter(adapter)
            .frozen(false)
            .color(R.color.shimmer_color)
            .load(layout)
            .show()
    }

    fun showView(view: View,layout: Int){
        mSkeletonView = Skeleton.bind(view)
            .color(R.color.shimmer_color)
            .load(layout)
            .show()
    }

    fun hideList(){
       if(mSkeletonList!=null){
           mSkeletonList!!.hide()
       }
    }

    fun hideView(){
        if(mSkeletonView!=null){
            mSkeletonView!!.hide()
        }
    }
}