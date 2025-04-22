package com.comm.banner.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
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
) : LifecycleObserver {

    private var mObserver: BannerLifecycleObserver
    private var mLifecycleOwner: LifecycleOwner

    init{
        this.mObserver = observer
        this.mLifecycleOwner = lifecycleOwner
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart(){
        LogUtils.d("onStart")
        mObserver.onStart(mLifecycleOwner)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop(){
        LogUtils.d("onStop")
        mObserver.onStop(mLifecycleOwner)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        LogUtils.d("onDestroy")
        mObserver.onDestroy(mLifecycleOwner)
    }


}