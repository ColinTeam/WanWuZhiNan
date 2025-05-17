package com.wanwuzhinan.mingchang.app

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.viewbinding.ViewBinding
import com.colin.library.android.utils.Constants
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.helper.ThreadHelper
import com.colin.library.android.widget.base.BaseDialogFragment
import com.wanwuzhinan.mingchang.ui.pop.LoadingDialog
import java.lang.reflect.ParameterizedType


abstract class AppDialogFragment<VB : ViewBinding>() : BaseDialogFragment(Constants.ZERO) {

    private var _viewBinding: VB? = null
    internal val viewBinding: VB get() = _viewBinding!!
    var sure: ((View) -> Unit) = { }
    var cancel: ((View) -> Unit) = { }
    private var loadingDialog: LoadingDialog? = null

    override fun createView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _viewBinding = reflectViewBinding(inflater, container)
        return viewBinding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }
    override fun onDestroyView() {
        _viewBinding = null
        loadingDialog?.dismiss()
        loadingDialog = null
        super.onDestroyView()
    }

    override fun goBack(): Boolean {
        dismiss()
        return true
    }

    override fun loadData(refresh: Boolean) {

    }

    fun showLoading(show: Boolean = false) {
//        if (show) {
//            activity?.showLoadingExt()
//        } else {
//            dismissLoadingExt()
//        }
        if (loadingDialog?.isShowing() == show) return
        if (!show) {
            loadingDialog?.dismiss()
            return
        }
        loadingDialog = loadingDialog ?: LoadingDialog.newInstance()
        ThreadHelper.post(Runnable { loadingDialog!!.show(this) })
    }

    @Suppress("UNCHECKED_CAST")
    private fun <VB : ViewBinding> reflectViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): VB {
        val type = javaClass.genericSuperclass as ParameterizedType
        val cls = type.actualTypeArguments[0] as Class<*>
        try {
            val inflate = cls.getDeclaredMethod(
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

    /**
     * add an observer within the [ViewLifecycleOwner] lifespan
     */
    inline fun <reified OUT : Any> LiveData<out OUT?>.observe(crossinline observer: (OUT) -> Unit) {
        observe(viewLifecycleOwner) { it?.let(observer) }
    }
}
