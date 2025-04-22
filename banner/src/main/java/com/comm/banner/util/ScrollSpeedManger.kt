package com.comm.banner.util

import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.comm.banner.Banner
import com.comm.banner.itemdecoration.WrapContentLinearLayoutManager
import java.lang.reflect.Field

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.banner.util
 * ClassName: ScrollSpeedManger
 * Author:ShiMing Shi
 * CreateDate: 2022/10/8 14:33
 * Email:shiming024@163.com
 * Description:
 */
class ScrollSpeedManger constructor(banner: Banner<*, *>, manager: WrapContentLinearLayoutManager) :
    WrapContentLinearLayoutManager(banner.context, manager.orientation, false) {

    private var banner: Banner<*, *>

    init {
        this.banner = banner
    }

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView?,
        state: RecyclerView.State?,
        position: Int,
    ) {
        val linearSmoothScroller = object : LinearSmoothScroller(recyclerView?.context) {
            override fun calculateTimeForDeceleration(dx: Int): Int {
                return banner.scrollTime
            }
        }
        linearSmoothScroller.targetPosition = position
        startSmoothScroll(linearSmoothScroller)
    }

    companion object {
        fun reflectLayoutManager(banner: Banner<*, *>) {
            if (banner.scrollTime < 100) {
                return
            }
            try {
                val viewPager2 = banner.viewPager2
                val recyclerView = viewPager2.getChildAt(0) as RecyclerView
                recyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER

                val speedManger = ScrollSpeedManger(
                    banner,
                    recyclerView.layoutManager as WrapContentLinearLayoutManager
                )
                recyclerView.layoutManager = speedManger

                val mLayoutMangerField: Field =
                    ViewPager2::class.java.getDeclaredField("mLayoutManager")
                mLayoutMangerField.isAccessible = true
                mLayoutMangerField.set(viewPager2, speedManger)

                val pageTransformerAdapterField: Field =
                    ViewPager2::class.java.getDeclaredField("mPageTransformerAdapter")
                pageTransformerAdapterField.isAccessible = true
                val mPageTransformerAdapter: Any =
                    pageTransformerAdapterField.get(viewPager2) as Any

                val aClass: Class<*> = mPageTransformerAdapter.javaClass
                val layoutManager: Field = aClass.getDeclaredField("mLayoutManager")
                layoutManager.isAccessible = true
                layoutManager.set(mPageTransformerAdapter,speedManger)

                val scrollEventAdapterField: Field =
                    ViewPager2::class.java.getDeclaredField("mLayoutManager")
                scrollEventAdapterField.isAccessible = true
                val mScrollEventAdapter: Any =
                    scrollEventAdapterField.get(viewPager2) as Any
                val aClass2: Class<*> = mScrollEventAdapter.javaClass
                val layoutManager2: Field = aClass2.getDeclaredField("mLayoutManager")
                layoutManager2.isAccessible = true
                layoutManager2.set(mScrollEventAdapter,speedManger)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}