package com.ssm.comm.ui.base

import android.os.Build
import androidx.lifecycle.ViewModel
import com.comm.net_work.base.BaseRepository
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import org.reactivestreams.Subscription
import java.util.concurrent.TimeUnit

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.ui.act.base
 * ClassName: BaseViewModel
 * Author:ShiMing Shi
 * CreateDate: 2022/9/3 14:40
 * Email:shiming024@163.com
 * Description:
 */
abstract class BaseViewModel<M : BaseModel, R : BaseRepository>(mRepository: R) : ViewModel() {

    private var mSubscription: Subscription? = null

    open val repository by lazy {
        mRepository
    }

    val img =
        "https://img2.baidu.com/it/u=1814268193,3619863984&fm=253&fmt=auto&app=138&f=JPEG?w=632&h=500"

    fun onStartCountDownTime(
        count: Long = 2L,
        onNext: (aLong: Long?) -> Unit,
        onComplete: () -> Unit,
    ) {
        Flowable.interval(0, 1, TimeUnit.SECONDS)
            .onBackpressureBuffer()
            .take(count)
            .map { aLong ->
                count - aLong
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : org.reactivestreams.Subscriber<Long> {
                    override fun onSubscribe(s: Subscription?) {
                        mSubscription = s
                        s?.request(Long.MAX_VALUE)
                    }

                    override fun onNext(aLong: Long?) {
                        onNext(aLong)
                    }

                    override fun onComplete() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            mSubscription?.cancel()
                        }
                        onComplete()
                    }

                    override fun onError(throwable: Throwable?) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            mSubscription?.cancel()
                        }
                        onComplete()
                        throwable?.printStackTrace()
                    }
                })
    }
}