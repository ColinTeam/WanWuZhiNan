package com.wanwuzhinan.mingchang.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.ToastUtil
import com.colin.library.android.widget.base.BaseFragment
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.receiver.ScreenChangedReceiver
import com.wanwuzhinan.mingchang.utils.navigate
import java.lang.reflect.ParameterizedType


abstract class AppFragment<VB : ViewBinding, VM : AppViewModel> : BaseFragment(),
    ScreenChangedReceiver.OnScreenChangedListener {
    private var _viewBinding: VB? = null
    internal val viewBinding: VB get() = _viewBinding!!
    internal val viewModel: VM by lazy { reflectViewModel() }
    internal val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ScreenChangedReceiver.bind(this)
        requireActivity().onBackPressedDispatcher.addCallback(
            this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Log.e("handleOnBackPressed:$playCompleted")
                    if (playCompleted) {
                        findNavController().popBackStack()
                    }
                }

            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _viewBinding = reflectViewBinding(inflater, container)
        return viewBinding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.apply {
            showError.observe {
                Log.e("error state:$it")
                ToastUtil.show(it.msg)
                if (it.code == 2 || it.code == 4) {
                    navigate(R.id.action_toLogin)
                }
            }
        }
    }

    override fun onDestroyView() {
        _viewBinding = null
        super.onDestroyView()
    }

    override fun loadData(refresh: Boolean) {
    }

    override fun screenChanged(action: String) {
        Log.i(TAG, "screenChanged:$action")
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


}
