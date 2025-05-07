package com.wanwuzhinan.mingchang.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding
import com.colin.library.android.utils.Log
import com.colin.library.android.widget.base.BaseActivity
import com.wanwuzhinan.mingchang.entity.Config
import java.lang.reflect.ParameterizedType


abstract class AppActivity<VB : ViewBinding, VM : AppViewModel> : BaseActivity() {
    internal lateinit var viewBinding: VB
    internal val viewModel: VM by lazy { reflectViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        viewBinding = reflectViewBinding()
        setContentView(viewBinding.root, savedInstanceState)
    }

    override fun loadData(refresh: Boolean) {

    }

    /*如果想修改Store 可以重写此方法*/
    open fun bindViewModelStore() = viewModelStore


    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class)
    private fun <VB : ViewBinding> reflectViewBinding(): VB {
        try {
            val clazz = getActualClass<VB>(0)
            val inflate = clazz.getDeclaredMethod("inflate", LayoutInflater::class.java)
            return inflate.invoke(null, layoutInflater) as VB
        } catch (e: Exception) {
            Log.log(e)
        }
        throw IllegalStateException("reflectViewBinding fail in $TAG")
    }

    @Throws(IllegalStateException::class)
    private fun <VM : AppViewModel> reflectViewModel(): VM {
        try {
            return ViewModelProvider.create(bindViewModelStore())[getActualClass(1)]
        } catch (e: Exception) {
            Log.log(e)
        }
        throw IllegalStateException("reflectViewModel fail in $TAG")
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class)
    private fun <T> getActualClass(index: Int): Class<T> {
        return (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[index] as Class<T>
    }

    /**
     * add an observer within the [ViewModelStoreOwner] lifespan
     */
    inline fun <reified OUT : Any> LiveData<out OUT?>.observe(crossinline observer: (OUT) -> Unit) {
        observe(this@AppActivity) { it?.let(observer) }
    }

    fun showSystemBars(window: Window, show: Boolean = true) {
        val controller = WindowCompat.getInsetsController(window, window.decorView)
        if (show) {
            controller.show(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
        } else {
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

}




