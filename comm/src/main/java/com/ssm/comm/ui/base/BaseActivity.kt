package com.ssm.comm.ui.base

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.ssm.comm.R
import com.ssm.comm.ext.toastError
import com.ssm.comm.utils.NavigationBarUtil
import com.ssm.comm.utils.StatusBarUtils


/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.ui.act.base
 * ClassName: BaseActivity
 * Author:ShiMing Shi
 * CreateDate: 2022/9/3 11:08
 * Email:shiming024@163.com
 * Description:
 */
abstract class BaseActivity<DB : ViewDataBinding,VM : ViewModel>(var viewModel: VM) : AppCompatActivity(),
    IWrapView {

    var iWrapView : IWrapView? = null
    lateinit var mActivity : AppCompatActivity
    lateinit var mDataBinding : DB
    lateinit var mViewModel : VM

    @SuppressLint("SourceLockedOrientationActivity", "NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        NavigationBarUtil.hideNavigationBar(window)
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//        window.setDecorFitsSystemWindows(false)
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        window.navigationBarColor = Color.TRANSPARENT
//        window.statusBarColor = Color.TRANSPARENT

        this.mActivity = this
        this.iWrapView = WrapViewImpl(this)
        val layoutId = getLayoutId()
        if(layoutId <= 0){
            toastError("布局不能小于或等于0")
        }else{
            this.mDataBinding = DataBindingUtil.setContentView(this,layoutId)
            this.mDataBinding.lifecycleOwner = this
            this.mViewModel = ViewModelProvider(this)[viewModel::class.java]
//            setStatusBar()
            StatusBarUtils.setTranslucentStatus(this)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
            initView()
            initView(savedInstanceState)
            initClick()
            initRequest()
            if(isSetStatusBarAndText()){
                setStatusBarAndText()
            }
        }
    }

    open fun isSetStatusBarAndText(): Boolean{
        return true
    }

    @SuppressLint("NewApi")
    private  fun setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            window.setDecorFitsSystemWindows(false)
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.navigationBarColor = Color.TRANSPARENT
            window.statusBarColor = Color.TRANSPARENT
        }
        StatusBarUtils.setStatusBarColor(this, resources!!.getColor(R.color.transparent))
        StatusBarUtils.setStatusBarDarkTheme(this, true)
    }

    override fun getResources(): Resources? {
        val res: Resources? = super.getResources()
        if (res != null) {
            val config: Configuration = res.configuration
            if (config.fontScale !== 1.0f) {
                config.fontScale = 1.0f
                res.updateConfiguration(config, res.displayMetrics)
            }
        }
        return res
    }

    //点击软键盘之外的空白处，隐藏软件盘
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            hideSoftInput()
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return if (window.superDispatchTouchEvent(ev)) {
            true
        } else onTouchEvent(ev)
    }

    override fun onDestroy() {
        mDataBinding.unbind()
        super.onDestroy()
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
        return iWrapView?.getEditText(editText).toString().trim()
    }

    override fun <T : AppCompatActivity> launchActivity(clz: Class<T>?) {
        iWrapView?.launchActivity(clz)
    }

    override fun <T : AppCompatActivity> launchActivity(
        clz: Class<T>?,
        vararg pairs: Pair<String, Any?>
    ) {
        iWrapView?.launchActivity(clz, *pairs)
    }

    override fun <T : AppCompatActivity> launchActivityForResult(clz: Class<T>?, requestCode: Int) {
        iWrapView?.launchActivityForResult(clz,requestCode)
    }

    override fun <T : AppCompatActivity> launchActivityForResult(
        clz: Class<T>?,
        requestCode: Int,
        vararg pairs: Pair<String, Any?>
    ) {
        iWrapView?.launchActivityForResult(clz,requestCode, *pairs)
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

    private fun setStatusBarAndText(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    override fun getCurrentActivity(): AppCompatActivity {
        return mActivity
    }

    override fun copy(content: String) {
        iWrapView!!.copy(content)
    }

    override fun initView() {

    }

    override fun initView(savedInstanceState: Bundle?) {

    }
    open fun initClick() {}

    open fun initRequest(){}

    public fun finishRefreshLayout(refreshLayout: SmartRefreshLayout, more:String) {
        if (refreshLayout != null) {
            refreshLayout.finishRefresh()
            refreshLayout.finishLoadMore()
            refreshLayout.setEnableLoadMore(more.equals("1"))
        }
    }
}