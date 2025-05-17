package com.wanwuzhinan.mingchang.app

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding
import com.colin.library.android.network.data.HttpResult
import com.colin.library.android.utils.Constants
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.ToastUtil
import com.colin.library.android.utils.ext.onClick
import com.colin.library.android.utils.helper.ThreadHelper
import com.colin.library.android.widget.base.BaseActivity
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.entity.HTTP_CONFIRM
import com.wanwuzhinan.mingchang.entity.HTTP_TOKEN_EMPTY
import com.wanwuzhinan.mingchang.entity.HTTP_TOKEN_ERROR
import com.wanwuzhinan.mingchang.ui.LoginActivity
import com.wanwuzhinan.mingchang.ui.pop.LoadingDialog
import kotlinx.coroutines.Runnable
import java.lang.reflect.ParameterizedType


abstract class AppActivity<VB : ViewBinding, VM : AppViewModel> : BaseActivity() {
    internal lateinit var viewBinding: VB
    internal val viewModel: VM by lazy { reflectViewModel() }
    private var loadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        showSystemBars(window, false)
        viewBinding = reflectViewBinding()
        setContentView(viewBinding.root, savedInstanceState)
        viewBinding.root.findViewById<View>(R.id.ivBack)?.onClick {
            if (!interceptorBack()) goBack()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.showLoading.observe {
            showLoading(it)
        }
        viewModel.showToast.observe {
            if (it.code == HTTP_CONFIRM) {
                return@observe
            }
            ToastUtil.show(it.msg)
        }
        viewModel.httpAction.observe {
            if (interceptorHttpAction(it)) return@observe
            if (it.code == HTTP_TOKEN_ERROR || it.code == HTTP_TOKEN_EMPTY) {
                LoginActivity.start(this@AppActivity)
                this@AppActivity.finish()
            }
        }
    }

    override fun onDestroy() {
        loadingDialog?.dismiss()
        loadingDialog = null
        super.onDestroy()
    }

    override fun loadData(refresh: Boolean) {

    }

    /*如果想修改Store 可以重写此方法*/
    internal open fun bindViewModelStore() = viewModelStore

    //是否拦截网络反馈code事件
    internal open fun interceptorHttpAction(action: HttpResult.Action) = false

    //是否拦截返回按钮事件
    internal open fun interceptorBack() = false


    fun showLoading(show: Boolean = false) {
        if (loadingDialog?.isShowing() == show) return
        if (!show) {
            loadingDialog?.dismiss()
            return
        }
        loadingDialog = loadingDialog ?: LoadingDialog.newInstance()
        ThreadHelper.post(Runnable { loadingDialog!!.show(this) })
    }

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

    protected fun getExtrasPosition(bundle: Bundle?, savedInstanceState: Bundle?): Int {
        return savedInstanceState?.getInt(ConfigApp.EXTRAS_POSITION, Constants.ZERO)
            ?: bundle?.getInt(ConfigApp.EXTRAS_POSITION, Constants.ZERO) ?: Constants.ZERO
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // 自定义处理逻辑，避免 Activity 被重建
        if (viewBinding?.root != null) {
            viewBinding!!.root.requestLayout()
        }
    }
}




