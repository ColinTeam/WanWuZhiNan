package com.wanwuzhinan.mingchang.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.viewbinding.ViewBinding
import com.ssm.comm.ext.aspectRatio
import com.wanwuzhinan.mingchang.utils.Log
import java.lang.reflect.ParameterizedType


abstract class AppFragment<VB : ViewBinding, VM : ViewModel> : BaseFragment() {
    private var _viewBinding: VB? = null
    internal val viewBinding: VB get() = _viewBinding!!
    internal val viewModel: VM by lazy { reflectViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(requireContext().aspectRatio)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _viewBinding = reflectViewBinding(inflater, container)
        return viewBinding.root
    }

    override fun onDestroyView() {
        _viewBinding = null
        super.onDestroyView()
    }

    override fun loadData(refresh: Boolean) {
    }

    /*如果想修改Store 可以重写此方法*/
    open fun bindViewModelStore() = viewModelStore

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
    private fun <VM : ViewModel> reflectViewModel(): VM {
        try {
            return ViewModelProvider(
                bindViewModelStore(), ViewModelProvider.NewInstanceFactory(), CreationExtras.Empty
            )[getActualClass(1)]
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


}
