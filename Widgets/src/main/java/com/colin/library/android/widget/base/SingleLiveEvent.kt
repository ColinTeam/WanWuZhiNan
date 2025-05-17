package com.colin.library.android.widget.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-17 15:03
 *
 * Des   :SingleLiveEvent
 */
class SingleLiveEvent<T> : MutableLiveData<T>() {
    private val pending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner) { value ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(value)
            }
        }
    }

    override fun setValue(value: T?) {
        dispatchValue()
        super.setValue(value)
    }

    override fun postValue(value: T?) {
        dispatchValue()
        super.postValue(value)
    }

    private fun dispatchValue() {
        pending.set(true)
    }
}

//// 在 ViewModel 中使用
//class MyViewModel : ViewModel() {
//    private val _toastEvent = SingleLiveEvent<String>()
//    val toastEvent: LiveData<String> = _toastEvent
//
//    fun showToast() {
//        _toastEvent.value = "操作成功"
//    }
//}
//
//// Activity/Fragment 中观察
//viewModel.toastEvent.observe(this) { message ->
//    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//}