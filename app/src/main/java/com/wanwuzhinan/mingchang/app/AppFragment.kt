package com.wanwuzhinan.mingchang.app

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.colin.library.android.network.data.HttpResult
import com.colin.library.android.utils.Constants
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.ToastUtil
import com.colin.library.android.utils.ext.onClick
import com.colin.library.android.utils.helper.ThreadHelper
import com.colin.library.android.widget.base.BaseFragment
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.entity.HTTP_TOKEN_EMPTY
import com.wanwuzhinan.mingchang.entity.HTTP_TOKEN_ERROR
import com.wanwuzhinan.mingchang.ui.LoginActivity
import com.wanwuzhinan.mingchang.ui.pop.LoadingDialog
import kotlinx.coroutines.Runnable
import java.lang.reflect.ParameterizedType


abstract class AppFragment<VB : ViewBinding, VM : AppViewModel> : BaseFragment() {
    private var _viewBinding: VB? = null
    internal val viewBinding: VB get() = _viewBinding!!
    internal val viewModel: VM by lazy { reflectViewModel() }
    private var loadingDialog: LoadingDialog? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _viewBinding = reflectViewBinding(inflater, container)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.root.findViewById<View>(R.id.ivBack)?.onClick { goBack() }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    isEnabled = goBack()
                }
            })
    }

    private fun httpAction(action: HttpResult.Action) {
        if (interceptorHttpAction(action)) return
        if (action.code == HTTP_TOKEN_ERROR || action.code == HTTP_TOKEN_EMPTY) {
            activity?.let { act ->
                LoginActivity.start(act)
                act.finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.showLoading.observe {
            showLoading(it)
        }
        viewModel.showToast.observe {
            ToastUtil.show(it.msg)
        }
        viewModel.httpAction.observe {
            if (interceptorHttpAction(it)) return@observe
            if (it.code == HTTP_TOKEN_ERROR || it.code == HTTP_TOKEN_EMPTY) {
                LoginActivity.start(requireActivity())
                requireActivity().finish()
            }
        }
    }

    override fun onDestroyView() {
        loadingDialog?.dismiss()
        loadingDialog = null
        _viewBinding = null
        super.onDestroyView()
    }

    override fun loadData(refresh: Boolean) {
    }

    override fun goBack(): Boolean {
        if (interceptorBack()) {
            return true
        }
        return if (findNavController().popBackStack()) {
            true
        } else {
            activity?.finish()
            true
        }
    }

    fun showLoading(show: Boolean = false) {
        if (loadingDialog?.isShowing() == show) return
        if (!show) {
            loadingDialog?.dismiss()
            return
        }
        loadingDialog = loadingDialog ?: LoadingDialog.newInstance()
        ThreadHelper.post(Runnable { loadingDialog!!.show(this) })
    }

    /*如果想修改Store 可以重写此方法*/
    open fun bindViewModelStore() = viewModelStore

    //是否拦截网络反馈code事件
    internal open fun interceptorHttpAction(action: HttpResult.Action) = false

    //是否拦截返回按钮事件
    internal open fun interceptorBack() = false

    /**
     * add an observer within the [ViewLifecycleOwner] lifespan
     */
    inline fun <reified OUT : Any> LiveData<out OUT?>.observe(crossinline observer: (OUT) -> Unit) {
        observe(viewLifecycleOwner) { it?.let(observer) }
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class)
    private fun <VB : ViewBinding> reflectViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): VB {
        try {
            val clazz = getActualClass<VB>(0)
            val inflate = clazz.getDeclaredMethod(
                "inflate",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.javaPrimitiveType
            )
            return inflate.invoke(null, inflater, container, false) as VB
        } catch (e: Exception) {
            Log.log(e)
        }
        throw IllegalArgumentException("ViewBinding.inflate(inflater, container, false) error:$this")
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

    internal fun getExtrasPosition(
        bundle: Bundle?, savedInstanceState: Bundle?, def: Int = Constants.ZERO
    ): Int {
        return savedInstanceState?.getInt(ConfigApp.EXTRAS_POSITION, def) ?: bundle?.getInt(
            ConfigApp.EXTRAS_POSITION, def
        ) ?: def
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (_viewBinding?.root != null) {
            _viewBinding!!.root.requestLayout()
        }
    }

}
