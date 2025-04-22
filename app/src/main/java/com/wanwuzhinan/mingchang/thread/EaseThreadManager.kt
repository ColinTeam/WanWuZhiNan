package com.wanwuzhinan.mingchang.thread

import android.os.Handler
import android.os.Looper
import android.os.Process
import java.util.concurrent.Executor
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class EaseThreadManager private constructor() {

    companion object {

        @Volatile
        private var instance: EaseThreadManager? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: EaseThreadManager().also { instance = it }
        }
    }

    init {
        init()
    }

    private var mIOThreadExecutor: Executor? = null
    private var mMainThreadHandler: Handler? = null

    private fun init() {
        val numberOfCores = Runtime.getRuntime().availableProcessors()
        val keepAliveTime = 1L
        val keepAliveTimeUnit = TimeUnit.SECONDS
        val taskQueue = LinkedBlockingDeque<Runnable>()
        val mBackgroundThreadFactory = BackgroundThreadFactory(Process.THREAD_PRIORITY_BACKGROUND)
        mIOThreadExecutor = ThreadPoolExecutor(
            numberOfCores,
            numberOfCores * 2,
            keepAliveTime,
            keepAliveTimeUnit,
            taskQueue,
            mBackgroundThreadFactory
        )
        mMainThreadHandler = Handler(Looper.getMainLooper())
    }

    /**
     * 在异步线程执行
     * @param runnable
     */
    fun runOnIOThread(runnable: Runnable){
        mIOThreadExecutor?.execute(runnable)
    }

    /**
     * 在UI线程执行
     * @param runnable
     */
    fun runOnMainThread(runnable: Runnable){
        mMainThreadHandler?.post(runnable)
    }

    /**
     * 判断是否是主线程
     * @return true is main thread
     */
    fun isMainThread(): Boolean{
        return Looper.getMainLooper().thread == Thread.currentThread()
    }
}