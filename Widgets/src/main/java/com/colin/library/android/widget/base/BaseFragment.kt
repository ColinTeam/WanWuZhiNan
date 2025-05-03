package com.colin.library.android.widget.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.colin.library.android.utils.Log

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Date  :2024-09-11
 * Des   :BaseFragment基类:最简单的业务逻辑定义
 */
abstract class BaseFragment : Fragment(), IBase {
    val TAG = this::class.simpleName!!

    var refresh: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        initView(arguments,savedInstanceState)
        initData(arguments,savedInstanceState)
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

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

}