package com.colin.library.android.widget.base

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.colin.library.android.utils.Log

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Date  :2024-09-11
 * Des   :Activity基类:最简单的业务逻辑定义
 */
abstract class BaseActivity : AppCompatActivity(), IBase {
    val TAG = this::class.simpleName!!
    var refresh: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onRestart() {
        Log.d(TAG, "onRestart")
        super.onRestart()
    }

    override fun onStart() {
        Log.d(TAG, "onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "onResume")
        super.onResume()
        if (refresh) {
            refresh = false
            loadData(true)
        }
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) goBack()
        else super.onKeyDown(keyCode, event)
    }

    open fun setContentView(layoutRes: Int, savedInstanceState: Bundle?) {
        super.setContentView(layoutRes)
        initView(intent?.extras, savedInstanceState)
        initData(intent?.extras, savedInstanceState)
    }

    override fun goBack(): Boolean {
        finish()
        return true
    }

    /**
     * 重写Activity的视图设置方法并初始化组件及数据
     *
     * @param view 要设置的视图内容，将作为当前Activity的界面显示
     * @param savedInstanceState 保存的实例状态Bundle对象（可能为null），
     *        包含Activity上次被销毁时的状态数据
     *
     * 执行流程：
     * 1. 调用父类方法完成基础视图设置
     * 2. 初始化视图组件（从Intent参数和保存状态中获取数据）
     * 3. 初始化业务数据（从Intent参数和保存状态中获取数据）
     */
    fun setContentView(view: View, savedInstanceState: Bundle?) {/* 调用父类方法完成基础视图层级设置 */
        super.setContentView(view)

        /* 初始化视图组件：从Intent的extras和保存状态中解析参数 */
        initView(intent?.extras, savedInstanceState)

        /* 初始化业务数据：基于视图参数准备数据层状态 */
        initData(intent?.extras, savedInstanceState)
    }


    /*界面初始化的时候，是否需要检查语言*/
    open fun updateLocal() = false
}