package com.wanwuzhinan.mingchang.thread

import android.os.Process
import java.util.concurrent.ThreadFactory

class BackgroundThreadFactory constructor(threadPriority: Int) : ThreadFactory {

    private var mThreadPriority = 0

    init {
        this.mThreadPriority = threadPriority
    }

    override fun newThread(r: Runnable?): Thread {
        val wrapperRunnable = Runnable {
            try {
                Process.setThreadPriority(mThreadPriority)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            r?.run()
        }
        return Thread(wrapperRunnable)
    }


}