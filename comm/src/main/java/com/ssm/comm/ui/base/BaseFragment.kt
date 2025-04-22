package com.ssm.comm.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.scwang.smartrefresh.layout.SmartRefreshLayout

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.ui.fragment.base
 * ClassName: BaseFragment
 * Author:ShiMing Shi
 * CreateDate: 2022/9/6 12:06
 * Email:shiming024@163.com
 * Description:
 */
abstract class BaseFragment<DB : ViewDataBinding, VM : ViewModel>(var viewModel: VM) : Fragment(),
    IWrapView {

    private var iWrapView: IWrapView? = null
    protected lateinit var mActivity: AppCompatActivity
    lateinit var mDataBinding: DB
    lateinit var mViewModel: VM
    private var rootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        this.mActivity = activity as AppCompatActivity
        if (mActivity is BaseActivity<*, *>) {
            this.iWrapView = (mActivity as BaseActivity<*, *>).iWrapView
        } else {
            iWrapView = WrapViewImpl(mActivity)
        }
        mDataBinding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater, getLayoutId(), container, false
        ) as DB
        rootView = mDataBinding.root
        this.mViewModel = ViewModelProvider(this)[viewModel::class.java]
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initView(savedInstanceState)
        initClick()
        initRequest()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getResult(resultCode: Int) {
        iWrapView?.getResult(resultCode)
    }

    override fun getResult(resultCode: Int, bundle: Bundle?) {
        iWrapView?.getResult(resultCode, bundle)
    }

    override fun getBundle(): Bundle? {
        return iWrapView?.getBundle()
    }

    override fun getEditText(editText: EditText): String {
        return iWrapView?.getEditText(editText).toString()
    }

    override fun <T : AppCompatActivity> launchActivity(clz: Class<T>?) {
        iWrapView?.launchActivity(clz)
    }

    override fun <T : AppCompatActivity> launchActivity(
        clz: Class<T>?,
        vararg pairs: Pair<String, Any?>,
    ) {
        iWrapView?.launchActivity(clz, *pairs)
    }

    override fun <T : AppCompatActivity> launchActivityForResult(clz: Class<T>?, requestCode: Int) {
        iWrapView?.launchActivityForResult(clz, requestCode)
    }

    override fun <T : AppCompatActivity> launchActivityForResult(
        clz: Class<T>?,
        requestCode: Int,
        vararg pairs: Pair<String, Any?>,
    ) {
        iWrapView?.launchActivityForResult(clz, requestCode, *pairs)
    }

    override fun getCurrentActivity(): AppCompatActivity {
        return mActivity
    }

    override fun showKeyboard() {
        iWrapView?.showKeyboard()
    }

    override fun showKeyWord(editText: EditText?) {
        iWrapView?.showKeyWord(editText)
    }

    override fun hideSoftInput() {
        iWrapView?.hideSoftInput()
    }

    override fun closeKeyboard() {
        iWrapView?.closeKeyboard()
    }

    override fun getArrayImages(id: Int): IntArray? {
        return iWrapView?.getArrayImages(id)
    }

    override fun performLaunchWebPage(url: String?) {
        iWrapView?.performLaunchWebPage(url)
    }

    override fun showBaseLoading() {
        iWrapView?.showBaseLoading()
    }

    override fun showBaseLoading(text: String?) {
        iWrapView?.showBaseLoading()
    }

    override fun dismissBaseLoading() {
        iWrapView?.dismissBaseLoading()
    }

    override fun copy(content: String) {
        iWrapView?.copy(content)
    }

    override fun initView() {

    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    open fun initClick() {}

    open fun initRequest() {}

    public fun finishRefreshLayout(refreshLayout: SmartRefreshLayout, more:String) {
        if (refreshLayout != null) {
            refreshLayout.finishRefresh()
            refreshLayout.finishLoadMore()
            refreshLayout.setEnableLoadMore(more.equals("1"))
        }
    }
}