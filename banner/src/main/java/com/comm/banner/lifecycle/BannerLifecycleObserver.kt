package com.comm.banner.lifecycle

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.banner.util
 * ClassName: BannerLifecycleObserver
 * Author:ShiMing Shi
 * CreateDate: 2022/10/8 15:26
 * Email:shiming024@163.com
 * Description:
 */
interface BannerLifecycleObserver : LifecycleObserver {

    fun onStop(owner: LifecycleOwner)

    fun onStart(owner: LifecycleOwner)

    fun onDestroy(owner: LifecycleOwner)
}