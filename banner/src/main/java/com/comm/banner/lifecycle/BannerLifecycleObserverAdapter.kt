package com.comm.banner.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.comm.banner.util.LogUtils

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.banner.util
 * ClassName: BannerLifecycleObserverAdapter
 * Author:ShiMing Shi
 * CreateDate: 2022/10/8 15:23
 * Email:shiming024@163.com
 * Description:
 */
class BannerLifecycleObserverAdapter constructor(
    lifecycleOwner: LifecycleOwner,
    observer: BannerLifecycleObserver,
) : DefaultLifecycleObserver {

    private var mObserver: BannerLifecycleObserver
    private var mLifecycleOwner: LifecycleOwner

    init {
        this.mObserver = observer
        this.mLifecycleOwner = lifecycleOwner
    }

    override fun onStart(owner: LifecycleOwner) {
        LogUtils.d("onStart")
        mObserver.onStart(mLifecycleOwner)
    }

    override fun onStop(owner: LifecycleOwner) {
        LogUtils.d("onStop")
        mObserver.onStop(mLifecycleOwner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        LogUtils.d("onDestroy")
        mObserver.onDestroy(mLifecycleOwner)
    }

}